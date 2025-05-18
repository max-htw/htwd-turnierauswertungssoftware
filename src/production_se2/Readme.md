# Bemerkung für Entwickler
Das Projekt lässt sich nicht ohne der Klasse DevSettings kompilieren.
Vorgehensweise (einmalig): die Datei "DevSettings.java.se2" in das selbe Verzeichnis __kopieren__ und die __Kopie__ zu "DevSettings.java" umbenennen.

Warum?
Die Klasse DevSettings ermöglicht jedem Entwickler einige Standardeinstellungen 
mit eigenen Werten zu überschreiben. Damit die überschriebene Einstellungen für
andere Entwickler nicht übernommen werden, ist die Datei 
DevSettings.java in .gitignore eingetragen und wird nicht zum Github hochgeladen.

Auch die externe Bibliotheken, die wir verwenden (z.B Junit und SQLite)
muss jeder Entwickler einmalig manuell nach libs-Verzeichnis herunterladen: 
[Links zu den Bibliotheken](libs/index.md).

# VSCode
Datei `.vscode\settings.json`
```
{
    "java.project.sourcePaths": ["./"],
    "java.project.outputPath": "bin",
    "java.project.referencedLibraries": ["libs/*.jar"]
}
```
1. Quellcode-Dateien liegen im Wurzelverzeichnis.
1. Für die kompilierte Dateien das bin-Verzeichnis verwenden.
1. Das libs-Verzeichnis zum ClassPath hunzufügen.

# IntelliJ
- das libs-Verzeichnis zum ClassPath hunzufügen:
  - Menü -> File -> Project Structure... -> Project Settings -> Modules -> Registerkarte "Dependencies" -> "+" -> JARs or Directories

# Compilieren, ausführen und testen ohne IDE
- Voraussetzung: installierte Java Standard Edition.
  - Zum Beispiel von hier: https://adoptium.net/temurin/releases/
    - JDK-Packung auswählen, nicht JRE.

Zuerst in den passenden Ordner wechseln, z.B.: `C:\htwd-turnierauswertungssoftware\src\production_se2\`

### Compilieren der .java-Dateien
- `javac -cp libs/* *.java -d ./classes`
  - d - output directory
  - cp - class path. Verweis auf das Verzeichnis mit externen bibliotheken.
    - z.B wir verwenden Bibliotheken Junit und SQLite 
  - Ergebnis: Alle Klassen der Projekts liegen als .class-Dateien im Ordner ./classes

### Ausführen der Anwendung
- Windows: `java -cp "classes;libs/*" Main`<br>Linux: `java -cp "classes:libs/*" Main`
  - cp - class path. Wo die Java-Klassen (externe und gerade kompilierte) zu finden sind.
  - Main - Name der auszufuehrenden Klasse mit der main() methode (in diesem Beispiel die Klasse mit dem Namen "Main")

### Ausführen von Tests
- Windows: `java -cp "classes;libs/*" org.junit.runner.JUnitCore HelloworldJunitTest1 HelloworldJunitTest2`<br>Linux: als ClassPath-Separator den Colon benutzen: "classes:libs/*".
  - `cp` - class path. Wo die Java-Klassen (externe und gerade kompilierte) zu finden sind.
  - `org.junit.runner.JUnitCore` - JUnit-Klasse, die Tests ausführt.
  - `HelloworldJunitTest1`, `HelloworldJunitTest2` - die Klassen mit den Tests
    - In der aktuellen (2025-05) Projektstruktur müssen die Testklassen zusammen mit anderen .java-Dateien im Wurzelverzeichnis des Projekts liegen. Es ist nicht möglich die Tests in einem Unterverzeichnis unterzubringen.  

### Verpacken der Classen in eine jar-Datei
- `jar -v -c -f volley.jar -e Main -C ./classes .`
  - v - verbose
  - c - create
  - f - output filename
  - e Main - Name einer Klasse mit der main()-Methode, die standardmäßig ausgeführ werden soll.
  - C - Directory mit den zuverpackenden Dateien
  - . - alle Dateien im Verzeichnis verpacken
- Ausführen mit **java**
  - `java -jar volley.jar`

