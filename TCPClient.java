/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpclient.serverservice;
import java.io.*;
import java.net.*;

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

            System.out.println("\nEnter message to be sent to server in the form 'add or remove;event date;event time and event name' : ");
            message =  userEntry.readLine();
            out.println(message); 		
            response = in.readLine();		
            System.out.println("SERVER RESPONSE -> " + response);
        } while (!response.equals("TERMINATE"));     
        System.out.println("\n* Closing connection... *");
        link.close();
    }
    catch(IOException e)
    {
	e.printStackTrace();
    }        
    finally 
    {
        try 
        {
            link.close();				
	}catch(IOException e)
        {
            System.out.println("Unable to disconnect/close!");
            System.exit(1);
	}
    }
 } 
}