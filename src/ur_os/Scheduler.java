/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author prestamour
 */
public abstract class Scheduler {
    
    OS os;
    protected LinkedList<Process> processes;
    public Scheduler() {}
    private int totalContextSwitches;

    public boolean isQuantumExceeded() {
        return quantumExceeded;
    }

    public void setQuantumExceeded(boolean quantumExceeded) {
        this.quantumExceeded = quantumExceeded;
    }

    private boolean quantumExceeded;

    public boolean isStopAtQuantumExceeded() {
        return stopAtQuantumExceeded;
    }

    public void setStopAtQuantumExceeded(boolean stopAtQuantumExceeded) {
        this.stopAtQuantumExceeded = stopAtQuantumExceeded;
    }

    private boolean stopAtQuantumExceeded;

    
    public Scheduler(OS os){
        this.os = os;
        processes = new LinkedList<Process>();
        totalContextSwitches = 0;
        quantumExceeded = false;
        stopAtQuantumExceeded = false;
    }

    public void getNext(){
        getNext(false);
    }
    
    public abstract void getNext(boolean cpuEmpty);
    public abstract void newProcess(boolean cpuEmpty); //Implement for Preemtive schedulers
    public abstract void IOReturningProcess(boolean cpuEmpty); //Implement for Preemtive schedulers
    
    public void addContextSwitch(){
        this.totalContextSwitches++;
    }
    
    public int getTotalContextSwitches(){
        return this.totalContextSwitches;
    }
    
    public boolean isEmpty(){
        return this.processes.isEmpty();
    }
    
    public Process tieBreaker(Process p1, Process p2){
        Process p = null;
        switch(os.SCHEDULER_TIEBREAKER_TYPE){

            case LARGEST_PID:
                if(p1.getPid() > p2.getPid()){
                    p = p1;
                }else{
                    p = p2;
                }
            break;
            
            case SMALLEST_PID:
                if(p1.getPid() < p2.getPid()){
                    p = p1;
                }else{
                    p = p2;
                }
            break;
            
            case PRIORITY:
                if(p1.getPriority() < p2.getPriority()){
                    p = p1;
                }else{
                    p = p2;
                }
            break;
            
            case PRIORITY_LARGEST_PID:
                if(p1.getPriority() < p2.getPriority()){
                    p = p1;
                }else if(p1.getPriority() > p2.getPriority()){
                    p = p2;
                }else{
                    if(p1.getPid() > p2.getPid()){
                        p = p1;
                    }else{
                        p = p2;
                    }
                }
            break;
            
            case PRIORITY_SMALLEST_PID:
                if(p1.getPriority() < p2.getPriority()){
                    p = p1;
                }else if(p1.getPriority() > p2.getPriority()){
                    p = p2;
                }else{
                    if(p1.getPid() < p2.getPid()){
                        p = p1;
                    }else{
                        p = p2;
                    }
                }
            break;
            
            default:
                if(p1.getPid() > p2.getPid()){
                    p = p1;
                }else{
                    p = p2;
                }
            break;
            
        
        }
        
        return p;
    }
    
    public void addProcess(Process p){
        //The order in which process will be addedd to the ReadyQueue, based on the execution of the
        //simulator, is: New processes, Interrupted processed from CPU, Incomming process from I/O
        if(p.getState() == ProcessState.NEW){
            newProcess(os.isCPUEmpty()); //If the process is NEW, let the scheduler defines what it will do to update the queue to select the next
        }else if(p.getState() == ProcessState.IO){
            IOReturningProcess(os.isCPUEmpty()); //If the process is returning from IO, let the scheduler defines what it will do to update the queue to select the next
        }
        
        p.setState(ProcessState.READY); //If the process comes from the CPU, just add it to the list
        processes.add(p);
    }
    
    public void update(){
        getNext(os.isCPUEmpty());
    }
    
    public Process removeProcess(Process p){
        Process temp = p;
        processes.remove(p);
        return temp;
    }
    
    public String toString(){
        StringBuffer sb = new StringBuffer();
        
        for (Process process : processes) {
            sb.append(process);
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
     
}
