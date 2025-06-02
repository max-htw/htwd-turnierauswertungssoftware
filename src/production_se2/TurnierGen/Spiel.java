public class Spiel {
    private int matchID;
    private boolean istHinspiel;
    private int timeSlotNr;
    private int feldNr;
    private Match match;

    public Spiel(Match match, boolean istHinspiel, int slot, int feld) {
        this.match = match;
        this.istHinspiel = istHinspiel;
        this.timeSlotNr = slot;
        this.feldNr = feld;
        this.matchID = match != null ? match.getMatchID() : -1;
    }


    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public boolean getIstHinspiel() {
        return istHinspiel;
    }

    public void setIstHinspiel(boolean istHinspiel) {
        this.istHinspiel = istHinspiel;
    }

    public int getTimeSlotNr() {
        return timeSlotNr;
    }

    public void setTimeSlotNr(int timeSlotNr) {
        this.timeSlotNr = timeSlotNr;
    }

    public int getFeldNr() {
        return feldNr;
    }

    public void setFeldNr(int feldNr) {
        this.feldNr = feldNr;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
}
