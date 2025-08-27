/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class SJF_NP extends Scheduler {

    SJF_NP(OS os){
        super(os);
    }
    
    @Override
    public void getNext(boolean cpuEmpty) {
        if (!processes.isEmpty() && cpuEmpty) {
            Process shortestProcess = SJFUtils.getShortestJobProcess(processes, this);

            if (shortestProcess != null) {
                processes.remove(shortestProcess);
                os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, shortestProcess);
            }
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) {
        // Non-preemptive
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        // Non-preemptive
    }
}
