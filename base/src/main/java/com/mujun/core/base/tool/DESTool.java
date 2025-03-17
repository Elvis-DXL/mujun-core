package com.mujun.core.base.tool;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

public final class DESTool {
    private final Key key;
    private final Cipher cipher;

    private DESTool(String secretKey) {
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(secretKey.getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            kg.init(sr);
            this.key = kg.generateKey();
            this.cipher = Cipher.getInstance("DES");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed to construct encrypt key");
        }
    }

    public static DESTool getInstance(String secretKey) {
        DSTool.trueThrow(EmptyTool.isEmpty(secretKey), new IllegalArgumentException("secretKey must not be empty"));
        return new DESTool(secretKey);
    }

    public String encode(String aimStr) {
        if (EmptyTool.isEmpty(aimStr)) {
            return null;
        }
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return new String(Base64.getEncoder().encode(cipher.doFinal(aimStr.getBytes(StandardCharsets.UTF_8))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed to encode");
        }
    }

    public String decode(String aimStr) {
        if (EmptyTool.isEmpty(aimStr)) {
            return null;
        }
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(aimStr)), StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("failed to decode");
        }
    }
}