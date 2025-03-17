package com.mujun.core.base.tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public final class IOTool {
    private IOTool() {
        throw new AssertionError("Tool classes do not allow instantiation");
    }

    public static void inToOut(InputStream inStream, OutputStream outStream) {
        byte[] cache = new byte[1024 * 1024 * 20];
        try {
            int len = inStream.read(cache);
            while (len > 0) {
                outStream.write(cache, 0, len);
                outStream.flush();
                len = inStream.read(cache);
            }
            outStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(inStream, outStream);
        }
    }

    public static void closeStream(AutoCloseable... autoCloseables) {
        if (null == autoCloseables) {
            return;
        }
        for (AutoCloseable autoCloseable : autoCloseables) {
            if (null == autoCloseable) {
                continue;
            }
            try {
                autoCloseable.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}