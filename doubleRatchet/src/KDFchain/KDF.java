package KDFchain;

import java.nio.ByteBuffer;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import action.ConsistentHashingWithoutVirtualNode;
import action.ParseSystemUtil;


public class KDF {
    private String rootKey = "5d3c419c11b5c7583cad87a9570df74c";


    public String getRootKey() {
        return this.rootKey;
    }



    public String HKDF(String salt) throws Exception {
        // byte[] data = ParseSystemUtil.parseHexStr2Byte(salt);
        byte[] data = salt.getBytes("ISO-8859-1");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(data, "HmacSHA256"));
        // 执行消息摘要
        int hashvalue = ConsistentHashingWithoutVirtualNode.getHash(rootKey);
        byte[] value = ParseSystemUtil.intToButeArray(hashvalue);
        byte[] prk = mac.doFinal(value);
        byte[] blockN = new byte[0];
        int remainingBytes = 32;
        int iterations = (int) Math.ceil((double) remainingBytes / (double) value.length);
        ByteBuffer result = ByteBuffer.allocate(remainingBytes);
        int stepSize;
        for (int i = 0; i < iterations; i++) {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(prk, "HmacSHA256"));
            mac.update(blockN);
            mac.update((byte) (i + 1));
            blockN = mac.doFinal();
            stepSize = Math.min(remainingBytes, blockN.length);
            result.put(blockN, 0, stepSize);
            remainingBytes -= stepSize;
        }

        byte[] array = result.array();
        result.clear();
        byte[] chainKey = new byte[16];
        byte[] exportKey = new byte[16];
        System.arraycopy(array, 0, exportKey, 0, 16);
        System.arraycopy(array, 16, chainKey, 0, 16);
        // rootKey = ParseSystemUtil.parseByte2HexStr(chainKey);
        // System.out.println("chainKey:" + ParseSystemUtil.parseByte2HexStr(chainKey));
        rootKey = new String(chainKey, "ISO-8859-1");
        // return ParseSystemUtil.parseByte2HexStr(exportKey);
        // System.out.println("exportKey:" + ParseSystemUtil.parseByte2HexStr(exportKey));
        return new String(exportKey, "ISO-8859-1");
    }

    public static void main(String[] args) {
        try {
            KDF kdf = new KDF();
            KDF kdf2 = new KDF();
            String string = "n2345678901234567890123456789012345678=";
            System.out.println("KDF第一次：");
            String string1 = kdf.HKDF(string);
            System.out.println("KDF2第一次：");
            String string2 = kdf2.HKDF(string);
            if (string1.equals(string2))
                System.out.println("第一次通信成功");
            System.out.println("KDF第二次：");
            string1 = kdf.HKDF(string);
            System.out.println("KDF2第二次：");
            string2 = kdf2.HKDF(string);
            if (string1.equals(string2))
                System.out.println("第二次通信成功");
            System.out.println("KDF第三次：");
            string1 = kdf.HKDF(string);
            System.out.println("KDF2第三次：");
            string2 = kdf2.HKDF(string);
            if (string1.equals(string2))
                System.out.println("第三次通信成功");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
