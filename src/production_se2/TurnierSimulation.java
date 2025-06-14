import java.util.*;

public class TurnierSimulation {
    public static void main(String[] args) {
        int anzahlTeams = 8;
        int anzahlGruppen = 1;
        int maxMatches = anzahlTeams * (anzahlTeams - 1) / 2; // 28 Spiele bei 8 Teams
        List<Integer> anzTeamsInGruppe = List.of(anzahlTeams);
        boolean rueckspielErlaubt = false;

        System.out.println("=== Simulation aller Kombinationen von Slots und Feldern ===");
        System.out.printf("Ziel: %d Spiele geplant bekommen (Round-Robin)%n", maxMatches);

        for (int anzahlTimeSlots = 10; anzahlTimeSlots <= 24; anzahlTimeSlots += 2) {
            for (int anzahlSpielfelder = 1; anzahlSpielfelder <= 4; anzahlSpielfelder++) {
                List<TurnierplanGenerator.Spiel> plan = TurnierplanGenerator.generierePlan_01(
                    anzahlGruppen,
                    anzTeamsInGruppe,
                    anzahlTimeSlots,
                    anzahlSpielfelder,
                    rueckspielErlaubt
                );

                int geplanteSpiele = plan.size();
                boolean vollstaendig = geplanteSpiele == maxMatches;

                System.out.printf("Slots=%2d, Felder=%d → Spiele: %2d/%2d %s%n",
                        anzahlTimeSlots,
                        anzahlSpielfelder,
                        geplanteSpiele,
                        maxMatches,
                        vollstaendig ? "gut" : "");
            }
        }
    }
}
