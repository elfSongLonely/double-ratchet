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
     * ��ʼ��ͨ�ŷ����ߵ���Կ
     * 
     * @throws Exception
     */
    public void initKey() throws Exception {
        // ʵ������Կ��������
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        // ��ʼ����Կ��������
        keyPairGenerator.initialize(KEY_SIZE);
        // ������Կ��
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (DHPublicKey) keyPair.getPublic();
        this.privateKey = (DHPrivateKey) keyPair.getPrivate();

    }

    /**
     * ��ʼ��ͨ�Ž����ߵ���Կ
     * 
     * @param key �����߹�Կ
     * @throws Exception
     */
    public void initKey(byte[] key) throws Exception {
        // ���������߹�Կ,ת����Կ����
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(key);
        // ʵ������Կ����
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // ���������߹�Կ
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // �ɷ����߹�Կ������������Կ
        DHParameterSpec dhParameterSpec = ((DHPublicKey) pubKey).getParams();
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(KEY_ALGORITHM);
        keyPairGenerator.initialize(KEY_SIZE);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        this.publicKey = (DHPublicKey) keyPair.getPublic();
        this.privateKey = (DHPrivateKey) keyPair.getPrivate();
    }

    /**
     * ������Կ
     * 
     * @param publicKey ��Կ
     * @param privateKey ˽Կ
     * @return byte[] ������Կ
     * @throws Exception
     */
    public void getSecretKey(byte[] publicKey, byte[] privateKey) throws Exception {
        // ʵ������Կ����
        KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
        // ��ʼ����Կ
        // ��Կ����ת��
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKey);
        // ������Կ
        PublicKey pubKey = keyFactory.generatePublic(x509KeySpec);
        // ��ʼ��˽Կ
        // ��Կ����ת��
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKey);
        // ����˽Կ
        PrivateKey priKey = keyFactory.generatePrivate(pkcs8KeySpec);
        // ʵ����
        KeyAgreement keyAgree = KeyAgreement.getInstance(keyFactory.getAlgorithm());
        // ��ʼ��
        keyAgree.init(priKey);
        keyAgree.doPhase(pubKey, true);
        // ���ɱ�����Կ
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
        System.out.println("���ͷ�Э����Կ:\n" + alice.getNegotiationKey());
        System.out.println("���շ�Э����Կ:\n" + bob.getNegotiationKey());

        bob.initKey(alice.getPublicKey());
        alice.initKey(bob.getPublicKey());
        alice.getSecretKey(bob.getPublicKey(), alice.getPrivateKey());
        bob.getSecretKey(alice.getPublicKey(), bob.getPrivateKey());
        System.out.println("���ͷ�Э����Կ:\n" + alice.getNegotiationKey());
        System.out.println("���շ�Э����Կ:\n" + bob.getNegotiationKey());



    }


}
