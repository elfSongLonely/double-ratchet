package DHRatchet;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DHParameterSpec;
import org.apache.commons.codec.binary.Base64;

public class DH {
    private static final String KEY_ALGORITHM = "DH";
    private static final String SELECT_ALGORITHM = "AES";
    private static final int KEY_SIZE = 512;
    public DHPublicKey publicKey;
    private DHPrivateKey privateKey;
    private String negotiationKey;

    public byte[] getPublicKey() {
        return this.publicKey.getEncoded();
    }

    public byte[] getPrivateKey() {
        return this.privateKey.getEncoded();
    }

    public String getNegotiationKey() {
        return this.negotiationKey;
    }

    /**
     * 初始化通信发起者的密钥
     * 
     * @throws Exception
     */
    public void initKey() throws Exception {
        // 实例化密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥对生成器
        keyPairGenerator.initialize(KEY_SIZE);
        // 生成密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (DHPublicKey) keyPair.getPublic();
        this.privateKey = (DHPrivateKey) keyPair.getPrivate();

    }

    /**
     * 初始化通信接收者的密钥
     * 
     * @param key 发起者公钥
     * @throws Exception
     */
    public void initKey(byte[] key) throws Exception {
        // 解析发起者公钥,转换公钥材料
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 产生发起者公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 由发起者公钥构建接收者密钥
        DHParameterSpec dhParameterSpec = ((DHPublicKey) pubKey).getParams();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (DHPublicKey) keyPair.getPublic();
        this.privateKey = (DHPrivateKey) keyPair.getPrivate();
    }

    /**
     * 构建密钥
     * 
     * @param publicKey 公钥
     * @param privateKey 私钥
     * @return byte[] 本地密钥
     * @throws Exception
     */
    public void getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
        // 实例化密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // 初始化公钥
        // 密钥材料转换
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        // 产生公钥
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // 初始化私钥
        // 密钥材料转换
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        // 产生私钥
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // 实例化
        KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory.getAlgorithm());
        // 初始化
        keyAgree.init(priKey);
        keyAgree.doPhase(pubKey, true);
        // 生成本地密钥
        SecretKey secretKey = keyAgree.generateSecret(SELECT_ALGORITHM);
        this.negotiationKey = Base64.encodeBase64String(secretKey.getEncoded());
    }

    public static void main(String[] args) throws Exception {
        DH alice = new DH();
        alice.initKey();
        DH bob = new DH();
        bob.initKey(alice.getPublicKey());
        alice.getSecretKey(bob.getPublicKey(), alice.getPrivateKey());
        bob.getSecretKey(alice.getPublicKey(), bob.getPrivateKey());
        System.out.println("发送方协商密钥:\n" + alice.getNegotiationKey());
        System.out.println("接收方协商密钥:\n" + bob.getNegotiationKey());

        bob.initKey(alice.getPublicKey());
        alice.initKey(bob.getPublicKey());
        alice.getSecretKey(bob.getPublicKey(), alice.getPrivateKey());
        bob.getSecretKey(alice.getPublicKey(), bob.getPrivateKey());
        System.out.println("发送方协商密钥:\n" + alice.getNegotiationKey());
        System.out.println("接收方协商密钥:\n" + bob.getNegotiationKey());



    }


}
