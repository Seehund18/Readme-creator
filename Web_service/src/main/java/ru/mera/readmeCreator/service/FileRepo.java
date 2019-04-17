/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Files repository
 */
public class FileRepo {
    private File filesDirectory = new File("files");

    {
        //Creating 'files' directory, if one is missing
        filesDirectory.mkdir();
    }

    /**
     * Injected file generator
     */
    @Autowired
    private final FileGenerator fileGenerator;

    FileRepo(FileGenerator fileGenerator) {
        this.fileGenerator = fileGenerator;
    }

    /**
     * Gets file from repo
     * @param name name of the file
     * @return file
     * @throws NoSuchFileException server didn't find file in repo
     * @throws IOException problems with generating "Hello_world.rtf" file
     */
    public File getFile(String name) throws IOException {
        switch (name) {
            case "Hello_world.rtf":
                return getHelloWorldFile();
            case "User_data.rtf":
                return getUserDataFile();
        }
        throw new NoSuchFileException("Server didn't find file in repo: " + name);
    }

    /**
     * Returns "Hello_world.rtf" file. If the file is missing, generates it
     * @return generated file
     * @throws IOException problems with generating "Hello_world.rtf" file
     */
    private File getHelloWorldFile() throws IOException {
        String filePath = filesDirectory + "/Hello_world.rtf";
        File helloWorldFile = new File(filePath);
        if (!helloWorldFile.exists()) {
            return fileGenerator.generate(helloWorldFile, "Hello World!");
        }
        return helloWorldFile;
    }

    /**
     * Returns "User_data.rtf" file. This file must be first generated via 'POST' request to service.
     * @return generated file
     * @throws NoSuchFileException file doesn't exists
     */
    private File getUserDataFile() throws NoSuchFileException {
        String filePath = filesDirectory + "/User_data.rtf";
        File userDataFile = new File(filePath);
        if (!userDataFile.exists()) {
            throw new NoSuchFileException("User_data.rtf file doesn't exists");
        }
        return userDataFile;
    }

    /**
     * Delegates generating of file to fileGenerator
     * @param name name of the file
     * @param info information to print
     * @return generated file
     * @throws IOException exception while generating file
     */
    public File generate(String name, String info) throws IOException {
        File file = new File(filesDirectory + "/" + name);
        return fileGenerator.generate(file, info);
    }
}
