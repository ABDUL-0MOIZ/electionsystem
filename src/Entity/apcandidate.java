/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.logging.Logger;

/**
 *
 * @author j
 */
public class apcandidate {
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getVotes() {
        return votes;
    }

    public void setVotes(long votes) {
        this.votes = votes;
    }
    private String electionname;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private String sector;
    private String partyname;
    private String cnic;
     private String aprovel;
     private long votes;

    public apcandidate() {
    }

    public String getAprovel() {
        return aprovel;
    }

    public void setAprovel(String aprovel) {
        this.aprovel = aprovel;
    }

    @Override
    public String toString() {
        return "apcandidate{" + "electionname=" + electionname + ", sector=" + sector + ", partyname=" + partyname + ", cnic=" + cnic + '}';
    }
    
    public String getElectionname() {
        return electionname;
    }

    public void setElectionname(String electionname) {
        this.electionname = electionname;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getPartyname() {
        return partyname;
    }

    public void setPartyname(String partyname) {
        this.partyname = partyname;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }
    
    
}
