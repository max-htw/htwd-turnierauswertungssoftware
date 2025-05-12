import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyHelpers {

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
        }
        return result;
    }

    public static IntPair GroupIDandTeamIDfromBezeichnung(String groupBezeichnung){
      int groupID = -1;
      int teamID = -1;
      try {
        groupID = Integer.parseInt("" + groupBezeichnung.charAt(0));
        teamID = groupBezeichnung.charAt(1) - 96;
        if(groupID <=0 || groupID >9 || teamID <= 0 || teamID > 9){
          groupID = -1;
          teamID = -1;
        }
      }
      catch (Exception e){

      }
      return new IntPair(groupID, teamID);
    }

    public static String TeamBezeichnung(Integer groupID, Integer teamID){
      if(groupID <=0 || groupID >9 || teamID <= 0 || teamID > 9){
        return "0o";
      }
      return groupID.toString() + (char)(teamID + 96);
    }

    public static class IntPair implements Comparable<IntPair>, Serializable{
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


    public static class Match implements Serializable {

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
            if(gID > 0 && gID <= DataBaseQueries.get_anzGroups()){
                if(firstTeamNr > 0 && firstTeamNr <= DataBaseQueries.get_anzTeams(gID)){
                    if(secondTeamNr > 0 && secondTeamNr <= DataBaseQueries.get_anzTeams(gID)){
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

    public  static  class FeldSpiele implements Serializable {
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

    public static class TurnierArchiv implements Serializable{

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

}
