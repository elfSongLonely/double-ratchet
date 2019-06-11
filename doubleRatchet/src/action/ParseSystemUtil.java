package action;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

// 进制转换类
public class ParseSystemUtil {
    /**
     * 将二进制转换成16进制
     * 
     * @param buf 二进制字节数组
     * @return buf数组的16进制形式
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
     * 将16进制转换成而二进制
     * 
     * @param buf 16进制字节数组
     * @return buf数组的二进制形式
     */
    // 弊端：如果16进制字节数组为偶数就抛弃
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
     * 将int转换成而二进制
     * 
     * @param n 整型
     * @return n的二进制形式
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
