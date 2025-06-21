import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.*;
import java.util.ArrayList;
import java.util.Map;

public class WebserverInternal_ContextHandler implements HttpHandler, RoleWithTaskBase_Renderer.ActionStringGenerator {

    private final int urlSectionsOffset = 0;

    private StringsRole _role = StringsRole.Stranger;
    private StringsRole getRole(){
        return _role;}
    private void setRole(StringsRole role){
        _role = role;}

    private StringsRole.RoleTask _task = StringsRole.KeineRoleTasks.SelectRole;
    private StringsRole.RoleTask getTask(){
        return _task;}
    private void setTask(StringsRole.RoleTask task){
        _task = task;}

    private int _groupNr = 0;
    private int getGroupNr(){
        return _groupNr;}
    private void setGroupNr(int groupnr){
        _groupNr = groupnr;}

    private int _teamNr = 0;
    private int getTeamNr(){
        return _teamNr;}
    private void setTeamNr(int teamnr){
        _teamNr = teamnr;}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //Attribute zuruecksetzen, da sonst sie erhalten ihre Werte zwischen den Requests
        setRole(StringsRole.Stranger);
        setTask(StringsRole.KeineRoleTasks.SelectRole);
        setGroupNr(0);
        setTeamNr(0);

        extractRole_Task_GroupNr_TeamNr_FromUrl(exchange.getRequestURI().getPath());
        Map<String, String> qs = MyHelpers.queryToMap(exchange.getRequestURI().getQuery());
        RequestVerteiler v = new RequestVerteiler(getRole(),
                getTask(), getGroupNr(), getTeamNr(), qs);

        ByteArrayOutputStream bs = new ByteArrayOutputStream();

        //Hier wird Response generiert:
        bs.write(v.getResponseHtmlBytes(this));

        exchange.sendResponseHeaders(200, bs.size());
        OutputStream os = exchange.getResponseBody();
        bs.writeTo(os);
        os.flush();
        os.close();
        exchange.close();
    }

    public static ArrayList<String> urlSectionsList(String path){
        //path: teil des URLs nach dem Domainname. Faengt immer mit dem Slash an.
        ArrayList<String> ausgabe = new ArrayList<>();
        for (String section : path.split("/")) {
            if (section.isEmpty()) continue;
            ausgabe.add(section.toLowerCase());
        }
        return  ausgabe;
    }

    private void extractRole_Task_GroupNr_TeamNr_FromUrl(String path) {
        ArrayList<String> sections = urlSectionsList(path);

        if (sections.size() < (urlSectionsOffset + 1)) {
            //Default role and default task (see above) will be used
        } else if (sections.get(urlSectionsOffset + 0).equals(StringsRole.Admin.name().toLowerCase())) {
            setRole(StringsRole.Admin);
            setTask(StringsRole.AdminTasks.Status);
            if (sections.size() >= (urlSectionsOffset + 2)) {
                String sect1 = sections.get(urlSectionsOffset + 1);
                if (sect1.equals(StringsRole.AdminTasks.Einstellungen.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Einstellungen);
                } else if (sect1.equals(StringsRole.AdminTasks.Turnierplan.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Turnierplan);
                } else if (sect1.equals(StringsRole.AdminTasks.Status.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Status);
                } else if (sect1.equals(StringsRole.AdminTasks.Matchdetails.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Matchdetails);
                } else if (sect1.equals(StringsRole.AdminTasks.Home.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Home);
                } else if (sect1.equals(StringsRole.AdminTasks.Historie.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Historie);
                }
                else if (sect1.equals(StringsRole.AdminTasks.Ergebnisse.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Ergebnisse);
                }
            }
            else{
                //Default task (see above) will be used
            }
        } else {
            //Pruefung, ob in dem Role-Abschnitt eine gueltige Gruppe steht:
            //es wird nur die Gueltigkeits des Formats bestimmt
            //ob die Zahlen stimmen oder nicht, wird spaeter im controller geprueft.

            if (sections.get(urlSectionsOffset + 0).length() == 2) {
                //Teamnummer wird in form "dL" dargestellt, mit d - Ziffer, L - Buchstabe.
                //z.B: 1A oder 8C.
                //
                //hier nehmen ich an, dass die Programm-interne nullbasierte nummerierung
                //wird nach aussen als 1-basierte Nummern angezeigt.
                //deswegen die im URL stehende TeamNr um 1 verkleinern.
                //die selbe vorgehensweise, aber in andere richtung gibt es in
                // generateActionString()
                setGroupNr(sections.get(urlSectionsOffset + 0).charAt(0) - 48 - 1);
                if (getGroupNr() < 0) {
                    setGroupNr(-1);
                } else {
                    setTeamNr(sections.get(urlSectionsOffset + 0).charAt(1) - 96 - 1);
                    if (getTeamNr() < 0)
                        setTeamNr(-1);
                }
            }
            if (getGroupNr() > -1 && getTeamNr() > -1) {
                setRole(StringsRole.Team);
                setTask(StringsRole.TeamTasks.Overview);
                if (sections.size() >= (urlSectionsOffset + 2)) {
                    String sect1 = sections.get(urlSectionsOffset + 1);
                    if (sect1.equals(StringsRole.TeamTasks.Overview.name().toLowerCase())) {
                        setTask(StringsRole.TeamTasks.Overview);
                    } else if (sect1.equals(StringsRole.TeamTasks.Matchdetails.name().toLowerCase())) {
                        setTask(StringsRole.TeamTasks.Matchdetails);
                    } else if (sect1.equals(StringsRole.TeamTasks.Turnierplan.name().toLowerCase())) {
                        setTask(StringsRole.TeamTasks.Turnierplan);
                    } else if (sect1.equals(StringsRole.TeamTasks.Stand.name().toLowerCase())) {
                        setTask(StringsRole.TeamTasks.Stand);
                    }
                }
                else{
                    //Default task will be used
                }
            }
        }
    }


  public String generateActionString(RoleWithTaskBase_Renderer.ActionForRoleAndTask action) {
      if(action == null){
        return "/todo/generateActionString(null)";
      }
      String roleTeil = action.role.name().toLowerCase();
      if(action.role == StringsRole.Team){
        //hier ist wie in extractRole_Task_GroupNr_TeamNr_FromUrl():
        // intern im Programm sind die Group/Team-Nummern nullbasiert,
        // aber der Nutzer soll 1-basierte nummern sehen.
        roleTeil = "" + (action.groupID + 1) + (char)(action.teamID + 96+1);
      }
      String out = "/" + roleTeil + "/" + action.task.toString().toLowerCase();
      String separator = "?";
      for(StringsActions sa : action.parameters.keySet()){
        out += separator + sa.name().toLowerCase() + "=" + action.parameters.get(sa);
        separator = "&";
      }
    return out;
  }

  public static class CssHttpHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange he) throws IOException {

      Headers headers = he.getResponseHeaders();
      headers.add("Content-Type", "text/css");

      File file = new File("output.css");
      byte[] bytes = new byte[(int) file.length()];
      //System.out.println(file.getAbsolutePath());
      //System.out.println("length:" + file.length());

      FileInputStream fileInputStream = new FileInputStream(file);
      BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
      bufferedInputStream.read(bytes, 0, bytes.length);

      he.sendResponseHeaders(200, file.length());
      OutputStream outputStream = he.getResponseBody();
      outputStream.write(bytes, 0, bytes.length);
      outputStream.close();
    }
  }

}


