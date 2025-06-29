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
    private static final int PORT = 12349; 
 
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
        return switch (command.toLowerCase()) {
            case "insert" -> insertVoter(data);
            case "insert_party" -> insertparty(data);
            case "insert_election" -> insertelection(data);
            case "insert_candidate"->insertcandidate(data);
            case "getallparty" -> getAllparty();    
            case "castvote_cnic" -> castVoteByCandidateCnic(data);
            case "get_results_by_election_name" -> getResultsByElectionName(data);
          case "get_party_winner_summary" -> getPartyWinnerSummary();



            case "getall" -> getAllVoters();
            case "getallcandidates" -> getAllCandidates();
            case "getallelection" -> getAllElection();
            default -> "Invalid Command";
        }; 
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
 
 
    private static String insertparty(String data) { 
        String[] fields = data.split(","); 
        if (fields.length != 2) return "Invalid Data"; 
        try (Connection conn = getConnection(); 
             CallableStatement stmt = conn.prepareCall("{CALL add_party(?,?)}")) { 
            stmt.setString(1, fields[0]); 
            stmt.setString(2, fields[1]); 
            stmt.executeUpdate(); 
            return "Succesfull Inserted Successfully"; 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return "Error: " + e.getMessage(); 
        } 
    } 
 
    private static String insertelection(String data) {
        
        String[] fields = data.split(","); 
        if (fields.length != 5) return "Invalid Data"; 
        try (Connection conn = getConnection(); 
             CallableStatement stmt = conn.prepareCall("{CALL add_election(?,?,?,?,?)}")) { 
            stmt.setString(1, fields[0]); 
            stmt.setString(2, fields[1]);
            stmt.setString(3, fields[2]);
            stmt.setString(4, fields[3]);
            stmt.setString(5, fields[4]);
            stmt.executeUpdate(); 
            return "Succesfull Inserted Successfully"; 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return "Error: " + e.getMessage(); 
        } 
    } 
 
    private static String insertcandidate(String data) {
        
        String[] fields = data.split(","); 
        if (fields.length != 4) return "Invalid Data"; 
        try (Connection conn = getConnection();  
                
             CallableStatement stmt = conn.prepareCall("{CALL add_candidate_by_details(?,?,?,?)}")) { 
            stmt.setString(1, fields[0]); 
            stmt.setString(2, fields[1]);
            stmt.setString(3, fields[2]);
            stmt.setString(4, fields[3]);
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
     
     private static String getAllparty() { 
        StringBuilder sb = new StringBuilder(); 
        try (Connection conn = getConnection(); 
             CallableStatement stmt = conn.prepareCall("{CALL get_all_parties()}"); 
             ResultSet rs = stmt.executeQuery()) { 
            while (rs.next()) { 
                sb.append( rs.getInt("party_id")).append(",") 
                  .append(rs.getString("name")).append(",")
                         .append(rs.getString("leader_name")).append("\n"); 
            } 
            return sb.toString(); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
            return "Error: " + e.getMessage(); 
        } 
    } 
private static String getAllElection() { 
    StringBuilder sb = new StringBuilder(); 
    try (Connection conn = getConnection(); 
         CallableStatement stmt = conn.prepareCall("{CALL get_all_elections()}"); 
         ResultSet rs = stmt.executeQuery()) { 
        while (rs.next()) { 
            sb.append(rs.getInt("election_id")).append(",") 
              .append(rs.getString("name")).append(",")
              .append(rs.getString("sector")).append(",")
              .append(rs.getString("election_date")).append(",")
              .append(rs.getString("election_time")).append("\n"); 
        } 
        sb.append("END");  // Add this line
        return sb.toString(); 
    } catch (SQLException e) { 
        e.printStackTrace(); 
        return "Error: " + e.getMessage(); 
    } 
}

private static String getAllCandidates() {
    StringBuilder sb = new StringBuilder();
    try (Connection conn = getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL get_all_candidate_details()}");
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            sb.append(rs.getString("CNIC")).append(",")
              .append(rs.getString("Candidate_Name")).append(",")
              .append(rs.getString("Party_Name")).append(",")
              .append(rs.getString("Election_Name")).append(",")
              .append(rs.getString("Sector")).append("\n");
        }
        return sb.toString();
    } catch (SQLException e) {
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }
}

private static String castVoteByCandidateCnic(String data) {
    String[] fields = data.split(",");
    if (fields.length != 2) return "Error: Invalid vote data.";

    String userCnic = fields[0];
    String candidateCnic = fields[1];

    try (Connection conn = getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL cast_vote_by_candidate_cnic(?, ?)}")) {

        stmt.setString(1, userCnic);
        stmt.setString(2, candidateCnic);
        stmt.executeUpdate();

        return "Vote cast successfully.";

    } catch (SQLException e) {
        if (e.getMessage().contains("already voted")) {
            return "Error: This user has already voted.";
        } else {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
private static String getResultsByElectionName(String electionName) {
    StringBuilder sb = new StringBuilder();

    try (Connection conn = getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL get_results_by_election_name(?)}")) {

        stmt.setString(1, electionName);

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                sb.append(rs.getInt("candidate_id")).append(",")
                  .append(rs.getString("candidate_name")).append(",")
                  .append(rs.getString("party_name")).append(",")
                  .append(rs.getString("election_name")).append(",")
                  .append(rs.getString("sector")).append(",")
                  .append(rs.getInt("total_votes")).append("\n");
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }

    sb.append("END\n"); // This is important!
    return sb.toString();
}
private static String getPartyWinnerSummary() {
    StringBuilder sb = new StringBuilder();
    try (Connection conn = getConnection();
         CallableStatement stmt = conn.prepareCall("{CALL get_party_winner_summary_after_endtime()}");
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            // party_name,total_wins
            sb.append(rs.getString("party_name")).append(",")
              .append(rs.getInt("total_wins")).append("\n");
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }
    sb.append("END");
    return sb.toString();
}




} 