# brouhaha

[![License: MIT](https://img.shields.io/badge/License-MIT-red.svg)](https://github.com/lhmzhou/brouhaha/blob/master/LICENSE)

Demonstrate how to use server-side blockchain to generate a digital signature that chains blocks together, create/maintain a wallet, send/receive mulitple transactions per block, mine a block after submitting a transaction, and validate current blockchain.

## Design Concept

<img width="1021" alt="blockchain diagram" src="https://user-images.githubusercontent.com/16420802/73094758-440b1f80-3eaf-11ea-8f0a-de148477cf0f.png">


## Design Components

### Wallet 

The wallet generates the public/private key pairs in the network. The private key is used for signing. The public key is for signature verification.

### Ledger

The append-only `ledger` creates a chain of blocks and uses it as a storage that provides data integrity and historical links to previous blocks. 

### Transaction

A transaction occurs chronologically. New transaction within the blockchain means the build of a new block.

### Hash

A `hash` is the "digital fingerprint." The `hash`, important to the build of the chain, is calculated based on transactions. The hash from a previous block creates the chain of blocks and gives blockchains its immutability.

### Timestamp

The `timestamp` marks the creation of this block.

### Nonce

`nonce` is an arbitrary number used in cryptography.

### Block POJO

The block serves as a data structure for keeping a list of transactions and corresponding timestamps, the hash of the block, and the hash from the previous block. 

```
public class Block {
    private String hash;
    private String previousHash;
    private List<Transaction> transactions;
    private long timeStamp;
    private int nonce; 
  
    public Block(String data, String previousHash, long timeStamp) {
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.nonce = 0; 
        this.hash = calculateBlockHash();
    }
}
```

## Usage

Add following third party crypto package dependency
```
<dependency>
    <groupId>org.bouncycastle</groupId>
    <artifactId>bcprov-jdk15on</artifactId>
    <version>1.57</version>
</dependency>
```

Building from source:
```
mvn install
```

Or:
```
mvn clean package
```

To run:
```
mvn spring-boot:run
```

### Production Tools

Ensure latest JDK is installed, with [Maven](http://maven.apache.org/) downloaded.
</br>
</br> 
The BouncyCastle Crypto package is a Java implementation of cryptographic algorithms. 
See more BouncyCastle [available here](http://www.bouncycastle.org/java.html).

