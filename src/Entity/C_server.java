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
import java.time.LocalDate;
import java.time.LocalTime;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 *
 * @author j
 */
public class C_server {

 

    public C_server() {

    }
    static PrintWriter out;
    static BufferedReader in;

    public static void connectToServer() {
        try {
            Socket socket;

            socket = new Socket("localhost", 12349);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // resultArea.append("Connected to server successfully.\n"); 
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error connecting to server: " + e.getMessage(),
                    "Connection Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }


    public static void addVoter(String cnic, String name, String psd) {

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

    public static void addParty(String name, String leader) {

        try {
            String data = String.join(",", name, leader);
            out.println("insert_party;" + data);

            String response = in.readLine();
            //        resultArea.append("Add Voter Response: " + response + "\n"); 
            if (response.contains("Successfully")) {
                JOptionPane.showMessageDialog(null, "Your Data is succesfully Register");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error communicating with server: " + e.getMessage() + "\n");
        }
    }

    public static void addelection(String name, String sec, LocalDate d, LocalTime t,LocalTime t1) {

        try {

            String data = String.join(",", name, sec, d.toString(), t.toString(),t1.toString());
            out.println("insert_election;" + data);

            String response = in.readLine();
            if (response != null && response.contains("Successfully")) {
                JOptionPane.showMessageDialog(null, "Your Data is successfully Registered");
            } else if (response == null) {
                JOptionPane.showMessageDialog(null, "No response from server.");
            } else {
                JOptionPane.showMessageDialog(null, "Server responded with: " + response);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error communicating with server: " + e.getMessage() + "\n");
        }
    }

    public static void addcandidate(String cnic, String name, String sec, String partyname) {

        try {

            String data = String.join(",", sec, partyname, cnic, name);
            out.println("insert_candidate;" + data);

            String response = in.readLine();
            if (response != null && response.contains("Successfully")) {
                JOptionPane.showMessageDialog(null, "Your Data is successfully Registered");
            } else if (response == null) {
                JOptionPane.showMessageDialog(null, "No response from server.");
            } else {
                JOptionPane.showMessageDialog(null, "Server responded with: " + response);
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
                    User u = new User();
                    u.setUserId(Integer.parseInt(fields[0]));
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

    public static void getAllparty() {
        try {
            out.println("getallparty;");

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                response.append(line).append("\n");
            }
            //resultArea.append("All Voters:\n"); 
            String[] voters = response.toString().split("\n");

            manger.p.clear();
            for (String voter : voters) {
                String[] fields = voter.split(",");

                if (fields.length == 3) {
                    //      resultArea.append(String.format( 
                    //                "CNIC: %s, Name: %s, Constituency: %s, Phone: %s, Email: %s\n", 
                    //              fields[0], fields[1], fields[2], fields[3], fields[4] 
                    //             ));
                    party p = new party();
                    p.setId(Integer.parseInt(fields[0]));
                    p.setName(fields[1]);
                    p.setLogo(fields[2]);

                    manger.p.add(p);
                    System.out.println(manger.p);
                }
            }
        } catch (IOException e) {
            manger.error("Error communicating with server: " + e.getMessage() + "\n");
        }
    }

    public static void getAllelection() {
        try {
            out.println("getallelection;");

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                response.append(line).append("\n");
            }

            manger.e.clear();
            String[] elections = response.toString().split("\n");

            for (String record : elections) {
                String[] fields = record.split(",");
                if (fields.length == 5) {
                    Election e = new Election();
                    e.setElectionId(Integer.parseInt(fields[0]));
                    e.setName(fields[1]);
                    e.setSector(fields[2]);
                    e.setElectionDate(fields[3]);
                    e.setElectionTime(fields[4]);
                    manger.e.add(e);
                } else {
                    System.out.println("Malformed line: " + record); // DEBUG
                }
            }
        } catch (IOException e) {
            manger.error("Error communicating with server: " + e.getMessage());
        }
    }

    public static void getAllCandidates() {
        try {
            out.println("getallcandidates;");

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                response.append(line).append("\n");
            }

            manger.apc.clear(); // assume you have a list

            String[] rows = response.toString().split("\n");
            for (String row : rows) {
                String[] fields = row.split(",");
                if (fields.length == 5) {
                    apcandidate c = new apcandidate();
                    c.setCnic(fields[0]);
                    c.setName(fields[1]);
                    c.setPartyname(fields[2]);
                    c.setElectionname(fields[3]);
                    c.setSector(fields[4]);

                    manger.apc.add(c);
                    System.out.println(manger.apc);
                } else {
                    System.out.println("Malformed line: " + row);
                }
            }

        } catch (IOException e) {
            manger.error("Error communicating with server: " + e.getMessage());
        }
    }
public static void castVoteByCandidateCnic(String voterCnic, String candidateCnic) {
    try {
        String data = String.join(",",voterCnic,candidateCnic ) ;
        out.println("castvote_cnic;" + data);

        String response = in.readLine();
         if (response != null && response.contains("Successfully")) {
                JOptionPane.showMessageDialog(null, "Your Data is successfully Registered");
            } else if (response == null) {
                JOptionPane.showMessageDialog(null, "No response from server.");
            } else {
                JOptionPane.showMessageDialog(null, "Server responded with: " + response);
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error communicating with server: " + e.getMessage() + "\n");
        }
}
public static void getResultsByElectionName(String electionName) {
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            try {
                // Request to server
                out.println("get_results_by_election_name;" + electionName);

                StringBuilder response = new StringBuilder();
                String line;

                // Read until END is received
                while ((line = in.readLine()) != null && !line.equals("END")) {
                    response.append(line).append("\n");
                }

                // Clear previous results list
                manger.rsc.clear();

                String[] rows = response.toString().split("\n");
                for (String row : rows) {
                    String[] fields = row.split(",");
                    if (fields.length == 6) {
                        apcandidate e = new apcandidate();
                        e.setId(Integer.parseInt(fields[0]));
                        e.setName(fields[1]);
                        e.setPartyname(fields[2]);
                        e.setElectionname(fields[3]);
                        e.setSector(fields[4]);
                        e.setVotes(Integer.parseInt(fields[5]));
                        manger.rsc.add(e);

                     
                          }
                }

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error getting results: " + e.getMessage());
            }
            return null;
        }
    };
    worker.execute();
}
public static void getPartyWinnerCounts() {
    try {
        out.println("get_party_winners;");

        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null && !line.equals("END")) {
            response.append(line).append("\n");
        }

        manger.PartyWinners.clear();
        String[] rows = response.toString().split("\n");
        for (String row : rows) {
            String[] fields = row.split(",");
            if (fields.length == 2) {
                PartyWinner pw = new PartyWinner(fields[0], Integer.parseInt(fields[1]));
                manger.PartyWinners.add(pw);
            }
        }

    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Error retrieving party winner data: " + e.getMessage());
    }
}
public static void getPartyWinnerSummary() {
    SwingWorker<Void, Void> worker = new SwingWorker<>() {
        @Override
        protected Void doInBackground() throws Exception {
            try {
                // Send command to server
                out.println("get_party_winner_summary;");

                // Read server response
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null && !line.equals("END")) {
                    response.append(line).append("\n");
                }

                // Parse and store into manger.PartyWinners
                manger.PartyWinners.clear();
                String[] rows = response.toString().split("\n");
                for (String row : rows) {
                    String[] fields = row.split(",");
                    if (fields.length == 2) {
                        PartyWinner pw = new PartyWinner(fields[0], Integer.parseInt(fields[1]));
                        manger.PartyWinners.add(pw);
                    }
                }

                
               

            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error retrieving summary: " + e.getMessage());
            }
            return null;
        }
    };
    worker.execute();
}




}
