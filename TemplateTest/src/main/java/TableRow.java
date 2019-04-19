/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

public class TableRow {

    static String createTableRow(String jiraId, String jiraDescription) {
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
