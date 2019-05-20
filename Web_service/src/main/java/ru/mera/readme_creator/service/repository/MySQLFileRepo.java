/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.service.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.mera.readme_creator.service.generator.ByteDataGenerator;
import ru.mera.readme_creator.service.generator.GeneratorException;
import ru.mera.readme_creator.service.UserData;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;

/**
 * MySQL database repository of files
 */
@Repository
public class MySQLFileRepo implements FileRepo {
    private final Logger log = LoggerFactory.getLogger(MySQLFileRepo.class);

    /**
     * Spring class which manages all database interactions
     */
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Generator of byte array for files
     */
    @Autowired
    private ByteDataGenerator byteDataGenerator;

    /**
     * SQL request for inserting file's byte array into database.
     * If file with such name and extension already exists, replace it.
     */
    private static final String INSERT_SQL_FILE = "INSERT INTO files(file_name, extension, file) VALUES(?,?,?) " +
                                           "ON DUPLICATE KEY UPDATE file = VALUES(file)";

    /**
     * SQL request for fetching file from the database
     */
    private static final String FETCH_SQL_FILE = "SELECT file_name, extension, file FROM files WHERE (file_name = ?)" +
                                          "AND (extension = ?)";

    @Override
    public void addFile(String fullFileName, UserData userData) throws RepositoryException {
        byte[] templateFileBytes = getFile("Template.rtf");
        byte[] out;
        try {
            out = byteDataGenerator.generateWithTemplate(userData, templateFileBytes);
        } catch (GeneratorException e) {
            throw new RepositoryException("Exception while trying to generate file", e);
        }

        String[] separFileName = separateFileName(fullFileName);
        log.info("Adding new {} file to database...", fullFileName);
        jdbcTemplate.update(INSERT_SQL_FILE, separFileName[0], separFileName[1], out);
    }

    @Override
    public byte[] getFile(String fullFileName) {
        String[] separFileName = separateFileName(fullFileName);
        log.info("Getting {} file from database...", fullFileName);
        return jdbcTemplate.queryForObject(FETCH_SQL_FILE, new ByteArrayMapper(), separFileName[0], separFileName[1]);
    }

    /**
     * Separates full file name into file name and its extension
     * @return array in which the first element is a fileName and the second is extension
     */
    private String[] separateFileName(String fullFileName) {
        int lastDotIndex = fullFileName.lastIndexOf('.');
        String fileName = fullFileName.substring(0, lastDotIndex);
        String extension = fullFileName.substring(lastDotIndex + 1);
        return new String[] {fileName, extension};
    }

    /**
     * Mapper for database table rows, which writes file into byte array
     */
    private class ByteArrayMapper implements RowMapper<byte[]> {

        @Override
        public byte[] mapRow(ResultSet rs, int rowNum) throws SQLException {
            byte[] resultBytes;

            //Getting binary stream from 'file' column of database table and writing it to a byte array
            InputStream byteIn = rs.getBlob("file").getBinaryStream();
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 BufferedReader in = new BufferedReader(new InputStreamReader(byteIn, StandardCharsets.UTF_8))) {
                int symbolCode;
                while ((symbolCode = in.read()) != -1) {
                    out.write(symbolCode);
                }
                out.flush();
                resultBytes = out.toByteArray();
            } catch (IOException e) {
                throw new SQLException("Error while trying to read file's byte array", e);
            }
            return resultBytes;
        }
    }
}