import java.util.*;

public class TurnierTest {
    public static void main(String[] args) {
        int anzahlGruppen = 1;
        List<Integer> anzTeamsInGruppe = List.of(8);
        int anzahlTimeSlots = 16;
        int anzahlSpielfelder = 2;
        boolean rueckspielErlaubt = false;

        List<Spiel> plan = TurnierplanGenerator.generierePlan(
            anzahlGruppen,
            anzTeamsInGruppe,
            anzahlTimeSlots,
            anzahlSpielfelder,
            rueckspielErlaubt
        );

        druckePlanNachSlots(plan);
        zaehleTeamAktivitaet(plan, anzTeamsInGruppe.get(0));
    }

    public static void druckePlanNachSlots(List<Spiel> turnierplan) {
        turnierplan.sort(Comparator
            .comparingInt(Spiel::getTimeSlotNr)
            .thenComparingInt(Spiel::getFeldNr));

        int aktuellerSlot = -1;

        for (Spiel spiel : turnierplan) {
            int slot = spiel.getTimeSlotNr();
            int feld = spiel.getFeldNr();
            Match match = spiel.getMatch();
            int team1 = match.getTeam1Nr();
            int team2 = match.getTeam2Nr();
            int schiri = spiel.getIstHinspiel()
                ? match.getRichterHinspielTeamNr()
                : match.getRichterRueckspielTeamNr();

            if (slot != aktuellerSlot) {
                System.out.println("\n=== Slot " + slot + " ===");
                aktuellerSlot = slot;
            }

            System.out.println("Feld " + feld + ": " + team1 + " vs " + team2 + " | Schiri: " + schiri);
        }
    }

    public static void zaehleTeamAktivitaet(List<Spiel> turnierplan, int anzahlTeams) {
        int[] spieleAlsSpieler = new int[anzahlTeams];
        int[] spieleAlsSchiri = new int[anzahlTeams];

        for (Spiel spiel : turnierplan) {
            Match match = spiel.getMatch();
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
            System.out.println("Team " + i + ": Spieler=" + spieleAlsSpieler[i] + ", Schiri=" + spieleAlsSchiri[i]);
        }
    }
}
