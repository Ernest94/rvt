package nu.educom.rvt.rest.filter;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.UnrecoverableKeyException;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.io.Decoders;

public class Token {

	public Token() {}
	
	public static SecretKey getSecretTokenKey() throws Exception{
		
		if (System.getenv("KEY_STORE_FILE")==null || System.getenv("KEY_STORE_PW") == null) {
			throw new KeyStoreException("Failed accessing keystore");
		}
		else {
			KeyStore ks = KeyStore.getInstance("PKCS12");
		
		    try (FileInputStream fis = new FileInputStream(System.getenv("KEY_STORE_FILE"))) {
		        ks.load(fis, System.getenv("KEY_STORE_PW").toCharArray());
		        SecretKey secretKey = (SecretKey) ks.getKey("tokenkey",System.getenv("KEY_STORE_PW").toCharArray());
		        return secretKey;
		    }
		}
		
	}
	
	
}
