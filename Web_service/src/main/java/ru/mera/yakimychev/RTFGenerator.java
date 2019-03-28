package ru.mera.yakimychev;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfPara.*;

import java.io.*;

public class RTFGenerator implements FileGenerator {

    @Override
    public File generate() throws IOException {
        return generateHelloWorldFile();
    }

    private File generateHelloWorldFile() throws IOException {
        File helloWoldFile = new File("files/Hello World.rtf");

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
