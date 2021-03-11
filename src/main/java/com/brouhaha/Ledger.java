package com.brouhaha;

import java.util.ArrayList;
import java.util.List;

public class Ledger {
	
	private static Ledger ledger = null;
	private static int MINING_DIFFICULTY = 5;
	private List<Block> blockChain;

	private Ledger() {
		blockChain = new ArrayList<Block>();
	} 
	
	public static Ledger getLedger() {
		if (ledger == null) {
			 ledger = new Ledger();
		}
		return ledger;
	}

	public void addBlock(List<Transaction> transactions) throws Exception {
		if(!verifier.verify()) {
			throw new Exception("Transaction not verified.");
		}

		try {
			String previousHash = "0";

			// check if blockchain is empty
			if(blockChain.isEmpty()) {
				System.out.println("Adding genesis block...");
			} else { 
				System.out.println("Adding block " + blockChain.size() + "...");
				previousHash = blockChain.get(blockChain.size()-1).getHash();
			}
			Block block = Block.mine(transactions, previousHash, MINING_DIFFICULTY);

			// block is added to end of the chain
			blockChain.add(block);
		} catch (Exception e) {
		   e.printStackTrace();
		}
	}

	// before adding to the chain, isChainValid() validates the integrity of the blockchain
	public boolean isChainValid() throws Exception {
		Block currentBlock; 
		Block previousBlock;
		
		// loop through all the blocks to check hashes in blockchain 
		for(int i = 1; i < blockChain.size(); i++) {
			currentBlock = blockChain.get(i);
			previousBlock = blockChain.get(i - 1);

			// compare registered hash and calculated hash
			if(!currentBlock.getHash().equals(currentBlock.calculateHash()) ){
				System.out.println("Oops, current hashes not equal");		
				// any change to the blockchain will return false
				return false;
			}

			// compare the new block’s previous hash to the last block (head) of the chain
			if(!previousBlock.getHash().equals(currentBlock.getPreviousHash()) ) {
				System.out.println("Oops, previous hashes not equal");
				// any change to the blockchain will return false
				return false;
			}
		}
		
		System.out.println("Success! Blockchain is deemed valid.");
		return true;
	}
}