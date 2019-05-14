/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

@Component
public class ReadmePatchGenerator implements FileGenerator {

    @Override
    public File generate(File generatedFile, String info) throws IOException {
        return null;
    }

    @Override
    public byte[] generateOnTemplate(File generatedFile, Object data, byte[] templateFile) throws GeneratorException {
        UserData userData = (UserData) data;

        byte[] buff = new byte[templateFile.length];
        ByteArrayOutputStream o = new ByteArrayOutputStream();
        try (BufferedReader is = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(templateFile)));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(o))) {

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
        } catch (IOException e) {
            throw new GeneratorException("Error while creating patch file", e);
        }

        return o.toByteArray();
    }

    private void insertRows(BufferedWriter out, List<JiraPair> jiras) throws IOException {
        for (JiraPair jira: jiras) {
            String row = createTableRow(jira.getJiraId(), jira.getJiraDescrip());
            out.write(row);
        }
        out.flush();
    }

    private String createTableRow(String jiraId, String jiraDescription) {
        String templateRow = "\n\n\\pard\\plain\\ltrpar\\s65\\ql\\li0\\ri0\\widctlpar\\intbl\\wrapdefault"
                + "\\hyphpar0\\faauto\\rin0\\lin0\\pararsid7218467 \\rtlch\\fcs1 \\af1\\afs20\\alang1025 "
                + "\\ltrch\\fcs0 \\fs20\\lang1033\\langfe1033\\kerning1\\loch\\af1\\hich\\af39\\dbch\\af13\\cgrid"
                + "\\langnp1033\\langfenp1033 {\\rtlch\\fcs1 \\af0\\afs24\\ltrch\\fcs0 \\insrsid5141355"
                + "\\charrsid7218467 \\hich\\af39\\dbch\\af13\\loch\\f1 " + jiraId + "}{\\rtlch\\fcs1 \\af0\\afs24 "
                + "\\ltrch\\fcs0 \\insrsid8218224 \\cell }\\pard \\ltrpar\\s65\\ql \\li0\\ri0\\widctlpar\\intbl"
                + "\\wrapdefault\\hyphpar0\\faauto\\rin0\\lin0 {\\rtlch\\fcs1 \\af0\\afs24 \n"
                + "\\ltrch\\fcs0 \\insrsid5141355\\charrsid7218467 \\hich\\af39\\dbch"
                + "\\af13\\loch\\f1 " + jiraDescription + "}"
                + "{\\rtlch\\fcs1 \\af0\\afs24 \\ltrch\\fcs0 \\insrsid8218224 \\cell }\n"
                + "\\pard\\plain \\ltrpar\\ql \\li0\\ri0\\sa200\\sl276\\slmult1\n"
                + "\\widctlpar\\intbl\\wrapdefault\\aspalpha\\aspnum\\faauto\\adjustright\\rin0\\lin0 \\rtlch\\fcs1 "
                + "\\af39\\afs22\\alang1025 \\ltrch\\fcs0 \\fs22\\lang1033\\langfe1033\\kerning1\\loch\\af39\\hich"
                + "\\af39\\dbch\\af31505\\cgrid\\langnp1033\\langfenp1033 {\\rtlch\\fcs1 \\af0\\afs24 \\ltrch\\fcs0 \n"
                + "\\insrsid8218224 \\trowd \\irow1\\irowband1\\lastrow \\ltrrow\\ts11\\trleft361\\trftsWidth3"
                + "\\trwWidth9359\\trpaddfl3\\trpaddft3\\trpaddfb3\\trpaddfr3\\tblrsid12080545\\tblind469\\tblindtype3 "
                + "\\clvertalt\\clbrdrt\\brdrs\\brdrw5\\brdrcf20 \\clbrdrl\\brdrs\\brdrw5\\brdrcf20 \\clbrdrb\n"
                + "\\brdrs\\brdrw5\\brdrcf20 \\clbrdrr\\brdrs\\brdrw5\\brdrcf20 \\clcbpat8\\cltxlrtb\\clftsWidth3"
                + "\\clwWidth1766\\clpadt108\\clpadr108\\clpadft3\\clpadfr3\\clcbpatraw8 \\cellx2127\\clvertalt\\clbrdrt"
                + "\\brdrs\\brdrw5\\brdrcf20 \\clbrdrl\\brdrs\\brdrw5\\brdrcf20 \\clbrdrb\n"
                + "\\brdrs\\brdrw5\\brdrcf20 \\clbrdrr\\brdrs\\brdrw5\\brdrcf20 \\clcbpat8\\cltxlrtb\\clftsWidth3"
                + "\\clwWidth7593\\clpadt108\\clpadr108\\clpadft3\\clpadfr3\\clcbpatraw8 \\cellx9720\\row }\n\n";
        return templateRow;
    }
}
