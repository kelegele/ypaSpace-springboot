package com.kelegele.ypaSpace.utils;

import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;

public class FileUtil {

    public static byte[] readFullyToByteArray(DataInput in) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {
            while(true) {
                baos.write(in.readByte());
            }
        } catch (EOFException var3) {
            return baos.toByteArray();
        }
    }
}
