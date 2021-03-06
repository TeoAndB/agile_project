package rcm.unitTest;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

import rcm.model.ContainerStatus;

public class ContainerStatusTest {

    @Test
    public void testHashCode() {
        LocalDateTime timestamp = LocalDateTime.of(2020, 3, 13, 4, 20);
        ContainerStatus s1 = new ContainerStatus(timestamp, 13.5, 75.0, 1.01, "New York");
        ContainerStatus s2 = new ContainerStatus(timestamp, 13.5, 75.0, 1.01, "New York");

        ContainerStatus sn1 = new ContainerStatus(null, 13.5, 75.0, 1.01, "New York");
        ContainerStatus sn2 = new ContainerStatus(null, 13.5, 75.0, 1.01, "New York");

        assertEquals(s1.hashCode(), s2.hashCode());
        assertEquals(sn1.hashCode(), sn2.hashCode());
        ContainerStatus sl = new ContainerStatus(timestamp, 13.0, 75.0, 1.01, null);
        assertNotEquals(0, sl.hashCode());
    }

    @Test
    public void testEquals() {
        LocalDateTime timestamp = LocalDateTime.of(2020, 3, 13, 4, 20);
        LocalDateTime timestamp2 = LocalDateTime.of(2020, 3, 12, 4, 20);

        ContainerStatus s = new ContainerStatus(timestamp, 13.5, 75.0, 1.01, "New York");
        assertTrue(s.equals(s));
        assertFalse(s.equals(null));
        assertFalse(s.equals(timestamp));
        assertFalse(s.equals(new ContainerStatus(timestamp, 14.0, 75.0, 1.01, "New York")));
        assertFalse(s.equals(new ContainerStatus(timestamp, 13.5, 80.0, 1.01, "New York")));
        assertFalse(s.equals(new ContainerStatus(timestamp, 13.5, 75.0, 1.05, "New York")));

        ContainerStatus sn = new ContainerStatus(null, 13.5, 75.0, 1.01, "New York");
        assertFalse(sn.equals(s));
        assertTrue(sn.equals(new ContainerStatus(null, 13.5, 75.0, 1.01, "New York")));
        ContainerStatus s2 = new ContainerStatus(timestamp2, 13.5, 75.0, 1.01, "New York");
        assertFalse(s.equals(s2));
        ContainerStatus sl1 = new ContainerStatus(timestamp, 14.0, 75.0, 1.01, null);
        assertFalse(sl1.equals(s));
        ContainerStatus sl2 = new ContainerStatus(timestamp, 13.5, 75.0, 1.01, "Copenhagen");
        assertFalse(s.equals(sl2));
        ContainerStatus sl3 = new ContainerStatus(timestamp, 14.0, 75.0, 1.01, null);
        assertTrue(sl1.equals(sl3));
    }
    
    @Test
    public void testGetters() {
        ContainerStatus cs = new ContainerStatus(LocalDateTime.of(2020, 3, 13, 4, 20), 35.0, 95.0, 1.01, "Copenhagen");
        assertTrue(cs.getTemperature() == 35.0);
        assertTrue(cs.getHumidity() == 95.0);
        assertTrue(cs.getPressure() == 1.01);
        assertTrue(cs.getLocation().equals("Copenhagen"));
    }
}
