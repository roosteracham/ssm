package com.zsf.util.encode;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.UUID;

public class MD5 {

    public static byte[] md5Encode() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        Calendar calendar = Calendar.getInstance();
        MessageDigest md5 = MessageDigest.getInstance("md5");
        return md5.digest((calendar + UUID.randomUUID().toString()).getBytes("UTF-8"));
    }
}
