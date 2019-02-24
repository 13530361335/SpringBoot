package com.demo.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ImageUtil {

    private final static Logger logger = LoggerFactory.getLogger(ImageUtil.class);

    /**
     * @param in
     * @return
     */
    public static String parseImg(InputStream in) {
        byte[] data = null;
        try{
            data = new byte[in.available()];
            in.read(data);
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
        } finally {
            IOUtils.closeQuietly(in);
        }
        return new BASE64Encoder().encode(data);
    }

    /**
     * @param imgStr
     * @param out
     * @return
     */
    public static boolean generateImg(String imgStr, OutputStream out) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            out.write(b);
            out.flush();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(),e);
            return false;
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

}
