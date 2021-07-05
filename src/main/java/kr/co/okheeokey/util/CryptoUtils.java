package kr.co.okheeokey.util;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Base64;
import java.util.UUID;

public class CryptoUtils {
    private static SecretKey KEY;
    private static IvParameterSpec IV;

    static {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert keyGenerator != null;
        keyGenerator.init(128, new SecureRandom());
        CryptoUtils.KEY = keyGenerator.generateKey();

        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        CryptoUtils.IV = new IvParameterSpec(iv);
    }

    public static String encryptUuid(UUID uuid) {
        String encryptUuid = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, KEY, IV);
            encryptUuid = Base64.getUrlEncoder().encodeToString(cipher.doFinal(asBytes(uuid)));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encryptUuid;
    }

    public static UUID decryptUuid(String encryptUuid) {
        UUID uuid = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, KEY, IV);
            uuid = asUuid(cipher.doFinal(Base64.getUrlDecoder().decode(encryptUuid)));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return uuid;
    }

    private static UUID asUuid(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long firstLong = bb.getLong();
        long secondLong = bb.getLong();
        return new UUID(firstLong, secondLong);
    }

    private static byte[] asBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
