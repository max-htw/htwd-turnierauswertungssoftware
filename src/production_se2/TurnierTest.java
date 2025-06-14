import java.util.*;

public class TurnierTest {
    public static void main(String[] args) {
        //List<Integer> anzTeamsInGruppe = List.of(6, 4); // definieren anzahl der Gruppen und Anzahl der Teams
List<Integer> anzTeamsInGruppe = List.of(3,5,3);
int anzahlTimeSlots = 1000;
int anzahlSpielfelder = 3;
boolean rueckspielErlaubt = false;

        int anzahlGruppen = anzTeamsInGruppe.size();

        List<TurnierplanGenerator.Spiel> plan = TurnierplanGenerator.generierePlan_01(
            anzahlGruppen,
            anzTeamsInGruppe,
            anzahlTimeSlots,
            anzahlSpielfelder,
            rueckspielErlaubt
        );

        druckePlanNachSlots(plan, anzTeamsInGruppe);
        int totalTeams = anzTeamsInGruppe.stream().mapToInt(i -> i).sum();
        zaehleTeamAktivitaet(plan, totalTeams, anzTeamsInGruppe);
    }

    public static void druckePlanNachSlots(List<TurnierplanGenerator.Spiel> turnierplan, List<Integer> anzTeamsInGruppe) {
        turnierplan.sort(Comparator
            .comparingInt(TurnierplanGenerator.Spiel::getTimeSlotNr)
            .thenComparingInt(TurnierplanGenerator.Spiel::getFeldNr));

        int aktuellerSlot = -1;

        for (TurnierplanGenerator.Spiel spiel : turnierplan) {
            int slot = spiel.getTimeSlotNr();
            int feld = spiel.getFeldNr();
            TurnierplanGenerator.Match match = spiel.getMatch();
            int team1 = match.getTeam1Nr();
            int team2 = match.getTeam2Nr();
            int schiri = spiel.getIstHinspiel()
                ? match.getRichterHinspielTeamNr()
                : match.getRichterRueckspielTeamNr();

            if (slot != aktuellerSlot) {
                System.out.println("\n=== Slot " + slot + " ===");
                aktuellerSlot = slot;
            }

            System.out.println("Feld " + feld + ": " +
                getTeamLabel(team1, anzTeamsInGruppe) + " vs " +
                getTeamLabel(team2, anzTeamsInGruppe) + " | Schiri: " +
                getTeamLabel(schiri, anzTeamsInGruppe));
        }
    }

    public static void zaehleTeamAktivitaet(List<TurnierplanGenerator.Spiel> turnierplan, int anzahlTeams, List<Integer> anzTeamsInGruppe) {
        int[] spieleAlsSpieler = new int[anzahlTeams];
        int[] spieleAlsSchiri = new int[anzahlTeams];

        for (TurnierplanGenerator.Spiel spiel : turnierplan) {
            TurnierplanGenerator.Match match = spiel.getMatch();
            int t1 = match.getTeam1Nr();
            int t2 = match.getTeam2Nr();
            int schiri = spiel.getIstHinspiel()
                ? match.getRichterHinspielTeamNr()
                : match.getRichterRueckspielTeamNr();

            spieleAlsSpieler[t1]++;
            spieleAlsSpieler[t2]++;
            if (schiri != -1) {
                spieleAlsSchiri[schiri]++;
            }
        }

        System.out.println("\n=== Spiele pro Team ===");
        for (int i = 0; i < anzahlTeams; i++) {
            System.out.println(getTeamLabel(i, anzTeamsInGruppe) + ": Spieler=" + spieleAlsSpieler[i] + ", Schiri=" + spieleAlsSchiri[i]);
        }
    }

    public static String getTeamLabel(int teamNr, List<Integer> anzTeamsInGruppe) {
        int gruppe = 0;
        int teamOffset = teamNr;
        for (int i = 0; i < anzTeamsInGruppe.size(); i++) {
            int size = anzTeamsInGruppe.get(i);
            if (teamOffset < size) {
                gruppe = i;
                break;
            } else {
                teamOffset -= size;
            }
        }
        return "[G" + gruppe + "-T" + teamOffset + "]";
    }
}
