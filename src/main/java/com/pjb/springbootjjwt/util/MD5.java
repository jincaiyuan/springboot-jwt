package com.pjb.springbootjjwt.util;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    private MD5(){}

    public static String encryptByMD5(String str) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        BASE64Encoder encoder = new BASE64Encoder();

        String encode = encoder.encode(md5.digest(str.getBytes("utf-8")));
        return encode;
    }

    public static boolean checkEqual(String writtenPass, String savedPass) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        if (encryptByMD5(writtenPass).equals(savedPass))
            return true;
        return false;

    }
}
