import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleKeine_TaskSelectRole extends RoleWithTaskBase{

    RoleKeine_TaskSelectRole(ByteArrayOutputStream s, Map<String, String> q) {
        super(s,q);
    }

    @Override
    public void handleRequest() throws IOException {
        bs.write(("<!DOCTYPE html>\n<html>\n<head>").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head><body>\n").getBytes());
        bs.write(("<h1>Bitte waehlen Sie Ihr <span class=\"" + StringsCSS.htwdOrange + "\">Team</span> aus:</h1>\n").getBytes());

        bs.write(("<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" +
                StringsRole.Admin.name().toLowerCase() + "/\">Organisator</a><br><br>\n").getBytes());

        for(int gr = 1; gr <= AppSettings.get_anzGroups(); gr++){
            if(AppSettings.get_anzGroups() > 1){
                bs.write(("<br>Leistungsgruppe " + gr + "<br><br>\n").getBytes());
            }
            for(int tm = 1; tm <= AppSettings.get_anzTeams(gr); tm++){
                bs.write(("<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" +
                        gr + Character.toLowerCase(AppSettings.getTeamLetter(tm)) + "/\">" +
                        AppSettings.getTeamLetter(tm) + "</a>\n").getBytes());
            }
            bs.write(("<br>\n").getBytes());
        }

        bs.write(("</body></html>").getBytes());
    }
}
