package ur_os;

public class CFS extends Scheduler {

    // Red-Black tree to store processes ordered by vruntime
    private RedBlackTree<Process> runQueue;

    // Track number of processes for efficiency
    private int processCount;

    /**
     * Constructor with OS parameter
     */
    public CFS(OS os) {
        super(os);
        this.runQueue = new RedBlackTree<>();
        this.processCount = 0;
    }

    /**
     * Get the next process to run - O(log n) operation using Red-Black Tree
     */
    @Override
    public void getNext(boolean cpuEmpty) {
        if (processCount == 0 || !cpuEmpty) {
            return;
        }

        Node<Process> minNode = runQueue.minimum(runQueue.root);
        if (minNode != runQueue.NIL) {
            Process nextProcess = minNode.getData();

            runQueue.delete(nextProcess);
            processCount--;

            // Send to CPU
            os.interrupt(InterruptType.SCHEDULER_RQ_TO_CPU, nextProcess);
        }
    }

    /**
     * Handle new process arrival - preempt only if new process has lower vruntime
     */
    @Override
    public void newProcess(boolean cpuEmpty) {
        // This method is called before the process is added to our queue
        // The actual preemption decision is made in addProcess() where we have access
        // to the new process
        // We'll implement smart preemption there
    }

    /**
     * Handle process returning from I/O - preempt only if returning process has
     * lower vruntime
     */
    @Override
    public void IOReturningProcess(boolean cpuEmpty) {
        // This method is called before the process is added to our queue
        // The actual preemption decision is made in addProcess() where we have access
        // to the returning process
        // We'll implement smart preemption there
    }

    /**
     * Add process to Red-Black Tree with smart vruntime-based preemption
     */
    @Override
    public void addProcess(Process p) {
        // Initialize vruntime for new processes
        if (p.getVruntime() == 0) {
            p.setVruntime(p.getPriority());
        }

        p.calculateWeight();

        boolean shouldPreempt = false;

        if (!os.isCPUEmpty()) {
            Process currentProcess = os.getProcessInCPU();
            // Preempt only if incoming process has lower vruntime
            if (p.getVruntime() < currentProcess.getVruntime()) {
                shouldPreempt = true;
                System.out.println("CFS: Preempting process " + currentProcess.getPid() +
                        " (vruntime=" + currentProcess.getVruntime() +
                        ") for process " + p.getPid() +
                        " (vruntime=" + p.getVruntime() + ")");
            } else {
                System.out.println("CFS: No preemption - process " + currentProcess.getPid() +
                        " (vruntime=" + currentProcess.getVruntime() +
                        ") continues over process " + p.getPid() +
                        " (vruntime=" + p.getVruntime() + ")");
            }
        }

        runQueue.insert(p);
        processCount++;

        System.out.println("CFS: Adding process " + p.getPid() +
                " (priority=" + p.getPriority() +
                ", vruntime=" + p.getVruntime() +
                ") to Red-Black Tree");

        if (shouldPreempt) {
            os.interrupt(InterruptType.SCHEDULER_CPU_TO_RQ, null);
        }

        p.setState(ProcessState.READY);
    }

    /**
     * Update vruntime and trigger scheduling
     */
    @Override
    public void update() {
        int referenceWeight = 10;
        Process runningProcess = os.getProcessInCPU();
        if (runningProcess != null) {
            // Update vruntime
            long vruntimeIncrease = Math.max(1,  (SystemOS.getClock() - runningProcess.time_init) * referenceWeight / runningProcess.getWeight());
            runningProcess.addVruntime(vruntimeIncrease);

            System.out.println("CFS: Process " + runningProcess.getPid() +
                    " vruntime updated to " + runningProcess.getVruntime());
        }

        getNext(os.isCPUEmpty());
    }

    /**
     * Check if scheduler is empty
     */
    public boolean isEmpty() {
        return processCount == 0;
    }

    /**
     * Get number of ready processes
     */
    public int size() {
        return processCount;
    }

    @Override
    public String toString() {
        return "CFS (Red-Black Tree): " + processCount + " processes ready";
    }
}