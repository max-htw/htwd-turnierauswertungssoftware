import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class RoleWithTaskBase_Renderer<Class_of_Daten extends  RoleWithTaskBase_Data> {

    protected Class_of_Daten daten;
    public ArrayList<String> scriptSections = new ArrayList<>();
    public ArrayList<String> styleSections = new ArrayList<>();
    public abstract Class_of_Daten getEmptyDaten();

    private String _cssHeaderString = "<link href=\"/output.css\" rel=\"stylesheet\" />\n";
    public void turnOffCss(){
      _cssHeaderString = "";
    }

    RoleWithTaskBase_Renderer(){
        daten = getEmptyDaten();
        _actionStringGenerator = new ActionStringGenerator() {
          @Override
          public String generateActionString(ActionForRoleAndTask action) {
            return "/ToDo/RoleWithTaskBase_Renderer()/init/_actionStringGenerator";
          }
        };
    }

    public StringBuilder renderHtmlResponse(){

        turnOffCss();

        StringBuilder r = new StringBuilder();

        if(!this.getClass().getName().equals(RoleWithTaskBase_Renderer.class.getName())) {
            r.append("<p>Das ist die Standardimplementierung von RoleWithTaskBase_Renderer.renderHtmlResponse().<br>\n" +
                    "Sie soll in " + this.getClass().getName() + ".renderHtmlResponse() überschrieben werden.<br>\n" +
                    "</p>\n");
        }

        //einfache HTML-Ausgabe der Daten
        r.append(daten.htmlUebersicht(_actionStringGenerator).toString());

        return  r;
    }

    public String RenderHtmlAnfang_(){

        String s = "<!DOCTYPE html>\n<html>\n<head>\n";
        s+= "<meta charset=\"utf-8\">\n" +
          "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
          _cssHeaderString ;
        if(!daten.htmlTitel.isEmpty()) {
          s+= "<title>" + daten.htmlTitel + "</title>\n";
        }

        if(!_cssHeaderString.isEmpty()){
          for(String st: styleSections){
            s += "<style>\n" + st + "\n</style>\n";
          }
        }

        //wenn css ausgeschaltet ist, gehe ich davon aus, dass JavaScript
        //auch ausgeschaltet werden muss.
        //sonst muss man hier noch ein JavaScript-Flag implementieren.
        if(!_cssHeaderString.isEmpty()){
          for(String scr: scriptSections){
            s += "<script>\n" + scr + "\n</script>\n";
          }
        }
        s += "\n</head>\n<body>\n\n";

        return  s;
    }

    public String RenderHtmlEnde_(){
        return "\n</body>\n</html>";
    }

    public String RenderBodyAnfang_(){
      return "";
    }
    public String RenderBodyEnde_(){
      return "";
    }

    public  static  class HyperLink {
        HyperLink(String text, ActionForRoleAndTask action, boolean active) {
            linkText = text;
            linkAction = action ;
            isActive = active;
        }
        public String linkText;
        ActionForRoleAndTask linkAction;
        public boolean isActive;
    }

    public static class ActionForRoleAndTask{
        public StringsRole role;
        public StringsRole.RoleTask task;
        public int groupID;
        public int teamID;
        public HashMap<StringsActions, String>  parameters;

        public ActionForRoleAndTask(StringsRole role, StringsRole.RoleTask task, int groupID, int teamID){
          this.role = role;
          this.task = task;
          this.groupID = groupID;
          this.teamID = teamID;
          this.parameters = new HashMap<>();
        }
    }

    public interface ActionStringGenerator{
      public String generateActionString(ActionForRoleAndTask action);
    }

    private ActionStringGenerator _actionStringGenerator;
    public void setActionStringGenerator(ActionStringGenerator actionStringGenerator){
      _actionStringGenerator = actionStringGenerator;
    }
    public ActionStringGenerator get_actionStringGenerator() {
      return _actionStringGenerator;
    }
    public String getHref(ActionForRoleAndTask action){
      return _actionStringGenerator.generateActionString(action);
    }

  /*
    Methoden zur Ausgabe eines Swing JPanel
     */
  public JPanel renderJPanel(int width, int height, ActionListener actionListener){
    JPanel p = new JPanel();

    p.setSize(width, height);

    String msg = "ToDo: renderJPanel()";
    if(!this.getClass().getName().equals(RoleWithTaskBase_Renderer.class.getName())) {
      msg = "Das ist die Standardimplementierung von RoleWithTaskBase_Renderer.renderJPanel().\n" +
        "Sie soll in " + this.getClass().getName() + ".renderJPanel() ueberschrieben werden.\n";
    }

    JTextArea msgLbl = new JTextArea(msg);
    p.add(msgLbl);
    return p;
  }

  public static class JTableButtonRenderer implements TableCellRenderer {
    @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                             boolean hasFocus, int row, int column) {
      return (JButton)value;
    }
  }

}

