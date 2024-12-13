import java.util.ArrayList;

public class AppSettings {
    public  static final String post_score_team1_rueckspiel = "score_team1_rueckspiel";
    public  static final String post_score_team2_rueckspiel = "score_team2_rueckspiel";
    public  static final String post_score_team1_hinspiel = "score_team1_hinspiel";
    public  static final String post_score_team2_hinspiel = "score_team2_hinspiel";
    public  static final String post_saveTurnierAsName = "saveturnier";

    private static int _role;
    private static boolean _needPrefillScores = false;

    private static final int _maxAnzTeams = 8;
    private static int _anzTeams[] = new int[_maxAnzTeams];
    static{
        for(int i=0; i<_maxAnzTeams; i++){
            _anzTeams[i] = 3;
        }
    }

    public static final int _maxAnzGroups = 4;
    private static int _anzGroups = 1;

    private static int _maxAnzSpielfelder = 4;
    private static int _anzSpielfelder = 2;

    private static boolean _needRueckspiele = true;
    public static boolean needRueckspiele() { return _needRueckspiele;}
    public static void set_needRueckspiele(boolean y_n){
        _needRueckspiele = y_n;
        DataBaseQueries.initializeMatches();
        DataBaseQueries.clearCurrentTurnierplan();
    }

    public static String getRoleStr(int role){
        String ausgabe = "Organisator";
        if (role < 0) ausgabe = "Gast";
        else if (role > 0) ausgabe = "Team " + role;
        return ausgabe;
    }
    public static int getRole(){return _role;}
    public static void setRole(int role){
        if(_role > _maxAnzTeams) _role = -1;
        _role = role;
    }

    public static boolean getNeedPrefillScores(){return _needPrefillScores;}
    public static  void setNeedPrefillScores(boolean y_n){
        if(getNeedPrefillScores() != y_n) {
            _needPrefillScores = y_n;
            DataBaseQueries.initializeMatches();
        }
    }

    public static int get_maxAnzSpielfelder(){return _maxAnzSpielfelder;}
    public static int get_anzSpielfelder(){return _anzSpielfelder;}
    public static void set_anzSpielfelder(int anz){
        _anzSpielfelder = (anz>_maxAnzSpielfelder || anz<1)?2:anz;
        DataBaseQueries.clearCurrentTurnierplan();
    }

    public  static  int get_maxAnzGroups(){return _maxAnzGroups;}
    public  static  int get_anzGroups(){return _anzGroups;}
    public static void set_anzGroups(int anz)
    {
        if(get_anzGroups() != anz){
            _anzGroups = (anz>_maxAnzGroups || anz<1)?1:anz;
            DataBaseQueries.initializeMatches();
            DataBaseQueries.clearCurrentTurnierplan();
        }
    }

    public  static  int get_maxAnzTeams(){return _maxAnzTeams;}

    public static int get_anzTeams(int groupID){
        return _anzTeams[groupID - 1];
    }

    public static void set_anzTeams(int anz, int groupID){
        if(get_anzTeams(groupID) != anz){
            _anzTeams[groupID - 1] = (anz>_maxAnzTeams || anz<3)?3:anz;
            DataBaseQueries.initializeMatches();
            DataBaseQueries.clearCurrentTurnierplan();

        }
    }

    public static void  quietSetProperties(ArrayList<Integer> anzGroupsAndTeams,
                                           int anzSpielfelder, boolean needRueckspiele){
        _anzGroups = anzGroupsAndTeams.size();
        for(int t = 0; t < _anzGroups; t++){
            if(_anzTeams.length > t) {
                _anzTeams[t] = anzGroupsAndTeams.get(t);
            }
        }
        _anzSpielfelder = anzSpielfelder;
        _needRueckspiele = needRueckspiele;
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

    public static String getTimeSlotStr(int nr){
        if (nr < 0) return "";
        int timeOffset = 6;
        String anfang = (nr/2 +timeOffset) + ":" + (30*(nr % 2) + (30*(nr % 2)>0?"":"0"));
        String ende = (nr/2 +timeOffset + (nr % 2)) + ":" + (30*((nr + 1) % 2) + (30*((nr + 1) % 2)>0?"":"0"));
        return anfang + " - " + ende;
    }

}
