# Datenmanipulationen
gruppiert nach der Seite, aus der die Datenmanipulation veranlasst wird.

- Keine Role / Select
  - no actions
- Admin / Status
  - no actions
- Admin / Turnierplan
  - createTurnierplan()
- Admin / Einstellungen
  - resetTurnierplan()
  - setAnzGroups(int anzGroups)
  - setAnzTeamsByeGroup(int anzTeams, int GroupID)
  - setNeedRueckspiele(bool need)
  - setAnzSpielfelder(int anz)
  - setNeedPrefillScores()
  - fillRandomPunktzahlen()
  - resetPunktzahlen()
  - saveTurnierKopie(String Turniername)
  - loadSavedTurnier(int turnierID)
- Admin / Matchdetails
  - setPunktzahlen(boolean Hinspiel, int pktTeam1, int pktTeam2)
- Team / Overview
  - no actions
- Team / Matchdetails
  - setPunktzahlen(boolean Hinspiel, int pktTeam1, int pktTeam2)
- Team / Turnierplan
  - no actions
- Team / Standk
  - no actions
  
# Datenabfragen
gruppiert nach der Seite
- Keine Role / Select
  - getGruppen(): (nr, Name)-Liste
  - getTeamsByGroup(int groupID): (nr, Name)-Liste
- Admin / Status
  - getGruppen(): (nr, Name)-Liste
  - getMatchesByGroup(int groupID): (team1Nr, team1Name, team1PunkteHinspiel, team1PunkteRueckspiel, team2Nr, team2Name, team2PunkteHinspiel, team2PunkteRueckspiel)-Liste
  - getTurnierAuswertungByGroup(int groupID): (teamNr, Punkte, anzFertigeSpiele)-Liste
    - sorted by Punkte
- Admin / Turnierplan
  - getTimeSlots(): (nr, textRepresentation)-Liste
  - getTurnierPlan(): (timeSlotNr, timeSlotText, feldNr, groupID, team1Nr, team1Name, team2Nr, team2Name, richterGroupID, richterTeamNr, richterTeamName)
    - geordnet nach Timeslot und dann nach FeldNr
- Admin / Einstellungen
  - getAnzTeamsByGroup(): (groupNr, anzTeams)-Liste
  - getAnzSpielfelder(): int
  - getNeedRueckspiele(): boolean
  - getNeedPrefillScores(): boolean
  - getSavedTurniere(): (nr, Name)-Liste
- Admin / Matchdetails
  - getMatch(int groupID, int team1ID, int team2ID): (team1Name, team2Name, hinspielRichterName, hinspielFeldNr, hinspielTimeslotText, hinspielPunkteTeam1, hispielPunkteTeam2, rueckspielRichterName, rueckspielFeldNr, rueckspielTimeslotText, rueckspielPunkteTeam1, hispielPunkteTeam2
- Team / Overview
  - getNextTimeslotsRelevantForTeam(int groupID, int teamNr): (timeSlotNr, timeSlotText, feldNr, team1Nr, team1Name, team2Nr, team2Name, richterGroupID, richterTeamNr, richterTeamName)
- Team / Matchdetails
  - getMatch(siehe oben)
- Team / Turnierplan
  - getTurnierplanRelevantForTeam(int groupID, int teamNr): (timeSlotNr, timeSlotText, feldNr, team1Nr, team1Name, team1Punkte, team2Nr, team2Name, team2Punkte, richterGroupID, richterTeamNr, richterTeamName)
- Team / Stand
 