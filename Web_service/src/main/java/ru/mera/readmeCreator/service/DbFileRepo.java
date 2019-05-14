/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;

import java.util.*;
import java.util.Date;

@Repository
public class DbFileRepo implements FileRepo {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FileGenerator fileGenerator;

    private final String INSERT_SQL_FILE = "INSERT INTO files(file_name, extension, file) VALUES(?,?,?) " +
                                           "ON DUPLICATE KEY UPDATE file=VALUES(file)";

    private final String FETCH_SQL_FILE = "SELECT file_name, extension, file FROM files WHERE (file_name = ?)" +
                                          "AND (extension = ?)";


    @Override
    public byte[] getFile(String fullFileName) {
        int lastDotIndex = fullFileName.lastIndexOf(".");
        String fileName = fullFileName.substring(0, lastDotIndex);
        String extension = fullFileName.substring(lastDotIndex + 1);

        return jdbcTemplate.queryForObject(FETCH_SQL_FILE, new FileMapper(), fileName, extension);
    }

    private class FileMapper implements RowMapper<byte[]> {

        @Override
        public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            String fileName = rs.getString("file_name");
            String extension = rs.getString("extension");
            //            File dbFile = new File(fileName + "." + extension);

            byte[] out;
            try (ByteArrayOutputStream os = new ByteArrayOutputStream();
                 InputStream is = rs.getBlob("file").getBinaryStream()) {
                int i;
                while ((i = is.read()) != -1) {
                    os.write(i);
                }
                os.flush();
                out = os.toByteArray();
            } catch (IOException e) {
                throw new SQLException("Error while trying to create file", e);
            }
            return out;
        }
    }

    @Override
    public void addFile(String fullFileName, UserData userData) throws IOException, SQLException {
        File patchFile = new File(fullFileName);
        byte[] templateFileBytes = getFile("Template.rtf");

        byte[] out;
        try {
            out = fileGenerator.generateOnTemplate(patchFile, userData, templateFileBytes);
        } catch (GeneratorException e) {
            throw new RepositoryException(e.getMessage(), e);
        }

        int lastDotIndex = fullFileName.lastIndexOf(".");
        String fileName = fullFileName.substring(0, lastDotIndex);
        String extension = fullFileName.substring(lastDotIndex + 1);

        jdbcTemplate.update(INSERT_SQL_FILE, fileName, extension, out);
    }
}
