import java.lang.reflect.Array;
import java.util.*;

public class DataBaseQueries {
    private static Random _random;

    //erster Integer-Parameter: Hinspiel-Hash des Matches
    //zweiter Parameter: der Match selbst
    private static HashMap<Integer, MyHelpers.Match> _matches = new HashMap<>();
    static {
        initializeMatches();
    }

    public static void  initializeMatches(){
        if(_random==null) _random = new Random();
        _matches.clear();
        for(int g = 1; g <= AppSettings.get_anzGroups(); g++){
            for(int t1 = 1; t1 <= AppSettings.get_anzTeams(g); t1++){
                for(int t2 = t1+1; t2<= AppSettings.get_anzTeams(g); t2++){
                    MyHelpers.Match m = new MyHelpers.Match(g, t1, t2);
                    if(AppSettings.getNeedPrefillScores()){
                        //meanings: 0 = not played yet, 1 = team 1 winner, 2 = team 2 winner;
                        int resHin = _random.nextInt(3);
                        if (resHin == 0) {
                            m.set_firstTeamHinspielPunkte(-1);
                            m.set_secondTeamHinspielPunkte(-1);
                        } else if (resHin == 1) {
                            m.set_firstTeamHinspielPunkte(25);
                            m.set_secondTeamHinspielPunkte(_random.nextInt(23));
                        } else {
                            m.set_firstTeamHinspielPunkte(_random.nextInt(23));
                            m.set_secondTeamHinspielPunkte(25);
                        }
                        int resRueck = _random.nextInt(3);
                        if (resRueck == 0) {
                            m.set_firstTeamRueckspielPunkte(-1);
                            m.set_secondTeamRueckspielPunkte(-1);
                        } else if (resRueck == 1) {
                            m.set_firstTeamRueckspielPunkte(25);
                            m.set_secondTeamRueckspielPunkte(_random.nextInt(23));
                        } else {
                            m.set_firstTeamRueckspielPunkte(_random.nextInt(23));
                            m.set_secondTeamRueckspielPunkte(25);
                        }
                    }
                    _matches.put(m.hashCode(), m);
                }
            }
        }
    }

    public static MyHelpers.Match getMatchByHash(int hash){
        if(_matches.containsKey(hash))
            return  _matches.get(hash);

        int anotherForm = (hash/100)*100 + (hash % 10)*10 + (hash % 100)/10;
        if(_matches.containsKey(anotherForm))
            return  _matches.get(anotherForm);

        return  null;
    }

    public static MyHelpers.SpielStats getSpielStatsByHash(int hash){
        MyHelpers.SpielStats a = new MyHelpers.SpielStats();
        if(hash <=0)
            return  a;
        MyHelpers.Match m = getMatchByHash(hash);
        if(m == null)
            return a;

        boolean isHinspiel = (hash - hash/100)/10 < (hash % 10);
        a.groupid = m.groupID();
        a.team1 = m.get_firstTeam();
        a.team2 = m.get_secondTeam();
        a.isHinspiel = isHinspiel;
        if(isHinspiel) {
            a.team1Punkte = m.get_firstTeamHinspielPunkte();
            a.team2Punkte = m.get_secondTeamHinspielPunkte();
            a.richter = m.getRichterHinspiel();
            a.feldID = m.get_feldNrHinspiel();
        }
        else{
            a.team1Punkte = m.get_firstTeamRueckspielPunkte();
            a.team2Punkte = m.get_secondTeamRueckspielPunkte();
            a.richter = m.getRichterRueckspiel();
            a.feldID = m.get_feldNrRueckspiel();
        }

        return  a;
    }

    private static HashMap<MyHelpers.IntPair, MyHelpers.IntPair> _currentHinspielScores[]
            = new HashMap[AppSettings._maxAnzGroups];
    static{
        for (int i = 0; i < _currentHinspielScores.length; i++){
            _currentHinspielScores[i] = new HashMap<MyHelpers.IntPair, MyHelpers.IntPair>();
        }
    }

    private static HashMap<MyHelpers.IntPair, MyHelpers.IntPair> _currentRueckspielScores[]
            = new HashMap[AppSettings._maxAnzGroups];
    static{
        for (int i = 0; i < _currentRueckspielScores.length; i++){
            _currentRueckspielScores[i] = new HashMap<MyHelpers.IntPair, MyHelpers.IntPair>();
        }
    }


    private static ArrayList<MyHelpers.FeldSpiele_new> _turnierPlan = new ArrayList<>();

    public static HashMap<MyHelpers.IntPair, MyHelpers.IntPair> getHinspielScores(int groupID) {
        if(_currentHinspielScores[groupID - 1].isEmpty() && AppSettings.getNeedPrefillScores()) {

        }
        return  new HashMap<MyHelpers.IntPair, MyHelpers.IntPair>(_currentHinspielScores[groupID - 1]);
    }

    public static HashMap<MyHelpers.IntPair, MyHelpers.IntPair> getRueckspielScores(int groupID) {
        if(_currentRueckspielScores[groupID - 1].isEmpty() &&
                AppSettings.getNeedPrefillScores() &&
                AppSettings.needRueckspiele()) {
            if(_random==null) _random = new Random();

            for (int j = 1; j < AppSettings.get_anzTeams(groupID); j++) {
                for (int i = j + 1; i <= AppSettings.get_anzTeams(groupID); i++) {
                    //meanings: 0 = not played yet, 1 = team 1 winner, 2 = team 2 winner;
                    int res = _random.nextInt(3);
                    if (res == 0) {
                        _currentRueckspielScores[groupID - 1].put(new MyHelpers.IntPair(i, j), new MyHelpers.IntPair(-1, -1));
                    } else if (res == 1) {
                        _currentRueckspielScores[groupID - 1].put(new MyHelpers.IntPair(i, j),
                                new MyHelpers.IntPair(25, _random.nextInt(23)));
                    } else if (res == 2) {
                        _currentRueckspielScores[groupID - 1].put(new MyHelpers.IntPair(i, j),
                                new MyHelpers.IntPair(_random.nextInt(23), 25));
                    }
                }
            }
        }
        return  new HashMap<MyHelpers.IntPair, MyHelpers.IntPair>(_currentRueckspielScores[groupID - 1]);
    }



        public static void clearAllCurrentScores(){
        for(int gid = 1; gid <= AppSettings.get_maxAnzGroups(); gid++){
            clearCurrentScores_(gid);
        }
    }

    public static void clearCurrentScores_(int groupID){
        //fehler
        _currentHinspielScores[groupID - 1].clear();
        _currentRueckspielScores[groupID - 1].clear();
    }

    public static ArrayList<MyHelpers.AuswertungsEintrag> calculateAuswertung_new(int groupID){
        ArrayList<MyHelpers.AuswertungsEintrag> ausgabe = new ArrayList<>();
        for(int i=1; i<=AppSettings.get_anzTeams(groupID);i++) {

            //newEintrag.score.x - Anzahl der Siege des Teams
            //newEintrag.score.y - Summe der verdienten Punkte in allen Spielen.
            MyHelpers.AuswertungsEintrag newEintrag = new MyHelpers.AuswertungsEintrag(i);
            for (int j=1; j<=AppSettings.get_anzTeams(groupID);j++) {
                if(j == i) continue;

                MyHelpers.Match m = getMatchByHash(groupID*100 + i*10 + j);
                int teamHinspielPunkte = 0;
                int gegnerHinspielPunkte = 0;
                int teamRueckspielPunkte = 0;
                int gegnerRueckspielPunkte = 0;

                if(i == m.get_firstTeam()){
                    teamHinspielPunkte = m.get_firstTeamHinspielPunkte() >= 0 ? m.get_firstTeamHinspielPunkte():-1;
                    gegnerHinspielPunkte = m.get_secondTeamHinspielPunkte() >=0 ? m.get_secondTeamHinspielPunkte():-1;
                    teamRueckspielPunkte = m.get_firstTeamRueckspielPunkte() >=0 ? m.get_firstTeamRueckspielPunkte():-1;
                    gegnerRueckspielPunkte = m.get_secondTeamRueckspielPunkte() >=0 ? m.get_secondTeamRueckspielPunkte():-1;
                }
                else{
                    gegnerHinspielPunkte = m.get_firstTeamHinspielPunkte() >= 0 ? m.get_firstTeamHinspielPunkte():-1;
                    teamHinspielPunkte = m.get_secondTeamHinspielPunkte() >=0 ? m.get_secondTeamHinspielPunkte():-1;
                    gegnerRueckspielPunkte = m.get_firstTeamRueckspielPunkte() >=0 ? m.get_firstTeamRueckspielPunkte():-1;
                    teamRueckspielPunkte = m.get_secondTeamRueckspielPunkte() >=0 ? m.get_secondTeamRueckspielPunkte():-1;
                }

                if(teamHinspielPunkte >= 0 && gegnerHinspielPunkte >= 0){
                    if (teamHinspielPunkte > gegnerHinspielPunkte)
                        newEintrag.score.x += 1;
                    newEintrag.score.y += teamHinspielPunkte - gegnerHinspielPunkte;
                }

                if(teamRueckspielPunkte >= 0 && gegnerRueckspielPunkte >= 0){
                    if (teamRueckspielPunkte > gegnerRueckspielPunkte)
                        newEintrag.score.x += 1;
                    newEintrag.score.y += teamRueckspielPunkte - gegnerRueckspielPunkte;
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


    public static ArrayList<MyHelpers.FeldSpiele_new> getTurnierplan_new() throws Exception {
        if(_turnierPlan.isEmpty()){
            for(int i=0; i<AppSettings.get_anzSpielfelder(); i++){
                _turnierPlan.add(new MyHelpers.FeldSpiele_new(i+1));
            }


            HashSet<Integer> matchHashesHS = new HashSet<>();
            ArrayList<Integer> matchHashesArr = new ArrayList<>();
            for(int groupID = 1; groupID <= AppSettings.get_anzGroups(); groupID ++){
                for(int t1ID = 1; t1ID <= AppSettings.get_anzTeams(groupID); t1ID++){
                    for(int t2ID = t1ID + 1; t2ID <= AppSettings.get_anzTeams(groupID); t2ID++){
                        MyHelpers.Match m = new MyHelpers.Match(groupID,t1ID, t2ID);
                        matchHashesHS.add(m.hashCode()); //hashcode des Hinspiels
                        matchHashesArr.add(m.hashCode());
                        if(AppSettings.needRueckspiele()){
                            matchHashesHS.add(m.hashCodeRueckspiel());
                            matchHashesArr.add(m.hashCodeRueckspiel());
                        }
                    }
                }
            }

            int timeslotCnt = 0;
            int watchdog = 10000;
            while (matchHashesHS.size() > 0){
                watchdog--;
                if(watchdog == 0) {
                    throw new RuntimeException("watchdog in getTurnierplan");
                }

                int mh = 0;

                //erster integer: groupID, zweiter: teamID
                HashSet<MyHelpers.IntPair> teamsAlreadyInTimeslot = new HashSet<>();

                //Auf jedem Feld fuer den Timeslot ein Match suchen, der gespielt werden kann
                //und auch einen Richter finden
                for(int f = 0; f < AppSettings.get_anzSpielfelder(); f++){
                    boolean needEmptyMatch = true;
                    for(int i = matchHashesArr.size() - 1; i >= 0 ; i--){
                        int h = matchHashesArr.get(i);
                        if(!matchHashesHS.contains(h)){
                            matchHashesArr.remove(i);
                            continue;
                        }
                        boolean isHinspiel = ((h % 100)/10) < (h%10);
                        int g = h / 100;
                        int t1 = (h - g*100) / 10;
                        int t2 = h % 10;

                        // ob ein der beiden match-Teams im selben timeslot shon auf einem vorherigen Feld spielen.
                        boolean shonVorhanden =
                                teamsAlreadyInTimeslot.contains(new MyHelpers.IntPair(g,t1)) ||
                                teamsAlreadyInTimeslot.contains(new MyHelpers.IntPair(g,t2));


                        if(shonVorhanden) continue;

                        teamsAlreadyInTimeslot.add(new MyHelpers.IntPair(g, t1));
                        teamsAlreadyInTimeslot.add(new MyHelpers.IntPair(g, t2));

                        needEmptyMatch = false;
                        //the match h can be added to the field,
                        _turnierPlan.get(f).addSpiel(h);

                        //now the richter has to be found:
                        for(int nr = 1; nr <= AppSettings.get_anzTeams(g); nr++){
                            if(teamsAlreadyInTimeslot.contains(new MyHelpers.IntPair(g,nr))){
                                //das Team kann nicht pfeifen, da es schon an einem anderen Feld beschaeftigt ist
                                continue;
                            }
                            else{
                                MyHelpers.Match m = getMatchByHash(h);
                                if(isHinspiel) {
                                    m.set_richterHinspiel(new MyHelpers.IntPair(g, nr));
                                    m.set_feldNrHinspiel(f);
                                    m.set_timeslotHinspiel(timeslotCnt);
                                }
                                else {
                                    m.set_richterRueckspiel(new MyHelpers.IntPair(g, nr));
                                    m.set_feldNrRueckspiel(f);
                                    m.set_timeslotRueckspiel(timeslotCnt);
                                }
                                teamsAlreadyInTimeslot.add(new MyHelpers.IntPair(g, nr));

                                break;
                            }
                        }
                        matchHashesHS.remove(h);
                        break;
                    }
                    if(needEmptyMatch){
                        _turnierPlan.get(f).addSpiel((-1)*timeslotCnt + (-10)*timeslotCnt + (-100)* timeslotCnt);
                    }
                }
                timeslotCnt += 1;
            }
            int dummy = 5;
        }
        return _turnierPlan;
    }
}
