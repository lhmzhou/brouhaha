package com.brouhaha;

import com.brouhaha.Ledger;
import com.brouhaha.chain.Transaction;
import com.brouhaha.chain.TransactionVerifier;
import com.brouhaha.chain.Wallet;
import com.brouhaha.crypto.Merkle;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Date;

import org.apache.log4j.*;

public class Block {

	private String hash;
	private String previousHash;
	private List<Transaction> transactions; 
	private long timestamp; // of milliseconds since 1/1/1970.
	private int nonce;
	private String merkleRoot;
	
	
	private Block(String previousHash, List<Transaction> transactions) { 
		this.transactions = transactions;
		this.previousHash = previousHash;
		this.nonce = 0;
		this.timeStamp = new Date().getTime();
		// this.hash = "9999999999";

		this.hash = calculateHash();
	}
	
	
	public String getPreviousHash() {
		return previousHash; // return reference to the previous block’s hash
	}

	public String getHash() {
		return hash; // returns the hash of the block (which for a valid, solved block should be below the target).
	}

	public List<Transaction> getTransactions() {
        return transactions; 
	}
	
	public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
	}
	
	public long getTimeStamp() {
		return timestamp;
	}

	public void setTimeStamp(long timestamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public String getMerkleRoot() {
		return merkleRoot;
	}

	public void setMerkleRoot(String merkleRoot) {
		this.merkleRoot = merkleRoot;
	}

	/** 
	 *	calculateHash() computes new hash based on block's contents 
	 *
	 *	@return String calculated hash 
	*/
	public String calculateHash() throws UnsupportedEncodingException, NoSuchAlgorithmException {
		String merkleRoot = Merkle.getMerkleRoot(transactions);
		this.merkleRoot = merkleRoot;
		return Merkle.applySha256((previousHash + merkleRoot + timestamp + nonce).getBytes("UTF-8"));
	}
	
	/** 
	 *	mine() generates the hash value of the Block in accordance to the difficulty
	 *
	 *  @param list of transactions
 	 *  @param previousHash, 
	 *  @param difficulty 
	 *	@return block 
	*/
	public static Block mine(List<Transaction> transactions, String previousHash, int difficulty) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		Block block = new Block(previousHash, transactions);

		// String target = new String(new char[difficulty]).replace('\0', '0'); 
		
		// create a string with difficulty * "0" 
		String target = Merkle.getDifficultyString(difficulty);

		// Is hash solved?
		while(!block.hash.substring(0, difficulty).equals(target)) {

			/*
			FYI on difficulty:
			Low difficulty like 1 or 2 can be mined nearly instantly on most computers.
			Blocks with higher difficulty can take longer to mine.
			*/ 
			
			block.nonce ++; // if wrong target, increment nonce
			// log.debug("nonce = " + nonce);  
			block.hash = block.calculateHash();
		}
		System.out.println("Jackpot! Block Mined: " + block.hash); 
		return block;
	}
}