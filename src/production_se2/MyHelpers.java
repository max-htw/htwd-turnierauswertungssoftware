import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyHelpers {

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

    public static IntPair GroupIDandTeamIDfromBezeichnung(String groupBezeichnung){
      int groupID = -1;
      int teamID = -1;
      try {
        groupID = Integer.parseInt("" + groupBezeichnung.charAt(0));
        teamID = groupBezeichnung.charAt(1) - 96;
        if(groupID <=0 || groupID >9 || teamID <= 0 || teamID > 9){
          groupID = -1;
          teamID = -1;
        }
      }
      catch (Exception e){

      }
      return new IntPair(groupID, teamID);
    }

    public static String TeamBezeichnung(Integer groupID, Integer teamID){
      if(groupID <=0 || groupID >9 || teamID <= 0 || teamID > 9){
        return "0o";
      }
      return groupID.toString() + (char)(teamID + 96);
    }

    public static class IntPair implements Comparable<IntPair>, Serializable{
        int x;
        int y;

        IntPair(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof IntPair)) return false;
            return ((IntPair) obj).x == this.x && ((IntPair) obj).y == this.y;
        }

        @Override
        public int hashCode() {
            return this.x * 10 + this.y;
        }

        public static IntPair hashDecode(int hash){
            return  new IntPair(hash / 10, hash % 10);
        }

        @Override
        public int compareTo(IntPair o) {
            if(this.x > o.x)
                return  1;
            else if(this.x < o.x)
                return -1;
            else if(this.y > o.y)
                return 1;
            else if(this.y < o.y)
                return  -1;
            else
                return 0;
        }
    }


/*
    public static class SpielStats{
        public boolean isHinspiel = false;
        public int groupid = -1;
        public int team1 = -1;
        public int team2 = -1;
        public int team1Punkte = 0;
        public int team2Punkte = 0;
        public int feldID = -1;
        public IntPair richter = new IntPair(-1,-1);
    }

 */

}
