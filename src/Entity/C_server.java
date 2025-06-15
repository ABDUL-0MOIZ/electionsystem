/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JOptionPane;

/**
 *
 * @author j
 */
public class C_server {
       
public C_server(){
    
}    
static PrintWriter out; 
 static BufferedReader in;
    
    public static void connectToServer() { 
        try { 
            Socket socket; 
            
            socket = new Socket("localhost", 12345); 
            out = new PrintWriter(socket.getOutputStream(), true); 
            in = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
           // resultArea.append("Connected to server successfully.\n"); 
        } catch (IOException e) { 
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(), 
                    "Connection Error", JOptionPane.ERROR_MESSAGE); 
            System.exit(1); 
        } 
    } 
      public static void addVoter(String cnic,String name,String psd) { 
        
            try { 
                String data = String.join(",", cnic, name, psd); 
                out.println("insert;" + data); 
 
                String response = in.readLine(); 
        //        resultArea.append("Add Voter Response: " + response + "\n"); 
                if (response.contains("Successfully")) { 
                    JOptionPane.showMessageDialog(null, "Your Data is succesfully Register");
                } 
            } catch (IOException e) { 
              JOptionPane.showMessageDialog(null, "Error communicating with server: " + e.getMessage() + "\n"); 
            } 
        } 
      public static void getAllVoters() { 
        try { 
            out.println("getall;"); 
             
            StringBuilder response = new StringBuilder(); 
            String line; 
            while ((line = in.readLine()) != null && !line.isEmpty()) { 
                response.append(line).append("\n");  
            } 
            //resultArea.append("All Voters:\n"); 
            String[] voters = response.toString().split("\n"); 
            
            manger.u.clear();
            for (String voter : voters) { 
                String[] fields = voter.split(","); 
                if (fields.length == 4) { 
              //      resultArea.append(String.format( 
        //                "CNIC: %s, Name: %s, Constituency: %s, Phone: %s, Email: %s\n", 
          //              fields[0], fields[1], fields[2], fields[3], fields[4] 
       //             ));
       User u=new User();
       u.setUserId(Integer.parseInt( fields[0]));
       u.setName(fields[1]);
       u.setCnic(fields[2]);
       u.setPassword(fields[3]);
       
      manger.u.add(u);
                    System.out.println(manger.u);
                } 
            } 
        } catch (IOException e) { 
          manger.error("Error communicating with server: " + e.getMessage() + "\n"); 
        } 
      }
    }

