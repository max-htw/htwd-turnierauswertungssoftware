# Bemerkung für Entwickler
Das Projekt lässt sich nicht ohne der Klasse DevSettings kompilieren.
Vorgehensweise (einmalig): die Datei "DevSettings.java.se2" in das selbe Verzeichnis __kopieren__ und die __Kopie__ zu "DevSettings.java" umbenennen.

Warum?
Die Klasse DevSettings ermöglicht jedem Entwickler einige Standardeinstellungen 
mit eigenen Werten zu überschreiben. Damit die überschriebene Einstellungen für
andere Entwickler nicht übernommen werden, ist die Datei 
DevSettings.java in .gitignore eingetragen und wird nicht zum Github hochgeladen. 
