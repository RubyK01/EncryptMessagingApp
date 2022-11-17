/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package securetcpclientserver;
import java.io.*;
import java.net.*;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * TCPClient.java
 * Product Version: Apache NetBeans IDE 12.5
 * @author Aaron Kearns (Student number: x20513033)
 * Advanced Programming CA Part 2
 * 05/11/2022
*/
public class TCPClient {
 private static InetAddress host;
    private static final int PORT = 1234;
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public static final String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.;:+-=";

    public static void main(String[] args) {
     try 
     {
        host = InetAddress.getLocalHost();
     } 
     catch(UnknownHostException e) 
     {
	System.out.println("Host ID not found!");
	System.exit(1);
     }
     run();
   }
//    //https://www.javatpoint.com/caesar-cipher-program-in-java
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
    
   private static void run() { 
    Socket link = null;				
    try 
    {
	link = new Socket(host,PORT);		
            
        BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
        PrintWriter out = new PrintWriter(link.getOutputStream(),true);	 

        String message;
        String response;
         do {
            BufferedReader userEntry =new BufferedReader(new InputStreamReader(System.in));

            System.out.println("\nEnter an event related message in the form 'add or remove;event date;event time and event name'.\nEnter 'save' to save events to a text file.\nEnter 'display event and hash value' for server interface to display stored events and their hash value.\nEnter 'stop' to end the application.");
            message =  userEntry.readLine();
         
            //printing the encrypted input to show user it their message has been encrypted.
            System.out.println("Encrypted Message : " + encryptData(message,5));  
            //sending encrypted message to the server.
            out.println(encryptData(message,5)); 
            //Receiving encrypted response from server
            response = in.readLine();
            if(!response.equals("TERMINATE")){
               //Showing the user the servers message was encryted and showing the decrypted version
               System.out.println("RECIEVED ENCRYPTED RESPONSE -> "+response);
               System.out.println("DECRYPTED SERVER RESPONSE -> " + decryptData(response,5)); 
            }
            else{//We dont encrypt the terminate response as that message is not important.
                System.out.println("RECIEVED RESPONSE -> "+response);
            }
        } while (!response.equals("TERMINATE"));     
        System.out.println("\n* Closing connection... *");
        link.close();				
	}catch(IOException e)
        {
            System.out.println("Unable to disconnect/close!");
            System.exit(1);
	}
    }
 } 
}
