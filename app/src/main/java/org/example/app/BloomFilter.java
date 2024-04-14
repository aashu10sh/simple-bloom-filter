package org.example.app;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

//MessageDigest


public class BloomFilter {
    MessageDigest hasher256;
    MessageDigest hasher512;
    MessageDigest hasher1;
    final boolean[] bitmap = new boolean[33];

    MessageDigest getHasher(String algorithm) throws IOException {
        try{
            return MessageDigest.getInstance(algorithm);
        }catch(
                NoSuchAlgorithmException e){
            System.out.println(e);
            throw new IOException("No such algorithm: " + algorithm);
        }
    }

    int getDigit(String text, MessageDigest hasher)  {
        try{
            int hash = Math.abs(Arrays.hashCode(hasher.digest(text.getBytes())));
            return hash % 32;
        }catch (Exception e){
            System.out.println("Digest exception: " + e);
            System.exit(1);
        }
        return 0;
    }


    public static BloomFilter getInstance(){
        return new BloomFilter();
    }


    BloomFilter(){
        try {
            hasher256 = getHasher("SHA-256");
            hasher1 = getHasher("SHA-1");
            hasher512 = getHasher("SHA-512");
        }catch (IOException e){
            System.out.println("Error while creating Hashers"+ e);
            System.exit(1);
        }
    }

    String pad( String text, int len){
        if (text.length() < len) {
            int remaining = len - text.length();
            for (int i = 0; i < remaining; i++) {
                text = text.concat("");
            }
        }
        return text;
    }

    void setFilter( Filter to_add){
        bitmap[to_add.hash1()] = true;
        bitmap[to_add.hash256()] = true;
        bitmap[to_add.hash512()] = true;

    }
    Filter getFilter(String text){
        return new Filter(
                getDigit(pad(text, 32), hasher256),
                getDigit(pad(text,64), hasher512),
                getDigit(pad(text,32), hasher1)
        );
    }

    public void add(String text){
        Filter instance = getFilter(text);
        setFilter(instance);
    }

    public boolean check(String text){
        Filter instance = getFilter(text);
        return checkFilter(instance);
    }

    private boolean checkFilter(Filter instance) {
        if(bitmap[instance.hash1()] && bitmap[instance.hash256()]){
            return true;
        }
        if (bitmap[instance.hash512()] && bitmap[instance.hash256()]){
            return true;
        }
        if(bitmap[instance.hash1()] && !bitmap[instance.hash512()]){
            return true;
        }
        return false;
    }
}