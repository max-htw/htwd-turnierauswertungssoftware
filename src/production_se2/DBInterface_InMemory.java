import java.util.ArrayList;
import java.util.TreeMap;

public class DBInterface_InMemory extends DBInterfaceBase{

  @Override
  TreeMap<Integer, String> getGruppen() {
    return null;
  }

  @Override
  TreeMap<Integer, String> getTeamsByGroup(int groupID) {
    return null;
  }

  @Override
  ArrayList<TurnierMatch> getMatchesByGroup(int groupID) {
    return null;
  }
}
