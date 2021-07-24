package kr.co.okheeokey.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

public class CryptoUtils {
    private static final Long KEY_NUM = 16L;
    private static final int SALT_LEN = 8;

    private static final List<SecretKey> KEYS = new ArrayList<>();
    private static final IvParameterSpec IV;

    static {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        assert keyGenerator != null;
        keyGenerator.init(56, new SecureRandom());

        for(int i = 0; i < KEY_NUM; i++)
            KEYS.add(keyGenerator.generateKey());

        byte[] iv = new byte[8];
        new SecureRandom().nextBytes(iv);
        IV = new IvParameterSpec(iv);
    }

    public static String encryptUuid(UUID uuid) {
        StringBuilder encryptUuidBuffer = new StringBuilder();
        try {
            int keyIndex = new SecureRandom().nextInt(KEY_NUM.intValue());
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, KEYS.get(keyIndex), IV);

            byte[] salt = new byte[SALT_LEN];
            new SecureRandom().nextBytes(salt);

            ByteBuffer bb = ByteBuffer.wrap(new byte[16 + SALT_LEN]);
            bb.put(salt);
            bb.put(asBytes(uuid));

            encryptUuidBuffer.append(keyIndex);
            encryptUuidBuffer.append("$");
            encryptUuidBuffer.append(Base64.getUrlEncoder().encodeToString(cipher.doFinal(bb.array())));
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        return encryptUuidBuffer.toString();
    }

    public static UUID decryptUuid(String encryptUuid) {
        UUID uuid = null;
        try {
            String[] split = encryptUuid.split("\\$");
            if (split.length != 2)
                throw new GeneralSecurityException("Invalid encryptUuid format");

            int keyIndex = Integer.parseInt(split[0]);
            Cipher cipher = Cipher.getInstance("DES/CBC/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, KEYS.get(keyIndex), IV);

            byte[] uuidBuffer = cipher.doFinal(Base64.getUrlDecoder().decode(split[1]));
            uuidBuffer = Arrays.copyOfRange(uuidBuffer, SALT_LEN, uuidBuffer.length);
            uuid = asUuid(uuidBuffer);
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
