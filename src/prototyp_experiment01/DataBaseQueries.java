import java.lang.reflect.Array;
import java.util.*;

public class DataBaseQueries {
    private static HashMap<MyHelpers.IntPair, MyHelpers.IntPair> _currentHinspielScores = new HashMap<>();
    private static ArrayList<MyHelpers.FeldSpiele> _turnierPlan = new ArrayList<>();

    public static HashMap<MyHelpers.IntPair, MyHelpers.IntPair> getHinspielScores() {
        if(_currentHinspielScores.isEmpty()) {
            Random random = new Random();

            for (int i = 1; i < AppSettings.get_anzTeams(); i++) {
                for (int j = i + 1; j <= AppSettings.get_anzTeams(); j++) {
                    //meanings: 0 = not played yet, 1 = team 1 winner, 2 = team 2 winner;
                    int res = random.nextInt(3);
                    if (res == 0) {
                        _currentHinspielScores.put(new MyHelpers.IntPair(i, j), new MyHelpers.IntPair(-1, -1));
                    } else if (res == 1) {
                        _currentHinspielScores.put(new MyHelpers.IntPair(i, j), new MyHelpers.IntPair(25, random.nextInt(23)));
                    } else if (res == 2) {
                        _currentHinspielScores.put(new MyHelpers.IntPair(i, j), new MyHelpers.IntPair(random.nextInt(23), 25));
                    }
                }
            }
        }
        return  new HashMap<MyHelpers.IntPair, MyHelpers.IntPair>(_currentHinspielScores);
    }
    public static void clearCurrentScores(){
        _currentHinspielScores.clear();
    }

    public static MyHelpers.IntPair getSingleHinspielScore(MyHelpers.IntPair match){
        if(_currentHinspielScores.isEmpty()) getHinspielScores();
        //Hinspieleintraege sind immer mit kleinerem ersten TeamID und grosserem zweiten TeamID
        boolean needSwap = false;
        if(match.x > match.y) {
            needSwap = true;
            int tmp = match.y;
            match.y = match.x;
            match.x = tmp;
        }
        MyHelpers.IntPair ausgabe = new MyHelpers.IntPair(-1,-1);
        if(_currentHinspielScores.containsKey(match)) {
            MyHelpers.IntPair tmp = _currentHinspielScores.get(match);
            if(!needSwap) {
                ausgabe.x = tmp.x;
                ausgabe.y = tmp.y;
            }
            else{
                ausgabe.x = tmp.y;
                ausgabe.y = tmp.x;
            }
        }
        return  ausgabe;
    }

    public static ArrayList<MyHelpers.AuswertungsEintrag> calculateAuswertung(){
        ArrayList<MyHelpers.AuswertungsEintrag> ausgabe = new ArrayList<>();
        for(int i=1; i<=AppSettings.get_anzTeams();i++) {
            MyHelpers.AuswertungsEintrag newEintrag = new MyHelpers.AuswertungsEintrag(i);
            for (MyHelpers.IntPair match : getHinspielScores().keySet()) {
                MyHelpers.IntPair matchResult = getSingleHinspielScore(match);
                if (matchResult.x >= 0 || matchResult.y >= 0) {
                    if (match.x == i) {
                        newEintrag.score.y += matchResult.x;
                        if (matchResult.x > matchResult.y) newEintrag.score.x += 1;
                    } else if (match.y == i) {
                        newEintrag.score.y += matchResult.y;
                        if (matchResult.y > matchResult.x) newEintrag.score.x += 1;
                    }
                }
            }
            ausgabe.add(newEintrag);
        }
        ausgabe.sort(Collections.reverseOrder());
        return  ausgabe;
    }

    public static void clearCurrentTurnierplan(){
        _turnierPlan.clear();
    }

    public static ArrayList<MyHelpers.FeldSpiele> getTurnierplan() throws Exception {
        if(_turnierPlan.size() == 0){

            for(int i=0; i<AppSettings.get_anzSpielfelder(); i++){
                _turnierPlan.add(new MyHelpers.FeldSpiele(i+1));
            }

            ArrayList<MyHelpers.IntPair> possibleMatches = new ArrayList<>();
            for(int i=1; i<= AppSettings.get_anzTeams(); i++){
                for(int j=i+1; j<= AppSettings.get_anzTeams(); j++){
                    possibleMatches.add(new MyHelpers.IntPair(i,j));
                }
            }
            int wathchDog = 5000;
            while (possibleMatches.size()>0 && wathchDog > 0){
                int nextFeldToUse = 0;
                wathchDog -= 1;
                if(wathchDog == 0){throw new Exception();}
                for(int tid=1; tid<=AppSettings.get_anzTeams(); tid++){
                    //jeder Team (als Richter) sucht sich ein Match, wo er selbst nicht teilnimmt:
                    for(int idx = 0; idx<possibleMatches.size(); idx++){
                        boolean spielImPlan = false;
                        MyHelpers.IntPair m = possibleMatches.get(idx);
                        if(m.x != tid && m.y !=tid ) {
                            //jetzt muss man aufpassen, um nicht denselben Team auf zwei unterschiedlichen
                            //Spielfeldern gleichzeitig zu platzieren.
                            ArrayList<MyHelpers.IntPair> freieTimeslots = new ArrayList<MyHelpers.IntPair>();
                            for(int fn = 0;fn<AppSettings.get_anzSpielfelder();fn++){
                                freieTimeslots.add(new MyHelpers.IntPair(_turnierPlan.get(fn).getAnzahlSpiele(), fn));
                            }
                            Collections.sort(freieTimeslots);
                            for(MyHelpers.IntPair tsl:freieTimeslots){
                                boolean timeslotOK = true;

                                for(int fn = 0;fn<AppSettings.get_anzSpielfelder();fn++) {
                                    MyHelpers.IntPair chckMatch = _turnierPlan.get(fn).getMatchByIdx(tsl.x);
                                    Integer chckRichter = _turnierPlan.get(fn).getRichterByIdx(tsl.x);
                                    if((chckRichter == null && chckMatch ==null) || (
                                            chckRichter != tid && chckMatch.x != tid && chckMatch.y != tid &&
                                            chckRichter != m.x && chckMatch.x != m.x && chckMatch.y != m.x &&
                                            chckRichter != m.y && chckMatch.x != m.y && chckMatch.y != m.y
                                    )){

                                    }
                                    else{
                                        timeslotOK = false;
                                        break;
                                    }
                                }
                                if(timeslotOK){
                                    _turnierPlan.get(tsl.y).addSpiel(new MyHelpers.IntPair(m.x,m.y),tid);
                                    possibleMatches.remove(idx);
                                    spielImPlan = true;
                                    break;
                                }
                            }

                            if(spielImPlan)
                            break;
                        }
                    }
                }
            }
        }

        return _turnierPlan;
    }
}
