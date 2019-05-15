/*
 * Copyright Avaya Inc., All Rights Reserved. THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF Avaya Inc. The copyright
 * notice above does not evidence any actual or intended publication of such source code. Some third-party source code
 * components may have been modified from their original versions by Avaya Inc. The modifications are Copyright Avaya
 * Inc., All Rights Reserved. Avaya - Confidential & Restricted. May not be distributed further without written
 * permission of the Avaya owner.
 */

package ru.mera.readmeCreator.service;

/**
 * Files repository
 */
public interface FileRepo {

    /**
     * Gets file from the repository
     * @param name name of the file
     * @return file
     * @throws RepositoryException
     */
    byte[] getFile(String name) throws RepositoryException;

    /**
     * Add file to repository     *
     * @param name name of the file
     * @param userData data from the clients
     * @throws RepositoryException
     */
    void addFile(String name, UserData userData) throws RepositoryException;
}
