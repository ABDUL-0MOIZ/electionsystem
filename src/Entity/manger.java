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
    static{
        u=new ArrayList<>();
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
}
