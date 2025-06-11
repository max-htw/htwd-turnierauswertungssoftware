import java.io.IOException;
import java.util.Date;
import java.util.TimerTask;

public class archivateTimerTask extends TimerTask {
    @Override
    public void run() {
        if (DataBaseQueries.currentTurnierChanged){
            try {
                DataBaseQueries.archiviateCurrentTurnierToFile();
            } catch (IOException e) {
                System.out.println("archivateTimerTask, IOException: " + e.getMessage());
            }
            DataBaseQueries.currentTurnierChanged = false;
        }
    }
}
