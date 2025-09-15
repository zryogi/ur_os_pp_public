import org.junit.jupiter.api.Test;
import ur_os.SchedulerType;
import ur_os.SystemOS;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShortestJobFirstTest {
    // SJF-P (Shortest Job First - Preemptive) Tests
    
    @Test
    public void shortestJobFirstPreemptiveSimplerTest() {
        SystemOS sos = new SystemOS(SchedulerType.SJF_P, 2);
        sos.run();

        assertEquals("0 0 1 1 1 0 0 0 3 3 3 3 0 0 0 1 1 1 1 1 1 3 3 3 3 3 3 3 2 2 2 2 2 2 2 -1 -1 -1 2 2 2 2 2 ",
                sos.getGanttChart());

        assertEquals(0.9302325581395349, sos.calcCPUUtilization());
        assertEquals(0.09302325581395349, sos.calcThroughput());
        assertEquals(22.75, sos.calcTurnaroundTime());
        assertEquals(9.0, sos.calcAvgWaitingTime());
        assertEquals(2.25, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(2.75, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(5.5, sos.calcResponseTime());
    }

    @Test
    public void shortestJobFirstPreemptiveSimpler2Test() {
        SystemOS sos = new SystemOS(SchedulerType.SJF_P, 3);
        sos.run();

        assertEquals("0 0 1 1 1 1 1 1 1 1 3 3 3 3 3 3 3 3 3 2 2 2 2 2 2 2 2 2 2 0 0 0 0 0 0 0 0 0 0 0 0 0 2 2 2 2 2 2 2 2 2 2 2 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ",
                sos.getGanttChart());

        assertEquals(1.0, sos.calcCPUUtilization());
        assertEquals(0.037037037037037035, sos.calcThroughput());
        assertEquals(75.75, sos.calcTurnaroundTime());
        assertEquals(42.0, sos.calcAvgWaitingTime());
        assertEquals(2.25, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(3.5, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(3.75, sos.calcResponseTime());
    }

    // SJF-NP (Shortest Job First - Non-Preemptive) Tests
    
    @Test
    public void shortestJobFirstNonPreemptiveSimplerTest() {
        SystemOS sos = new SystemOS(SchedulerType.SJF_NP, 2);
        sos.run();

        assertEquals("0 0 0 0 0 1 1 1 3 3 3 3 0 0 0 1 1 1 1 1 1 3 3 3 3 3 3 3 2 2 2 2 2 2 2 -1 -1 -1 2 2 2 2 2 ",
                sos.getGanttChart());

        assertEquals(0.9302325581395349, sos.calcCPUUtilization());
        assertEquals(0.09302325581395349, sos.calcThroughput());
        assertEquals(22.75, sos.calcTurnaroundTime());
        assertEquals(9.0, sos.calcAvgWaitingTime());
        assertEquals(2.0, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(2.0, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(6.25, sos.calcResponseTime());
    }

    @Test
    public void shortestJobFirstNonPreemptiveSimpler2Test() {
        SystemOS sos = new SystemOS(SchedulerType.SJF_NP, 3);
        sos.run();

        assertEquals("0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 1 1 1 1 1 1 1 1 3 3 3 3 3 3 3 3 3 2 2 2 2 2 2 2 2 2 2 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 1 2 2 2 2 2 2 2 2 2 2 2 2 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 3 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ",
                sos.getGanttChart());

        assertEquals(1.0, sos.calcCPUUtilization());
        assertEquals(0.037037037037037035, sos.calcThroughput());
        assertEquals(76.75, sos.calcTurnaroundTime());
        assertEquals(43.0, sos.calcAvgWaitingTime());
        assertEquals(2.0, sos.calcAvgContextSwitches()); // Solo Gantt
        assertEquals(2.0, sos.calcAvgContextSwitches2()); // Completo
        assertEquals(13.5, sos.calcResponseTime());
    }
}
