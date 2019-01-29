package com.jtsec.common.util.security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.SecureRandom;
import java.security.Security;

/**
 * DES对称加密操作,如果在加密或是解密的操作过程中出现问题，可以使用bak中DESUtil替换
 * 此工具类中包含内部类	final class BASE64Encoder
 * 此工具类用于对升级包进行加密、获得配置文件中中文转义字符
 * @author surpassE
 *
 */
public class DESUtil { 

	private static Key key; 
	private static String KEYAPSS = "SURPASS1990EVERYTHING01CHAOYUEYIQIE16JTSEC";
	private static String FILE_SUFFIX = ".se";
	static{
		createKey();
	}
	/** 
	 * 根据参数生成 KEY 
	 */  
	private static void createKey(){
		try {
			KeyGenerator generator = KeyGenerator.getInstance("DES");  
			generator.init(new SecureRandom(KEYAPSS.getBytes()));  
			key = generator.generateKey();  
			generator = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}  

	/**
	 * 对字符串串进行加密操作
	 * @param strMing
	 * @return
	 */
	public static String encryptStr(String strMing) {  
		byte [] byteMi = null ;  
		byte [] byteMing = null ;  
		String strMi = "" ;  
		try {  
			byteMing = strMing.getBytes( "UTF8" );  
			byteMi = encryptByte(byteMing);  
//			strMi = base64en.encode(byteMi);
			strMi = BASE64Encoder.encode(byteMi);
		} catch (Exception e) {  
			throw new RuntimeException(  "Error initializing SqlMap class. Cause: " + e);  
		} finally {  
			byteMing = null ;  
			byteMi = null ;  
		}  
		return strMi;  
	}  

	/** 
	 * 对加密的字符串进行解密的操作
	 * @param strMi 
	 * @return 
	 */  
	public static String decryptStr(String strMi) { 
		byte [] byteMing = null ;  
		byte [] byteMi = null ;  
		String strMing = "" ;  
		try {  
//			byteMi = BASE64Encoder.decodeBuffer(strMi);
			byteMi = BASE64Encoder.decode(strMi);
			byteMing = decryptByte(byteMi);  
			strMing = new String(byteMing, "UTF8" );  
		} catch (Exception e) {  
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);
		} finally {  
			byteMing = null ;  
			byteMi = null ;  
		}  
		return strMing;  
	}  
	
	/** 
	 * 对字节数组进行加密操作
	 * @param byteS 
	 * @return 
	 */  
	private static byte [] encryptByte( byte [] byteS) {  
		byte [] byteFina = null ;  
		Cipher cipher;  
		try {  
			cipher = Cipher.getInstance ( "DES" );  
			cipher.init(Cipher. ENCRYPT_MODE , key );  
			byteFina = cipher.doFinal(byteS);  
		} catch (Exception e) {  
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);  
		} finally {  
			cipher = null ;  
		}  
		return byteFina;  
	}  

	/** 
	 * 对加密的字节数组进行解密操作
	 * @param byteD 
	 * @return 
	 */  
	private static byte [] decryptByte( byte [] byteD) {  
		Cipher cipher;  
		byte [] byteFina = null ;  
		try {  
			cipher = Cipher.getInstance ("DES");  
			cipher.init(Cipher. DECRYPT_MODE , key);  
			byteFina = cipher.doFinal(byteD);  
		} catch (Exception e) {  
			throw new RuntimeException("Error initializing SqlMap class. Cause: " + e);  
		} finally {  
			cipher = null ;  
		}  
		return byteFina;  
	}  

	/** 
	 * 文件filePath进行加密并保存目标文件到filePath+".se" 
	 * @param filePath 要加密的文件路径 如 c:/test/srcFile.txt
	 * @return 返回加密后的文件路径即与要加密的文件同一路径,只是文件后缀名称加上.se(此文件名称后缀名自定义不建议更改)
	 */  
	public static String encryptFile(String filePath) throws Exception {
		String destFilePath = filePath + FILE_SUFFIX;
		Cipher cipher = Cipher.getInstance ("DES");  
		cipher.init(Cipher.ENCRYPT_MODE, key);  
		InputStream is = new FileInputStream(filePath);  
		OutputStream out = new FileOutputStream(destFilePath);  
		CipherInputStream cis = new CipherInputStream(is, cipher);  
		byte [] buffer = new byte [1024];  
		int r;  
		while ((r = cis.read(buffer)) > 0) {  
			out.write(buffer, 0, r);  
		}  
		cis.close();  
		is.close();  
		out.close();
		return destFilePath;
	}  

	/** 
	 * 文件采用 DES算法解密文件 
	 * @param filePath  已加密的文件 如 c:/xxx.txt.se 加密的文件必须是以.se结尾的文件名称
	 * @return 解密后的文件路径，解密后的文件名称不包含.se后缀
	 */  
	public static String decryptFile(String filePath) throws Exception { 
		String destFilePath = filePath.substring(0, filePath.length() - FILE_SUFFIX.length());
		Cipher cipher = Cipher.getInstance ("DES");  
		cipher.init(Cipher. DECRYPT_MODE , key );  
		InputStream is = new FileInputStream(filePath);  
		OutputStream out = new FileOutputStream(destFilePath);  
		CipherOutputStream cos = new CipherOutputStream(out, cipher);  
		byte [] buffer = new byte [1024];  
		int r;  
		while ((r = is.read(buffer)) >= 0) {  
			cos.write(buffer, 0, r);  
		}  
		cos.close();  
		out.close();  
		is.close();
		return destFilePath;
	}  

	public static void main(String[] args) throws Exception {  
		// DES 加密文件
		//		des.encryptFile("F:/demo/RSAKey.txt", "F:/demo/RSAKey.txt.se");
		//		DESUtil.encryptFile("F:/demo/web_ROOT.bin", "F:/demo/web_ROOT.bin.se");
				DESUtil.encryptFile("G:/encrypt/demo/web_ROOT.bin");
		// DES 解密文件
		//		des.decryptFile("F:/demo/RSAKey.txt.se", "F:/demo/RSAKey.txt");
		//		DESUtil.decryptFile("F:/demo/web_ROOT.bin.se", "F:/demo/web_ROOT.bin");
//				DESUtil.decryptFile("G:/encrypt/demo/hello.txt.se");


		//		String str1 = " 要加密的字符串 test" ;  
		//		// DES 加密字符串  
		//		String str2 = des.encryptStr(str1);  
		//		// DES 解密字符串  
		//		String deStr = des.decryptStr(str2);  
		//		System. out .println( " 加密前： " + str1);  
		//		System. out .println( " 加密后： " + str2);  
		//		System. out .println( " 解密后： " + deStr);  
	} 
	
	//===================== 以下方法用于远程配置中对数据进行加密的操作方法===========================
	/** 字符串默认键值     */
	private static String strDefaultKey = "national";
	/** 加密工具     */
	private static Cipher encryptCipher = null;
	/** 解密工具     */
	private static Cipher decryptCipher = null;

	/**  
	 * 默认构造方法，使用默认密钥  
	 * @throws Exception  
	 */
	public DESUtil() throws Exception {
		this(strDefaultKey);
	}

	/**  
	 * 指定密钥构造方法  
	 * @param strKey 指定的密钥  
	 * @throws Exception  
	 */
	public DESUtil(String strKey) throws Exception {}

	/**  
	 * 将byte数组转换为表示16进制值的字符串， 如：byte[]{8,18}转换为：0813， 和public static byte[]  
	 * hexStr2ByteArr(String strIn) 互为可逆的转换过程  
	 * @param arrB 需要转换的byte数组  
	 * @return 转换后的字符串  
	 * @throws Exception   本方法不处理任何异常，所有异常全部抛出  
	 */
	public static String byteArr2HexStr(byte[] arrB) throws Exception {
		int iLen = arrB.length;
		// 每个byte用两个字符才能表示，所以字符串的长度是数组长度的两倍   
		StringBuffer sb = new StringBuffer(iLen * 2);
		for (int i = 0; i < iLen; i++) {
			int intTmp = arrB[i];
			// 把负数转换为正数   
			while (intTmp < 0) {
				intTmp = intTmp + 256;
			}
			// 小于0F的数需要在前面补0   
			if (intTmp < 16) {
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}

	/**  
	 * 将表示16进制值的字符串转换为byte数组， 和public static String byteArr2HexStr(byte[] arrB)  
	 * 互为可逆的转换过程  
	 * @param strIn   需要转换的字符串  
	 * @return 转换后的byte数组  
	 * @throws Exception  本方法不处理任何异常，所有异常全部抛出  
	 * @author <a href="mailto:leo841001@163.com">LiGuoQing</a>  
	 */
	public static byte[] hexStr2ByteArr(String strIn) throws Exception {
		byte[] arrB = strIn.getBytes();
		int iLen = arrB.length;
		// 两个字符表示一个字节，所以字节数组长度是字符串长度除以2   
		byte[] arrOut = new byte[iLen / 2];
		for (int i = 0; i < iLen; i = i + 2) {
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}

	/**  
	 * 加密字节数组  
	 * @param arrB 需加密的字节数组  
	 * @return 加密后的字节数组  
	 * @throws Exception  
	 */
	public static byte[] encrypt(byte[] arrB) throws Exception {
		if(encryptCipher == null){
			encryptCipher = Cipher.getInstance("DES");
			encryptCipher.init(Cipher.ENCRYPT_MODE, getKeyValue());
		}

		return encryptCipher.doFinal(arrB);
	}

	/**  
	 * 加密字符串  
	 * @param strIn 需加密的字符串  
	 * @return 加密后的字符串  
	 * @throws Exception  
	 */
	public static String encryptData(String data) {
		String msg = "";
		try {
			msg = byteArr2HexStr(encrypt(data.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	/**  
	 * 解密字节数组  
	 * @param arrB 需解密的字节数组  
	 * @return 解密后的字节数组  
	 * @throws Exception  
	 */
	public static byte[] decrypt(byte[] arrB) throws Exception {
		if(decryptCipher == null){
			decryptCipher = Cipher.getInstance("DES");
			decryptCipher.init(Cipher.DECRYPT_MODE, getKeyValue());
		}
		return decryptCipher.doFinal(arrB);
	}

	/**  
	 * 解密字符串  
	 * @param strIn  需解密的字符串  
	 * @return 解密后的字符串  
	 * @throws Exception  
	 */
	public static String decryptData(String data) {
		String msg = "";
		try {
			msg = new String(decrypt(hexStr2ByteArr(data)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return msg;
	}

	@SuppressWarnings("restriction")
	public static Key getKeyValue() throws Exception{
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		return getKey(KEYAPSS.getBytes());
	}

	/**  
	 * 从指定字符串生成密钥，密钥所需的字节数组长度为8位 不足8位时后面补0，超出8位只取前8位  
	 * @param arrBTmp   构成该字符串的字节数组  
	 * @return 生成的密钥  
	 * @throws Exception
	 */
	private static Key getKey(byte[] arrBTmp) throws Exception {
		// 创建一个空的8位字节数组（默认值为0）   
		byte[] arrB = new byte[8];
		// 将原始字节数组转换为8位   
		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}
		// 生成密钥   
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
		return key;
	}
	
}


/**
 * BASE64Encoder是import sun.misc.BASE64Decoder和 import sun.misc.BASE64Encoder的替代工具类
 * 导入import sun.misc.BASE64Decoder和 import sun.misc.BASE64Encoder在编译时会出现"sun.misc.BASE64Decoder 是 Sun 的专用 API，可能会在未来版本中删除"警告
 * 在maven旧的版本中执行打包的操作就不会成功
 * @author surpassE
 *
 */
final class BASE64Encoder {
    /** 
     * Base64编码表。
     */
    private static final char[] BASE64CODE =
        {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
            'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/',};
    
    /** 
     * Base64解码表。
     */
    private static final byte[] BASE64DECODE =
        {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
            -1, -1, -1,
            -1,
            -1, // 注意两个63，为兼容SMP，
            -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 63,
            -1,
            63, // “/”和“-”都翻译成63。
            52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, 0, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11,
            12, 13,
            14, // 注意两个0：
            15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1,
            -1, // “A”和“=”都翻译成0。
            -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51,
            -1, -1, -1, -1, -1,};
    
    private static final int HEX_255 = 0x0000ff;
    
    private static final int HEX_16515072 = 0xfc0000;
    
    private static final int HEX_258048 = 0x3f000;
    
    private static final int HEX_4032 = 0xfc0;
    
    private static final int HEX_63 = 0x3f;
    
    private static final int HEX_16711680 = 0xff0000;
    
    private static final int HEX_65280 = 0x00ff00;
    
    private static final int NUMBER_TWO = 2;
    
    private static final int NUMBER_THREE = 3;
    
    private static final int NUMBER_FOUR = 4;
    
    private static final int NUMBER_SIX = 6;
    
    private static final int NUMBER_EIGHT = 8;
    
    private static final int NUMBER_TWELVE = 12;
    
    private static final int NUMBER_SIXTEEN = 16;
    
    private static final int NUMBER_EIGHTEEN = 18;
    
    /**
     * 构造方法私有化，防止实例化。   
     */
    private BASE64Encoder(){}
    
    /** 
     * Base64编码。将字节数组中字节3个一组编码成4个可见字符。 
     * 
     * @param b 
     *            需要被编码的字节数据。 
     * @return 编码后的Base64字符串。 
     */
    public static String encode(byte[] b)
    {
        int code = 0;
        
        // 按实际编码后长度开辟内存，加快速度
        StringBuffer sb = new StringBuffer(((b.length - 1) / NUMBER_THREE) << NUMBER_TWO + NUMBER_FOUR);
        
        // 进行编码
        for (int i = 0; i < b.length; i++)
        {
            code |=
                (b[i] << (NUMBER_SIXTEEN - i % NUMBER_THREE * NUMBER_EIGHT))
                    & (HEX_255 << (NUMBER_SIXTEEN - i % NUMBER_THREE * NUMBER_EIGHT));
            if (i % NUMBER_THREE == NUMBER_TWO || i == b.length - 1)
            {
                sb.append(BASE64CODE[(code & HEX_16515072) >>> NUMBER_EIGHTEEN]);
                sb.append(BASE64CODE[(code & HEX_258048) >>> NUMBER_TWELVE]);
                sb.append(BASE64CODE[(code & HEX_4032) >>> NUMBER_SIX]);
                sb.append(BASE64CODE[code & HEX_63]);
                code = 0;
            }
        }
        
        // 对于长度非3的整数倍的字节数组，编码前先补0，编码后结尾处编码用=代替，
        // =的个数和短缺的长度一致，以此来标识出数据实际长度
        if (b.length % NUMBER_THREE > 0)
        {
            sb.setCharAt(sb.length() - 1, '=');
        }
        if (b.length % NUMBER_THREE == 1)
        {
            sb.setCharAt(sb.length() - NUMBER_TWO, '=');
        }
        return sb.toString();
    }
    
    /** 
     * Base64解码。 
     * 
     * @param code 
     *            用Base64编码的ASCII字符串 
     * @return 解码后的字节数据    
     */
    public static byte[] decode(String code)
    {
        // 检查参数合法性
        if (code == null)
        {
            return null;
        }
        int len = code.length();
        if (len % NUMBER_FOUR != 0)
        {
            throw new IllegalArgumentException("Base64 string length must be 4*n");
        }
        if (code.length() == 0)
        {
            return new byte[0];
        }
        
        // 统计填充的等号个数
        int pad = 0;
        if (code.charAt(len - 1) == '=')
        {
            pad++;
        }
        if (code.charAt(len - NUMBER_TWO) == '=')
        {
            pad++;
        }
        
        // 根据填充等号的个数来计算实际数据长度
        int retLen = len / NUMBER_FOUR * NUMBER_THREE - pad;
        
        // 分配字节数组空间
        byte[] ret = new byte[retLen];
        
        // 查表解码
        char ch1, ch2, ch3, ch4;
        int i;
        for (i = 0; i < len; i += NUMBER_FOUR)
        {
            int j = i / NUMBER_FOUR * NUMBER_THREE;
            ch1 = code.charAt(i);
            ch2 = code.charAt(i + 1);
            ch3 = code.charAt(i + NUMBER_TWO);
            ch4 = code.charAt(i + NUMBER_THREE);
            int tmp =
                (BASE64DECODE[ch1] << NUMBER_EIGHTEEN) | (BASE64DECODE[ch2] << NUMBER_TWELVE)
                    | (BASE64DECODE[ch3] << NUMBER_SIX) | (BASE64DECODE[ch4]);
            ret[j] = (byte)((tmp & HEX_16711680) >> NUMBER_SIXTEEN);
            if (i < len - NUMBER_FOUR)
            {
                ret[j + 1] = (byte)((tmp & HEX_65280) >> NUMBER_EIGHT);
                ret[j + NUMBER_TWO] = (byte)((tmp & HEX_255));
                
            }
            else
            {
                if (j + 1 < retLen)
                {
                    ret[j + 1] = (byte)((tmp & HEX_65280) >> NUMBER_EIGHT);
                }
                if (j + NUMBER_TWO < retLen)
                {
                    ret[j + NUMBER_TWO] = (byte)((tmp & HEX_255));
                }
            }
        }
        return ret;
    }
}
