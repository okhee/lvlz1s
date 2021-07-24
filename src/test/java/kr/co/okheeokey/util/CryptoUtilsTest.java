package kr.co.okheeokey.util;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CryptoUtilsTest {
    @Test
    public void initTest() {
        UUID uuid = new UUID(1, 2);
        String encryptUuid = CryptoUtils.encryptUuid(uuid);
        assertEquals(uuid, CryptoUtils.decryptUuid(encryptUuid));
    }

}