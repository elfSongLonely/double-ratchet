package action;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// ����ת����
public class ParseSystemUtil {
    /**
     * ��������ת����16����
     * 
     * @param buf �������ֽ�����
     * @return buf�����16������ʽ
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String strHex = Integer.toHexString(buf[i]);
            if (strHex.length() > 3) {
                sb.append(strHex.substring(6));
            } else {
                if (strHex.length() < 2) {
                    sb.append("0" + strHex);
                } else {
                    sb.append(strHex);
                }
            }
        }
        return sb.toString();
    }

    /**
     * ��16����ת���ɶ�������
     * 
     * @param buf 16�����ֽ�����
     * @return buf����Ķ�������ʽ
     */
    // �׶ˣ����16�����ֽ�����Ϊż��������
    public static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1)
            return null;
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    /**
     * ��intת���ɶ�������
     * 
     * @param n ����
     * @return n�Ķ�������ʽ
     */
    public static byte[] intToButeArray(int n) {
        byte[] byteArray = null;
        try {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            DataOutputStream dataOut = new DataOutputStream(byteOut);
            dataOut.writeInt(n);
            byteArray = byteOut.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArray;
    }

    public static void main(String[] args) {
        String aString = new String("123456");
        byte[] a = parseHexStr2Byte(aString);
        System.out.println(a.toString());
        String bString = new String("123456");
        byte[] b = parseHexStr2Byte(bString);
        System.out.println(b.toString());
        if (a.equals(b))
            System.out.println("1111");

    }
}
