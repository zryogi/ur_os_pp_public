import org.junit.jupiter.api.Test;
import ur_os.SchedulerType;
import ur_os.SystemOS;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PriorityQueueTest {

    // Priority Queue Tests - Multiple queues with RoundRobin(os,9), RoundRobin(os,6), RoundRobin(os,3), RoundRobin(os,2,false)
    
    @Test
    public void priorityQueueSimplerTest() {
        SystemOS sos = new SystemOS(SchedulerType.PRIORITY, 2);
        sos.run();

        assertEquals("0 0 0 0 0 1 1 1 2 2 2 0 0 0 1 1 1 1 1 1 2 2 2 2 3 3 3 3 2 2 2 2 2 3 3 3 3 3 3 3 ",
                sos.getGanttChart());

        assertEquals(1.0, sos.calcCPUUtilization());
        assertEquals(0.1, sos.calcThroughput());
        assertEquals(22.75, sos.calcTurnaroundTime());
        assertEquals(9.0, sos.calcAvgWaitingTime());
        //assertEquals(2.25, sos.calcAvgContextSwitches()); // Solo Gantt
        //assertEquals(3.75, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(5.25, sos.calcResponseTime());
    }

    @Test
    public void priorityQueueSimpler2Test() {
        SystemOS sos = new SystemOS(SchedulerType.PRIORITY, 3);
        sos.run();

        assertEquals("0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 0 0 0 0 0 0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 2 2 2 2 2 2 3 3 3 3 3 3 2 2 2 2 3 3 3 -1 -1 2 2 2 2 2 2 3 3 3 3 3 3 2 2 2 2 2 2 3 3 3 3 3 3 3 3 3 3 3 ",
                sos.getGanttChart());

        assertEquals(0.9818181818181818, sos.calcCPUUtilization());
        assertEquals(0.03636363636363636, sos.calcThroughput());
        assertEquals(73.0, sos.calcTurnaroundTime());
        assertEquals(39.25, sos.calcAvgWaitingTime());
        //assertEquals(3.25, sos.calcAvgContextSwitches()); // Solo Gantt
        //assertEquals(4.25, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(29.75, sos.calcResponseTime());
    }
}
