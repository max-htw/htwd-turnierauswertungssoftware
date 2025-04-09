# Hinweise zur Nutzung von Tailwind
## Installation
- Voraussetzung: Vorinstalliertes [NPM](https://docs.npmjs.com/)
1. Tailwind installieren: `npm install tailwindcss @tailwindcss/cli`
2. Beim Arbeiten mit Tailwind `npx @tailwindcss/cli -i ./src/input.css -o ./src/output.css --watch` ausführen, damit die Eingabe-CSS-Datei von Tailwind konvertiert werden kann
3. Die HTML-Datei im Browser öffnen und nach Änderungen aktualisieren

## Sonstiges
- In Tailwind werden die verschiedenen Styles direkt in die HTML geschrieben, z.B. `<h1 class="text-4xl">Hello Tailwind!</h1>`
- Die Klasse text-4xl ist dabei in Tailwind vordefiniert. Alle dieser Definitionen findet man in der [Offiziellen Dokumentation](https://tailwindcss.com/docs/font-size)
- Zur Umsetzung von Custom Styles können in der input.css Definitionen vorgenommen werden, z.B. `--color-primary: #4287f5;`. Diese kann dann z.B. so `<h1 class="text-primary">Hello Tailwind!</h1>` in der HTML-Datei verwendet werden

## Beispiel
- Zur Demonstration habe ich die HTML aus `RoleAdmin_TaskEinstellungen.java` extrahiert und die dort enthaltene CSS in TailwindCSS konvertiert