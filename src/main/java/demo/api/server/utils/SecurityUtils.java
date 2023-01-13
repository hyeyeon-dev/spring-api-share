package demo.api.server.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class SecurityUtils {
    public static final String AES128_MEM_NAME = "MEM_NAME";
    public static final String AES128_MOBILE_NO = "MOBILE_NO";
    public static final String AES128_ADDR = "ADDR";
    public static final String AES128_ADDR_DTL = "ADDR_DTL";
    public static final String AES128_RCVR_NAME = "RCVR_NAME";

    /**
     * 문자열을 SHA256 단방향 암호화
     * @Author hanhyeyeon
     */
    public static String sha256(String str) {
        String sha = "";

        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");
            sh.update(str.getBytes());
            byte byteData[] = sh.digest();

            StringBuffer sb = new StringBuffer();
            for (int i=0; i<byteData.length; i++)
                sb.append(Integer.toString((byteData[i]&0xff)+0x100, 16).substring(1));

            sha = sb.toString();

        } catch (Exception e) {
            LogUtils.logStackTrace(e);
            sha = null;
        }

        return sha;
    }

    /**
     * AES128 양방향 암호화/복호화 : key값으로 secretKey, ips, keySpec 반환
     * @Attention 외부 호출 절대 불가!
     * @Author hanhyeyeon
     */
    private static Map<String, Object> aes128(String key) {
        Map<String, Object> result = new HashMap<>();

        String secretKey = key + "#ntr@ne~" + sha256(key);

        try {
            byte[] keyBytes = new byte[16];
            byte[] b = secretKey.getBytes(StandardCharsets.UTF_8);
            System.arraycopy(b, 0, keyBytes, 0, keyBytes.length);
            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

            result.put("secretKey", secretKey);
            result.put("ips", secretKey.substring(0, 16));
            result.put("keySpec", keySpec);
        } catch (Exception e) {
            result = null;
            LogUtils.logStackTrace(e);
        }

        return result;
    }

    /**
     * AES128 양방향 암호화
     * @Author hanhyeyeon
     */
    public static String encryptAES128(String str, String key) {
        Map<String, Object> aes128 = aes128(key);
        String ips = (String) aes128.get("ips");
        Key keySpec = (Key) aes128.get("keySpec");

        String result;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(ips.getBytes()));
            byte[] encrypted = cipher.doFinal(str.getBytes(StandardCharsets.UTF_8));
            result = Base64.encodeBase64String(encrypted);

        } catch (Exception e) {
            result = null;
            LogUtils.logStackTrace(e);
        }

        return result;
    }

    /**
     * AES128 양방향 복호화
     * @Author hanhyeyeon
     */
    public static String decryptAES128(String str, String key) {
        Map<String, Object> aes128 = aes128(key);
        String ips = (String) aes128.get("ips");
        Key keySpec = (Key) aes128.get("keySpec");

        String result;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(ips.getBytes()));
            byte[] byteStr = Base64.decodeBase64(str.getBytes());
            result = new String(cipher.doFinal(byteStr), StandardCharsets.UTF_8);

        } catch (Exception e) {
            result = null;
            LogUtils.logStackTrace(e);
        }

        return result;
    }

    /**
     * MySQL용 AES128 양방향 암호화(MySql 함수 로직과 동일함)
     * @Author hanhyeyeon
     */
    public static String encryptAES128ForMySql(String str, String key) {
        Map<String, Object> aes128 = aes128(key);
        String secretKey = (String) aes128.get("secretKey");

        String result;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, MySQLSetKey(secretKey, "UTF-8"));
            result = new String(Hex.encodeHex(cipher.doFinal(str.getBytes("UTF-8")))).toUpperCase();

        } catch (Exception e) {
            result = null;
            LogUtils.logStackTrace(e);
        }

        return result;

    }

    /**
     * MySQL용 AES128 양방향 복호화(MySql 함수 로직과 동일함)
     * @Author hanhyeyeon
     */
    public static String decryptAES128ForMySql(String str, String key) {
        if (StringUtils.isEmpty(str)) {
            return "";
        }
        Map<String, Object> aes128 = aes128(key);
        String secretKey = (String) aes128.get("secretKey");

        String result;
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, MySQLSetKey(secretKey, "UTF-8"));
            result = new String(cipher.doFinal(Hex.decodeHex(str.toCharArray())), StandardCharsets.UTF_8);

        } catch (Exception e) {
            result = null;
            LogUtils.logStackTrace(e);
        }

        return result;
    }

    /**
     * MySQL용 AES128 양방향 암호화/복호화 : KeySpec 생성
     * @Attention 외부 호출 절대 불가!
     * @Author hanhyeyeon
     */
    private static SecretKeySpec MySQLSetKey(final String key, final String encoding) {
        SecretKeySpec keySpec;
        try {
            final byte[] finalKey = new byte[16];
            int i = 0;
            for(byte b : key.getBytes(encoding))
                finalKey[i++%16] ^= b;
            keySpec = new SecretKeySpec(finalKey, "AES");
        } catch(Exception e) {
            keySpec = null;
            LogUtils.logStackTrace(e);
        }

        return keySpec;
    }
}
