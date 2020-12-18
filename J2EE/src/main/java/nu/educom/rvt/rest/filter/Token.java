package nu.educom.rvt.rest.filter;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.tools.javac.Main;

import io.jsonwebtoken.io.Decoders;

public class Token {

	private static String KEY_STORE_FILE;
	private static String KEY_STORE_PW;
	
	public Token() {}
	
	public static SecretKey getSecretTokenKey() throws Exception{
		
		if (System.getenv("KEY_STORE_FILE")==null && System.getenv("KEY_STORE_PW") == null) {
			KEY_STORE_FILE = "src/main/resources/educom_voortgang.keystore";
			KEY_STORE_PW = "?120qhZl";
		}
		else if (System.getenv("KEY_STORE_FILE")==null || System.getenv("KEY_STORE_PW") == null){
			throw new KeyStoreException("Failed accessing keystore");
		}
		else {
			KEY_STORE_FILE = System.getenv("KEY_STORE_FILE");
			KEY_STORE_PW = System.getenv("KEY_STORE_PW");
		}
		
		KeyStore ks = KeyStore.getInstance("PKCS12");
	    try (FileInputStream fis = new FileInputStream(KEY_STORE_FILE)) {
	        ks.load(fis, KEY_STORE_PW.toCharArray());
	        SecretKey secretKey = (SecretKey) ks.getKey("tokenkey",KEY_STORE_PW.toCharArray());
	        return secretKey;
	    }
		
	}
	
	
}
