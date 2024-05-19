import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Field;
import Model.MechanicCharacter;
import Model.Pipe;
import Model.Pump;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MechanicTest {

    private MechanicCharacter mechanic;
    private Field mockField;
    private Pump mockPump;
    private Pipe mockPipe;

    @BeforeEach
    public void setUp() {
        mockField = mock(Field.class);
        mockPump = mock(Pump.class);
        mockPipe = mock(Pipe.class);
        mechanic = new MechanicCharacter("TestMechanic", mockField);
    }

    @Test
    public void testConstructorWithName() {
        MechanicCharacter mc = new MechanicCharacter("MechanicName");
        assertEquals("MechanicName", mc.getName());
        assertNull(mc.getField());
        assertNull(mc.getPickedPump());
        assertNull(mc.getPickedPipe());
    }

    @Test
    public void testConstructorWithNameAndField() {
        assertEquals("TestMechanic", mechanic.getName());
        assertEquals(mockField, mechanic.getField());
        assertNull(mechanic.getPickedPump());
        assertNull(mechanic.getPickedPipe());
    }

    @Test
    public void testConstructorWithAllParameters() {
        MechanicCharacter mc = new MechanicCharacter("MechanicName", mockField, mockPipe, mockPump);
        assertEquals("MechanicName", mc.getName());
        assertEquals(mockField, mc.getField());
        assertEquals(mockPipe, mc.getPickedPipe());
        assertEquals(mockPump, mc.getPickedPump());
    }

    @Test
    public void testFix() {
        when(mockField.fix()).thenReturn(true);
        assertTrue(mechanic.fix());
        verify(mockField).fix();
    }

    @Test
    public void testPickupPumpSuccessful() {
        when(mockField.pickupPump(mechanic)).thenAnswer(invocation -> {
            mechanic.recievePump(mockPump);
            return true;
        });
        assertTrue(mechanic.pickupPump());
        assertEquals(mockPump, mechanic.getPickedPump());
    }

    @Test
    public void testPickupPumpFailure() {
        when(mockField.pickupPump(mechanic)).thenReturn(false);
        assertFalse(mechanic.pickupPump());
        assertNull(mechanic.getPickedPump());
    }

    @Test
    public void testPlacePumpToPipeSuccessful() {
        mechanic.recievePump(mockPump);
        when(mockField.placePump(mechanic)).thenReturn(true);
        assertTrue(mechanic.placePumpToPipe());
        assertNull(mechanic.getPickedPump());
    }

    @Test
    public void testPlacePumpToPipeFailure() {
        mechanic.recievePump(mockPump);
        when(mockField.placePump(mechanic)).thenReturn(false);
        assertFalse(mechanic.placePumpToPipe());
        assertEquals(mockPump, mechanic.getPickedPump());
    }

    @Test
    public void testGivePumpToPipe() {
        mechanic.recievePump(mockPump);
        assertEquals(mockPump, mechanic.givePumpToPipe());
    }

    @Test
    public void testPickupNewPipe_mcFailure() {
        when(mockField.pickupNewPipe(mechanic)).thenReturn(false);
        assertFalse(mechanic.pickupNewPipe_mc());
        assertNull(mechanic.getPickedPipe());
    }

    @Test
    public void testRecievePump() {
        assertTrue(mechanic.recievePump(mockPump));
        assertEquals(mockPump, mechanic.getPickedPump());
    }

    @Test
    public void testRecievePumpWhenAlreadyHasPump() {
        mechanic.recievePump(mockPump);
        Pump anotherPump = mock(Pump.class);
        assertFalse(mechanic.recievePump(anotherPump));
        assertEquals(mockPump, mechanic.getPickedPump());
    }

    @Test
    public void testHashCode() {
        MechanicCharacter mc1 = new MechanicCharacter("MechanicName", mockField, mockPipe, mockPump);
        MechanicCharacter mc2 = new MechanicCharacter("MechanicName", mockField, mockPipe, mockPump);
        assertEquals(mc1.hashCode(), mc2.hashCode());
    }

    @Test
    public void testToString() {
        MechanicCharacter mc = new MechanicCharacter("MechanicName", mockField, mockPipe, mockPump);
        String expectedString = "MechanicCharacter MechanicName " + mockField.getName() + " " + mockPipe.getName() + "," + mockPump.getName();
        assertEquals(expectedString, mc.toString());
    }
}
