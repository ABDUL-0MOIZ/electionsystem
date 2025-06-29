/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author j
 */
public class Election {
 private int electionId;
    private String name;
    private String sector;
    private String electionDate;
    private String electionTime;
   

    // Default constructor
    public Election() {
    }

    // Parameterized constructor
    public Election(int electionId, String name, String sector, String electionDate, String electionTime) {
        this.electionId = electionId;
        this.name = name;
        this.sector = sector;
        this.electionDate = electionDate;
        this.electionTime = electionTime;
    }

    // Getters and Setters
    public int getElectionId() {
        return electionId;
    }

    public void setElectionId(int electionId) {
        this.electionId = electionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getElectionDate() {
        return electionDate;
    }

    public void setElectionDate(String electionDate) {
        this.electionDate = electionDate;
    }

    public String getElectionTime() {
        return electionTime;
    }

    public void setElectionTime(String electionTime) {
        this.electionTime = electionTime;
    }

    // Optional: toString() method for easy debugging
    @Override
    public String toString() {
        return "Election{" +
                "electionId=" + electionId +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                ", electionDate='" + electionDate + '\'' +
                ", electionTime='" + electionTime + '\'' +
                '}';
    }
   
}
