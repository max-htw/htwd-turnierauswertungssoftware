# SQLite Dokumentation
## Implementation
SQLite wird beim Kompilieren direkt mit integriert und muss nicht lokal installiert werden. Genauere Hinweise dazu findet ihr in der [README.md](/src/prototyp_experiment01/README.md)

## Dateienstruktur
- Unter prototyp_experiment01/src/database.db befindet sich die SQLite-Datei. In dieser werden alle Tabellen angelegt.
- Unter prototyp_experiment01/src/DataBaseManager.java befindet sich die Java-Datei, die genutzt wird um auf die Tabellen zuzugreifen (z.B. per SELECT, INSERT, UPDATE etc.). 

## Beispiel
In den o.g. Dateien habe ich jeweils ein Beispiel angelegt, wie ein Zugriff dieser Art funktionieren könnte.