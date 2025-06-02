public class Match {
    private int matchID;
    private int groupID;
    private int team1Nr;
    private int team2Nr;
    private int richterHinspielTeamNr = -1;
    private int richterRueckspielTeamNr = -1;

    public int getMatchID() {
        return matchID;
    }

    public void setMatchID(int matchID) {
        this.matchID = matchID;
    }

    public int getGroupID() {
        return groupID;
    }

    public void setGroupID(int groupID) {
        this.groupID = groupID;
    }

    public int getTeam1Nr() {
        return team1Nr;
    }

    public void setTeam1Nr(int team1Nr) {
        this.team1Nr = team1Nr;
    }

    public int getTeam2Nr() {
        return team2Nr;
    }

    public void setTeam2Nr(int team2Nr) {
        this.team2Nr = team2Nr;
    }

    public int getRichterHinspielTeamNr() {
        return richterHinspielTeamNr;
    }

    public void setRichterHinspielTeamNr(int richterHinspielTeamNr) {
        this.richterHinspielTeamNr = richterHinspielTeamNr;
    }

    public int getRichterRueckspielTeamNr() {
        return richterRueckspielTeamNr;
    }

    public void setRichterRueckspielTeamNr(int richterRueckspielTeamNr) {
        this.richterRueckspielTeamNr = richterRueckspielTeamNr;
    }
}
