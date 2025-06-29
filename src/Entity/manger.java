/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author j
 */
public class manger {
    public static ArrayList<User> u;
    public static ArrayList<party> p;
    public static ArrayList<Election> e;
    public static ArrayList<apcandidate> apc;
     public static ArrayList<apcandidate> rsc;
  public static ArrayList<PartyWinner> PartyWinners;
    public static String cnic;
    
    static{
        u=new ArrayList<>();
        p=new ArrayList<>();
        e=new ArrayList<>();
         apc=new ArrayList<>();
          rsc=new ArrayList<>();
          PartyWinners=new ArrayList<>();
    
    }
    
    
    
    
    
    
    
    public static boolean checkuser(String cnic ,String psd){
        for(User s:u){
            if(s.getCnic().equals(cnic) && s.getPassword().equals(psd)){
               return true;
            }
            
        }
        return false;
    }
    public static void error(String msg){
        JOptionPane.showMessageDialog(null, msg);
    }
    public static party searchparty(String n){
        for (int i = 0; i < manger.p.size(); i++) {
            if(manger.p.get(i).getName().equals(n)){
                return manger.p.get(i);
            }
        }
        return null;
    }
    public static apcandidate searchCandidateByName(String name) {
    for (apcandidate c : apc) {
        if (c.getName().equalsIgnoreCase(name)) {
            return c;
        }
    }
    return null; // Not found
}
public static Election searchElectionByName(String name) {
    for (Election e : manger.e) {
        if (e.getName().equalsIgnoreCase(name)) {
            return e;
        }
    }
    return null; // Not found
}
public static ArrayList<apcandidate> getCandidatesByElectionName(String electionName) {
    ArrayList<apcandidate> result = new ArrayList<>();
    for (apcandidate c : apc) {
        if (c.getElectionname().equalsIgnoreCase(electionName)) {
            result.add(c);
        }
    }
    return result;
}

}
