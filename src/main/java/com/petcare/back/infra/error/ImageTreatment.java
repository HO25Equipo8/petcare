package com.petcare.back.infra.error;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class ImageTreatment {

    public byte[] process(MultipartFile file) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(512, 512)       // Redimensionar
                .outputQuality(0.7)   // Comprimir
                .toOutputStream(baos);

        return baos.toByteArray(); // 👈 devolver el arreglo de bytes
    }
}
