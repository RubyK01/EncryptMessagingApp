/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package tcpclient.serverservice;
import java.io.*;
import java.net.*;

/**
 * TCPServer.java
 * Product Version: Apache NetBeans IDE 12.5
 * @author Aaron Kearns (Student number: x20513033)
 * Advanced Programming CA Part 2
 * 05/11/2022
*/
public class TCPServer {
  private static ServerSocket servSock;
  private static final int PORT = 1234;
  private static int clientConnections = 0;
  
    public static void main(String[] args) {
    System.out.println("Opening port...\n");
    try 
    {
        servSock = new ServerSocket(PORT);
    }
    catch(IOException e) 
    {
         System.out.println("Unable to attach to port!");
         System.exit(1);
    }
    
    do 
    {
         run();
    }while (true);

  }
  
  private static void run()
  {
    Socket link = null;
    try 
    {
        link = servSock.accept(); 
        clientConnections++;
        String client_ID = "Client "+ clientConnections;
        Runnable resource = new TCPClientConnectionRun(link, client_ID);
        Thread t = new Thread (resource);
        t.start();
    }
    catch(IOException e1)
    {
        e1.printStackTrace();
        try {
	    System.out.println("\n* Closing connection... *");
            link.close();
	}
       catch(IOException e2)
       {
            System.out.println("Unable to disconnect!");
	    System.exit(1);
       }
    }
  } 
}
