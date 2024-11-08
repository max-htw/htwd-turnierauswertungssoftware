public class AppSettings {
    private static int _role;

    private static int _maxAnzTeams = 8;
    private static int _anzTeams = 3;
    private static int _maxAnzSpielfelder = 4;
    private static int _anzSpielfelder = 2;

    public static String getRoleStr(int role){
        String ausgabe = "Organisator";
        if (role < 0) ausgabe = "Gast";
        else if (role > 0) ausgabe = "Team " + role;
        return ausgabe;
    }
    public static int getRole(){return _role;}
    public static void setRole(int role){
        if(_role > _anzTeams) _role = -1;
        _role = role;
    }

    public static int get_maxAnzSpielfelder(){return _maxAnzSpielfelder;}
    public static int get_anzSpielfelder(){return _anzSpielfelder;}
    public static void set_anzSpielfelder(int anz){
        _anzSpielfelder = (anz>4 || anz<1)?2:anz;
        DataBaseQueries.clearCurrentTurnierplan();
    }

    public  static  int get_maxAnzTeams(){return _maxAnzTeams;}
    public static int get_anzTeams(){return _anzTeams;}
    public static void set_anzTeams(int anz)
    {
        if(_anzTeams != anz){
            DataBaseQueries.clearCurrentScores();
            DataBaseQueries.clearCurrentTurnierplan();
        }
        _anzTeams = (anz>8 || anz<3)?3:anz;
    }
    public static char getTeamLetter(int tID){
        char ausgabe = '-';
        if (tID > 0 && tID <= AppSettings.get_maxAnzTeams())
            ausgabe = (char) (64 + tID);
        return  ausgabe;
    }

    public static String getTeamColor(int tID){
        switch (tID) {
            case 1:
                return "orange";
            case 2:
                return "#00786C";
            case 3:
                return "blue";
            case 4:
                return "red";
            case 5:
                return "#8C1DA7";
            case 6:
                return "#680722";
            case 7:
                return  "#6A6525";
            case 8:
                return "#000080";
            default:
                return "#000000";
        }

    }

}
