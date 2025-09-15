import org.junit.jupiter.api.Test;
import ur_os.SchedulerType;
import ur_os.SystemOS;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MultiFeedbackQueueTest {

    // Multi-Feedback Queue (MFQ) Tests
    
    @Test
    public void mfqSimplerTest() {
        SystemOS sos = new SystemOS(SchedulerType.MFQ, 2);
        sos.run();

        assertEquals("0 0 1 1 2 2 3 3 0 0 1 1 2 2 0 0 1 1 2 2 0 1 1 1 2 2 2 3 3 3 2 2 2 3 3 3 3 3 3 3 ",
                sos.getGanttChart());

        assertEquals(1.0, sos.calcCPUUtilization());
        assertEquals(0.1, sos.calcThroughput());
        assertEquals(22.25, sos.calcTurnaroundTime());
        assertEquals(8.5, sos.calcAvgWaitingTime());
        assertEquals(6.75, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(8.75, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(5.0, sos.calcResponseTime());
    }

    @Test
    public void mfqSimpler2Test() {
        SystemOS sos = new SystemOS(SchedulerType.MFQ, 3);
        sos.run();

        assertEquals("0 0 1 1 2 2 3 3 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 0 1 1 0 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 2 2 2 2 2 2 3 3 3 3 3 3 2 2 2 2 3 3 3 -1 -1 2 2 2 2 2 2 3 3 3 3 3 3 2 2 2 2 2 2 3 3 3 3 3 3 3 3 3 3 3 ",
                sos.getGanttChart());

        assertEquals(0.972972972972973, sos.calcCPUUtilization());
        assertEquals(0.027027027027027028, sos.calcThroughput());
        assertEquals(98.5, sos.calcTurnaroundTime());
        assertEquals(64.75, sos.calcAvgWaitingTime());
        assertEquals(9.5, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(10.5, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(18.75, sos.calcResponseTime());
    }
}
