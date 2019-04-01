package ru.mera.readmeCreator.server;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.*;

import java.io.*;
import java.nio.file.NoSuchFileException;

public class RTFGenerator implements FileGenerator {

    @Override
    public File generate(String name) throws IOException {
        if(name.equals("HelloWorld.rtf")) {
            return generateHelloWorldFile();
        }
        throw new NoSuchFileException("Server can't find file " + name);
    }

    private File generateHelloWorldFile() throws IOException {
        File helloWoldFile = new File("files/HelloWorld.rtf");

        if(!helloWoldFile.exists()) {
            try (FileWriter out = new FileWriter(helloWoldFile)) {
                rtf()
                  .section(p("Hello World!").alignCentered())
                  .out(out);
            }
        }
        return helloWoldFile;
    }
}