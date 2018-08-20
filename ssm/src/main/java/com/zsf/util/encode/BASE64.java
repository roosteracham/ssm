package com.zsf.util.encode;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class BASE64 {

    public static String base64Encode(BASE64Encoder base64Encoder)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {
        return base64Encoder.encode(MD5.md5Encode());
    }
}
