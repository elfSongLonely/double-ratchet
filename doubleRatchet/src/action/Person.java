package action;

import java.util.Random;
import DHRatchet.DH;
import KDFchain.KDF;

public class Person {
    private String firstSalt;
    private DH DHChain;
    private KDF KDFChain;
    private String messageKey;

    public Person() {
        DHChain = new DH();
        KDFChain = new KDF();
    }

    public String getFirstSalt() {
        return firstSalt;
    }

    public String getMessageKey() {
        return this.messageKey;
    }

    public static void consult(Person A, Person B) {
        String string = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890+/";
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < 43; i++) {
            int number = random.nextInt(64);
            stringBuffer.append(string.charAt(number));
        }
        A.firstSalt = stringBuffer.toString();
        B.firstSalt = stringBuffer.toString();
    }

    public void initKey() throws Exception {
        DHChain.initKey();
    }

    public byte[] DHpublicKey() {
        return DHChain.getPublicKey();
    }

    public byte[] DHprivateKey() {
        return DHChain.getPrivateKey();
    }

    public String getNegotiationKey() {
        return DHChain.getNegotiationKey();
    }

    public void getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
        DHChain.getSecretKey(publicKey, privateKey);
    }

    public void initKey(byte[] key) throws Exception {
        DHChain.initKey(key);
    }

    public void HKDF(String key) throws Exception {
        messageKey = KDFChain.HKDF(key);
    }

    public byte[] AESencrypt(String content, String password) {
        return AES.encrypt(content, password);
    }

    public byte[] AESdecrypt(byte[] content, String password) {
        return AES.decrypt(content, password);
    }

    public static void main(String[] args) throws Exception {
        Person alice = new Person();
        Person bob = new Person();
        consult(alice, bob);

        String message1 = "Hello,I'm Alice.";
        alice.HKDF(alice.getFirstSalt());
        alice.initKey();
        byte[] encrypt = alice.AESencrypt(message1, alice.getMessageKey());
        String hexStrResult = ParseSystemUtil.parseByte2HexStr(encrypt);
        System.out.println("Alice加密生成的密文（16进制）：" + hexStrResult);

        bob.HKDF(bob.getFirstSalt());
        byte[] decrypt = bob.AESdecrypt(encrypt, bob.getMessageKey());
        System.out.println("BOb解密后的内容：" + new String(decrypt, "utf-8"));
        bob.initKey(alice.DHpublicKey());
        String message2 = "Hello,I'm Bob.Pardon?";
        bob.getSecretKey(alice.DHpublicKey(), bob.DHprivateKey());
        bob.HKDF(bob.getNegotiationKey());
        encrypt = bob.AESencrypt(message2, bob.getMessageKey());
        hexStrResult = ParseSystemUtil.parseByte2HexStr(encrypt);
        System.out.println("Bob加密生成的密文（16进制）：" + hexStrResult);

        alice.getSecretKey(bob.DHpublicKey(), alice.DHprivateKey());
        alice.HKDF(alice.getNegotiationKey());
        decrypt = alice.AESdecrypt(encrypt, alice.getMessageKey());
        System.out.println("Alice解密后的内容：" + new String(decrypt, "utf-8"));
        alice.initKey(bob.DHpublicKey());
        alice.getSecretKey(bob.DHpublicKey(), alice.DHprivateKey());
        alice.HKDF(alice.getNegotiationKey());
        encrypt = alice.AESencrypt(message1, alice.getMessageKey());
        hexStrResult = ParseSystemUtil.parseByte2HexStr(encrypt);
        System.out.println("Alice加密生成的密文（16进制）：" + hexStrResult);

        bob.getSecretKey(alice.DHpublicKey(), bob.DHprivateKey());
        bob.HKDF(bob.getNegotiationKey());
        decrypt = bob.AESdecrypt(encrypt, bob.getMessageKey());
        System.out.println("BOb解密后的内容：" + new String(decrypt, "utf-8"));



    }

}
