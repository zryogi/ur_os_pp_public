/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.*;

/**
 * Priority Queue Scheduler Implementation
 * Based on Operating System Concepts by Silberschatz, Galvin, and Gagne
 *
 * This implements a multilevel priority queue where:
 * - Lower priority numbers indicate higher priority processes
 * - Higher priority processes can preempt lower priority ones
 * - Each priority level uses Round Robin scheduling with different time quanta
 */
public class PriorityQueue extends Scheduler {

    int currentScheduler;

    private final ArrayList<Scheduler> schedulers;

    PriorityQueue(OS os){
        super(os);
        currentScheduler = -1;
        schedulers = new ArrayList<>();
    }

    PriorityQueue(OS os, Scheduler... s){ //Received multiple schedulers for different priority levels
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if(s.length > 0)
            currentScheduler = 0;
    }

    @Override
    public void addProcess(Process p){
        int targetQueue = p.getPriority();
        p.setCurrentScheduler(targetQueue);
        schedulers.get(targetQueue).addProcess(p);
    }

    void defineCurrentScheduler(){
        currentScheduler = -1;

        for (int i = 0; i < schedulers.size(); i++) {
            if(!schedulers.get(i).isEmpty()){
                currentScheduler = i;
                break;
            }
        }
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        defineCurrentScheduler();

        if(cpuEmpty && currentScheduler == -1) {
            return;
        }

        if(cpuEmpty) {
            schedulers.get(currentScheduler).getNext(true);
            return;
        }

        int cpuSchedulerIndex = os.getProcessInCPU().currentScheduler;
        Scheduler cpuScheduler = schedulers.get(cpuSchedulerIndex);

        if (currentScheduler > cpuSchedulerIndex || currentScheduler == -1) {
            cpuScheduler.setStopAtQuantumExceeded(false);
        } else {
            cpuScheduler.setStopAtQuantumExceeded(true);
        }

        if (!cpuScheduler.isQuantumExceeded()) {
            cpuScheduler.getNext(false);
            if(os.isCPUEmpty()) {
                if(currentScheduler != -1) {
                    schedulers.get(currentScheduler).getNext(true);
                }
            }
        } else {
            if(currentScheduler != -1) {
                schedulers.get(currentScheduler).getNext(true);
            } 
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {
    }

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
    }
}

