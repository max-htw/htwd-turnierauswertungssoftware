<script lang="ts">
export default {
  props: {
    team: {
      type: Number,
      default: 0
    }
  }
}
</script>

<style>
td {
  @apply font-normal;
}
</style>
<template>
  <h2 class="text-2xl">Aktuelles Spiel ({{ team }}):</h2><br>
  <table class="border-2 rounded-lg table border-separate border-spacing-2">
    <thead>
      <tr>
        <th>Uhrzeit</th>
        <th>Spielfeld</th>
        <th>Team A</th>
        <th>Team B</th>
        <th>Schiedsrichter</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>9:00-9:30</td>
        <td>1</td>
        <td>Schnelle Füchse</td>
        <td>Flinke Hasen</td>
        <td>Faule Giraffen</td>
      </tr>

    </tbody>
  </table>
</template>
