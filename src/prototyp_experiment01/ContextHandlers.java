import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class ContextHandlers {
    private static  String qs_setAnzahlGroups = "setAnzGroups";
    private static  String qs_setAnzahlTeams = "setAnzTeams";
    private static  String qs_refGroupID = "refGroupID";
    private static  String qs_setAnzahlSpielfelder = "setAnzFields";
    private static  String qs_setRole = "setRole";
    private static  String qs_setPrefillScores = "setPrefillScores";
    private static  String qs_matchID = "matchid";
    private static  String qs_editmatch = "editmatch";
    private static  String qs_needRueckspiele = "needrueckspiel";
    private static  String qs_loadArchiv = "loadarchiv";

    public static Map<String, String> queryToMap(String query) {
        Map<String, String> result = new HashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
        }
        return result;
    }

    public static  ArrayList<String> urlSectionsList(String path){
        //path: teil des URLs nach dem Domainname. Faengt immer mit dem Slash an.
        ArrayList<String> ausgabe = new ArrayList<>();
        for (String section : path.split("/")) {
            if (section.isEmpty()) continue;
            ausgabe.add(section.toLowerCase());
        }
        return  ausgabe;
    }

    static class CombHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            Map<String, String> qs = ContextHandlers.queryToMap(exchange.getRequestURI().getQuery());

            StringsRole role = StringsRole.Stranger;
            StringsRole.RoleTask task = StringsRole.KeineRoleTasks.SelectRole;
            RoleWithTaskBase roleHandler = new RoleKeine_TaskSelectRole(bs,qs);

            ArrayList<String> sections = urlSectionsList(exchange.getRequestURI().getPath());
            
            if (sections.size() < (AppSettings.urlSectionsOffset + 1)){
                //Default role and default task (see above) will be used
            } else if (sections.get(AppSettings.urlSectionsOffset + 0).equals(StringsRole.Admin.name().toLowerCase()))
            {
                role = StringsRole.Admin;
                task = StringsRole.AdminTasks.Status;
                roleHandler = new RoleAdmin_TaskStatus(bs,qs);
                if(sections.size() < (AppSettings.urlSectionsOffset + 2)){
                    //Default task will be used
                }
                else if (sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.AdminTasks.Einstellungen.name().toLowerCase())){
                    task = StringsRole.AdminTasks.Einstellungen;
                    roleHandler = new RoleAdmin_TaskEinstellungen(bs,qs);
                }
                else if(sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.AdminTasks.Turnierplan.name().toLowerCase())){
                    task = StringsRole.AdminTasks.Turnierplan;
                    roleHandler = new RoleAdmin_TaskTurnierplan(bs,qs);
                }
                else if(sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.AdminTasks.Status.name().toLowerCase())){
                    task = StringsRole.AdminTasks.Status;
                    roleHandler = new RoleAdmin_TaskStatus(bs,qs);
                }
                else if(sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.AdminTasks.Matchdetails.name().toLowerCase())){
                    task = StringsRole.AdminTasks.Matchdetails;
                    roleHandler = new RoleAdmin_TaskMatchdetails(bs,qs);
                }
            }
            else{
                //checking if the role-section contains a valid group:
                int groupNr = 0;
                int teamNr = 0;
                if(sections.get(AppSettings.urlSectionsOffset + 0).length() == 2){
                    groupNr = sections.get(AppSettings.urlSectionsOffset + 0).charAt(0) - 48;
                    if(groupNr < 1 || groupNr > AppSettings.get_anzGroups()) {
                        groupNr = 0;
                    }
                    else{
                        teamNr = sections.get(AppSettings.urlSectionsOffset + 0).charAt(1) - 96;
                        if(teamNr < 1 || teamNr > AppSettings.get_anzTeams(groupNr))
                            teamNr = 0;
                    }
                }
                if(groupNr > 0 && teamNr > 0){
                    role = StringsRole.Team;
                    task = StringsRole.TeamTasks.Overview;
                    roleHandler = new RoleTeam_TaskOverview(bs,qs, groupNr, teamNr);
                    if(sections.size() < (AppSettings.urlSectionsOffset + 2)){
                        //Default task will be used
                    }
                    else if (sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.TeamTasks.Overview.name().toLowerCase())){
                        task = StringsRole.TeamTasks.Overview;
                        roleHandler = new RoleTeam_TaskOverview(bs,qs, groupNr, teamNr);
                    }
                    else if(sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.TeamTasks.Matchdetails.name().toLowerCase())){
                        task = StringsRole.TeamTasks.Matchdetails;
                        roleHandler = new RoleTeam_TaskMatchdetails(bs,qs, groupNr, teamNr);
                    }
                    else if(sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.TeamTasks.Turnierplan.name().toLowerCase())){
                        task = StringsRole.TeamTasks.Turnierplan;
                        roleHandler = new RoleTeam_TaskTurnierplan(bs,qs, groupNr, teamNr);
                    }
                    else if(sections.get(AppSettings.urlSectionsOffset + 1).equals(StringsRole.TeamTasks.Stand.name().toLowerCase())){
                        task = StringsRole.TeamTasks.Stand;
                        roleHandler = new RoleTeam_TaskStand(bs,qs, groupNr, teamNr);
                    }

                }
            }

            if(AppSettings.urlSectionsOffset > sections.size()){
                for(int i = sections.size(); i<AppSettings.urlSectionsOffset; i++){
                    roleHandler.pathPrefix += "/s" + (i+1);
                }
            }
            for(int i=0; i<AppSettings.urlSectionsOffset && i<sections.size(); i++){
                roleHandler.pathPrefix += "/" + sections.get(i);
            }
            roleHandler.handleRequest();

            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }

    static class TurnierPdfHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            /**
             * Create font object with times roman font & give it name as F1.
             */
            PDFObject fontName = new PDFObject("Font");
            fontName.addKey("Subtype", "/Type1");
            fontName.addKey("BaseFont", "/Times-Roman");
            PDFObject font = new PDFObject("Font", new PDFObject("F1", fontName));

            /**
             * Create text object with above font with size 18 & coordinate position (10,10)
             * with text "Hello World"
             */

            String inhaltStr = "Turnierplan (erstellt am " + LocalDateTime.now() +
                    "); Anzahl Felder: " + AppSettings.get_anzSpielfelder() +
                    "; Anzahl Gruppen: " + AppSettings.get_anzGroups() +
                    "; " + (AppSettings.needRueckspiele()?"mit Rueckspielen":"ohne Rueckspiele");


            PDFObject text = new PDFObject(4, 0);
            text.addKey("Length", Integer.toString(inhaltStr.getBytes().length));
            text.addTextStream("F1", 10, 10, 300, inhaltStr);


            /**
             * Create page object with above font & text
             */
            PDFObject page = new PDFObject(3, 0, "Page");
            page.addObjectKey("Resources", font);
            page.addObjectReferenceKey("Contents", text);

            /**
             * Create pages object with above page.
             */
            PDFObject pages = new PDFObject(2, 0, "Pages");
            pages.addKey("Count", "1");
            pages.addKey("MediaBox", "[0 0 600 400]");
            pages.addObjectReferenceArrayKey("Kids", page);

            page.addObjectReferenceKey("Parent", pages);

            /**
             * Create root object wrapping pages object.
             */
            PDFObject root = new PDFObject(1, 0, "Catalog");
            root.addObjectReferenceKey("Pages", pages);

            /**
             * Create PDF with abvoe root & all of the objects
             */
            PDF pdf = new PDF(root, pages, page, text);

            /**
             * Write PDF to a file.
             */
            //FileWriter fileWriter = new FileWriter("generatedPDF.pdf");
            //fileWriter.write(pdf.build());
            //fileWriter.close();

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write((pdf.build()).getBytes());

            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Content-Type", "application/pdf");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }

    static class TurnierCsvHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            ByteArrayOutputStream bs = new ByteArrayOutputStream();

            bs.write(("Zeit").getBytes());
            for(int i=0; i<AppSettings.get_anzSpielfelder(); i++){
                bs.write((";Feld " + (i+1) ).getBytes());
            }
            bs.write(("\n").getBytes());

            int idx=0;
            boolean endeErreicht = false;
            while(!endeErreicht) {
                endeErreicht = true;

                bs.write((AppSettings.getTimeSlotStr(idx)).getBytes());//Zeit-Spalte
                for (int i = 0; i < AppSettings.get_anzSpielfelder(); i++) {

                    bs.write((";").getBytes());
                    try{

                        MyHelpers.FeldSpiele f = DataBaseQueries.getTurnierplan().get(i);
                        if (f.getAnzahlSpiele() > idx) {
                            endeErreicht = false;
                            Integer h = f.getSpielHashByIdx(idx);
                            if(h > 0) {
                                MyHelpers.SpielStats stats = DataBaseQueries.getSpielStatsByHash(h);
                                bs.write(("" + stats.groupid + AppSettings.getTeamLetter(stats.team1) +
                                        " vs. " + stats.groupid + AppSettings.getTeamLetter(stats.team2) +
                                        " | " + stats.richter.x +
                                        AppSettings.getTeamLetter(stats.richter.y)).getBytes());
                            }
                        }
                    }
                    catch(Exception e){}
                }
                bs.write(("\n").getBytes());
                idx += 1;
            }







            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Content-Type", "text/csv");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }
}
