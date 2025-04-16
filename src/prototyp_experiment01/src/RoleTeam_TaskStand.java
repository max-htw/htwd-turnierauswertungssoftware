import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleTeam_TaskStand extends RoleWithTaskBase{
    private int _groupID;
    private int _teamID;

    RoleTeam_TaskStand(ByteArrayOutputStream s, Map<String, String> q, int groupID, int teamID) {
        super(s, q);
        _groupID = groupID;
        _teamID = teamID;
    }

    @Override
    public void handleRequest() throws IOException {
        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td{text-align:center;vertical-align:middle;padding:5px;}\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        bs.write(("<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + teamNameForURL(_groupID, _teamID) + "/" +
                StringsRole.TeamTasks.Overview.name().toLowerCase() + "\">Home</a><br>\n").getBytes());

        bs.write(("<h1>Aktueller Stand, <span class=\"" + StringsCSS.htwdOrange + "\">Team</span> " + _groupID + AppSettings.getTeamLetter(_teamID) + "</h1>\n").getBytes());
        bs.write(("</body></html>").getBytes());
    }
}
