# Experimenteller Prototyp
## Compilieren und Ausführen
- Voraussetzung: installierte Java Standard Edition.
  - Zum Beispiel von hier: https://adoptium.net/temurin/releases/
    - JDK-Packung auswählen, nicht JRE.
- Befehle zum compilieren der .java-Dateien mit **javac** und verpacken sie mit **jar**
  - `javac *.java -d ./classes`
    - d - output directory
  - `jar -v -c -f volley.jar -e Main -C ./classes .` (Achtung: Punkt am Ende nicht vergessen)
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
- Konfiguration des Turniers möglich:
  - Anzahl Gruppen, Anzahl Teams pro Gruppe, mit/ohne Rückspiele, Anzahl Spielfelder
- Eintragen/Ändern der Spielergebnisse
- Automatische Turnierplangenerierung (Algorythmus muss verbessert werden)
- Übersicht und Auswertung der aktuellen Spielstände sind auf der Hauptseite in tabelarischer Form dargestellt.
- Speichern des Turniers und Laden eines gespeicherten Turniers (nicht Permanent, nur im RAM)
- Export zu PDF und CSV (nur Ansatz)
- Benutzerrolen sind "Organisator", "Gast", "Team ..." (nur Ansatz)

## Fehlende Funktionalität
- Daten können nicht permanent gespeichert werden.
- Http-Server kann nicht mehrere anfragen gleichzeitig bearbeiten.
- Kein effizienter Algorythmus für die Turnierplanung
- Visuelle Gestaltung fehlt.

