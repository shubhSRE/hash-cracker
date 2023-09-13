package hashcracker;

import hashcracker.hashgen.Hash;
import hashcracker.config.Config;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class createThread implements Runnable {

    Hash hash;
    long lineStart;
    long lineEnd;
    createThread(Hash hash, long lineStart, long lineEnd) {
        this.hash = hash;
        this.lineStart = lineStart;
        this.lineEnd = lineEnd;
    }

    @Override
    public void run() {
        try {
            hash.genNThash(lineStart, lineEnd);
        }
        catch (Exception err) {
            err.printStackTrace();
        }
    }
}

public class Main {

    short numberOfFilePart = 0;
    long lineNumbers;
    long lineStart = 0;
    long lineEnd = 0;

    public void divideFileEqually (short numberOfFilePart, String wordlist) throws Exception {
        long totalLineNumbers = 0;
        if(lineNumbers == 0) {
            try (BufferedReader br = new BufferedReader(new FileReader(wordlist))) {
                while (br.readLine() != null) totalLineNumbers++;
                lineNumbers = totalLineNumbers / numberOfFilePart;
            }
        }
        lineStart = lineEnd + 1;
        lineEnd = lineEnd + lineNumbers;
    }
    public static void main(String[] args) {

        Main obj = new Main();
        Hash hash = new Hash();
        obj.numberOfFilePart = (short) 5;

        new Hash(Config.getProperty("wordlist"), Config.getProperty("outputFile"));

        ExecutorService executor = Executors.newFixedThreadPool(obj.numberOfFilePart);

        for (int i = 0; i < obj.numberOfFilePart; i++) {
            try {
                obj.divideFileEqually(obj.numberOfFilePart,Config.getProperty("wordlist"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            executor.execute(new createThread(hash, obj.lineStart, obj.lineEnd));
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
