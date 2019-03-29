package ru.mera.readmeCreator.server;

import java.io.File;
import java.io.IOException;

public interface FileGenerator {
    File generate() throws IOException;
}
