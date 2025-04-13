import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;

public class WebserverInternal_ContextHandler implements HttpHandler {

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
        bs.write(v.getResponseBytes());

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
                } else if (sect1.equals(StringsRole.AdminTasks.Hallo.name().toLowerCase())) {
                    setTask(StringsRole.AdminTasks.Hallo);
                }
            }
            else{
                //Default task (see above) will be used
            }
        } else {
            //checking if the role-section contains a valid group:

            if (sections.get(urlSectionsOffset + 0).length() == 2) {
                //Teamnummer wird in form "dL" dargestellt, mit d - Ziffer, L - Buchstabe.
                //z.B: 1A oder 8C.
                setGroupNr(sections.get(urlSectionsOffset + 0).charAt(0) - 48);
                if (getGroupNr() < 1 || getGroupNr() > DataBaseQueries.get_anzGroups()) {
                    setGroupNr(0);
                } else {
                    setTeamNr(sections.get(urlSectionsOffset + 0).charAt(1) - 96);
                    if (getTeamNr() < 1 || getTeamNr() > DataBaseQueries.get_anzTeams(getGroupNr()))
                        setTeamNr(0);
                }
            }
            if (getGroupNr() > 0 && getTeamNr() > 0) {
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
}
