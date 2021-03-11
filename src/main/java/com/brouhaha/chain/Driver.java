package com.brouhaha;

import com.brouhaha.Ledger;
import com.brouhaha.chain.Transaction;
import com.brouhaha.chain.TransactionVerifier;
import com.brouhaha.chain.Wallet;
import com.brouhaha.crypto.Merkle;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.provider.JCEECPrivateKey;
import org.bouncycastle.jce.provider.JCEECPublicKey;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier; 
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.crypto.params.DHParameters;
import org.bouncycastle.crypto.params.DHPublicKeyParameters;
import org.bouncycastle.crypto.params.DHValidationParameters;
import org.bouncycastle.crypto.params.DSAParameters;
import org.bouncycastle.crypto.params.DSAPublicKeyParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;

import java.security.*;
import java.util.zip.CRC32;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.security.spec.InvalidKeySpecException;
import java.util.Set;


public class Driver {

    /* static {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    } */

    public static void main(String[] args) throws IllegalAccessException, ClassNotFoundException, InvalidKeySpecException, NoSuchAlgorithmException, NoSuchMethodException, IOException {

        try {
            // set-up BouncyCastle as a Security Provider
            Security.addProvider(new BouncyCastleProvider());
            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider()); 

            Ledger ledger = ledger.getLedger();
            Wallet walletA = new Wallet(); 
            Wallet walletB = new Wallet();
            Wallet bank = new Wallet();

            Transaction transaction1 = new Transaction(bank.getPublicKey(), walletA.getPublicKey(),1000);
            transaction1.sign(getSignature(bank.getPrivateKey(),transaction1));
            
            Transaction transaction2 = new Transaction(bank.getPublicKey(), walletB.getPublicKey(),1000);
            transaction2.sign(getSignature(bank.getPrivateKey(),transaction2));
            
            List<Transaction> transactions = new ArrayList<Transaction>();
            transactions.add(transaction1);
            transactions.add(transaction2);
            
            ledger.addBlock(transactions);
            
            Transaction transaction3 = new Transaction(walletB.getPublicKey(), walletA.getPublicKey(),40);
            transaction3.sign(getSignature(walletB.getPrivateKey(),transaction3));
            
            Transaction transaction4 = new Transaction(walletA.getPublicKey(), walletB.getPublicKey(),50);
            transaction4.sign(getSignature(walletA.getPrivateKey(),transaction4));
            
            List<Transaction> moreTransactions = new ArrayList<Transaction>();
            ledger.addBlock(moreTransactions);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
		
    }

    public static byte[] getSignature(PrivateKey senderPrivateKey, Transaction transaction) throws Exception {
       
        // apply ECDSA Signature and return the result
        return Merkle.applyECDSASigature(senderPrivateKey, transaction.toString());	
    }
}