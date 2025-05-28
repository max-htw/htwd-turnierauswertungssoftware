import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.TreeMap;

public class RoleAdmin_TaskEinstellungen_Data extends RoleWithTaskBase_Data{

  public int anzGruppen;
  public ArrayList<RoleWithTaskBase_Renderer.HyperLink> anzahlGruppen_Links = new ArrayList<>();

  public TreeMap<Integer, Integer> anzTeams_proGruppe = new TreeMap<>();
  public TreeMap<Integer, ArrayList<RoleWithTaskBase_Renderer.HyperLink>> anzTeams_Dictionary = new TreeMap<>();

  public boolean mitRueckspielen;
  public RoleWithTaskBase_Renderer.HyperLink mit_RueckspielenLink = new
    RoleWithTaskBase_Renderer.HyperLink("",null, false);

  public int anzSpielfelder;
  public ArrayList<RoleWithTaskBase_Renderer.HyperLink> anzSpielfelder_aendernLinks = new ArrayList<>();

  public boolean vorausfuellenData;
  public RoleWithTaskBase_Renderer.HyperLink vorausfuellenData_aendernLink = new
    RoleWithTaskBase_Renderer.HyperLink("", null, false);

  public RoleWithTaskBase_Renderer.ActionForRoleAndTask htmlFormAction =
    new RoleWithTaskBase_Renderer.ActionForRoleAndTask(null, null, -1, -1);

  public StringsActions textBoxName = StringsActions.saveTurnier;

  public ArrayList<RoleWithTaskBase_Renderer.HyperLink> savedTurniereLinks = new ArrayList<>();

  // Turnierkonfiguration
  public StringBuilder htmlConfig(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator) {
    StringBuilder r = new StringBuilder();

    // Dropdown-Menü zur Auswahl der Gruppen-Anzahl
    r.append("<p>Anzahl Gruppen: <select onchange=\"location.href=this.value\">");
    for(RoleWithTaskBase_Renderer.HyperLink n: anzahlGruppen_Links){
        String actionUrl = actionStringGenerator.generateActionString(n.linkAction);
        boolean selected = !n.isActive; // Nur das aktuelle Element ist "nicht aktiv" → also gerade gewählt
        r.append("<option value=\"").append(actionUrl).append("\"")
        .append(selected ? " selected" : "")
        .append(">")
        .append(n.linkText)
        .append("</option>");
    }
    r.append("</select></p>\n");

    // Dropdown-Menü zur Auswahl der Teams pro Gruppe
    for(int i = 0; i < anzGruppen; i++){
        r.append("<p>Anzahl Teams für Gruppe ").append(i + 1).append(": ");
        r.append("<select onchange=\"location.href=this.value\">");
        for(RoleWithTaskBase_Renderer.HyperLink n: anzTeams_Dictionary.get(i)){
            String actionUrl = actionStringGenerator.generateActionString(n.linkAction);
            boolean selected = !n.isActive;
            r.append("<option value=\"").append(actionUrl).append("\"")
            .append(selected ? " selected" : "")
            .append(">")
            .append(n.linkText)
            .append("</option>");
        }
        r.append("</select></p>\n");
    }

    // Dropdown-Menü für Hin-/Rückspiel
    r.append("<p>Hin- und Rückspiel: <select onchange=\"location.href=this.value\">");

    boolean aktuellTrue = mitRueckspielen == true;

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aTrue = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aTrue.parameters.put(StringsActions.needrueckspiel, "ja");
    String urlTrue = actionStringGenerator.generateActionString(aTrue);
    r.append("<option value=\"").append(urlTrue).append("\"").append(aktuellTrue ? " selected" : "").append(">true</option>");

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aFalse = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aFalse.parameters.put(StringsActions.needrueckspiel, "nein");
    String urlFalse = actionStringGenerator.generateActionString(aFalse);
    r.append("<option value=\"").append(urlFalse).append("\"").append(!aktuellTrue ? " selected" : "").append(">false</option>");

    r.append("</select></p>\n");

    // Dropdown-Menü für Anzahl der Spielfelder
    r.append("<p>Anzahl Spielfelder: <select onchange=\"location.href=this.value\">");
    for(RoleWithTaskBase_Renderer.HyperLink n: anzSpielfelder_aendernLinks){
        String actionUrl = actionStringGenerator.generateActionString(n.linkAction);
        boolean selected = !n.isActive;
        r.append("<option value=\"").append(actionUrl).append("\"")
        .append(selected ? " selected" : "")
        .append(">")
        .append(n.linkText)
        .append("</option>");
    }
    r.append("</select></p>\n");

    // Eingabefeld für die Startzeit des Turniers
    r.append("<p>Startzeit des Turniers: <input type=\"time\" name=\"startzeit\" value=\"08:00\"></p>\n");


    // Dropdown-Menp zur Auswahl der Spieldauer
    r.append("<p>Spieldauer (Minuten): <select name=\"spieldauer\">\n");
    r.append("<option value=\"15\">15 Minuten</option>\n");
    r.append("<option value=\"20\">20 Minuten</option>\n");
    r.append("<option value=\"25\">25 Minuten</option>\n");
    r.append("<option value=\"30\">30 Minuten</option>\n");
    r.append("</select></p>\n");


    // Dropdown für Vorausfüllen mit Zufallsdaten
    r.append("<p>(((Vorausfüllen aktivieren:))) <select onchange=\"location.href=this.value\">");

    boolean vorausfuellenAktuellTrue = vorausfuellenData == true;

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aPrefillTrue = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aPrefillTrue.parameters.put(StringsActions.setPrefillScores, "true");
    String urlPrefillTrue = actionStringGenerator.generateActionString(aPrefillTrue);
    r.append("<option value=\"").append(urlPrefillTrue).append("\"").append(vorausfuellenAktuellTrue ? " selected" : "").append(">true</option>");

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aPrefillFalse = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aPrefillFalse.parameters.put(StringsActions.setPrefillScores, "false");
    String urlPrefillFalse = actionStringGenerator.generateActionString(aPrefillFalse);
    r.append("<option value=\"").append(urlPrefillFalse).append("\"").append(!vorausfuellenAktuellTrue ? " selected" : "").append(">false</option>");

    r.append("</select></p>\n");

    // Aktuelles Turnier speichern
    r.append("<br>Aktuelles Turnier speichern unter:<br>\n")
      .append("<form action=\"").append(actionStringGenerator.generateActionString(htmlFormAction)).append("\" >\n")
      .append("<input type=\"text\" name=\"").append(textBoxName.name().toLowerCase()).append("\" value=\"")
      .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))).append("\">\n")
      .append("<button class=\"bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full\" type=\"submit\">Speichern</button>\n</form>");

    return r;
  }

  // vereint die beiden Methoden htmlUebersicht() und htmlOfDerrivedClass()
  @Override
  public StringBuilder htmlOfDerrivedClass(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator) {
    StringBuilder r = new StringBuilder();
    r.append("<hr>"); // optische Trennung
    r.append(htmlConfig(actionStringGenerator));
    return r;
  }
}
