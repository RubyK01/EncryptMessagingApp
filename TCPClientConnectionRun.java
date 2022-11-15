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
STOP
*/

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tcpclient.serverservice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;

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
    
    static Map<String, String> hm1 = new HashMap<>();
    
    public TCPClientConnectionRun(Socket connection, String cID) {
        this.client_link = connection;
        clientID = cID;     
  }
    
    public void run() {
        try{
            BufferedReader in = new BufferedReader( new InputStreamReader(client_link.getInputStream()));
            PrintWriter out = new PrintWriter(client_link.getOutputStream(),true);
      
            
            String message = "";
            
            do {
                message = in.readLine();
                System.out.println("\nMessage received from " + clientID + ":  "+ message);

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
                                out.println("Event successfully added, events due on "+theDate+": "+hm2);
                            }
                            else if(splitMsg[0].contains("remove")) {
                                hm1.remove(theEvent, theDate);
                                System.out.println("The HashMap: " + hm1);
                                if (hm1.isEmpty()) {
                                    System.out.println("The HashMap: " + hm1);
                                    out.println("Event successfully removed, all events removed.");
                                }
                                else {
                                    Map<String, String> hm2 = new HashMap<>();
                                    for (Map.Entry<String, String> entry : hm1.entrySet()) {
                                        if (entry.getValue().equals(theDate)) {
                                            hm2.put(entry.getKey(), entry.getValue());
                                        }                                
                                    }
                                    out.println("Event successfully removed, events due on "+theDate+": "+hm2);                            
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
                else if (!messageHasSpecialCharacter && !message.equals("STOP")) {
                    out.println("Invalid message");
                }
            }  while (!message.equals("STOP"));            
            System.out.println("TERMINATE");
            out.println("TERMINATE");
        }
        catch(IOException e)
        {
            e.printStackTrace();
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
