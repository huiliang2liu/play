package com.xh.play.utils;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class Util {
    private final static int LENGTH = 1024 * 1024;
    private final static String CHARSET = "utf-8";

    public static boolean stream2file(InputStream is, File file) throws FileNotFoundException {
        if (is == null || file == null) {
            return false;
        }
        File target = new File(file.getParentFile(), System.currentTimeMillis() + "");
        FileOutputStream fos = new FileOutputStream(target);
        if (fos == null) {
            return false;
        }

        boolean success = false;
        try {
            byte[] buff = new byte[LENGTH];
            int len = is.read(buff);
            while (len > 0) {
                fos.write(buff, 0, len);
                len = is.read(buff);
            }
            success = target.renameTo(file);
            fos.flush();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close(fos);
            close(is);
        }
        return success;
    }

    public static void close(Closeable closeable) {
        if (closeable == null)
            return;
        try {
            closeable.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
