import java.util.*;

public class TurnierplanGenerator {

public static List<Spiel> generierePlan(
        int anzahlGruppen,
        List<Integer> anzTeamsInGruppe,
        int anzahlTimeSlots,
        int anzahlSpielfelder,
        boolean rueckspielErlaubt
) {
    Map<Integer, Set<Integer>> belegteTeamsProSlot = new HashMap<>();
    Map<Integer, Set<Integer>> belegteTeamsTotalProSlot = new HashMap<>();
    List<Match> matchListe = new ArrayList<>();
    Map<Integer, Map<Integer, String>> rollenProTeam = new HashMap<>();
    int matchIdCounter = 0;

    for (int gruppe = 0; gruppe < anzahlGruppen; gruppe++) {
        int teamCount = anzTeamsInGruppe.get(gruppe);
        for (int i = 0; i < teamCount; i++) {
            for (int j = i + 1; j < teamCount; j++) {
                Match m = new Match();
                m.setGroupID(gruppe);
                m.setTeam1Nr(i);
                m.setTeam2Nr(j);
                m.setMatchID(matchIdCounter++);
                matchListe.add(m);
            }
        }
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
    Set<Integer> geplanteSpiele = new HashSet<>();

    for (int timeSlot = 0; timeSlot < anzahlTimeSlots; timeSlot++) {
        final int currentSlot = timeSlot;

        for (int feld = 0; feld < anzahlSpielfelder; feld++) {
            for (Spiel spiel : spielListe) {
                if (geplanteSpiele.contains(spiel.getMatchID())) continue;

                Match match = spiel.getMatch();
                int team1 = match.getTeam1Nr();
                int team2 = match.getTeam2Nr();

                Set<Integer> belegteTeams = belegteTeamsProSlot.getOrDefault(currentSlot, new HashSet<>());
                if (belegteTeams.contains(team1) || belegteTeams.contains(team2)) continue;

                // Temporär hinzufügen
                rollenProTeam.computeIfAbsent(currentSlot, k -> new HashMap<>()).put(team1, "Spiel");
                rollenProTeam.get(currentSlot).put(team2, "Spiel");

                // Wenn nicht erlaubt → entfernen и continue
                if (!rollenErlaubt(spiel, currentSlot, rollenProTeam, matchListe)) {
                    rollenProTeam.get(currentSlot).remove(team1);
                    rollenProTeam.get(currentSlot).remove(team2);
                    continue;
                }

                if (!rollenErlaubt(spiel, currentSlot, rollenProTeam, matchListe)) continue;

                spiel.setTimeSlotNr(currentSlot);
                spiel.setFeldNr(feld);

                // Temporarily add the game to the plan to simulate its presence
                turnierplan.add(spiel);

                if (spiel.getIstHinspiel()) {
                  match.setRichterHinspielTeamNr(-2);
                } else {
                  match.setRichterRueckspielTeamNr(-2);
                }

                List<Spiel> aktuelleSpieleImSlot = new ArrayList<>();
                for (Spiel s : turnierplan) {
                    if (s.getTimeSlotNr() == currentSlot) {
                        aktuelleSpieleImSlot.add(s);
                    }
                }

                Set<Integer> beschaeftigteTeams = new HashSet<>();
                for (Spiel s : aktuelleSpieleImSlot) {
                    Match m = s.getMatch();
                    beschaeftigteTeams.add(m.getTeam1Nr());
                    beschaeftigteTeams.add(m.getTeam2Nr());
                    int ref = s.getIstHinspiel() ? m.getRichterHinspielTeamNr() : m.getRichterRueckspielTeamNr();
                    if (ref != -1) beschaeftigteTeams.add(ref);
                }

                int schiriTeam = waehleSchiedsrichter(
                        spiel,
                        currentSlot,
                        rollenProTeam,
                        matchListe,
                        aktuelleSpieleImSlot,
                        anzTeamsInGruppe,
                        beschaeftigteTeams
                );

                if (schiriTeam == -1) {
                    turnierplan.remove(spiel);
                    continue;
                }

                beschaeftigteTeams.add(schiriTeam);


                spiel.setTimeSlotNr(currentSlot);
                spiel.setFeldNr(feld);

                if (spiel.getIstHinspiel()) {
                    match.setRichterHinspielTeamNr(schiriTeam);
                } else {
                    match.setRichterRueckspielTeamNr(schiriTeam);
                }

                geplanteSpiele.add(spiel.getMatchID());
                belegteTeamsProSlot.computeIfAbsent(currentSlot, k -> new HashSet<>()).add(team1);
                belegteTeamsProSlot.get(currentSlot).add(team2);

                aktualisiereBelegungen(spiel, schiriTeam, currentSlot, feld, rollenProTeam, belegteTeamsProSlot, match);

                belegteTeamsTotalProSlot.computeIfAbsent(currentSlot, k -> new HashSet<>()).add(team1);
                belegteTeamsTotalProSlot.get(currentSlot).add(team2);
                belegteTeamsTotalProSlot.get(currentSlot).add(schiriTeam);

                break;
            }
        }
    }
    return turnierplan;
}




    private static boolean feldFrei(int feld, int slot, Map<Integer, Set<Integer>> belegung) {
        return !belegung.getOrDefault(slot, new HashSet<>()).contains(feld);
    }

    private static boolean teamsFrei(Spiel spiel, int slot,
                                     Map<Integer, Map<Integer, String>> rollen, List<Match> matches) {
        Match match = findeMatchZuSpiel(spiel, matches);
        int t1 = match.getTeam1Nr();
        int t2 = match.getTeam2Nr();
        Map<Integer, String> rollenImSlot = rollen.getOrDefault(slot, new HashMap<>());
        return !rollenImSlot.containsKey(t1) && !rollenImSlot.containsKey(t2);
    }

    private static boolean rollenErlaubt(Spiel spiel, int slot,
                                     Map<Integer, Map<Integer, String>> rollen,
                                     List<Match> matches) {
    Match match = findeMatchZuSpiel(spiel, matches);
    int[] teams = {match.getTeam1Nr(), match.getTeam2Nr()};

    for (int team : teams) {
        if (hatDreiSpieleInFolge(team, slot, rollen)) return false;

        if (slot >= 2) {
            Map<Integer, String> r0 = rollen.getOrDefault(slot, new HashMap<>());
            Map<Integer, String> r1 = rollen.getOrDefault(slot - 1, new HashMap<>());
            Map<Integer, String> r2 = rollen.getOrDefault(slot - 2, new HashMap<>());

            String s0 = r0.getOrDefault(team, "Pause");
            String s1 = r1.getOrDefault(team, "Pause");
            String s2 = r2.getOrDefault(team, "Pause");

            if (s0.equals("Pause") && s1.equals("Pause") && s2.equals("Pause")) return false;
        }
    }

    return true;
    }

    private static boolean hatDreiSpieleInFolge(int team, int slot,
                                            Map<Integer, Map<Integer, String>> rollen) {
    if (slot < 2) return false;

    String r0 = rollen.getOrDefault(slot - 0, new HashMap<>()).getOrDefault(team, "Pause");
    String r1 = rollen.getOrDefault(slot - 1, new HashMap<>()).getOrDefault(team, "Pause");
    String r2 = rollen.getOrDefault(slot - 2, new HashMap<>()).getOrDefault(team, "Pause");

    return r0.equals("Spiel") && r1.equals("Spiel") && r2.equals("Spiel");
    }

   public static int waehleSchiedsrichter(
        Spiel spiel,
        int slot,
        Map<Integer, Map<Integer, String>> rollen,
        List<Match> matches,
        List<Spiel> aktuelleSpieleImSlot,
        List<Integer> anzTeamsInGruppe,
        Set<Integer> beschaeftigteTeams
) {
    Match match = findeMatchZuSpiel(spiel, matches);
    int t1 = match.getTeam1Nr();
    int t2 = match.getTeam2Nr();

    Map<Integer, Integer> schiriZaehler = new HashMap<>();
    Set<Integer> kandidaten = new HashSet<>();

    int maxTeamNumber = anzTeamsInGruppe.stream().mapToInt(i -> i).sum();

    for (int team = 0; team < maxTeamNumber; team++) {
        System.out.println("\nChecking team " + team + " for slot " + slot);

        // Skip if the team is playing in this match
        if (team == t1 || team == t2) {
            continue;
        }


        // Skip if the team is already playing or refereeing in this slot
        if (beschaeftigteTeams.contains(team)) {
            continue;
        }

        boolean allowed = true;

        // Check for forbidden referee patterns in previous slots
        if (slot >= 2) {
            String r0 = rollen.getOrDefault(slot, new HashMap<>()).getOrDefault(team, "Pause");
            String r1 = rollen.getOrDefault(slot - 1, new HashMap<>()).getOrDefault(team, "Pause");
            String r2 = rollen.getOrDefault(slot - 2, new HashMap<>()).getOrDefault(team, "Pause");

            System.out.println("- Previous roles: [" + r2 + ", " + r1 + ", " + r0 + "]");

            if ((r1.equals("Schiri") && r2.equals("Schiri")) ||
                (r2.equals("Schiri") && r1.equals("Pause") && r0.equals("Schiri"))) {
                allowed = false;
            }
        }

        // Add to candidates if all checks passed
        if (allowed) {
            kandidaten.add(team);

            // Count how often this team has been a referee
            int count = 0;
            for (Map<Integer, String> map : rollen.values()) {
                if ("Schiri".equals(map.get(team))) count++;
            }
            schiriZaehler.put(team, count);
        }
    }

    // Choose the team with the least referee assignments
    int chosen = kandidaten.stream()
            .min(Comparator.comparingInt(schiriZaehler::get))
            .orElse(-1);
    return chosen;
}




private static boolean istBeschaeftigt(int teamNr, int slot, List<Spiel> plan) {
    for (Spiel s : plan) {
        if (s.getTimeSlotNr() != slot) continue;

        Match m = s.getMatch();
        // spielt?
        if (m.getTeam1Nr() == teamNr || m.getTeam2Nr() == teamNr) return true;

        // Ricther?
        int schiri = s.getIstHinspiel()
                ? m.getRichterHinspielTeamNr()
                : m.getRichterRueckspielTeamNr();
        if (schiri == teamNr) return true;
    }
    return false;
}





    private static void aktualisiereBelegungen(Spiel spiel, int schiriTeam, int slot, int feld,
                                               Map<Integer, Map<Integer, String>> rollen,
                                               Map<Integer, Set<Integer>> belegung,
                                               Match match) {
        belegung.computeIfAbsent(slot, k -> new HashSet<>()).add(feld);

        rollen.computeIfAbsent(slot, k -> new HashMap<>()).put(match.getTeam1Nr(), "Spiel");
        rollen.get(slot).put(match.getTeam2Nr(), "Spiel");
        rollen.get(slot).put(schiriTeam, "Schiri");
    }

    private static Match findeMatchZuSpiel(Spiel spiel, List<Match> matches) {
        return matches.stream()
                .filter(m -> m.getMatchID() == spiel.getMatchID())
                .findFirst()
                .orElse(null);
    }
}
