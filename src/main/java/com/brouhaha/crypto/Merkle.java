package com.brouhaha;

import com.brouhaha.Ledger;
import com.brouhaha.chain.Transaction;
import com.brouhaha.chain.TransactionVerifier;
import com.brouhaha.chain.Wallet;

import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Key;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.MessageDigest; // get access to the SHA256 algorithm
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.crypto.*;

import org.bouncycastle.*;

import org.apache.log4j.*;

public class Merkle {

	private static final String SHA256 = "SHA-256";
	private static final String ECDSA = "ECDSA";
	private static final String BC = "BC";
	private static final String UTF8 = "UTF-8";

	private static final Logger logger = Logger.getLogger(CryptoHelper.class.getName());
	
	private static MessageDigest messageDigest = null;

	static {
		try {
			messageDigest = MessageDigest.getInstance(SHA256);
		} catch (NoSuchAlgorithmException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			throw new CryptoException(e.getMessage());
		}
	}
 
	/**
	 * Apply SHA256 to input. Applies Sha256 to a string and returns the result
	 *
	 * @param byte input
	 * @return String hexString.toString()
	 */
	public static String applySha256(byte[] input) {
		try {

			// get instance of the SHA-256 cryptographic hash function
			// MessageDigest messageDigest = MessageDigest.getInstance(SHA256);
			
			// applies sha256 to our input, generate byte array
			byte[] hash = messageDigest.digest(input);
			StringBuffer hexString = new StringBuffer(); // this will contain hash as hexidecimal
			
			// takes a string and applies SHA256 algorithm to it
			for (int i = 0; i < hash.length; i++) {
				// transform byte array into a hex string
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex); //
			}
			// and returns the generated signature as a string
			return hexString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// applies ECDSA Signature and returns the result (as bytes). ECDSA private key is used for signing
	public static byte[] applyECDSASigature(PrivateKey privateKey, String input) throws GeneralSecurityException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
	
		Signature signature;
		byte[] output = new byte[0];
		try {
			signature = Signature.getInstance(ECDSA, BC);
			signature.initSign(privateKey); 
			byte[] strByte = input.getBytes();
			signature.update(strByte);
			byte[] realSig = signature.sign();
			output = realSig;
		} catch (GeneralSecurityException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
			throw new IOException(e.getMessage(), e);
		}
		return output;

	}

	// now let's verify the signature using the public key
	public static boolean verifyECDSASignature(PublicKey publicKey, String data, byte[] signature)  throws GeneralSecurityException, NoSuchProviderException, NoSuchAlgorithmException, InvalidKeyException, SignatureException  {
		try {
			Signature ecdsaVerify = Signature.getInstance(ECDSA , BC);
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			return ecdsaVerify.verify(signature); // signature verification
		} catch (GeneralSecurityException | NoSuchProviderException | NoSuchAlgorithmException | InvalidKeyException | SignatureException ex) {
			throw new IOException(e.getMessage(), e);
		}
	}

	// returns difficulty string target, to compare to hash. eg difficulty of 5 will return "00000"
	public static String getDificultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }


	/**
	 * getMerkleRoot() constructs the merkle root 
	 * 
	 * @param list of transactions
	 * @return merkleRoot
	 */
	public static String getMerkleRoot(List<Transaction> transactions) throws Exception {
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for(Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.getTransactionId());
			// log.debug("transactionId = " + transactionId);  
		}
		ArrayList<String> treeLayer = previousTreeLayer;
		while(count > 1) {
			treeLayer = new ArrayList<String>();
			for(int i=1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(applySha256((previousTreeLayer.get(i-1) + previousTreeLayer.get(i)).getBytes(UTF8)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}

	public static String getStringFromKey(Key key) {
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}

}