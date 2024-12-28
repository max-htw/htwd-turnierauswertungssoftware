<template>
  <div>
    <a href="/">Spielstand</a><br>
  <a href="/turnierplan">Turnierplan</a><br><br>
  <a href="/einstellungen">Einstellungen</a><br><br>
  Match: 1B vs. 1C<div class="section">
    Hinspiel:<br>
    <br>
    Richter: 1A<br>
    Spielfeld: 1<br>
    Timeslot: 6:30 - 7:00<br>
    <br>
    Punkte Team 1B: <br>
    Punkte Team 1C: <br>
  </div>
  <br>
  <div class="section">
    Rueckspiel:<br>
    <br>
    Richter: 1A<br>
    Spielfeld: 1<br>
    Timeslot: 6:00 - 6:30<br>
    <form action="/">
      <input type="hidden" name="editmatch" value="123"><br>
      Punkte Team 1B: <input type="text" name="score_team1_rueckspiel"><br>
      Punkte Team 1C: <input type="text" name="score_team2_rueckspiel"><br>
      <button type="submit">speichern</button><br>
    </form>
  </div>
  </div>
</template>
