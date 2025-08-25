/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ur_os;

/**
 *
 * @author prestamour
 */
public class SJF_NP extends Scheduler{
    SJF_NP(OS os){
        super(os);
    }
    
    @Override 
    public void getNext(boolean cpuEmpty) { // este elige quién va a la CPU si está libre
        if (cpuEmpty && !os.readyQueue.isEmpty()) { // si la CPU está vacía y hay procesos esperando
            Process shortestJob = null; // guardo acá el proceso más cortito
            int shortestTime = Integer.MAX_VALUE; // arranco con un valor gigante para comparar
            for (Process p : os.readyQueue) { // recorro toda la cola de listos
                int nextBurst = p.getNextCpuBurst(); // pregunto cuánto CPU necesita este proceso
                if (nextBurst < shortestTime) { // si este necesita menos que el más corto hasta ahora
                    shortestTime = nextBurst; // actualizo el tiempo más corto
                    shortestJob = p; // y me guardo este proceso
                }
            }
            if (shortestJob != null) { // si encontré alguno
                os.readyQueue.remove(shortestJob); // lo saco de la cola
                os.cpu.assignProcess(shortestJob); // y lo mando a la CPU
            }
        }
    }
    
    @Override
    public void newProcess(boolean cpuEmpty) { // cuando entra un proceso nuevo
        if (cpuEmpty) { // si la CPU está libre
            getNext(true); // mando a correr el más corto
        } // si no, queda esperando (porque no-preemptive)
    }
    
    @Override
    public void IOReturningProcess(boolean cpuEmpty) { // cuando vuelve uno de I/O
        if (cpuEmpty) { // si la CPU está libre
            getNext(true); // otra vez, elijo el más corto
        } // si no, también espera (porque no-preemptive)
    }


}
