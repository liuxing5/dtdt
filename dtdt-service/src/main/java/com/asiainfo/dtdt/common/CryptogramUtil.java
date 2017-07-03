package com.asiainfo.dtdt.common;

import com.sun.crypto.provider.SunJCE;
import org.bouncycastle.util.encoders.Base64;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.security.MessageDigest;
import java.security.Security;

public class CryptogramUtil {
    private static String CodingType = "UTF-8";
    private static String CryptAlgorithm = "DESede/CBC/PKCS5Padding";
    private static String KeyAlgorithm = "DESede";
    private static byte[] defaultIV = { 1, 2, 3, 4, 5, 6, 7, 8 };

    static {
        try {
            Security.addProvider(new SunJCE());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String Encrypt(String strTobeEnCrypted, String strKey, byte[] byteIV)
            throws Exception {
        byte[] input = strTobeEnCrypted.getBytes(CodingType);
        Key k = KeyGenerator(strKey);
        IvParameterSpec IVSpec = (byteIV.length != 0) ? IvGenerator(byteIV) :
                IvGenerator(defaultIV);
        Cipher c = Cipher.getInstance(CryptAlgorithm);
        c.init(1, k, IVSpec);
        byte[] output = c.doFinal(input);
        return new String(Base64Encode(output), CodingType);
    }

    private static Key KeyGenerator(String KeyStr)
            throws Exception {
        byte[] input = Hex.decode(KeyStr);
        DESedeKeySpec KeySpec = new DESedeKeySpec(input);
        SecretKeyFactory KeyFactory =
                SecretKeyFactory.getInstance(KeyAlgorithm);
        return KeyFactory.generateSecret
                (KeySpec);
    }

    public static String Decrypt(String strTobeDeCrypted, String strKey, byte[] byteIV)
            throws Exception {
        byte[] input = Base64Decode(strTobeDeCrypted);
        Key k = KeyGenerator(strKey);
        IvParameterSpec IVSpec = (byteIV.length != 0) ? IvGenerator(byteIV) :
                IvGenerator(defaultIV);
        Cipher c = Cipher.getInstance(CryptAlgorithm);
        c.init(2, k, IVSpec);
        byte[] output = c.doFinal(input);
        return new String(output, CodingType);
    }

    public static byte[] originalEncrypt(String key, byte[] iv, byte[] input)
            throws Exception {
        Key k = KeyGenerator(key);
        IvParameterSpec IVSpec = (iv.length != 0) ? IvGenerator(iv) :
                IvGenerator(defaultIV);
        Cipher c = Cipher.getInstance(CryptAlgorithm);
        c.init(1, k, IVSpec);
        return c.doFinal(input);
    }

    public static byte[] originalDecrypt(String key, byte[] iv, byte[] input)
            throws Exception {
        Key k = KeyGenerator(key);
        IvParameterSpec IVSpec = (iv.length != 0) ? IvGenerator(iv) :
                IvGenerator(defaultIV);
        Cipher c = Cipher.getInstance(CryptAlgorithm);
        c.init(2, k, IVSpec);
        return c.doFinal(input);
    }

    public static byte[] Base64Encode(byte[] b)
            throws Exception {
        return Base64.encode(b);
    }

    public static byte[] Base64Decode(byte[] b)
            throws Exception {
        return Base64.decode(b);
    }

    public static byte[] Base64Decode(String s)
            throws Exception {
        return Base64.decode(s);
    }

    public static byte[] FromBase64String(String str)
            throws Exception {
        return Base64.decode(str);
    }

    public static String ToBase64String(byte[] buf) {
        return new String(Base64.encode(buf));
    }

    public static String ConvertByteArrayToString(byte[] buf) {
        try {
            return new String(buf, CodingType);
        }
        catch (Exception ex) {
        }
        return null;
    }

    public static byte[] ConvertStringToByteArray(String str) {
        try {
            return str.getBytes(CodingType);
        }
        catch (Exception ex) {
        }
        return null;
    }

	public static String URLEncode(String strToBeEncode)
            throws Exception {
        return URLEncoder.encode(strToBeEncode, "UTF-8");
    }

    public static String URLDecode(String strToBeDecode) throws Exception {
        return URLDecoder.decode(strToBeDecode, "UTF-8");
    }

    public static String GenerateDigest(String strTobeDigest)
            throws Exception {
        byte[] input = strTobeDigest.getBytes(CodingType);
        byte[] output = null;
        MessageDigest DigestGenerator = MessageDigest.getInstance("SHA1");
        DigestGenerator.update(input);
        output = DigestGenerator.digest();
        return new String(Base64Encode(output));
    }

    public static String Md5Encry(String str)
            throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes());
        return new String(Base64Encode(md.digest()));
    }

    private static IvParameterSpec IvGenerator(byte[] b) throws Exception {
        IvParameterSpec IV = new IvParameterSpec(b);
        return IV;
    }

    public static void main(String[] args) {
        try {
            System.out.println(GenerateDigest("123qwe"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}