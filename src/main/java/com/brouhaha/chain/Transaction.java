package com.brouhaha;

import com.brouhaha.Ledger;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.UUID;

public class Transaction {

    private String transactionId;
	private PublicKey sender;
	private PublicKey recipient;
	private int amount;
	private byte[] signature;
	
	public Transaction(PublicKey sender, PublicKey recipient, int amount) {
		this.sender = sender;
		this.recipient = recipient;
		this.amount = amount;
		this.transactionId = UUID.randomUUID().toString();
	}

	public String getTransactionId() {
		return transactionId;
	}
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
	public PublicKey getSender() {
		return sender;
	}
	public void setSender(PublicKey sender) {
		this.sender = sender;
	}
	
	public PublicKey getRecipient() {
		return recipient;
	}
	public void setRecipient(PublicKey recipient) {
		this.recipient = recipient;
	}
	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}

	public void sign(byte[] signature) throws Exception {
		if(Merkle.verifyECDSASignature(sender, this.toString() , signature)) {
			this.signature = signature;
		} else {
			throw new Exception("Transaction not allowed.");
		}
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	@Override
	public String toString() {
		return "Transaction [transactionId=" + transactionId + 
								", sender=" + Base64.getEncoder().encodeToString(sender.getEncoded()) + 
								", recipient=" + Base64.getEncoder().encodeToString(recipient.getEncoded()) +
								", amount=" + amount + "]";
	}
    
}