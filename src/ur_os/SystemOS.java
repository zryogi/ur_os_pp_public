/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ur_os;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import static ur_os.SchedulerType.FAIR;
import static ur_os.SchedulerType.PRIORITY;
import static ur_os.SchedulerType.RR;
import ur_os.CreateFile;
import static ur_os.CreateFile.returnFile;

/**
 *
 * @author super
 */
public final class SystemOS implements Runnable {
    private static int clock = 0;
    private static final int MAX_SIM_CYCLES = 1000;
    private static final int MAX_SIM_PROC_CREATION_TIME = 50;
    private static final double PROB_PROC_CREATION = 0.1;
    private static Random r = new Random(1235);
    private int CPUUtilization = 0; // Tiempo que la CPU estuvo ocupada
    private int cpucount = 0; // Contador de ciclos de CPU utilizados
    private ArrayList<Process> processes; // Lista de procesos
    private ArrayList<Integer> execution; // Lista de ejecución de procesos
    private OS os;
    private CPU cpu;
    private IOQueue ioq;
    private ReadyQueue readyqueue;
    private SchedulerType selectedScheduler;

    private int simulation;
    boolean menu = false;

    public SystemOS() {
        this(false);
    }

    public static int getClock() {
        return clock;
    }

    public SystemOS(boolean menu) {
        cpu = new CPU();
        ioq = new IOQueue();
        if (menu) {
            menu();
        } else {
            selectedScheduler = SchedulerType.RR;
            simulation = 3; // Simpler2
        }

        readyqueue = new ReadyQueue(os, selectedScheduler);
        os = new OS(this, cpu, ioq, selectedScheduler);
        cpu.setOS(os);
        ioq.setOS(os);
        execution = new ArrayList<>();
        processes = new ArrayList<>();

        simulation(simulation);
    }

    public SystemOS(SchedulerType selectedScheduler, int simulation) {
        cpu = new CPU();
        ioq = new IOQueue();
        os = new OS(this, cpu, ioq, selectedScheduler);
        readyqueue = new ReadyQueue(os, selectedScheduler);
        cpu.setOS(os);
        ioq.setOS(os);
        execution = new ArrayList<>();
        processes = new ArrayList<>();
        this.simulation = simulation;

        simulation(this.simulation);
    }

    public int getTime() {
        return clock;
    }

    public ArrayList<Process> getProcessAtI(int i) {
        ArrayList<Process> ps = new ArrayList<>();

        for (Process process : processes) {
            if (process.getTime_init() == i) {
                ps.add(process);
            }
        }

        return ps;
    }

    public void initSimulationQueue() {
        double tp;
        Process p;
        for (int i = 0; i < MAX_SIM_PROC_CREATION_TIME; i++) {
            tp = r.nextDouble();
            if (PROB_PROC_CREATION >= tp) {
                p = new Process();
                p.setTime_init(clock);
                processes.add(p);
            }
            clock++;
        }
        clock = 0;
    }

    public void simulation(int simulation) {
        switch (simulation) {
            case 1 -> {
                initSimulationQueue();
                showProcesses();
            }
            case 2 -> {
                initSimulationQueueSimpler();
                showProcesses();
            }
            case 3 -> {
                initSimulationQueueSimpler2();
                showProcesses();
            }
        }
    }

    public void initSimulationQueueSimple() {
        Process p;
        int cont = 0;
        for (int i = 0; i < MAX_SIM_PROC_CREATION_TIME; i++) {
            if (i % 4 == 0) {
                p = new Process(cont++, -1);
                p.setTime_init(clock);
                processes.add(p);
            }
            clock++;
        }
        clock = 0;
    }

    public void initSimulationQueueSimpler() {

        Process p = new Process(false);
        p.setPriority(0);
        ProcessBurst temp = new ProcessBurst(5, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(4, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(3, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(0);
        processes.add(p);

        p = new Process(false);
        p.setPriority(1);
        temp = new ProcessBurst(3, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(5, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(6, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(2);
        processes.add(p);

        p = new Process(false);
        p.setPriority(2);
        temp = new ProcessBurst(7, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(3, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(5, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(6);
        processes.add(p);

        p = new Process(false);
        p.setPriority(3);
        temp = new ProcessBurst(4, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(3, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(7, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(8);
        processes.add(p);

        clock = 0;
    }

    public void initSimulationQueueSimpler2() {
        Process p = new Process(false);
        p.setPriority(0);
        ProcessBurst temp = new ProcessBurst(15, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(12, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(21, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(0);
        p.setPid(0);
        processes.add(p);

        p = new Process(false);
        p.setPriority(0);
        temp = new ProcessBurst(8, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(4, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(16, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(2);
        p.setPid(1);
        processes.add(p);

        p = new Process(false);
        p.setPriority(1);
        temp = new ProcessBurst(10, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(5, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(12, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(6);
        p.setPid(2);
        processes.add(p);

        p = new Process(false);
        p.setPriority(1);
        temp = new ProcessBurst(9, ProcessBurstType.CPU);
        p.addBurst(temp);
        temp = new ProcessBurst(6, ProcessBurstType.IO);
        p.addBurst(temp);
        temp = new ProcessBurst(17, ProcessBurstType.CPU);
        p.addBurst(temp);
        p.setTime_init(8);
        p.setPid(3);
        processes.add(p);

        clock = 0;
    }

    public boolean isSimulationFinished() {
        boolean finished = true;

        for (Process p : processes) {
            finished = finished && p.isFinished();
        }

        return finished;
    }

    public void menu() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Menu =====");
        System.out.println("1. FCFS");
        System.out.println("2. SJF No Preventivo");
        System.out.println("3. SJF Preventivo");
        System.out.println("4. Round Robin");
        System.out.println("5. Por Prioridades");
        System.out.println("6. MFQ");
        System.out.println("7. CFS (Completely Fair Scheduler)");
        System.out.print("Select the algorithm (1-7): ");

        int schedulerChoice = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer

        selectedScheduler = switch (schedulerChoice) {
            case 1 -> SchedulerType.FCFS;
            case 2 -> SchedulerType.SJF_NP;
            case 3 -> SchedulerType.SJF_P;
            case 4 -> SchedulerType.RR;
            case 5 -> SchedulerType.PRIORITY;
            case 6 -> SchedulerType.MFQ;
            case 7 -> SchedulerType.CFS;
            default -> throw new IllegalArgumentException("Scheduler inválido");
        };

        readyqueue = new ReadyQueue(os, selectedScheduler);

        System.out.println("\n===== Simulation Menu =====");
        System.out.println("1. initSimulationQueue");
        System.out.println("2. initSimulationQueueSimpler");
        System.out.println("3. initSimulationQueueSimpler2");
        System.out.print("Select the simulation (1-3): ");
        simulation = scanner.nextInt();
        scanner.nextLine(); // Limpiar buffer
    }

    @Override
    public void run() {
        double tp;
        ArrayList<Process> ps;

        System.out.println("***SIMULATION START***");

        int i = 0;
        Process temp_exec;
        int tempID;
        while (!isSimulationFinished() && i < MAX_SIM_CYCLES) {// MAX_SIM_CYCLES is the maximum simulation time, to
                                                               // avoid infinite loops
            System.out.println("***Clock: " + i + "***");
            System.out.println(cpu);
            System.out.println(ioq);

            if (i == 32) { // This allows you to stop the simulation at a given time (i) if a debugging
                           // stoping point is located here
                int a = 0;
            }

            // Crear procesos, si aplica en el ciclo actual
            ps = getProcessAtI(i);
            for (Process p : ps) {
                os.create_process(p);
            } // If the scheduler is preemtive, this action will trigger the extraction from
              // the CPU, is any process is there.

            // Actualizar el OS, quien va actualizar el Scheduler
            os.update();
            // os.update() prepares the system for execution. It runs at the beginning of
            // the cycle.

            clock++;

            temp_exec = cpu.getProcess();
            if (temp_exec == null) {
                tempID = -1;
                cpucount++;
            } else {
                tempID = temp_exec.getPid();
                if (temp_exec.getFirstExecutionTime() == -1) {
                    temp_exec.setFirstExecutionTime(clock);
                }
            }
            execution.add(tempID);

            // Actualizar la CPU
            cpu.update();

            ///Actualizar la IO
            ioq.update();

            // Las actualizaciones de CPU y IO pueden generar interrupciones que actualizan
            // a cola de listos, cuando salen los procesos
            System.out.println("After the cycle: ");
            System.out.println(cpu);
            System.out.println(ioq);
            i++;
        }

        // os.showProcesses(); // Si quieres imprimir procesos, hazlo también aquí

        StringBuilder sb = new StringBuilder();
        sb.append("***SIMULATION FINISHES***\n");
        sb.append("***Process Execution***\n");
        sb.append(this.getGanttChart()).append("\n");
        sb.append("***Performance Indicators***").append("\n");
        sb.append("Total execution cycles: ").append(clock).append("\n");
        sb.append("CPU Utilization: ").append(this.calcCPUUtilization()).append("\n");
        sb.append("Throughput: ").append(this.calcThroughput()).append("\n");
        sb.append("Average Turnaround Time: ").append(this.calcTurnaroundTime()).append("\n");
        sb.append("Average Waiting Time: ").append(this.calcAvgWaitingTime()).append("\n");
        sb.append("Average Context Switches (solo Gantt): ").append(this.calcAvgContextSwitches()).append("\n");
        sb.append("Average Context Switches (completo): ").append(this.calcAvgContextSwitches2()).append("\n");
        sb.append("Average Response Time: ").append(this.calcResponseTime()).append("\n");
        sb.append("***SIMULATION FINISHES***\n");

        System.out.print(sb);

        try (PrintWriter pw = new PrintWriter(new FileWriter(returnFile()))) {
            pw.print(sb);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("*********Comparation:************************");
        compareFiles("FCFS.txt", "FCFS.txt");
    }

    public String getGanttChart() {
        StringBuilder sb = new StringBuilder();

        for (Integer e : this.execution) {
            sb.append(e).append(" ");
        }
        return sb.toString();
    }

    public void showProcesses() {
        System.out.println("Process list:");
        StringBuilder sb = new StringBuilder();

        for (Process process : processes) {
            sb.append(process).append("\n");
        }

        System.out.println(sb);
    }

    public int processTurnAroundTime(Process p) {
        if (p.getTime_finished() == -1)
            return 0;
        return p.getTime_finished() - p.getTime_init();
    }

    public int getFinishedProcesses() {
        if (processes.isEmpty())
            return 0;

        int finished = 0;

        for (Process p : processes) {
            if (p.getTime_finished() != -1)
                finished++;
        }

        return finished;
    }

    public double calcCPUUtilization() {
        if (clock == 0)
            return 0;

        return (double) (clock - cpucount) / clock;
    }

    public double calcTurnaroundTime() {
        if (processes.isEmpty())
            return 0;

        int totalTurnaroundTime = 0;

        int finishedProcesses = getFinishedProcesses();
        if (finishedProcesses == 0)
            return 0;

        for (Process p : processes) {
            if (p.getTime_finished() != -1) {
                totalTurnaroundTime += processTurnAroundTime(p);
            }
        }

        return (double) totalTurnaroundTime / finishedProcesses;
    }

    public double calcThroughput() {
        if (processes.isEmpty() || clock == 0)
            return 0;

        return (double) processes.size() / clock;
    }

    public double calcAvgWaitingTime() {
        if (processes.isEmpty())
            return 0;

        int totalWaitingTime = 0;

        int finishedProcesses = getFinishedProcesses();
        if (finishedProcesses == 0)
            return 0;

        for (Process p : processes) {
            if (p.getTime_finished() != -1) {
                int totalExecTime = processTurnAroundTime(p);
                int idealExecTime = p.getTotalExecutionTime();
                int waitingTime = totalExecTime - idealExecTime;

                totalWaitingTime += waitingTime;
            }
        }

        return (double) totalWaitingTime / finishedProcesses;
    }

    // Everytime a process is taken out from memory, when an interruption occurs
    public double calcAvgContextSwitches2() {
        if (processes.isEmpty())
            return 0;

        int finishedProcesses = getFinishedProcesses();
        if (finishedProcesses == 0)
            return 0;

        int totalContextSwitches = 0;

        for (Process p : processes) {
            if (p.getTime_finished() != -1) {
                totalContextSwitches += p.getContextSwitches();
            }
        }

        return (double) totalContextSwitches / finishedProcesses;
    }

    // Just context switches based on the execution timeline
    public double calcAvgContextSwitches() {
        if (execution.isEmpty() || processes.isEmpty())
            return 0;

        int finishedProcesses = getFinishedProcesses();
        if (finishedProcesses == 0)
            return 0;

        int contextSwitches = 0;
        int lastPid = Integer.MIN_VALUE;

        for (Integer currentPid : execution) {
            if (currentPid != lastPid && currentPid >= 0) {
                contextSwitches++;
            }
            lastPid = currentPid;
        }

        return (double) contextSwitches / finishedProcesses;
    }

    public double calcResponseTime() {
        if (processes.isEmpty())
            return 0;

        int finishedProcesses = getFinishedProcesses();
        if (finishedProcesses == 0)
            return 0;

        int totalResponseTime = 0;

        for (Process p : processes) {
            if (p.getTime_finished() != -1) {
                // Adjust for the timing offset where firstExecutionTime is set 1 cycle late
                int adjustedResponseTime = p.getResponseTime() - 1;
                totalResponseTime += Math.max(0, adjustedResponseTime); // Ensure non-negative
            }
        }

        return (double) totalResponseTime / finishedProcesses;
    }

    public void compareFiles(String filePath1, String filePath2) {
        try (BufferedReader reader1 = new BufferedReader(new FileReader(filePath1));
                BufferedReader reader2 = new BufferedReader(new FileReader(filePath2))) {

            String line1, line2;
            int lineNum = 1;
            boolean differenceFound = false;

            while ((line1 = reader1.readLine()) != null | (line2 = reader2.readLine()) != null) {
                if (line1 == null || line2 == null || !line1.equals(line2)) {
                    System.out.println("Difference at line " + lineNum + ":");
                    System.out.println("File1: " + (line1 != null ? line1 : "[EOF]"));
                    System.out.println("File2: " + (line2 != null ? line2 : "[EOF]"));
                    differenceFound = true;
                }
                lineNum++;
            }
            if (!differenceFound) {
                System.out.println("The files are identical.");
            }

        } catch (IOException e) {
            System.err.println("Error comparing files: " + e.getMessage());
        }
    }
}