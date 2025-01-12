<template>
  <div>
    <Nav/>

    <div style="border: 0.2vmin solid #0c0;padding: 1rem;width: 40vmin;font-size:3vmin;">
      <span style="text-decoration:underline;">Aktuelles Turnier</span><br><br>
      <div class="section">Anzahl Gruppen: 1<br>
        (aendern zu <a href="./einstellungen?setAnzGroups=2">2</a> | <a href="./einstellungen?setAnzGroups=3">3</a> | <a
          href="./einstellungen?setAnzGroups=4">4</a>)<br><br>
        Anzahl Teams Gruppe 1: 3<br>
        (aendern zu <a href="./einstellungen?setAnzTeams=4&refGroupID=1">4</a> | <a
          href="./einstellungen?setAnzTeams=5&refGroupID=1">5</a> | <a
          href="./einstellungen?setAnzTeams=6&refGroupID=1">6</a> | <a
          href="./einstellungen?setAnzTeams=7&refGroupID=1">7</a> | <a
          href="./einstellungen?setAnzTeams=8&refGroupID=1">8</a>)</div>
      <br/>
      Anzahl Spielfelder: 2<br/>
      (aendern zu <a href="./einstellungen?setAnzFields=1">1</a> | <a href="./einstellungen?setAnzFields=3">3</a> | <a
        href="./einstellungen?setAnzFields=4">4</a><br><br>
      mit Rueckspielen: true<br>
      (aendern zu <a href="./einstellungen?needrueckspiel=false">false</a>)<br>
      <br>
      Benutzerrolle: Organisator<br>
      (aendern zu <a href="./einstellungen?setRole=-1">Gast</a> | <a href="./einstellungen?setRole=3">Team 3</a><br><br>
      Vorausfuellen mit zufaelligen Punktzahlen: false<br>
      (aendern zu <a href="./einstellungen?setPrefillScores=true">true</a>)<br>
    </div>
    <br>
    <form>
      Aktuellen Turnier speichern unter:<input type="text" name="saveturnier" value="2024-12-25_22-54-25"><button
        type="submit">speichern</button>
    </form>
    <br>
    einen gespeicherten Turnier laden:<br>
  </div>
</template>
