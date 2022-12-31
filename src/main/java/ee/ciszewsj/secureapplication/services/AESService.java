package ee.ciszewsj.secureapplication.services;

import org.springframework.stereotype.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.constraints.NotNull;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

@Service
public class AESService {
	public String encrypt(@NotNull String password, int mode, @NotNull String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		byte[] inf = text.getBytes();
		Cipher cipher = Cipher.getInstance("AES");
		SecretKey secretKey = new SecretKeySpec(password.getBytes(), "AES");
		cipher.init(mode, secretKey);
		return Arrays.toString(cipher.doFinal());
	}
}
