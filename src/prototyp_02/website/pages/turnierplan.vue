<style>
td {
  @apply text-center align-middle min-w-36 h-12 text-lg font-bold;
}
</style>
<template>
  <a href="/">Spielstand</a><br>
  <a href="/turnierplan">Turnierplan</a><br><br>
  <a href="/einstellungen">Einstellungen</a><br><br>
  <br>
  <a href="/turnier.pdf" target="blanck">PDF Herunterladen</a><br>
  <a href="/turnier.csv" target="blanck">Excel Herunterladen</a><br><br>
  <table>
    <tbody>
      <tr>
        <th></th>
        <th>Feld 1</th>
        <th>Feld 2</th>
      </tr>
      <tr>
        <td>6:00 - 6:30</td>
        <td><a href="/match/132">1B vs. 1C | 1A</a></td>
        <td></td>
      </tr>
      <tr>
        <td>6:30 - 7:00</td>
        <td><a href="/match/123">1B vs. 1C | 1A</a></td>
        <td></td>
      </tr>
      <tr>
        <td>7:00 - 7:30</td>
        <td><a href="/match/131">1A vs. 1C | 1B</a></td>
        <td></td>
      </tr>
      <tr>
        <td>7:30 - 8:00</td>
        <td><a href="/match/113">1A vs. 1C | 1B</a></td>
        <td></td>
      </tr>
      <tr>
        <td>8:00 - 8:30</td>
        <td><a href="/match/121">1A vs. 1B | 1C</a></td>
        <td></td>
      </tr>
      <tr>
        <td>8:30 - 9:00</td>
        <td><a href="/match/112">1A vs. 1B | 1C</a></td>
        <td></td>
      </tr>
      <tr>
        <td>9:00 - 9:30</td>
        <td></td>
        <td></td>
      </tr>
    </tbody>
  </table>
</template>
