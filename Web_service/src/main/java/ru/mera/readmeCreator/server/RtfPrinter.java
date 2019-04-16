/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.server;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.p;

/**
 * Class encapsulates third party Jrtf library, which prints in .rtf format
 */
public class RtfPrinter {

    /**
     * Prints 'Hello World!' at the center of the file
     * @param helloWorld file to which print
     * @throws IOException problem with writing to file
     */
    void printHelloWorld(File helloWorld) throws IOException {
        try (FileWriter out = new FileWriter(helloWorld)) {
            rtf().section(p("Hello World!").alignCentered())
                    .out(out);
        }
    }

    /**
     * Prints user information to file
     * @param userDataFile file to which print
     * @param info information, entered by user
     * @throws IOException problem with writing to file
     */
    void printUserData(File userDataFile, String info) throws IOException {
        try (FileWriter out = new FileWriter(userDataFile)) {
            rtf().section(p(info).alignCentered())
                    .out(out);
        }
    }
}
