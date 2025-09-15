/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

import java.util.ArrayList;
import java.util.Arrays;

public class MFQ extends Scheduler {

    private int currentScheduler;
    private ArrayList<Scheduler> schedulers;

    MFQ(OS os) {
        super(os);
        schedulers = new ArrayList<>();
        currentScheduler = -1;
    }

    MFQ(OS os, Scheduler... s) {
        this(os);
        schedulers.addAll(Arrays.asList(s));
        if (!schedulers.isEmpty()) {
            currentScheduler = 0;
        }
    }

    @Override
    public void addProcess(Process p) {
        if (p.getState() == ProcessState.CPU) {
            int nivel = Math.min(p.getCurrentScheduler() + 1, schedulers.size() - 1);
            p.setCurrentScheduler(nivel);
        }
        else if (p.getState() == ProcessState.IO) {
            p.setCurrentScheduler(0);
        }
        schedulers.get(p.getCurrentScheduler()).addProcess(p);
    }

    private void defineCurrentScheduler() {
        for (int i = 0; i < schedulers.size(); i++) {
            if (!schedulers.get(i).isEmpty()) {
                currentScheduler = i;
                return;
            }
        }
        currentScheduler = -1; // No hay procesos
    }

    @Override
    public void getNext(boolean cpuEmpty) {
        if (cpuEmpty) {
            defineCurrentScheduler();
            if (currentScheduler != -1) {
                schedulers.get(currentScheduler).getNext(true);
            }
        } else {
            schedulers.get(currentScheduler).getNext(false);
            if (os.isCPUEmpty()) { // si se liberó la CPU
                defineCurrentScheduler();
                if (currentScheduler != -1) {
                    schedulers.get(currentScheduler).getNext(true);
                }
            }
        }
    }

    @Override
    public void newProcess(boolean cpuEmpty) {}

    @Override
    public void IOReturningProcess(boolean cpuEmpty) {}
}
