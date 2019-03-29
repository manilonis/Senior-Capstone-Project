/**
 * Michael E Anilonis
 * Mar 29, 2019
 */
package encryption;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * @author manil
 *
 */
public class KeyGenerator {

	public static void main(String[] args) {
		generateKeys();

	}
	
	public static void generateKeys() {
		String fileBase = "mykey";
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			KeyPair kp = kpg.generateKeyPair();
			
			PublicKey pubKey = kp.getPublic();
			PrivateKey privKey = kp.getPrivate();
			
			try (FileOutputStream out = new FileOutputStream(fileBase + ".key")) {
			    out.write(kp.getPrivate().getEncoded());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try (FileOutputStream out = new FileOutputStream(fileBase + ".pub")) {
			    out.write(kp.getPublic().getEncoded());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
	}

}
