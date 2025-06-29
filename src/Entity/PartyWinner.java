/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

/**
 *
 * @author j
package Entity;
*/
public class PartyWinner {
    private String partyName;
    private int winners;

    public PartyWinner() {
    }

    public PartyWinner(String partyName, int winners) {
        this.partyName = partyName;
        this.winners = winners;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public int getWinners() {
        return winners;
    }

    public void setWinners(int winners) {
        this.winners = winners;
    }

    @Override
    public String toString() {
        return "Party: " + partyName + ", Winners: " + winners;
    }
}

