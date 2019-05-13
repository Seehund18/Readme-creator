/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

import java.io.*;
import java.sql.*;
import java.util.*;

public class DbFileRepo implements FileRepo {
    private static String dbUrl = "jdbc:mysql://localhost:3306/service";
    private static Properties properties = new Properties();

    static {
        try {
            properties.load(new FileReader("src/main/resources/database.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Connection connection;


    public static void main(String[] args) throws SQLException {

        connection = DriverManager.getConnection(dbUrl, properties);
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT file_name FROM files WHERE file_name='Template'");

        while (rs.next()) {
            try (FileOutputStream os = new FileOutputStream(new File("Template.rtf"));
                 InputStream is = rs.getBlob(1).getBinaryStream()){

                int i;
                while ( (i = is.read()) != -1) {
                    os.write(i);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public File getFile(String name) throws IOException {
        try {
            connection = DriverManager.getConnection(dbUrl, properties);
        } catch (SQLException e) {
            throw new
        }
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT file FROM files WHERE file_name='Template'");
    }

    @Override
    public File addFile(String name, UserData userData) throws IOException, SQLException {
        connection = DriverManager.getConnection(dbUrl, properties);

        try (BufferedReader is = getTemplate();
            BufferedWriter out = new BufferedWriter(new FileWriter("Final_file.rtf"))) {

            int a;
            while ((a = is.read()) != -1) {
                char letter = (char) a;
                if (letter == '<') {
                    StringBuilder str = new StringBuilder();
                    while ( (letter = (char) is.read()) != '>') {
                        str.append(letter);
                    }
                    switch (str.toString()) {
                        case "Release_version":
                            out.write(userData.getReleaseVer());
                            break;
                        case "Patch_name":
                            out.write(userData.getPatchName());
                            break;
                        case "Date":
                            out.write(userData.getDate());
                            break;
                        case "Update_id":
                            out.write(userData.getUpdateId());
                            break;
                        case "Table_row":
                            insertRows(out, userData.getJiraList());
                            break;
                        default:
                            out.write("<" + str.toString() + ">");
                    }
                    continue;
                }
                out.write(a);
            }
            out.flush();
        }
    }

    private static void insertRows(BufferedWriter out, List<JiraPair> jiras) throws IOException {
        for (JiraPair jira: jiras) {
            String row = TableRow.createTableRow(jira.getJiraId(), jira.getJiraDescrip());
            out.write(row);
        }
    }

    private BufferedReader getTemplate() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery("SELECT file FROM files WHERE file_name='Template'");

        rs.next();
        return new BufferedReader(new InputStreamReader(rs.getBlob(1).getBinaryStream()));
    }
}
