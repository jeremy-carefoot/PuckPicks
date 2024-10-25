package com.carefoot.puckpicks.authentication;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Class for handling OAuth 2.0 Proof Key for Code Exchange
 * PuckPicks is a public client; therefore this is an additional security measure for OAuth
 * 
 * @author jeremycarefoot
 */
public class PKCEHandler {
	
	private static final SecureRandom secureRandom = new SecureRandom();
	public static final String CHALLENGE_METHOD = "S256"; 	// algorithm used to encrypt code challenge
	
	/**
	 * Generates a cryptographically random string of characters for a code verifier, application state code, etc.
	 * @return Random character string (Base64 Encoded)
	 */
	public static String generateSecureCode(int byteCount) {
		byte[] entropicBytes = new byte[ byteCount ];
		secureRandom.nextBytes(entropicBytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(entropicBytes);
	}
	
	/**
	 * Generates a code challenge to be used during the OAuth authentication process (uses SHA-256 transformation).
	 * Requires a code verifier to transform
	 * @param codeVerifier Random string of characters
	 * @return Code challenge in String form
	 * @throws NoSuchAlgorithmException
	 * @throws UnsupportedEncodingException
	 */
	public static String generateCodeChallenge(String codeVerifier) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] hashedVerifier = digest.digest(codeVerifier.getBytes("UTF-8"));
		return Base64.getUrlEncoder().withoutPadding().encodeToString(hashedVerifier);
	}

}
