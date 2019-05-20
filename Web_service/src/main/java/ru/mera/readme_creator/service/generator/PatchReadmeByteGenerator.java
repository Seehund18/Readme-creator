/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readme_creator.service.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.mera.readme_creator.service.JiraPair;
import ru.mera.readme_creator.service.UserData;

import java.io.*;
import java.util.List;

/**
 * Generator of patch readme file in rtf format
 */
@Component
public class PatchReadmeByteGenerator implements ByteDataGenerator {
    private final Logger log = LoggerFactory.getLogger(PatchReadmeByteGenerator.class);

    /**
     * Generates patch readme byte array from template and user data which is sent from client
     * @param data user data which was sent from clients
     * @param templateCharBytes bytes from Template.rtf from database
     * @return patch readme byte array
     */
    @Override
    public byte[] generateWithTemplate(Object data, byte[] templateCharBytes) throws GeneratorException {
        log.info("Generating patch readme byte array ...");
        UserData userData = (UserData) data;

        //Reading from template byte array and writing to patchOutStream which
        //will be transformed into byte array
        ByteArrayOutputStream patchOutStream = new ByteArrayOutputStream();
        try (BufferedReader in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(templateCharBytes)));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(patchOutStream))) {

            int symbolCode;
            while ((symbolCode = in.read()) != -1) {
                char currentSymbol = (char) symbolCode;

                //If symbol is '<', reading string until '>' occurred and then checking that string
                if (currentSymbol == '<') {
                    StringBuilder str = new StringBuilder();
                    while ( (currentSymbol = (char) in.read()) != '>') {
                        str.append(currentSymbol);
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
                out.write(symbolCode);
            }
            out.flush();
        } catch (IOException e) {
            throw new GeneratorException("Error while creating readme patch file", e);
        }
        return patchOutStream.toByteArray();
    }

    /**
     * Insert rows into table in patch readme rtf file.
     * @param out stream to write
     * @param jiraList list of jiras, which will be written to the table
     * @throws IOException exception while writing to a file
     */
    private void insertRows(BufferedWriter out, List<JiraPair> jiraList) throws IOException {
        for (JiraPair jira: jiraList) {
            String row = createTableRow(jira.getJiraId(), jira.getJiraDescrip());
            out.write(row);
        }
        out.flush();
    }

    /**
     * Creates code for rtf table row.
     * @param jiraId id which inserted into row
     * @param jiraDescription description which inserted into row
     * @return rtf code for one table row with given information
     */
    private String createTableRow(String jiraId, String jiraDescription) {
        return "\n\n\\pard\\plain\\ltrpar\\s65\\ql\\li0\\ri0\\widctlpar\\intbl\\wrapdefault"
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
    }
}
