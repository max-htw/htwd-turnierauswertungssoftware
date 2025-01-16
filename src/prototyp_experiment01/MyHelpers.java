import java.util.*;

public class MyHelpers {
    public static class IntPair implements Comparable<IntPair>{
        int x;
        int y;

        IntPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof IntPair)) return false;
            return ((IntPair) obj).x == this.x && ((IntPair) obj).y == this.y;
        }

        @Override
        public int hashCode() {
            return this.x * 10 + this.y;
        }

        public static IntPair hashDecode(int hash){
            return  new IntPair(hash / 10, hash % 10);
        }

        @Override
        public int compareTo(IntPair o) {
            if(this.x > o.x)
                return  1;
            else if(this.x < o.x)
                return -1;
            else if(this.y > o.y)
                return 1;
            else if(this.y < o.y)
                return  -1;
            else
                return 0;
        }
    }

    public static class Match{

        private int _groupID = -1;
        public int groupID(){
            return _groupID;
        }

        private int _firstTeam = -1;
        public int get_firstTeam(){
            return _firstTeam;
        }

        private int _secondTeam = -1;
        public int get_secondTeam(){
            return _secondTeam;
        }

        private int _firstTeamHinspielPunkte = -1;
        public int get_firstTeamHinspielPunkte(){return _firstTeamHinspielPunkte;}
        public void set_firstTeamHinspielPunkte(int punkte){
            if(punkte >= 0) _firstTeamHinspielPunkte = punkte;
        }

        private int _secondTeamHinspielPunkte = -1;
        public  int get_secondTeamHinspielPunkte(){return _secondTeamHinspielPunkte;}
        public void set_secondTeamHinspielPunkte(int punkte){
            if(punkte >= 0) _secondTeamHinspielPunkte = punkte;
        }

        private int _firstTeamRueckspielPunkte = -1;
        public int get_firstTeamRueckspielPunkte(){return  _firstTeamRueckspielPunkte;}
        public void set_firstTeamRueckspielPunkte(int punkte){
            if(punkte >= 0) _firstTeamRueckspielPunkte = punkte;
        }

        private int _secondTeamRueckspielPunkte = -1;
        public int get_secondTeamRueckspielPunkte(){return _secondTeamRueckspielPunkte;}
        public void set_secondTeamRueckspielPunkte(int punkte){
            if(punkte >= 0) _secondTeamRueckspielPunkte = punkte;
        }

        //erste Integer: groupID, zweite Integer: teamID in der Gruppe
        private IntPair _richterHinspiel = new IntPair(-1, -1);
        public IntPair getRichterHinspiel() {return  new IntPair(_richterHinspiel.x, _richterHinspiel.y);}
        public void set_richterHinspiel(IntPair richter){
            _richterHinspiel.x = richter.x;
            _richterHinspiel.y = richter.y;
        }

        //erste Integer: groupID, zweite Integer: teamID in der Gruppe
        private IntPair _richterRueckspiel = new IntPair(-1, -1);
        public IntPair getRichterRueckspiel() {return  new IntPair(_richterRueckspiel.x, _richterRueckspiel.y);}
        public void set_richterRueckspiel(IntPair richter){
            _richterRueckspiel.x = richter.x;
            _richterRueckspiel.y = richter.y;
        }

        private int _feldNrHinspiel = -1;
        public int get_feldNrHinspiel(){return _feldNrHinspiel;}
        public void set_feldNrHinspiel(int fNr){_feldNrHinspiel = fNr;}

        private int _feldNrRueckspiel = -1;
        public int get_feldNrRueckspiel(){return _feldNrRueckspiel;}
        public void set_feldNrRueckspiel(int fNr){_feldNrRueckspiel = fNr;}

        private int _timeslotHinspiel = -1;
        public int get_timeslotHinspiel(){return _timeslotHinspiel;}
        public void set_timeslotHinspiel(int slot){_timeslotHinspiel = slot;}

        private int _timeslotRueckspiel = -1;
        public int get_timeslotRueckspiel(){return _timeslotRueckspiel;}
        public void set_timeslotRueckspiel(int slot){_timeslotRueckspiel = slot;}

        Match(int gID, int firstTeamNr, int secondTeamNr){
            if(gID > 0 && gID <= AppSettings.get_anzGroups()){
                if(firstTeamNr > 0 && firstTeamNr <= AppSettings.get_anzTeams(gID)){
                    if(secondTeamNr > 0 && secondTeamNr <= AppSettings.get_anzTeams(gID)){
                        _groupID = gID;
                        _firstTeam = Math.min(firstTeamNr, secondTeamNr);
                        _secondTeam = Math.max(firstTeamNr, secondTeamNr);
                    }
                }
            }
        }

        Match(Match other){
            _groupID = other._groupID;
            _firstTeam = other._firstTeam;
            _secondTeam = other._secondTeam;
            _firstTeamHinspielPunkte = other._firstTeamHinspielPunkte;
            _secondTeamHinspielPunkte = other._secondTeamHinspielPunkte;
            _firstTeamRueckspielPunkte = other._firstTeamRueckspielPunkte;
            _secondTeamRueckspielPunkte = other._secondTeamRueckspielPunkte;
            _richterHinspiel = new IntPair(other._richterHinspiel.x,other._richterHinspiel.y);
            _richterRueckspiel = new IntPair(other._richterRueckspiel.x,other._richterRueckspiel.y);
            _feldNrHinspiel = other._feldNrHinspiel;
            _feldNrRueckspiel = other._feldNrRueckspiel;
            _timeslotHinspiel = other._timeslotHinspiel;
            _timeslotRueckspiel = other._timeslotRueckspiel;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Match)) return false;
            return ((Match) obj)._groupID == this._groupID &&
                    ((Match) obj)._firstTeam == this._firstTeam &&
                    ((Match) obj)._secondTeam == this._secondTeam;
        }

        @Override
        public int hashCode() {
            //um Hinspiel vom Rueckspiel anhand von Hash zu unterscheiden
            //tauche ich paziere ich
            // beim Hinspiel: firstTeam an die zweite Dezimalstelle(von links) und
            // das secondTeam an die dritte
            // bei hashCodeRueckspiel mache ich das umgekehrt
            return this._groupID * 100 + this._firstTeam * 10 + this._secondTeam;
        }

        public int hashCodeRueckspiel(){
            //um Hinspiel vom Rueckspiel anhand von Hash zu unterscheiden
            //tauche ich paziere ich
            // beim Hinspiel: firstTeam an die zweite Dezimalstelle(von links) und
            // das secondTeam an die dritte
            // bei hashCodeRueckspiel mache ich das umgekehrt

            return this._groupID * 100 + this._secondTeam * 10 + this._firstTeam;
        }

        public static Match hashDecode(int hash){
            return  new Match(hash / 100, (hash - (hash / 100)) / 10 ,hash % 10);
        }

    }

    public static class SpielStats{
        public boolean isHinspiel = false;
        public int groupid = -1;
        public int team1 = -1;
        public int team2 = -1;
        public int team1Punkte = 0;
        public int team2Punkte = 0;
        public int feldID = -1;
        public IntPair richter = new IntPair(-1,-1);
    }

    public  static  class AuswertungsEintrag implements Comparable<AuswertungsEintrag>{
        final int teamId;
        IntPair score = new IntPair(0,0);

        AuswertungsEintrag(int id) {
            this.teamId = id;
        }

        public int compareTo(AuswertungsEintrag a){
            if(this.score.x > a.score.x) // wenn this mehr Siege hat als a
                return 1;
            else if(a.score.x > this.score.x)// wenn this weniger siege hat als a
                return -1;
            else{ //wenn beide gleiche Anzahle der Siege haben
                if(this.score.y > a.score.y) //jetzt muessen die gesammte Punkte beruecksichtigt werden
                    return 1;
                else if(a.score.y > this.score.y)
                    return -1;
            }
            return  0; // unentschieden, da gleiche anzahl von siegen und gesamtpunkte sind gleich
        }
    }

    public  static  class FeldSpiele {
        public  final int feldNr;

        // Integer-Parameter: hashcode eines Matches 100*groupID + 10*team1 + team2
        // dabei gilt: Bei den hinspielen team1 ist groesser als team2
        //             Bei den rueckspielen team2 ist groesser
        private ArrayList<Integer> _Spiele = new ArrayList<>();

        FeldSpiele(int fNr){
            feldNr = fNr;
        }

        FeldSpiele(FeldSpiele other){
            feldNr = other.feldNr;
            for(int s: other._Spiele){
                _Spiele.add(s);
            }
        }

        public int getAnzahlSpiele(){
            return _Spiele.size();
        }

        public void addSpiel(Integer spielHash){
            _Spiele.add(spielHash);
        }

        public Integer getSpielHashByIdx(int idx){
            if(_Spiele.size() <= idx){
                return  null;
            }
            return  (Integer)_Spiele.get(idx);
        }
    }

    public static class TurnierArchiv{

        TurnierArchiv(String fileName){
            this.fileName = fileName;
        }

        public String fileName = "";
        public ArrayList<Integer> groups = new ArrayList<>();
        public int anzSpielfelder = -1;
        public boolean needRueckspiele = true;
        public HashMap<Integer, MyHelpers.Match> matches = new HashMap<>();
        public ArrayList<FeldSpiele> turnierPlan = new ArrayList<>();
    }

    public static String htmlNavigationLinks(){
        //Spielstand
        //Auswertung
        //Turnierplan
        //Match-Details
        return "<a href=\"/\">Spielstand</a><br>\n" +
                "<a href=\"/turnierplan\">Turnierplan</a><br><br>\n" +
                "<a href=\"/einstellungen\">Einstellungen</a><br>\n" +
                "<a href=\"http://volleyball.htwd\">http://volleyball.htwd</a><br><br>\n";
    }

    public static String btnLinkCss(){
        String ausgabe = "";

        ausgabe += "\na." + StringsCSS.w3btn + ":hover{box-shadow:none;background-color:#174872 !important;outline-width:0;}\n";
        ausgabe += "a." + StringsCSS.w3btn + "{color:#ffffff;border-color:#205c90;background-color:#205c90;font-size:18px;" +
                "font-family:'Source Sans Pro', sans-serif;" +
                "border-radius:5px;user-select:none;padding:8px 16px;overflow:hidden;text-decoration:none;" +
                "text-align:center;cursor:pointer;white-space:nowrap;line-height:1.5;box-sizing:inherit;" +
                "}\n";
        ausgabe += "." + StringsCSS.htwdOrange + "{color:#ec660c;}\n";
        ausgabe += "body{font-family:sans-serif;}\n";

        return ausgabe;
    }

    public static String dropDownCSS() {
        return
                ".dropbtn {" +
                        "background-color: #205c90;" +
                        "color: white;" +
                        "padding: 16px;" +
                        "font-size: 16px;" +
                        "border: none;" +
                        "cursor: pointer;" +
                        "}" +

                        ".dropdown {" +
                        "position: relative;" +
                        "display: inline-block;" +
                        "}" +

                        ".dropdown-content {" +
                        "display: none;" +
                        "position: absolute;" +
                        "right: 0;" +
                        "background-color: #f9f9f9;" +
                        "min-width: 160px;" +
                        "box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);" +
                        "z-index: 1;" +
                        "}" +

                        ".dropdown-content a {" +
                        "color: black;" +
                        "padding: 12px 16px;" +
                        "text-decoration: none;" +
                        "display: block;" +
                        "}" +

                        ".dropdown-content a:hover {background-color: #f1f1f1;}" +
                        ".dropdown:hover .dropdown-content {display: block;}" +
                        ".dropdown:hover .dropbtn {background-color: #174872;}";
    }

    public static String dropDownHTML(boolean rechtsbuendig, String caption, ArrayList<String> values, ArrayList<String> hrefs){
        String ausgabe = "<div class=\"dropdown\" style=\"float:";
        if(rechtsbuendig)
            ausgabe += "right";
        else
            ausgabe += "left";

        ausgabe += ";\">\n";
        ausgabe += "<button class=\"dropbtn\">" + caption + "</button>\n";
        ausgabe += "<div class=\"dropdown-content\"";
        if(rechtsbuendig)
            ausgabe += " style=\"left:0;\"";
        ausgabe += ">\n";
        for(int i = 0; i < hrefs.size(); i++){
            String valStr = "???";
            if(values.size()>i)
                valStr = values.get(i);
            ausgabe += "<a href=\"" + hrefs.get(i) + "\">" + valStr + "</a>\n";
        }
        ausgabe += "</div></div>\n";
        return  ausgabe;
    }

    public static String dropDownForTeam(boolean rechtsbuendig, String hrefPraefix, int groupID, int teamID){
        ArrayList<String> groupVals = new ArrayList<>();
        ArrayList<String> groupLinks = new ArrayList<>();
        ArrayList<String> teamVals = new ArrayList<>();
        ArrayList<String> teamLinks = new ArrayList<>();
        for(int i = 0; i< AppSettings.get_anzGroups(); i++){
            groupVals.add(""+(i+1));
            char linkTeamChr = 'a';
            if(groupID > 0)
                linkTeamChr = Character.toLowerCase(AppSettings.getTeamLetter(teamID));

            groupLinks.add(hrefPraefix + "/" + (i+1) + linkTeamChr);
            if((i+1) == groupID){
                for(int j = 0; j < AppSettings.get_anzTeams(i+1);j++){
                    teamVals.add("" + AppSettings.getTeamLetter (j+1));
                    teamLinks.add(hrefPraefix + "/" + (i+1) + Character.toLowerCase(AppSettings.getTeamLetter(j+1)));
                }
            }
        }
        groupVals.add(StringsRole.Admin.name());
        groupLinks.add((hrefPraefix + "/" + StringsRole.Admin.name().toLowerCase()));

        String groupIDCaption = "" + groupID;
        if(groupID == 0)
            groupIDCaption = StringsRole.Admin.name();

        String ausgabe = "";
        if(groupID>0)
            ausgabe  += dropDownHTML(rechtsbuendig, "" + AppSettings.getTeamLetter(teamID), teamVals, teamLinks);

        ausgabe += dropDownHTML(rechtsbuendig, groupIDCaption, groupVals, groupLinks);
        return  ausgabe;
    }
}
