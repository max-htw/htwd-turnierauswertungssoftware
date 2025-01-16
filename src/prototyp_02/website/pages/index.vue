<template>
  <div class="m-1">
    <Nav />

    <h1 class="text-3xl">Startseite: Turnierauswertung</h1><br>

    <select class="rounded-lg p-1 bg-slate-100 hover:bg-slate-200">
      <option value="1">Schnelle Füchse</option>
    </select>

    <hr class="w-full mt-4 mb-4 border-b-1/2 border-black" />
    <Spielplan :team="3" />
    <hr class="w-full mt-4 mb-4 border-b-1/2 border-black" />
    <h2 class="text-2xl">Links:</h2><br>
    <ul class="list-decimal pl-5">
      <li><a href="turnierplan">Turnierplan</a></li>
      <li><a href="details">Match-Details</a></li>
      <li><a href="aktuell">Aktueller Stand</a></li>
    </ul>
  </div>
</template>
