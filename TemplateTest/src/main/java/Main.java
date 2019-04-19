/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

import java.io.*;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class Main {
    static File template = new File("Template.rtf");
    static File finalFile = new File("Final_file.rtf");

    static String releaseVer = "3.5.0.1";
    static String patchName = "AvayaOceana_UAC_3.5.0.1.4";
    static String date = "08/02/2019";
    static String updateId = "AvayaOceana_UAC_3.5.0.1.4.521002001";
    static LinkedHashMap<String, String> jiras = new LinkedHashMap<>();


    static {
        jiras.put("UNIDESK-14682", "Add page push url event to Widget Framework");
//        jiras.put("UNIDESK-test", "Test message");
//        jiras.put("Another UNIDESK", "Another message");
//        jiras.put("One more UNIDESK", "One more message");
//        jiras.put("UNIDESK-6578595", "7698665798595");
//        jiras.put("UNIDESK-27321", "hhwedkwefwef");
    }

    public static void main(String[] args) throws IOException {
        try (BufferedReader inStr = new BufferedReader(new FileReader(template));
            BufferedWriter out = new BufferedWriter(new FileWriter(finalFile))) {
            int a;
            while ((a = inStr.read()) != -1) {
                char letter = (char) a;
                if (letter == '<') {
                    StringBuilder str = new StringBuilder();
                    while ( (letter = (char) inStr.read()) != '>') {
                        str.append(letter);
                    }
                    switch (str.toString()) {
                        case "Release_version":
                            out.write(releaseVer);
                            break;
                        case "Patch_name":
                            out.write(patchName);
                            break;
                        case "Date":
                            out.write(date);
                            break;
                        case "Update_id":
                            out.write(updateId);
                            break;
                        case "Table_row":
                            insertRows(out);
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
    private static void insertRows(BufferedWriter out) throws IOException {
        for (HashMap.Entry<String, String> id: jiras.entrySet()) {
            String row = TableRow.createTableRow(id.getKey(), id.getValue());
            out.write(row);
        }
    }
}
