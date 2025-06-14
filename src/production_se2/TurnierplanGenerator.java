import java.util.*;

public class TurnierplanGenerator {

    // Steuerungsflags
    public static boolean verbieteDreiPausen = false;
    public static boolean verbieteSchiriSchiri = true;
    public static boolean verbieteSchiriPauseSchiri = true;
    public static boolean verbieteDreiSpiele = true;
    public static boolean ausgabeLogs = false;

    public static List<Spiel> generierePlan(
            List<Integer> anzTeamsInGruppe,
            int anzahlTimeSlots,
            int anzahlSpielfelder,
            boolean rueckspielErlaubt
    ) {
        return generierePlan_01(
                anzTeamsInGruppe.size(),
                anzTeamsInGruppe,
                anzahlTimeSlots,
                anzahlSpielfelder,
                rueckspielErlaubt
        );      
    }

    public static List<Spiel> generierePlan_01(
            int anzahlGruppen,
            List<Integer> anzTeamsInGruppe,
            int anzahlTimeSlots,
            int anzahlSpielfelder,
            boolean rueckspielErlaubt
    ) {
        List<Match> matchListe = new ArrayList<>();
        int matchIdCounter = 0;

        int teamOffset = 0;
        for (int gruppe = 0; gruppe < anzahlGruppen; gruppe++) {
            int teamCount = anzTeamsInGruppe.get(gruppe);
            for (int i = 0; i < teamCount; i++) {
                for (int j = i + 1; j < teamCount; j++) {
                    Match m = new Match();
                    m.setGroupID(gruppe);
                    m.setTeam1Nr(i + teamOffset);
                    m.setTeam2Nr(j + teamOffset);
                    m.setMatchID(matchIdCounter++);
                    matchListe.add(m);
                }
            }
            teamOffset += teamCount;
        }

        List<Spiel> spielListe = new ArrayList<>();
        for (Match match : matchListe) {
            Spiel hinspiel = new Spiel(match, true, -1, -1);
            hinspiel.setMatchID(match.getMatchID());
            spielListe.add(hinspiel);
            if (rueckspielErlaubt) {
                Spiel rueckspiel = new Spiel(match, false, -1, -1);
                rueckspiel.setMatchID(match.getMatchID());
                spielListe.add(rueckspiel);
            }
        }

        List<Spiel> turnierplan = new ArrayList<>();
        Map<Integer, Map<Integer, String>> rollenProTeam = new HashMap<>();
        Map<Integer, Integer> ersterSlotProTeam = new HashMap<>();
        Set<Spiel> geplanteSpiele = new HashSet<>();

        for (int slot = 0; slot < anzahlTimeSlots; slot++) {
            for (int feld = 0; feld < anzahlSpielfelder; feld++) {
                for (Spiel spiel : spielListe) {
                    if (geplanteSpiele.contains(spiel)) continue;

                    Match match = spiel.getMatch();
                    int team1 = match.getTeam1Nr();
                    int team2 = match.getTeam2Nr();

                    if (istTeamBeschaeftigt(turnierplan, slot, team1) || istTeamBeschaeftigt(turnierplan, slot, team2))
                        continue;

                    rollenProTeam.computeIfAbsent(slot, k -> new HashMap<>()).put(team1, "Spiel");
                    rollenProTeam.get(slot).put(team2, "Spiel");

                    Set<Integer> beschaeftigteTeams = ermittleBeschaeftigteTeams(turnierplan, slot);
                    int schiriTeam = waehleSchiedsrichter(
                            spiel, slot, rollenProTeam, matchListe,
                            anzTeamsInGruppe, beschaeftigteTeams);

                    if (schiriTeam == -1 ||
                            !rollenErlaubt(spiel, slot, rollenProTeam, matchListe, ersterSlotProTeam, schiriTeam)) {
                        rollenProTeam.get(slot).remove(team1);
                        rollenProTeam.get(slot).remove(team2);
                        continue;
                    }

                    spiel.setTimeSlotNr(slot);
                    spiel.setFeldNr(feld);

                    if (spiel.getIstHinspiel()) {
                        match.setRichterHinspielTeamNr(schiriTeam);
                    } else {
                        match.setRichterRueckspielTeamNr(schiriTeam);
                    }

                    rollenProTeam.get(slot).put(schiriTeam, "Schiri");

                    if (!ersterSlotProTeam.containsKey(team1)) ersterSlotProTeam.put(team1, slot);
                    if (!ersterSlotProTeam.containsKey(team2)) ersterSlotProTeam.put(team2, slot);
                    if (!ersterSlotProTeam.containsKey(schiriTeam)) ersterSlotProTeam.put(schiriTeam, slot);

                    turnierplan.add(spiel);
                    geplanteSpiele.add(spiel);
                    break;
                    }
                }
        }
        return turnierplan;
    }

    private static boolean rollenErlaubt(Spiel spiel, int slot,
                                         Map<Integer, Map<Integer, String>> rollen,
                                         List<Match> matches,
                                         Map<Integer, Integer> ersterSlotProTeam,
                                         int schiriTeam) {
        Match match = findeMatchZuSpiel(spiel, matches);
        int[] teams = {match.getTeam1Nr(), match.getTeam2Nr(), schiriTeam};

        for (int team : teams) {
            int start = ersterSlotProTeam.getOrDefault(team, slot);
            List<String> history = new ArrayList<>();
            for (int s = start; s <= slot; s++) {
                String rolle = rollen.getOrDefault(s, new HashMap<>()).getOrDefault(team, "Pause");
                history.add(rolle);
            }

            if (verbieteDreiPausen && history.size() >= 3) {
                for (int i = 0; i <= history.size() - 3; i++) {
                    if (history.get(i).equals("Pause") && history.get(i + 1).equals("Pause") && history.get(i + 2).equals("Pause")) {
                        if (TurnierplanGenerator.ausgabeLogs) System.out.printf("[INFO] Team %d hätte 3 Pausen.%n", team);
                        return false;
                    }
                }
            }

            if (verbieteSchiriSchiri && history.size() >= 2) {
                for (int i = 0; i <= history.size() - 2; i++) {
                    if (history.get(i).equals("Schiri") && history.get(i + 1).equals("Schiri")) {
                        if (TurnierplanGenerator.ausgabeLogs) System.out.printf("[INFO] Team %d wäre zweimal Schiri.%n", team);
                        return false;
                    }
                }
            }

            if (verbieteSchiriPauseSchiri && history.size() >= 3) {
                for (int i = 0; i <= history.size() - 3; i++) {
                    if (history.get(i).equals("Schiri") && history.get(i + 1).equals("Pause") && history.get(i + 2).equals("Schiri")) {
                        if (TurnierplanGenerator.ausgabeLogs) System.out.printf("[INFO] Team %d wäre Schiri–Pause–Schiri.%n", team);
                        return false;
                    }
                }
            }

            if (verbieteDreiSpiele && hatDreiSpieleInFolge(team, slot, rollen)) {
                if (TurnierplanGenerator.ausgabeLogs) System.out.printf("[INFO] Team %d hätte 3 Spiele in Folge.%n", team);
                return false;
            }
        }

        return true;
    }

    private static boolean istTeamBeschaeftigt(List<Spiel> plan, int slot, int team) {
    for (Spiel spiel : plan) {
        if (spiel.getTimeSlotNr() != slot) continue;
        Match m = spiel.getMatch();
        if (m.getTeam1Nr() == team || m.getTeam2Nr() == team) return true;
        int schiri = spiel.getIstHinspiel() ? m.getRichterHinspielTeamNr() : m.getRichterRueckspielTeamNr();
        if (schiri == team) return true;
    }
    return false;
}

    private static Set<Integer> ermittleBeschaeftigteTeams(List<Spiel> plan, int slot) {
        Set<Integer> result = new HashSet<>();
        for (Spiel spiel : plan) {
            if (spiel.getTimeSlotNr() != slot) continue;
            Match m = spiel.getMatch();
            result.add(m.getTeam1Nr());
            result.add(m.getTeam2Nr());
            int schiri = spiel.getIstHinspiel() ? m.getRichterHinspielTeamNr() : m.getRichterRueckspielTeamNr();
            if (schiri != -1) result.add(schiri);
        }
        return result;
    }

    private static boolean hatDreiSpieleInFolge(int team, int slot,
                                                Map<Integer, Map<Integer, String>> rollen) {
        if (slot < 2) return false;

        String r0 = rollen.getOrDefault(slot, new HashMap<>()).getOrDefault(team, "Pause");
        String r1 = rollen.getOrDefault(slot - 1, new HashMap<>()).getOrDefault(team, "Pause");
        String r2 = rollen.getOrDefault(slot - 2, new HashMap<>()).getOrDefault(team, "Pause");

        return r0.equals("Spiel") && r1.equals("Spiel") && r2.equals("Spiel");
    }

    private static int waehleSchiedsrichter(Spiel spiel, int slot,
                                            Map<Integer, Map<Integer, String>> rollen,
                                            List<Match> matches,
                                            List<Integer> anzTeamsInGruppe,
                                            Set<Integer> beschaeftigteTeams) {
        Match match = findeMatchZuSpiel(spiel, matches);
        int t1 = match.getTeam1Nr();
        int t2 = match.getTeam2Nr();

        int maxTeam = anzTeamsInGruppe.stream().mapToInt(i -> i).sum();
        Map<Integer, Integer> schiriZaehler = new HashMap<>();
        Set<Integer> kandidaten = new HashSet<>();

        for (int team = 0; team < maxTeam; team++) {
            if (team == t1 || team == t2 || beschaeftigteTeams.contains(team)) continue;

            boolean erlaubt = true;
            if (slot >= 2) {
                String r0 = rollen.getOrDefault(slot, new HashMap<>()).getOrDefault(team, "Pause");
                String r1 = rollen.getOrDefault(slot - 1, new HashMap<>()).getOrDefault(team, "Pause");
                String r2 = rollen.getOrDefault(slot - 2, new HashMap<>()).getOrDefault(team, "Pause");

                // blockieren nur Schiri–Schiri und Schiri–Pause–Schiri
                if (r1.equals("Schiri") && r2.equals("Schiri")) {
                    erlaubt = false;
                }
                if (r2.equals("Schiri") && r1.equals("Pause") && r0.equals("Schiri")) {
                    erlaubt = false;
                }
                // erlaubt: Schiri -> Spiel
            }


            if (erlaubt) {
                kandidaten.add(team);
                int count = 0;
                for (Map<Integer, String> r : rollen.values()) {
                    if ("Schiri".equals(r.get(team))) count++;
                }
                schiriZaehler.put(team, count);
            }
        }

        return kandidaten.stream()
                .min(Comparator.comparingInt(schiriZaehler::get))
                .orElse(-1);
    }

    private static Match findeMatchZuSpiel(Spiel spiel, List<Match> matches) {
        return matches.stream()
                .filter(m -> m.getMatchID() == spiel.getMatchID())
                .findFirst()
                .orElse(null);
    }

public static class Spiel {
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


public static class Match {
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


}
