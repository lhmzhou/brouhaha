package com.brouhaha;

import com.brouhaha.Ledger;
import com.brouhaha.chain.Transaction;
import com.brouhaha.chain.TransactionVerifier;
import com.brouhaha.crypto.Merkle;

import java.security.*;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.SecureRandom;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Wallet {

	private static final String ECDSA = "ECDSA";
	private static final String BC = "BC";
	private static final String SHA1PRNG = "SHA1PRNG";

	public Wallet() throws NoSuchAlgorithmException, NoSuchProviderException {

		private PublicKey publicKey;
		private PrivateKey privateKey;
		private int balance;

		public int getBalance() {
			return balance;
		}

		public void setBalance(int balance){
			this.balance = balance;
		}

		public PublicKey getPublicKey() {
			/* PublicKey publicKey = null;

			try {
				KeyStore messageKeyStore = KeyStore.getInstance(REST_AUTH_KEYSTORE_NAME);
				messageKeyStore.load(null);

				Certificate certificate = messageKeyStore.getCertificate(keyName);
				if (certificate != null) {
					publicKey = certificate.getPublicKey();
				}
			} catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
				throw new CryptoException(e.getMessage());
			} */

			return publicKey;
		}

		public PrivateKey getPrivateKey() {
			return privateKey;
		}

		public Wallet() {
			generateKeyPair(); 
		}

		public void generateKeyPair() {
			try {
				KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ECDSA, BC);
				SecureRandom random = SecureRandom.getInstance(SHA1PRNG);
				ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
				// initialize the key generator and generate a public-private key pair
				keyGen.initialize(ecSpec, random);   //256 bytes provides an acceptable security level
					KeyPair keyPair = keyGen.generateKeyPair();
					// set the public and private keys from the key pair
					privateKey = keyPair.getPrivate();
					publicKey = keyPair.getPublic();

					/*
					System.out.println("--- private key---");
					System.out.println("PRIVATE: " + Merkle.getStringFromKey(this.privateKey));
					*/
					
			} catch(Exception e) {
				throw new RuntimeException(e);
			}
		}

	} 

}