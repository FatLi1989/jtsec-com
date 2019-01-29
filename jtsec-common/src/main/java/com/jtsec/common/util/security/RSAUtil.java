package com.jtsec.common.util.security;

import com.jtsec.common.Constants.MoitorConstants;
import javax.crypto.Cipher;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;

/**
 * RSA 工具类-提供加密，解密，生成密钥对等方法，用于用户登录时对用户名、密码进行加密传输
 * 需要到http://www.bouncycastle.org下载bcprov-jdk14-123.jar,也可以到http://www.mvnrepository.com 下载
 * @author surpassE
 * 
 */
public class RSAUtil {

	/**
	 * 生成密钥对
	 * @return KeyPair
	 * @throws
	 */
	public static KeyPair generateKeyPair(String path) throws Exception {
		try {
			KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
			// 这个值关系到块加密的大小，可以更改，但是不要太大，否则效率会低
			final int KEY_SIZE = 1024;
			keyPairGen.initialize(KEY_SIZE, new SecureRandom());
			KeyPair keyPair = keyPairGen.generateKeyPair();
			System.out.println(keyPair.getPrivate());
			System.out.println(keyPair.getPublic());			
			//生成秘钥库文件
			saveKeyPair(keyPair, path);
			return keyPair;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 将秘钥库对象转为秘钥库文件
	 * @param kp
	 * @throws Exception
	 */
	public static void saveKeyPair(KeyPair kp, String path) throws Exception {
		FileOutputStream fos = new FileOutputStream(getRsaKeyStore(path));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		// 生成密钥
		oos.writeObject(kp);
		oos.close();
		fos.close();
	}	

	/**
	 * 将秘钥库文件转为秘钥对象
	 * @param path  如果传入值为空（""），则秘钥库文件为默认设置的路径，如果不为空，则为传入的路径
	 * @return 秘钥对象
	 * @throws Exception
	 */
	public static KeyPair getKeyPair(String path) throws Exception {
		FileInputStream fis = new FileInputStream(getRsaKeyStore(path));
		ObjectInputStream oos = new ObjectInputStream(fis);
		KeyPair kp = (KeyPair) oos.readObject();
		oos.close();
		fis.close();
		return kp;
	}	
	
	/**
	 * 获得RSA秘钥存储文件（秘钥库）路径
	 * 
	 * @param path	如果传入值为空（""），则秘钥库文件为默认设置的路径，如果不为空，则为传入的路径
	 * @return  秘钥库文件绝对路径
	 */
	public static String getRsaKeyStore(String path){
//		String rsaKeyStore = "c:/danxiangConf_external/RSAKey.txt";
		String rsaKeyStore = MoitorConstants.WEB_PATH + "shell/RSAKey.txt";
		if( null!=path && !"".equals(path) ){
			rsaKeyStore = path;
		}
//		if(SystemCons.OS_IS_LINUX){
//			rsaKeyStore =  Constants.WEB_PATH + "shell/RSAKey.txt";
//		}
		return rsaKeyStore;
	}	
	


	/**
	 * 生成公钥
	 * @param modulus  模
	 * @param publicExponent  公钥指数
	 * @return RSAPublicKey  RSA加密公钥
	 * @throws Exception
	 */
	public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}

		RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger( modulus), new BigInteger(publicExponent));
		try {
			return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}

	/**
	 * 生成私钥
	 * @param modulus  模
	 * @param privateExponent  私钥指数
	 * @return RSAPrivateKey  RSA加密私钥
	 * @throws Exception
	 */
	public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
		KeyFactory keyFac = null;
		try {
			keyFac = KeyFactory.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
		} catch (NoSuchAlgorithmException ex) {
			throw new Exception(ex.getMessage());
		}

		RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger( modulus), new BigInteger(privateExponent));
		try {
			return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
		} catch (InvalidKeySpecException ex) {
			throw new Exception(ex.getMessage());
		}
	}
	
	/**
	 * 加密
	 * @param pk 加密的密钥
	 * @param data 待加密的明文数据
	 * @return 加密后的数据
	 * @throws Exception
	 */
	public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA",
					new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, pk);
			int blockSize = cipher.getBlockSize();// 获得加密块大小，如：加密前数据为128个byte，而key_size=1024
			// 加密块大小为127
			// byte,加密后为128个byte;因此共有2个加密块，第一个127
			// byte第二个为1个byte
			int outputSize = cipher.getOutputSize(data.length);// 获得加密块加密后块大小
			int leavedSize = data.length % blockSize;
			int blocksSize = leavedSize != 0 ? data.length / blockSize + 1 : data.length / blockSize;
			byte[] raw = new byte[outputSize * blocksSize];
			int i = 0;
			while (data.length - i * blockSize > 0) {
				if (data.length - i * blockSize > blockSize)
					cipher.doFinal(data, i * blockSize, blockSize, raw, i * outputSize);
				else
					cipher.doFinal(data, i * blockSize, data.length - i * blockSize, raw, i * outputSize);
				// 这里面doUpdate方法不可用，查看源代码后发现每次doUpdate后并没有什么实际动作除了把byte[]放到
				// ByteArrayOutputStream中，而最后doFinal的时候才将所有的byte[]进行加密，可是到了此时加密块大小很可能已经超出了
				// OutputSize所以只好用dofinal方法。
				i++;
			}
			return raw;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 解密
	 * @param pk 解密的密钥
	 * @param raw 已经加密的数据
	 * @return 解密后的明文
	 * @throws Exception
	 */
	public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {
		try {
			Cipher cipher = Cipher.getInstance("RSA", new org.bouncycastle.jce.provider.BouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, pk);
			int blockSize = cipher.getBlockSize();
			ByteArrayOutputStream bout = new ByteArrayOutputStream(64);
			int j = 0;
			while (raw.length - j * blockSize > 0) {
				bout.write(cipher.doFinal(raw, j * blockSize, blockSize));
				j++;
			}
			return bout.toByteArray();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}	
	
	/**
	 * 使用默认秘钥库中的私钥，解密数据
	 * @param data  待解密的数据
	 * @return
	 */
	public static String getDecrytValue(String data){		
		String str = "";
		try {
			PrivateKey pk = getKeyPair("").getPrivate();
			str = getDecrytValue(data, pk);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
		
	}

	/**
	 * 使用指定私钥，解密数据
	 * 
	 * @param data  待解密的数据
	 * @param pk  私钥
	 * @return  解密后的数据
	 */
	public static String getDecrytValue(String data, PrivateKey pk){
		StringBuffer sb = new StringBuffer();
		try{
			byte[] buf = hexStringToBytes(data);
			byte[] result = decrypt(pk, buf);
			sb.append(new String(result));
		}catch (Exception e) {
			e.printStackTrace();
		}
		return sb.reverse().toString();
	}	
	
	/**
	 * 使用公钥加密数据
	 * 
	 * @param data  待解密的数据
	 * @param pk  公钥
	 * @return
	 */
	public static String getEncryt(String data, PublicKey pk){
		String str = "";
		try{
			byte[] result = encrypt(pk, data.getBytes());
//			str = StringUtil.byte2HexStr(result);
			str = new String(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}		
	
	
	/**
	 * 使用私钥解密数据（方法存在问题，会出现Bad arguments异常，待解决）
	 * 
	 * @param data  待加密的数据
	 * @param pk  私钥
	 * @return
	 */
	public static String getDecryt(String data, PrivateKey pk){
		String str = "";
		try{
			byte[] buf = hexStringToBytes(data);
			byte[] result = decrypt(pk, buf);
			str = new String(result);
		}catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
	


	
	/**  
	 * 16进制 To byte[] 解决RSA编码错误：Bad arguments 
	 * @param hexString  
	 * @return byte[]  
	 */  
	public static byte[] hexStringToBytes(String hexString) {  
		if (hexString == null || hexString.equals("")) {  
			return null;  
		}  
		hexString = hexString.toUpperCase();  
		int length = hexString.length() / 2;  
		char[] hexChars = hexString.toCharArray();  
		byte[] d = new byte[length];  
		for (int i = 0; i < length; i++) {  
			int pos = i * 2;  
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));  
		}  
		return d;  
	}

	/**  
	 * Convert char to byte  
	 * @param c char  
	 * @return byte  
	 */  
	private static byte charToByte(char c) {  
		return (byte) "0123456789ABCDEF".indexOf(c);  
	}

		
	
    /**
     * 使用模和指数生成RSA公钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static RSAPublicKey getPublicKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
            return (RSAPublicKey) keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
 
    /**
     * 使用模和指数生成RSA私钥
     * 注意：【此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同，如Android默认是RSA
     * /None/NoPadding】
     *
     * @param modulus
     *            模
     * @param exponent
     *            指数
     * @return
     */
    public static RSAPrivateKey getPrivateKey(String modulus, String exponent) {
        try {
            BigInteger b1 = new BigInteger(modulus);
            BigInteger b2 = new BigInteger(exponent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(b1, b2);
            return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
	
    /**
     * 公钥加密
     *
     * @param data
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data, RSAPublicKey publicKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        // 模长
        int key_len = publicKey.getModulus().bitLength() / 8;
        // 加密数据长度 <= 模长-11
        String[] datas = splitString(data, key_len - 11);
        String mi = "";
        //如果明文长度大于模长-11则要分组加密
        for (String s : datas) {
            mi += bcd2Str(cipher.doFinal(s.getBytes()));
        }
        return mi;
    }

    /**
     * 私钥解密
     *
     * @param data
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String data, RSAPrivateKey privateKey)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //模长
        int key_len = privateKey.getModulus().bitLength() / 8;
        byte[] bytes = data.getBytes();
        byte[] bcd = ASCII_To_BCD(bytes, bytes.length);
//        System.err.println(bcd.length);
        //如果密文长度大于模长则要分组解密
        String ming = "";
        byte[][] arrays = splitArray(bcd, key_len);
        for(byte[] arr : arrays){
            ming += new String(cipher.doFinal(arr));
        }
        return ming;
    }
    
    /**
     * ASCII码转BCD码
     *
     */
    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }
    
    public static byte asc_to_bcd(byte asc) {
        byte bcd;
 
        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }
    
    /**
     * BCD转字符串
     */
    public static String bcd2Str(byte[] bytes) {
        char temp[] = new char[bytes.length * 2], val;
 
        for (int i = 0; i < bytes.length; i++) {
            val = (char) (((bytes[i] & 0xf0) >> 4) & 0x0f);
            temp[i * 2] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
 
            val = (char) (bytes[i] & 0x0f);
            temp[i * 2 + 1] = (char) (val > 9 ? val + 'A' - 10 : val + '0');
        }
        return new String(temp);
    }
    
    /**
     * 拆分字符串
     */
    public static String[] splitString(String string, int len) {
        int x = string.length() / len;
        int y = string.length() % len;
        int z = 0;
        if (y != 0) {
            z = 1;
        }
        String[] strings = new String[x + z];
        String str = "";
        for (int i=0; i<x+z; i++) {
            if (i==x+z-1 && y!=0) {
                str = string.substring(i*len, i*len+y);
            }else{
                str = string.substring(i*len, i*len+len);
            }
            strings[i] = str;
        }
        return strings;
    }
    
    /**
     *拆分数组
     */
    public static byte[][] splitArray(byte[] data,int len){
        int x = data.length / len;
        int y = data.length % len;
        int z = 0;
        if(y!=0){
            z = 1;
        }
        byte[][] arrays = new byte[x+z][];
        byte[] arr;
        for(int i=0; i<x+z; i++){
            arr = new byte[len];
            if(i==x+z-1 && y!=0){
                System.arraycopy(data, i*len, arr, 0, y);
            }else{
                System.arraycopy(data, i*len, arr, 0, len);
            }
            arrays[i] = arr;
        }
        return arrays;
    }
    
    /**
     * 生成公钥和私钥
     * @throws NoSuchAlgorithmException
     *
     */
    public static HashMap<String, Object> getKeys() throws NoSuchAlgorithmException{
        HashMap<String, Object> map = new HashMap<String, Object>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024);
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        map.put("public", publicKey);
        map.put("private", privateKey);
        return map;
    }
    
	/**
	 * * *
	 * 
	 * @param args *
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		String rsaKeyStore = "c:/danxiangConf_external/RSAKey.txt";
		//生成秘钥库文件
//		KeyPair keyPair = RSAUtil.generateKeyPair("");
//		KeyPair keyPair = RSAUtil.generateKeyPair(rsaKeyStore);	
		KeyPair keyPair = getKeyPair(rsaKeyStore);
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
		System.out.println(publicKey);
		System.out.println(privateKey);
		System.out.println("pubKey-Exponent:" + publicKey.getPublicExponent().toString(16));
		System.out.println("pubKey-Modulus:" + publicKey.getModulus().toString(16));
		RSAUtil.test2(keyPair);		
	}

	
	public static void test1(KeyPair keyPair) throws Exception {
		String test = "hello world";
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();

		byte[] en_test = encrypt(publicKey, test.getBytes());
		System.out.println("Encrypt:" + new String(en_test));
		byte[] de_test = decrypt(privateKey, en_test);
		System.out.println("Decrypt:" + new String(de_test));
		
	}
	
	public static void test2(KeyPair keyPair) throws Exception {
		String test = "hello world";		
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
	
		String enTest = RSAUtil.getEncryt(test, publicKey);
		System.out.println("Encrypt:" + enTest);
		String deTest = RSAUtil.getDecryt(enTest, privateKey);
		System.out.println("Decrypt:" + deTest);	
	}
	
	public static void test3(KeyPair keyPair) throws Exception {
		String test = "hello world";
		RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
		RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
	
		String enTest = RSAUtil.encryptByPublicKey(test, publicKey);
		System.out.println("Encrypt:" + enTest);
		String deTest = RSAUtil.decryptByPrivateKey(enTest, privateKey);
		System.out.println("Decrypt:" + deTest);

	}
		
	
}
