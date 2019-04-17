/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Files repository
 */
public interface FileRepo {

    /**
     * Gets file from repo
     * @param name name of the file
     * @return file
     * @throws NoSuchFileException server didn't find file in repo
     * @throws IOException problems with generating "Hello_world.rtf" file
     */
    File getFile(String name) throws IOException;

    /**
     * Delegates generating of file to fileGenerator
     * @param name name of the file
     * @param info information to print
     * @return generated file
     * @throws IOException exception while generating file
     */
    File addFile(String name, String info) throws IOException;
}
