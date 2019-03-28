package ru.mera.yakimychev;

import java.io.File;
import java.io.IOException;

public interface FileGenerator {
    File generate() throws IOException;
}
