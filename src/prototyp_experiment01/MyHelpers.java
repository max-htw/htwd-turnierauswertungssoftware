import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

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

    public  static class FeldSpiele {
        public final int feldNr;
        private TreeMap<IntPair, Integer> _Spiele = new TreeMap<>();

        FeldSpiele(int fNr){
            feldNr = fNr;
        }

        public int getAnzahlSpiele(){
            return _Spiele.size();
        }

        public void addSpiel(IntPair match, int richterTeamId){
            _Spiele.put(match, richterTeamId);
        }

        public IntPair getMatchByIdx(int idx){
            if(_Spiele.size() <= idx){
                return  null;
            }
            return  (IntPair)_Spiele.keySet().toArray()[idx];
        }

        public Integer getRichterByIdx(int idx){
            if(_Spiele.size() <= idx){
                return  null;
            }
            return (Integer)_Spiele.values().toArray()[idx];
        }
    }

    public static String htmlNavigationLinks(){
        //Spielstand
        //Auswertung
        //Turnierplan
        //Match-Details
        return "<a href=\"/\">Spielstand</a><br>\n" +
                "<a href=\"/turnierplan\">Turnierplan</a><br><br>\n";
    }
}
