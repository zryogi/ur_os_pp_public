/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class RoundRobin extends Scheduler {

    int q;
    int cont;
    boolean multiQueue;
    
    RoundRobin(OS os){
        super(os);
        this.q = 5;
        this.cont = 0;
    }
    
    RoundRobin(OS os, int q){
        this(os);
        this.q = q;
    }

    RoundRobin(OS os, int q, boolean multiQueue){
        this(os);
        this.q = q;
        this.multiQueue = multiQueue;
    }
   
    @Override
    public void getNext(boolean cpuEmpty) {
        setQuantumExceeded(false);
        if (!cpuEmpty) { //runs when CPU is NOT empty
            cont++; //adds time unit for the time spent in the current process
            if (cont > q) { //checks if time spent surpasses the time quantum chosen
                setQuantumExceeded(true);
                if(isStopAtQuantumExceeded()) {
                    cont = 1;
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
                    return;
                }
                if (!processes.isEmpty()) {  //runs when processes is NOT empty
                    os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, processes.poll());
                } else {
                    // No other processes waiting, but quantum expired
                    os.getProcessInCPU().increaseContextSwitches();
                }
                cont = 1; //restarts the count of time spent
                setQuantumExceeded(false);
            }
        } else if (!processes.isEmpty()) { //runs when processes is NOT empty
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, processes.poll());
            cont = 1; //restarts the count of time spent
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {} //Non-preemtive in this event

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {} //Non-preemtive in this event
    
}

