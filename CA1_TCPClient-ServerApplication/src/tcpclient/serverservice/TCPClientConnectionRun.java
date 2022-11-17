/*Test data: 
add;2 November;12pm Food Market IFSC Square
add;2 November;6pm Fireworks Dublin City Centre
add;2 November;7.30 pm Jersey Boys Bord Gais Energy Theatre
add;5 November;3pm Shania Twain 3Arena
add;5 November;9pm Careers Fair RDS
remove;2 November;12pm Food Market IFSC Square
remove;2 November;6pm Fireworks Dublin City Centre
remove;2 November;7.30 pm Jersey Boys Bord Gais Energy Theatre
remove;5 November;3pm Shania Twain 3Arena
remove;5 November;9pm Careers Fair RDS
sdfgb
d5yz;
delete;2 November;12pm Food Market IFSC Square
add;2 November>6pm Fireworks Dublin City Centre
save
stop
*/

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package securetcpclientserver;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.security.NoSuchAlgorithmException;  
import java.math.BigInteger;  
import java.security.MessageDigest;  

/**
 * TCPClientConnectionRun.java
 * Product Version: Apache NetBeans IDE 12.5
 * @author Aaron Kearns (Student number: x20513033)
 * Advanced Programming CA Part 2
 * 05/11/2022
*/
public class TCPClientConnectionRun implements Runnable {
    Socket client_link = null;  
    String clientID;
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";  
    public static final String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.;:+-=";
    
    static Map<String, String> hm1 = new HashMap<>();
    
    final static String outputFilePath = "C:/Users/aaron/Desktop/Year 3 Semester 1/Security Fundamentals/CA1/SecurityCA_TCPClient-ServerService/encrypted_events_file.txt"; //copy and paste own directory in poject folder for file to be located in from Windows file explorer.
    
    public TCPClientConnectionRun(Socket connection, String cID) {
        this.client_link = connection;
        clientID = cID;     
    }
    
    //https://www.javatpoint.com/caesar-cipher-program-in-java
    //Ceaser Cipher by Ruby
    public static String encryptData(String message, int shiftKey)   
    {   
        // convert message into lower case   
        message = message.toLowerCase();   

        // encryptStr to store encrypted data   
        String encryptStr = "";   

        // use for loop for traversing each character of the input string   
        for (int i = 0; i < message.length(); i++)   
        {   
            // get position of each character of inputStr in ALPHABET   
            int pos = ALPHABET.indexOf(message.charAt(i));   

            // get encrypted char for each char of message
            //Checking if a character is a space, number or a special character
            if(message.charAt(i) == ' ' || Character.isDigit(message.charAt(i)) || specialChars.contains(Character.toString(message.charAt(i)))){
                //keeping the value of the space, number or special character the same and adding to the encryptStr variable
                char encryptChar = message.charAt(i);
                encryptStr += encryptChar;
            }
            else{
                //if the character is a letter the letter is shifted 5 times and added to encryptStr variable
                int encryptPos = (shiftKey + pos) % 26;   
                char encryptChar = ALPHABET.charAt(encryptPos);
                // add encrypted char to encrypted string   
                encryptStr += encryptChar;
            }
        }   

        // return encrypted string   
        return encryptStr;   
   }  
    //https://www.javatpoint.com/caesar-cipher-program-in-java
    //Ceaser Cipher by Ruby
   public static String decryptData(String message, int shiftKey)   
    {   
        // convert inputStr into lower case   
        message = message.toLowerCase();   

        // decryptStr to store decrypted data   
        String decryptStr = "";   

        // use for loop for traversing each character of the input string   
        for (int i = 0; i < message.length(); i++)   
        {   

            // get position of each character of inputStr in ALPHABET   
            int pos = ALPHABET.indexOf(message.charAt(i));   

            // get decrypted char for each char of inputStr  
            //Checking if a character is a space, number or a special character
            if(message.charAt(i) == ' ' || Character.isDigit(message.charAt(i)) || specialChars.contains(Character.toString(message.charAt(i)))){
                char decryptChar = message.charAt(i);
                decryptStr += decryptChar;
            }
            else{//If the character is not a number shift back 5 
                int decryptPos = (pos - shiftKey) % 26;   

                // if decryptPos is negative   
                if (decryptPos < 0){   
                    decryptPos = ALPHABET.length() + decryptPos;   
                }   
                char decryptChar = ALPHABET.charAt(decryptPos);   

                // add decrypted char to decrypted string   
                decryptStr += decryptChar; 
            }
        }   
        // return decrypted string   
        return decryptStr;   
    }
  
    //https://www.javatpoint.com/hashing-algorithm-in-java    
    public static byte[] obtainSHA(String s) throws NoSuchAlgorithmException  
    {  
    // Static getInstance() method is invoked with the hashing SHA-256  
    MessageDigest msgDgst = MessageDigest.getInstance("SHA-256");  

    // the digest() method is invoked  
    // to compute the message digest of the input  
    // and returns an array of byte  
    return msgDgst.digest(s.getBytes(StandardCharsets.UTF_8));  
    }  

    public static String toHexStr(byte[] hash)  
    {  
    // Converting the byte array in the signum representation  
    BigInteger no = new BigInteger(1, hash);  

    // Converting the message digest into the hex value  
    StringBuilder hexStr = new StringBuilder(no.toString(16));  

    // Padding with tbe leading zeros  
    while (hexStr.length() < 32)  
    {  
    hexStr.insert(0, '0');  
    }  

    return hexStr.toString();  
    } 
    
    
    public void run() {
        try{
            BufferedReader in = new BufferedReader( new InputStreamReader(client_link.getInputStream()));
            PrintWriter out = new PrintWriter(client_link.getOutputStream(),true);
      
            
            String message = "";
            String decodedMessage= "";
            
            do {
                message = in.readLine();
                decodedMessage = decryptData(message, 5);
                System.out.println("\nMessage received from " + clientID + ":  "+ message);
                decodedMessage = decryptData(message, 5);
                System.out.println("Decrypted the message: "+decodedMessage);
                message = decodedMessage;
                Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
                Matcher matcher = pattern.matcher(message);
                boolean messageHasSpecialCharacter = matcher.find();

                if(messageHasSpecialCharacter) { 
                    try {
                        String[] splitMsg = message.split(";"); 

                        String theDate = splitMsg[1].trim();
                        String theEvent = splitMsg[2].trim();                        
                        synchronized (this){
                            if(splitMsg[0].contains("add")) {
                                hm1.put(theEvent, theDate);
                                System.out.println("The HashMap: " + hm1);
                                Map<String, String> hm2 = new HashMap<>();                        
                                for (Map.Entry<String, String> entry : hm1.entrySet()) {
                                    if (entry.getValue().equals(theDate)) {                               
                                        hm2.put(entry.getKey(), entry.getValue());
                                    }                                
                                }
                                System.out.println("Sending encrypted message to client");
                                out.println(encryptData("Event successfully added, events due on "+theDate+": "+hm2,5));
                            }
                            else if(splitMsg[0].contains("remove")) {
                                hm1.remove(theEvent, theDate);
                                System.out.println("The HashMap: " + hm1);
                                if (hm1.isEmpty()) {
                                    System.out.println("The HashMap: " + hm1);
                                    System.out.println("Sending encrypted message to client");
                                    out.println(encryptData("Event successfully removed, all events removed.",5));
                                }
                                else {
                                    Map<String, String> hm2 = new HashMap<>();
                                    for (Map.Entry<String, String> entry : hm1.entrySet()) {
                                        if (entry.getValue().equals(theDate)) {
                                            hm2.put(entry.getKey(), entry.getValue());
                                        }                                
                                    }
                                    System.out.println("Sending encrypted message to client");
                                    out.println(encryptData("Event successfully removed, events due on "+theDate+": "+hm2,5));                            
                                }                                           
                            }
                            else if(!(splitMsg[0].contains("add") || splitMsg[0].contains("remove"))){
                                throw new IncorrectActionException();
                            }
                        }
                    }
                    catch (IncorrectActionException a) {
                        System.out.println(a.getMsg());
                        out.println(a.getMsg());
                    }
                    catch (ArrayIndexOutOfBoundsException e) {
                        out.println("Invalid message");
                    }                    
                }
                else if (message.equals("save")) {
                    out.println(encryptData("File successfully created",5));
                    //https://www.geeksforgeeks.org/write-hashmap-to-a-text-file-in-java/
                    File file = new File(outputFilePath);

                    BufferedWriter bf = null;
                    
                    try {

                        // create new BufferedWriter for the output file
                        bf = new BufferedWriter(new FileWriter(file));

                        // iterate map entries
                        for (Map.Entry<String, String> entry :
                             hm1.entrySet()) {
                            
                            
                            String hash = toHexStr(obtainSHA(entry.getKey() + ":"
                                     + entry.getValue()));

                            
                            // put key and value separated by a colon
                            bf.write(hash);

                            // new line
                            bf.newLine();
                        }

                        bf.flush();
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    catch (NoSuchAlgorithmException obj)  {
                        System.out.println("An exception is generated for the incorrect algorithm: " + obj);
                    }
                    finally {

                        try {

                            // always close the writer
                            bf.close();
                        }
                        catch (Exception e) {
                            out.println(encryptData("Error: file not created",5));                                                     
                        }
                    }
                }
                else if (message.equals("display event and hash value")) { 
                    
                }               
                else if (!messageHasSpecialCharacter && !message.equals("stop")) {
                    out.println("Invalid message");
                }
            }  while (!message.equals("stop"));            
            System.out.println("TERMINATE");
            out.println("TERMINATE");
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        catch (Exception ex) {
            Logger.getLogger(TCPClientConnectionRun.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally 
        {
            try {
                
                System.out.println("\n* Closing connection with " + clientID + " ... *");             
                client_link.close();
            }
            catch(IOException e)
            {
                System.out.println("Unable to disconnect!");
            }
        }
    }   
}

