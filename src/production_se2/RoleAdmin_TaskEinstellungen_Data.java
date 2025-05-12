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






  @Override
  public StringBuilder htmlOfDerrivedClass(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator) {
    StringBuilder r = new StringBuilder();

    r.append("<p>Anzahl Gruppen = ").append(anzGruppen).append("; Aendern zu: ");

    for(RoleWithTaskBase_Renderer.HyperLink n: anzahlGruppen_Links){
      String href_or_style = "href=\"" + actionStringGenerator.generateActionString(n.linkAction) +"\"";
      if(!n.isActive)
        href_or_style = "style=\"font-weight:bold;\" ";
      r.append("<a ").append(href_or_style).append(">").append(n.linkText).append("</a> | \n");
    }
    for(int g: anzTeams_proGruppe.keySet()){
      r.append("<br>Anzahl Teams in der Gruppe ").append(g).append(" = ").append(anzTeams_proGruppe.get(g))
        .append("; Aendern zu: ");
      for(RoleWithTaskBase_Renderer.HyperLink n:anzTeams_Dictionary.get(g)){
        String href_or_style = "href=\"" + actionStringGenerator.generateActionString(n.linkAction) +"\"";
        if(!n.isActive)
          href_or_style = "style=\"font-weight:bold;\" ";
        r.append("<a ").append(href_or_style).append(">").append(n.linkText).append("</a> | \n");
      }
    }

    r.append("<br>Mit Rueckspielen = ").append(mitRueckspielen).append("; Aendern zu ").
      append("<a href=\"").append(actionStringGenerator
        .generateActionString(mit_RueckspielenLink.linkAction))
        .append("\">")
        .append(mit_RueckspielenLink.linkText)
        .append("</a><br>\n");

    r.append("Anzahl Spielfelder = ").append(anzSpielfelder).append("; Aendern zu ");
    for(RoleWithTaskBase_Renderer.HyperLink n: anzSpielfelder_aendernLinks){
      String href = "href=\"" + actionStringGenerator.generateActionString(n.linkAction) +"\"";
      r.append("<a ").append(href).append(">").append(n.linkText).append("</a> | \n");
    }

    r.append("<br>Vorausfuellen mit Zufallsdaten = ").append(vorausfuellenData).append("; Aendern zu ")
        .append("<a href=\"").append(
          actionStringGenerator.generateActionString(vorausfuellenData_aendernLink.linkAction))
        .append("\">")
        .append(vorausfuellenData_aendernLink.linkText)
        .append("</a><br>\n");

    r.append("<br>Aktuelles Turnier speichern unter:<br>\n")
      .append("<form action=\"")
      .append(actionStringGenerator.generateActionString(htmlFormAction))
      .append("\" >\n<input type=\"text\" name=\"")
      .append(textBoxName.name().toLowerCase())
      .append("\" value=\"")
      .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss")))
      .append("\"><button type=\"submit\">speichern</button>\n</form>");

    r.append("<br>Ein gespeichertes Turnier laden:<br>\n");
      for(RoleWithTaskBase_Renderer.HyperLink n : savedTurniereLinks){
        r.append("<a href=\"");
        r.append(actionStringGenerator.generateActionString(n.linkAction));
        r.append("\">").append(n.linkText).append("</a><br>\n");
      }
    return  r;
  }
}
