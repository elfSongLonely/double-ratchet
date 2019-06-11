package action;

import org.apache.commons.codec.digest.DigestUtils;


public class MD5 {
    /**
     * MD5����
     * 
     * @param plaintext ����
     * @param key ��Կ
     * @return ciphertext ����
     * @throws Exception
     */
    public static String md5(String plaintext, String key) throws Exception {
        // ���ܺ���ַ���
        String ciphertext = DigestUtils.md5Hex(plaintext + key);
        return ciphertext;
    }

    /**
     * MD5��֤����
     * 
     * @param plaintext ����
     * @param key ��Կ
     * @param ciphertext ����
     * @return true/false
     * @throws Exception
     */
    public static boolean verify(String plaintext, String key, String ciphertext) throws Exception {
        // ���ݴ������Կ������֤
        String md5Text = md5(plaintext, key);
        if (md5Text.equalsIgnoreCase(ciphertext)) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        String encryptStr = "tester";
        String key = "1234q67k9m12/45=";
        String ciphertext = md5(encryptStr, key);
        System.out.print("tester���ܺ�õ���" + ciphertext + "\n");
        if (verify(encryptStr, key, ciphertext))
            System.out.println("true");

    }



}
