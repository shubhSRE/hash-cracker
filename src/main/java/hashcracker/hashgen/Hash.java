package hashcracker.hashgen;

import com.rabbitmq.client.Channel;
import hashcracker.AMQP.AMQPChannel;
import jcifs.util.Hexdump;
import jcifs.util.MD4;
import java.io.*;

public class Hash{
    static String wordlist = "";
    static String outputFile = "";
    public Hash(){

    }
    public Hash(String wordlist, String outputFile) {
        Hash.wordlist = wordlist;
        Hash.outputFile = outputFile;
    }

    private String getNThash(String password) {
        String ntlmHash = null;
        try {
            byte[] passwordBytes = password.getBytes("UTF-16LE");
            MD4 md4 = new MD4();
            md4.update(passwordBytes);
            byte[] ntlmHashBytes = md4.digest();

            ntlmHash = Hexdump.toHexString(ntlmHashBytes, 0, ntlmHashBytes.length * 2);

        } catch (Exception e) {
            System.out.println("Some Error in Generating NThash: " + e);
        }
        return ntlmHash;
    }

    public void genNThash(long lineStart, long lineEnd){

        AMQPChannel amqpChannel = new AMQPChannel();

        String[] fileNameArray = wordlist.split("/");
        String fileName = fileNameArray[fileNameArray.length - 1];

        long currentLineNumber = 1;
        long startTime = System.currentTimeMillis();

        try (BufferedReader br = new BufferedReader(new FileReader(wordlist), 1048576)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (currentLineNumber >= lineStart  && currentLineNumber <= lineEnd) {
                    String nthash = this.getNThash(line);
                    String data = line + " : " + nthash + "\n";
//                    this.writeNThash(data);
                    try {
                        Channel channel = amqpChannel.createChannel();
                        channel.queueDeclare(fileName, true, false, false, null);
                        channel.basicPublish("", fileName, null, data.getBytes());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                                    }
                currentLineNumber++;
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Total Time taken: " + (endTime - startTime));

        } catch (IOException e) {
            System.out.println("Some Error in Reading Lines from File: " + e);
        }
    }

    private void writeNThash(String data){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile, true))) {
            writer.write(data);
        } catch (IOException e) {
            System.out.println("Some Error while Writing data in File: " + e);
        }
    }
}