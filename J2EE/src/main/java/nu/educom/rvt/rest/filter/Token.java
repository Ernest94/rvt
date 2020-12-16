package nu.educom.rvt.rest.filter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.io.Decoders;

public class Token {

	public Token() {}
	
	public static SecretKey getSecretKey() {
		
		byte[] decodedKey = Decoders.BASE64.decode("wweOXBpCRGgihjY+zsXqy4FYcQ/NMU6E05ESPJIsqTo=");
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "HmacSHA256"); 
		return originalKey;
	}
	
	
}
