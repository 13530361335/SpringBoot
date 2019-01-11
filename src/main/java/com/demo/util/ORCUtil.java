package com.demo.util;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.stereotype.Component;
import java.awt.image.BufferedImage;

@Component
public class ORCUtil {

    public String simpleAnalysis(BufferedImage bufferedImage) throws TesseractException {
        ITesseract instance = new Tesseract();
        instance.setDatapath("D:\\GitHub\\springcloud\\rest\\target\\classes\\tessdata");
        instance.setLanguage("eng");//chi_sim ：简体中文， eng	根据需求选择语言库
        return instance.doOCR(bufferedImage);
    }

}
