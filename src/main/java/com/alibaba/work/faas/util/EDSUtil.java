package com.alibaba.work.faas.util;
 
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
 
import com.aliyuncs.utils.StringUtils;
 
import java.security.SecureRandom;
import org.apache.commons.codec.binary.Base64;
import javax.crypto.SecretKey;
import java.io.IOException;
import javax.crypto.Cipher;
 
public class DESUtil {
private final static String DES = "DES";
 
public static void main(String[] args) {
String data = "XXX";
String key = "yida12#$";
String encryptStr = encrypt(data, key);
System.out.println("加密后的字符串:" + encryptStr);
String decryptStr = decrypt(encryptStr, key);
System.out.println("解密后的字符串:" + decryptStr);
}
 
/**
* Description 根据键值进行加密
*
* @param data
* @param key 加密键byte数组
* @return
* @throws Exception
*/
public static String encrypt(String data, String key) {
    if (!StringUtils.isEmpty(data) && !StringUtils.isEmpty(key)) {
        try {
            byte[] bt = encrypt(data.getBytes(), key.getBytes());
            String strs = Base64.encodeBase64String(bt);
            return strs;
        } catch (Exception var4) {
            System.out.println("加密遇到错误:");
            var4.printStackTrace();
            return null;
        }
    } else {
        System.out.println("入参为空,data:" + data + ",key=" + key);
        return null;
    }
}
 
/**
* Description 根据键值进行解密
*
* @param data
* @param key 加密键byte数组
* @return
* @throws IOException
* @throws Exception
*/
public static String decrypt(String data, String key) {
    if (!StringUtils.isEmpty(data) && !StringUtils.isEmpty(key)) {
        try {
            byte[] dataNew = Base64.decodeBase64(data);
            byte[] bt = decrypt(dataNew, key.getBytes());
            return new String(bt);
        } catch (Exception var4) {
            System.out.println("解密遇到错误:");
            var4.printStackTrace();
            return null;
        }
    } else {
        System.out.println("入参为空,data:" + data + ",key=" + key);
        return null;
    }
}
/**
* Description 根据键值进行加密
*
* @param data
* @param key 加密键byte数组
* @return
* @throws Exception
*/
private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
// 生成一个可信任的随机数源
SecureRandom sr = new SecureRandom();
 
// 从原始密钥数据创建DESKeySpec对象
DESKeySpec dks = new DESKeySpec(key);
 
// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
SecretKey securekey = keyFactory.generateSecret(dks);
 
// Cipher对象实际完成加密操作
Cipher cipher = Cipher.getInstance(DES);
 
// 用密钥初始化Cipher对象
//cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
cipher.init(1, securekey, sr);
 
return cipher.doFinal(data);
}
 
 
/**
* Description 根据键值进行解密
*
* @param data
* @param key 加密键byte数组
* @return
* @throws Exception
*/
private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
// 生成一个可信任的随机数源
SecureRandom sr = new SecureRandom();
 
// 从原始密钥数据创建DESKeySpec对象
DESKeySpec dks = new DESKeySpec(key);
 
// 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
SecretKey securekey = keyFactory.generateSecret(dks);
 
// Cipher对象实际完成解密操作
Cipher cipher = Cipher.getInstance(DES);
 
// 用密钥初始化Cipher对象
//cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
cipher.init(2, securekey, sr);
return cipher.doFinal(data);
}
}
 