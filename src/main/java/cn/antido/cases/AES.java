package cn.antido.cases;

import java.nio.ByteBuffer;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;

public class AES {
  public static void main(String[] args) throws Exception {

  }

  private static void testAes() throws Exception {
    Random random = new Random();
    byte[] iv = new byte[16];
    random.nextBytes(iv);
    //指定使用AES加密
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    //指定生成的密钥长度为128
    keyGenerator.init(128);
    Key key = keyGenerator.generateKey();

    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    //cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] bytes = cipher.doFinal("12341234123412341234123412341234".getBytes());
    System.out.println("AES加密： " + Base64.getEncoder().encodeToString(bytes));

    ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + bytes.length);
    byteBuffer.put(iv);
    byteBuffer.put(bytes);
    byte[] cipherMessage = byteBuffer.array();


    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(cipherMessage,0,16));
    bytes = cipher.doFinal(cipherMessage,iv.length,cipherMessage.length - iv.length);
    System.out.println("AES解密： " + new String(bytes));
  }

  private static void testAes2() throws Exception {
    Random random = new Random();
    byte[] iv = new byte[16];
    random.nextBytes(iv);
    //指定使用AES加密
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    //使用KeyGenerator生成key，参数与获取cipher对象的algorithm必须相同
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    //指定生成的密钥长度为128
    keyGenerator.init(128);
    Key key = keyGenerator.generateKey();

    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    //cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
    byte[] bytes = cipher.doFinal("12341234123412341234123412341234".getBytes());
    System.out.println("AES加密： " + Base64.getEncoder().encodeToString(bytes));

//    ByteBuffer byteBuffer = ByteBuffer.allocate(iv.length + bytes.length);
//    byteBuffer.put(iv);
//    byteBuffer.put(bytes);
//    byte[] cipherMessage = byteBuffer.array();

    byte[] iv2 = new byte[16];
    random.nextBytes(iv);
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(bytes,0,16));
    bytes = cipher.doFinal(bytes);
    System.out.println("AES解密： " + new String(bytes));
  }
}
