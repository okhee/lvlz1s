package kr.co.okheeokey.util;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CryptoUtilsTest {
    @Test
    public void initTest() throws Exception {
        CryptoUtils cryptoUtils = new CryptoUtils();

        UUID uuid = new UUID(1, 2);
        String encryptUuid = cryptoUtils.encryptUuid(uuid);
        assertEquals(uuid, cryptoUtils.decryptUuid(encryptUuid));
    }

}