package ur_os;

import java.util.LinkedList;

public class SJFUtils {
    public static Process getShortestJobProcess(LinkedList<Process> processes, Scheduler scheduler){
        Process shortestProcess = null;
        if (!processes.isEmpty()) {
            int shortestTime = Integer.MAX_VALUE;

            for (Process p : processes) {
                int remainingTime = p.getRemainingTimeInCurrentBurst();

                if (remainingTime < shortestTime) {
                    shortestTime = remainingTime;
                    shortestProcess = p;
                } else if (remainingTime == shortestTime && shortestProcess != null) {
                    shortestProcess = scheduler.tieBreaker(p, shortestProcess);
                }
            }
        }
        return shortestProcess;
    }
}
