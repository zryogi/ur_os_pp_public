import org.junit.jupiter.api.Test;
import ur_os.SchedulerType;
import ur_os.SystemOS;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoundRobinTest {

    // Round Robin (RR-4) Tests

    @Test
    public void roundRobinSimplerTest() {
        SystemOS sos = new SystemOS(SchedulerType.RR, 2);
        sos.run();

        assertEquals("0 0 0 0 1 1 1 0 2 2 2 2 3 3 3 3 1 1 1 1 0 0 0 2 2 2 3 3 3 3 1 1 2 2 2 2 3 3 3 2 ",
                sos.getGanttChart());

        assertEquals(1.0, sos.calcCPUUtilization());
        assertEquals(0.1, sos.calcThroughput());
        assertEquals(29.5, sos.calcTurnaroundTime());
        assertEquals(15.75, sos.calcAvgWaitingTime());
        assertEquals(3.25, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(3.25, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(2.0, sos.calcResponseTime());
    }

    @Test
    public void roundRobinSimpler2Test() {
        SystemOS sos = new SystemOS(SchedulerType.RR, 3);
        sos.run();

        assertEquals("0 0 0 0 1 1 1 1 0 0 0 0 2 2 2 2 3 3 3 3 1 1 1 1 0 0 0 0 2 2 2 2 3 3 3 3 1 1 1 1 0 0 0 2 2 3 1 1 1 1 2 2 2 2 1 1 1 1 3 3 3 3 2 2 2 2 0 0 0 0 1 1 1 1 3 3 3 3 2 2 2 2 0 0 0 0 3 3 3 3 0 0 0 0 3 3 3 3 0 0 0 0 3 0 0 0 0 0 ",
                sos.getGanttChart());

        assertEquals(1.0, sos.calcCPUUtilization());
        assertEquals(0.037037037037037035, sos.calcThroughput());
        assertEquals(87.75, sos.calcTurnaroundTime());
        assertEquals(54.0, sos.calcAvgWaitingTime());
        assertEquals(7.25, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(7.5, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(4.0, sos.calcResponseTime());
    }
}
