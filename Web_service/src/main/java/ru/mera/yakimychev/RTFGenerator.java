package ru.mera.yakimychev;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import static com.tutego.jrtf.Rtf.rtf;
import static com.tutego.jrtf.RtfDocfmt.*;
import static com.tutego.jrtf.RtfHeader.*;
import static com.tutego.jrtf.RtfInfo.*;
import static com.tutego.jrtf.RtfFields.*;
import static com.tutego.jrtf.RtfPara.*;
import static com.tutego.jrtf.RtfSectionFormatAndHeaderFooter.*;
import static com.tutego.jrtf.RtfText.*;
import static com.tutego.jrtf.RtfUnit.*;

import java.io.*;

@Component
public class RTFGenerator implements FileGenerator {

    @Override
    public File generate() {
        return generateHelloWorldFile();
    }

    private File generateHelloWorldFile() {
        File helloWoldFile = new File("Web_service/files/Hello World.rtf");

        try(FileWriter out = new FileWriter(helloWoldFile)) {
            rtf()
                    .section( p("Hello World!").alignCentered() )
                    .out(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return helloWoldFile;
    }
}
