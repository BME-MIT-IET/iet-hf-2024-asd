import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Model.Field;
import Model.Pipe;
import Model.SaboteurCharacter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SaboteurTest {

    private SaboteurCharacter saboteur;
    private Field mockField;
    private Pipe mockPipe;

    @BeforeEach
    public void setUp() {
        mockField = mock(Field.class);
        mockPipe = mock(Pipe.class);
        saboteur = new SaboteurCharacter("TestSaboteur", mockField);
    }

    @Test
    public void testConstructorWithName() {
        SaboteurCharacter sc = new SaboteurCharacter("SaboteurName");
        assertEquals("SaboteurName", sc.getName());
        assertNull(sc.getField());
        assertNull(sc.getPickedPipe());
    }

    @Test
    public void testConstructorWithNameAndField() {
        assertEquals("TestSaboteur", saboteur.getName());
        assertEquals(mockField, saboteur.getField());
        assertNull(saboteur.getPickedPipe());
    }

    @Test
    public void testConstructorWithAllParameters() {
        SaboteurCharacter sc = new SaboteurCharacter("SaboteurName", mockField, mockPipe);
        assertEquals("SaboteurName", sc.getName());
        assertEquals(mockField, sc.getField());
        assertEquals(mockPipe, sc.getPickedPipe());
    }

    @Test
    public void testMakeSlipperySuccessful() {
        when(mockField.setToSlippery()).thenReturn(true);
        assertTrue(saboteur.makeSlippery());
        verify(mockField).setToSlippery();
    }

    @Test
    public void testMakeSlipperyFailure() {
        when(mockField.setToSlippery()).thenReturn(false);
        assertFalse(saboteur.makeSlippery());
        verify(mockField).setToSlippery();
    }

    @Test
    public void testToString() {
        SaboteurCharacter sc = new SaboteurCharacter("SaboteurName", mockField, mockPipe);
        String expectedString = "SaboteurCharacter\n"
                + "\tName: " + "SaboteurName" + "\n"
                + "\tField: " + mockField.getName() + "\n"
                + "\tPipe in hand: "+ mockPipe.getName() + "\n";
        assertEquals(expectedString, sc.toString());
    }

    @Test
    public void testToStringWithoutFieldAndPipe() {
        SaboteurCharacter sc = new SaboteurCharacter("SaboteurName");
        String expectedString = "SaboteurCharacter\n"
                + "\tName: " + "SaboteurName" + "\n"
                + "\tField: " + "-" + "\n"
                + "\tPipe in hand: "+ "-"+ "\n";
        assertEquals(expectedString, sc.toString());
    }
}

