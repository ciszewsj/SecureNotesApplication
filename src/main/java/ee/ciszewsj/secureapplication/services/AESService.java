package ee.ciszewsj.secureapplication.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.Base64;

@Service
@Slf4j
public class AESService {
	@Value("${ee.password.salt")
	private String salt;


	public String doAction(String password, String text, int mode) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidKeySpecException {
		byte[] inf;
		if (mode == Cipher.ENCRYPT_MODE) {
			inf = text.getBytes(StandardCharsets.UTF_8);
		} else {
			inf = Base64.getDecoder().decode(text);
		}
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		SecretKey tmp = factory.generateSecret(spec);
		SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");


		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(mode, secret);
		byte[] result = cipher.doFinal(inf);
		if (mode == Cipher.ENCRYPT_MODE) {
			return Base64.getEncoder().encodeToString(result);
		} else {
			return new String(result, StandardCharsets.UTF_8);
		}
	}
}
