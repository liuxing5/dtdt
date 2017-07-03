package com.asiainfo.dtdt.common;

import org.bouncycastle.util.encoders.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

/**
 * 加密
 */
public class AESEncryptUtil {
    private static final String ALGORITHM = "AES";
    private static final String CHARSET = "UTF-8";

    public static String encrypt(String valueToEnc, String factor) throws Exception {
        Key key = new SecretKeySpec(factor.getBytes(CHARSET), ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);

        return new String(Base64.encode(c.doFinal(valueToEnc.getBytes(CHARSET))), CHARSET);
    }

    public static String decrypt(String valueToEnc, String factor) throws Exception {
        Key key = new SecretKeySpec(factor.getBytes(CHARSET), ALGORITHM);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.DECRYPT_MODE, key);

        return new String((c.doFinal(Base64.decode(valueToEnc.getBytes(CHARSET)))), CHARSET);
    }
}

