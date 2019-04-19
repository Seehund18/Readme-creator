/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.p;
import static com.tutego.jrtf.RtfPara.row;
import static com.tutego.jrtf.RtfText.bold;
import static com.tutego.jrtf.RtfUnit.CM;
import static java.awt.SystemColor.info;

public class JrtfTesting {

    static File table = new File("Table_file.rtf");

    public static void main(String[] args) {
        try (FileWriter outFile = new FileWriter(table)) {
            rtf().section(
                    row(
                            bold( "S" ), bold( "T" )
                    ).bottomCellBorder().topCellBorder().leftCellBorder().rightCellBorder().cellSpace( 1, CM ),
                    row(
                            "Good", "nice"
                    ).cellSpace( 1, CM )).out(outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
