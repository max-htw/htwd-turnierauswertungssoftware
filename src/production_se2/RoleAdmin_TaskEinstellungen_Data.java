import com.sun.source.tree.Tree;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.TreeMap;

public class RoleAdmin_TaskEinstellungen_Data extends RoleWithTaskBase_Data{

  public int anzGruppen;
  public ArrayList<RoleWithTaskBase_Renderer.NaviLink> anzahlGruppen_Links = new ArrayList<>();

  public TreeMap<Integer, Integer> anzTeams_proGruppe = new TreeMap<>();
  public TreeMap<Integer, ArrayList<RoleWithTaskBase_Renderer.NaviLink>> anzTeams_Dictionary = new TreeMap<>();

  public boolean mitRueckspielen;
  public RoleWithTaskBase_Renderer.NaviLink mit_RueckspielenLink = new
    RoleWithTaskBase_Renderer.NaviLink("","", false);

  public int anzSpielfelder;
  public ArrayList<RoleWithTaskBase_Renderer.NaviLink> anzSpielfelder_aendernLinks = new ArrayList<>();

  public boolean vorausfuellenData;
  public RoleWithTaskBase_Renderer.NaviLink vorausfuellenData_aendernLink = new
    RoleWithTaskBase_Renderer.NaviLink("", "", false);

  @Override
  public StringBuilder htmlOfDerrivedClass() {
    StringBuilder r = new StringBuilder();

    r.append("<p>Anzahl Gruppen = ").append(anzGruppen).append("; Aendern zu: ");

    for(RoleWithTaskBase_Renderer.NaviLink n: anzahlGruppen_Links){
      String href_or_style = "href=\"" + n.linkUrl +"\"";
      if(n.isActive)
        href_or_style = "style=\"font-weight:bold;\" ";
      r.append("<a ").append(href_or_style).append(">").append(n.linkText).append("</a> | \n");
    }
    for(int g: anzTeams_proGruppe.keySet()){
      r.append("<br>Anzahl Teams in der Gruppe ").append(g).append(" = ").append(anzTeams_proGruppe.get(g))
        .append("; Aendern zu: ");
      for(RoleWithTaskBase_Renderer.NaviLink n:anzTeams_Dictionary.get(g)){
        String href_or_style = "href=\"" + n.linkUrl +"\"";
        if(n.isActive)
          href_or_style = "style=\"font-weight:bold;\" ";
        r.append("<a ").append(href_or_style).append(">").append(n.linkText).append("</a> | \n");
      }
    }

    r.append("<br>Mit Rueckspielen = ").append(mitRueckspielen).append("; Aendern zu ").
      append("<a href=\"").append(mit_RueckspielenLink.linkUrl).append("\">").append(mit_RueckspielenLink.linkText).
      append("</a><br>\n");

    r.append("Vorausfuellen mit Zufallsdaten = ").append(vorausfuellenData).append("; Aendern zu ").
      append("<a href=\"").append(vorausfuellenData_aendernLink.linkUrl).append("\">").
      append(vorausfuellenData_aendernLink.linkText).append("</a><br>\n");

    return  r;
  }
}
