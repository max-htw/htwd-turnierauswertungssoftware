import javax.xml.crypto.Data;

import java.util.ArrayList;
import java.util.HashMap;

public class WebserverMock implements  RoleWithTaskBase_Renderer.ActionStringGenerator{

  private DBInterfaceBase _dbBackend;

  public StringsRole requestRole;
  public StringsRole.RoleTask requestTask;
  public int requestGroupID;
  public int requestTeamNr;
  public HashMap<StringsActions, String> requestParams;

  public WebserverMock(DBInterfaceBase dbBackend){
    _dbBackend = dbBackend;
    requestParams = new HashMap<>();
  }

  public RoleWithTaskBase_Data serveUserRequest(){

    HashMap<String, String> params = new HashMap<>();
    for(StringsActions sa: requestParams.keySet()){
      params.put(sa.name().toLowerCase(), requestParams.get(sa));
    }
    RequestVerteiler v = new RequestVerteiler(requestRole, requestTask, requestGroupID, requestTeamNr, params, _dbBackend);
    RoleWithTaskBase_Data ausgabe = v.getResponseData();
    return ausgabe;
  }


  @Override
  public String generateActionString(RoleWithTaskBase_Renderer.ActionForRoleAndTask action) {
    return "Mock_Webserver_generiert_keine_ActionStrings";
  }

}
