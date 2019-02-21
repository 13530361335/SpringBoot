package com.demo.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ORCUtil {

    public static String simpleAnalysis(InputStream in) throws TesseractException, IOException {
        String path = new File(ORCUtil.class.getResource("/tessdata").getPath()).getPath();
        BufferedImage bufferedImage = ImageIO.read(in);

        ITesseract instance = new Tesseract();
        instance.setDatapath(path);
        instance.setLanguage("chi_sim"); // chi_sim ：简体中文， eng : 英文;	根据需求选择语言库

        return instance.doOCR(bufferedImage);
    }

}
