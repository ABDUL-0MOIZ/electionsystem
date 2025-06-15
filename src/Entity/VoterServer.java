/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author j
 */

import java.io.*; 
import java.net.*; 
import java.sql.*; 
 
public class VoterServer { 
    private static final int PORT = 12345; 
 
    public static void main(String[] args) { 
        try (ServerSocket serverSocket = new ServerSocket(PORT)) { 
            System.out.println("Server started and listening on port " + PORT); 
 
            while (true) { 
                try (Socket socket = serverSocket.accept()) { 
                    BufferedReader in = new BufferedReader(new 
InputStreamReader(socket.getInputStream())); 
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true); 
 
                    String request = in.readLine(); 
                    String[] parts = request.split(";", 2); 
                    String command = parts[0]; 
                    String data = parts.length > 1 ? parts[1] : ""; 
 
                    String response = handleCommand(command, data); 
                    out.println(response); 
                } catch (Exception e) { 
                    e.printStackTrace(); 
                } 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
    } 
 
    private static String handleCommand(String command, String data) { 
        switch (command.toLowerCase()) { 
            case "insert": 
                return insertVoter(data); 
            case "update": 
                return null;
            case "delete": 
                return null;
            case "getall": 
                return getAllVoters(); 
            default: 
                return "Invalid Command"; 
        } 
    } 
 
    private static Connection getConnection() throws SQLException { 
        String url = "jdbc:mysql://localhost:3306/es"; 
        String user = "root";    // replace 
        String password = "@ALal7717"; // replace 
        return DriverManager.getConnection(url, user, password); 
    } 
 
 
    private static String insertVoter(String data) { 
        String[] fields = data.split(","); 
        if (fields.length != 3) return "Invalid Data"; 
        try (Connection conn = getConnection(); 
             CallableStatement stmt = conn.prepareCall("{CALL add_user(?,?,?)}")) { 
            stmt.setString(1, fields[0]); 
            stmt.setString(2, fields[1]); 
            stmt.setString(3, fields[2]);  
            stmt.executeUpdate(); 
            return "Succesfull Inserted Successfully"; 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return "Error: " + e.getMessage(); 
        } 
    } 
 
 
    private static String getAllVoters() { 
        StringBuilder sb = new StringBuilder(); 
        try (Connection conn = getConnection(); 
             CallableStatement stmt = conn.prepareCall("{CALL get_all_users()}"); 
             ResultSet rs = stmt.executeQuery()) { 
            while (rs.next()) { 
                sb.append( rs.getInt("user_id")).append(",") 
                  .append(rs.getString("name")).append(",")
                  .append(rs.getString("cnic")).append(",")
                         .append(rs.getString("password")).append("\n"); 
            } 
            return sb.toString(); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return "Error: " + e.getMessage(); 
        } 
    } 
    
    
 

} 