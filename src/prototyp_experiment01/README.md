# Experimenteller Prototyp
## Compilieren und Ausführen
- Voraussetzung: installierte Java Standard Edition.
  - Zum Beispiel von hier: https://adoptium.net/temurin/releases/
    - JDK-Packung auswählen, nicht JRE.
- Befehle zum compilieren der .java-Dateien mit **javac** und verpacken sie mit **jar**
  - `javac *.java -d ./classes`
    - d - output directory
  - `jar -v -c -f volley.jar -e Main -C ./classes .`
    - v - verbose
    - c - create
	- f - output filename
	- e Main - Main-class ausfuehren
	- C - Directory mit den zuverpackenden Dateien
	- . - alle Dateien im Verzeichnis verpacken
- Ausführen mit **java**
  - `java -jar volley.jar`
  
nach dem Ausführen läuft auf dem System ein HTTP-Server auf dem Port :8440.
in einem Webbrowser die URL http://localhost:8440 aufrufen.

## Realisierter Umfang
- die Spielergebnisse können eingetragen und geändert werden
- Die Parameter "Anzahl Teams", "Anzahl Spilefelder", "Benutzerrole" funktionieren.
- Beim andern der "Anzahl Teams", "Anzahl Gruppen" werden die Spielergebnisse neu generiert.
- Übersicht und Auswertung der aktuellen Spielstände sind auf der Hauptseite in tabelarischer Form dargestellt.
- Bearbeiten des Spielstandes ist möglich durch klicken auf den Spielstand in der Tabelle.
- Auf der Seite "Turnierplan" wird ein automatisch generierter Turnierplan angezeigt.
- Benutzerrolen sind "Organisator", "Gast", "Team ..."
  - Organisator darf alle Spielstände bearbeiten.
  - Gast kann keine Spielstände bearbeiten.
  - Team X darf nur die Spielstände bearbeiten, wo es als Richter zugewiesen ist.
- Export zu PDF und CSV (aber sehr primitiv)

## Fehlende Funktionalität
- Http-Server kann nicht mehrere anfragen gleichzeitig bearbeiten.
- Daten können nicht gespeichert werden.
- Es können keine gespeicherten Turniere ausgewählt werden.
- Kein effizienter Algorythmus für die Turnierplanung
- Visuelle Gestaltung fehlt.

