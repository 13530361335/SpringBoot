package com.demo.util;

import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

public class ImageUtil {

    public static BufferedImage getImage(MultipartFile file) throws IOException {
        FileInputStream in = (FileInputStream) file.getInputStream();
        BufferedImage srcImage = ImageIO.read(in);
        return srcImage;
    }
    
}
