import java.io.*;
import java.util.*;

public class DataBaseQueries {

    private static String _currentTurnierFileName = "current.turnier";
    private static Random _random;

    public static boolean currentTurnierChanged = false;

    private static ArrayList<MyHelpers.FeldSpiele> _turnierPlan = new ArrayList<>();

    //erster Integer-Parameter: Hinspiel-Hash des Matches
    //zweiter Parameter: der Match selbst
    private static HashMap<Integer, MyHelpers.Match> _matches = new HashMap<>();
    static {
        File f = new File(_currentTurnierFileName);
        if(f.isFile()) {
            try {
                loadTurnierFromFile();
            } catch (IOException e) {
                System.out.println("DataBaseQueries, static initialisateion, IOException: " + e.getMessage());
            } catch (ClassNotFoundException e) {
                System.out.println("DataBaseQueries, static initialisateion, ClassNotFoundException: " + e.getMessage());
            }
            currentTurnierChanged = true;
        }
        else {
            initializeMatches();
        }
    }

    private static final int _maxAnzTeams = 8;
    private static int _anzTeams[] = new int[_maxAnzTeams];
    static{
        for(int i=0; i<_maxAnzTeams; i++){
            _anzTeams[i] = 3;
        }
    }

    public static final int _maxAnzGroups = 4;
    private static int _anzGroups = 1;

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

    private static boolean _needRueckspiele = true;
    public static boolean needRueckspiele() { return _needRueckspiele;}
    public static void set_needRueckspiele(boolean y_n){
        _needRueckspiele = y_n;
        DataBaseQueries.initializeMatches();
        DataBaseQueries.clearCurrentTurnierplan();
    }

    private static boolean _needPrefillScores = false;
    public static boolean getNeedPrefillScores(){return _needPrefillScores;}
    public static  void setNeedPrefillScores(boolean y_n){
        if(getNeedPrefillScores() != y_n) {
            _needPrefillScores = y_n;
            DataBaseQueries.initializeMatches();
        }
    }

    private static int _maxAnzSpielfelder = 4;
    private static int _anzSpielfelder = 2;
    public static int get_maxAnzSpielfelder(){return _maxAnzSpielfelder;}
    public static int get_anzSpielfelder(){return _anzSpielfelder;}
    public static void set_anzSpielfelder(int anz){
        if(anz != _anzSpielfelder)
            DataBaseQueries.currentTurnierChanged = true;
        _anzSpielfelder = (anz>_maxAnzSpielfelder || anz<1)?2:anz;
        DataBaseQueries.clearCurrentTurnierplan();

    }



    public static void  initializeMatches(){
        if(_random==null) _random = new Random();
        _matches.clear();
        for(int g = 1; g <= get_anzGroups(); g++){
            for(int t1 = 1; t1 <= get_anzTeams(g); t1++){
                for(int t2 = t1+1; t2<= get_anzTeams(g); t2++){
                    MyHelpers.Match m = new MyHelpers.Match(g, t1, t2);
                    if(getNeedPrefillScores()){
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
                        if(needRueckspiele()) {
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
                    }
                    _matches.put(m.hashCode(), m);
                }
            }
        }
        currentTurnierChanged = true;
    }

    public static MyHelpers.Match getMatchByHash(int hash){
        if(_matches.containsKey(hash))
            return  _matches.get(hash);

        int anotherForm = (hash/100)*100 + (hash % 10)*10 + (hash % 100)/10;
        if(_matches.containsKey(anotherForm))
            return  _matches.get(anotherForm);

        return  null;
    }

    public static DBInterfaceBase.SpielStats getSpielStatsByHash(int hash){
      DBInterfaceBase.SpielStats a = new DBInterfaceBase.SpielStats();
        if(hash <=0)
            return  a;
        MyHelpers.Match m = getMatchByHash(hash);
        if(m == null)
            return a;

        boolean isHinspiel = (hash % 100)/10 < (hash % 10);
        a.groupid = m.groupID();
        a.team1 = m.get_firstTeam();
        a.team2 = m.get_secondTeam();
        a.isHinspiel = isHinspiel;
        if(isHinspiel) {
            a.team1Punkte = m.get_firstTeamHinspielPunkte();
            a.team2Punkte = m.get_secondTeamHinspielPunkte();
            MyHelpers.IntPair richter = m.getRichterHinspiel();
            a.richterGroupID = richter.x;
            a.richterTeamID = richter.y;
            a.feldID = m.get_feldNrHinspiel();
        }
        else{
            a.team1Punkte = m.get_firstTeamRueckspielPunkte();
            a.team2Punkte = m.get_secondTeamRueckspielPunkte();
            MyHelpers.IntPair richter = m.getRichterRueckspiel();
            a.richterGroupID = richter.x;
            a.richterTeamID = richter.y;
            a.feldID = m.get_feldNrRueckspiel();
        }

        return  a;
    }

    private  static ArrayList<MyHelpers.TurnierArchiv> _turnierArichv = new ArrayList<>();
    static{
        addTestDataToTurnierArchiv();
    }

    public static void addTestDataToTurnierArchiv(){
        MyHelpers.TurnierArchiv ta1 = new MyHelpers.TurnierArchiv("1871_turnier");
        ta1.groups.add(3);
        ta1.groups.add(5);
        ta1.groups.add(3);
        ta1.anzSpielfelder = 3;
        ta1.needRueckspiele = true;
        //.... ToDo
        //_turnierArichv.add(ta1);
    }

    public  static void saveCurrentTurnierToArchive(String fileName){
        for(MyHelpers.TurnierArchiv t: _turnierArichv){
            if(t.fileName.equals(fileName)) return;
        }

        MyHelpers.TurnierArchiv t = new MyHelpers.TurnierArchiv(fileName);
        for(int i = 0; i<get_anzGroups();i++){
            t.groups.add(get_anzTeams(i+1));
        }
        t.anzSpielfelder = get_anzSpielfelder();
        t.needRueckspiele = needRueckspiele();
        for(int hash: _matches.keySet()){
            MyHelpers.Match m = new MyHelpers.Match(_matches.get(hash)); // m ist geklont
            t.matches.put(hash, m);
        }
        for(MyHelpers.FeldSpiele f: _turnierPlan){
            MyHelpers.FeldSpiele fn = new MyHelpers.FeldSpiele(f); // fn ist geklont
            t.turnierPlan.add(fn);
        }
        _turnierArichv.add(t);
    }

    public static void archiviateCurrentTurnierToFile() throws IOException {

        MyHelpers.TurnierArchiv t = new MyHelpers.TurnierArchiv(_currentTurnierFileName);
        for(int i = 0; i<get_anzGroups();i++){
            t.groups.add(get_anzTeams(i+1));
        }
        t.anzSpielfelder = get_anzSpielfelder();
        t.needRueckspiele = needRueckspiele();
        for(int hash: _matches.keySet()){
            MyHelpers.Match m = new MyHelpers.Match(_matches.get(hash)); // m ist geklont
            t.matches.put(hash, m);
        }
        for(MyHelpers.FeldSpiele f: _turnierPlan){
            MyHelpers.FeldSpiele fn = new MyHelpers.FeldSpiele(f); // fn ist geklont
            t.turnierPlan.add(fn);
        }

        FileOutputStream fos = new FileOutputStream(_currentTurnierFileName);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(t);
        oos.close();
    }

    public static void loadTurnierFromArchive(int pos){
        if(_turnierArichv.size() < (pos + 1))
            return;
        MyHelpers.TurnierArchiv t = _turnierArichv.get(pos);

        quietSetProperties(t.groups, t.anzSpielfelder, t.needRueckspiele);

        _matches.clear();
        for(int hash: t.matches.keySet()){
            MyHelpers.Match m = new MyHelpers.Match(t.matches.get(hash)); // m ist geklont
            _matches.put(hash, m);
        }
        _turnierPlan.clear();
        for(MyHelpers.FeldSpiele f: t.turnierPlan){
            MyHelpers.FeldSpiele fn = new MyHelpers.FeldSpiele(f); // fn ist geklont
            _turnierPlan.add(fn);
        }
        currentTurnierChanged = true;
    }

    public static void loadTurnierFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(_currentTurnierFileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        MyHelpers.TurnierArchiv t = (MyHelpers.TurnierArchiv) ois.readObject();
        ois.close();

        quietSetProperties(t.groups, t.anzSpielfelder, t.needRueckspiele);

        _matches.clear();
        for(int hash: t.matches.keySet()){
            MyHelpers.Match m = new MyHelpers.Match(t.matches.get(hash)); // m ist geklont
            _matches.put(hash, m);
        }
        _turnierPlan.clear();
        for(MyHelpers.FeldSpiele f: t.turnierPlan){
            MyHelpers.FeldSpiele fn = new MyHelpers.FeldSpiele(f); // fn ist geklont
            _turnierPlan.add(fn);
        }
        currentTurnierChanged = true;
    }

    public static ArrayList<String> GetTurnierArchivenames(){
        ArrayList<String> ausgabe = new ArrayList<>();
        for(MyHelpers.TurnierArchiv t: _turnierArichv){
            ausgabe.add(t.fileName);
        }
        return  ausgabe;
    }

    public static ArrayList<MyHelpers.AuswertungsEintrag> calculateAuswertung_new(int groupID){
        ArrayList<MyHelpers.AuswertungsEintrag> ausgabe = new ArrayList<>();
        for(int i=1; i<=get_anzTeams(groupID);i++) {

            //newEintrag.score.x - Anzahl der Siege des Teams
            //newEintrag.score.y - Summe der verdienten Punkte in allen Spielen.
            MyHelpers.AuswertungsEintrag newEintrag = new MyHelpers.AuswertungsEintrag(i);
            for (int j=1; j<=get_anzTeams(groupID);j++) {
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
                if(needRueckspiele()) {
                    if (teamRueckspielPunkte >= 0 && gegnerRueckspielPunkte >= 0) {
                        if (teamRueckspielPunkte > gegnerRueckspielPunkte)
                            newEintrag.score.x += 1;
                        newEintrag.score.y += teamRueckspielPunkte - gegnerRueckspielPunkte;
                    }
                }
            }
            ausgabe.add(newEintrag);
        }
        ausgabe.sort(Collections.reverseOrder());
        return  ausgabe;
    }

    public static void clearCurrentTurnierplan(){
        boolean needUpdate = (!_turnierPlan.isEmpty());
        _turnierPlan.clear();
        if(needUpdate)
            currentTurnierChanged = true;
    }


    public static ArrayList<MyHelpers.FeldSpiele> getTurnierplan() throws RuntimeException {
        if(_turnierPlan.isEmpty()){
            for(int i=0; i<get_anzSpielfelder(); i++){
                _turnierPlan.add(new MyHelpers.FeldSpiele(i+1));
            }

            HashSet<Integer> matchHashesHS = new HashSet<>();
            ArrayList<Integer> matchHashesArr = new ArrayList<>();
            for(int groupID = 1; groupID <= get_anzGroups(); groupID ++){
                for(int t1ID = 1; t1ID <= get_anzTeams(groupID); t1ID++){
                    for(int t2ID = t1ID + 1; t2ID <= get_anzTeams(groupID); t2ID++){
                        MyHelpers.Match m = new MyHelpers.Match(groupID,t1ID, t2ID);
                        matchHashesHS.add(m.hashCode()); //hashcode des Hinspiels
                        matchHashesArr.add(m.hashCode());
                        if(needRueckspiele()){
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

                //erster integer: groupID, zweiter: teamID
                HashSet<MyHelpers.IntPair> teamsAlreadyInTimeslot = new HashSet<>();

                //Auf jedem Feld fuer den Timeslot ein Match suchen, der gespielt werden kann
                //und auch einen Richter finden
                for(int f = 0; f < get_anzSpielfelder(); f++){
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
                        for(int nr = 1; nr <= get_anzTeams(g); nr++){
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
        }
        return _turnierPlan;
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


}
