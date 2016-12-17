package pl.agh.edu.intobl.ants.helpers;

import org.junit.Test;

import static org.junit.Assert.*;

public class IntegerElementTest {
    @Test
    public void should() throws Exception {
        final IntegerElement first = new IntegerElement(1, 2, 0);
        final IntegerElement second = new IntegerElement(2, 1, 0);

        assertFalse(first.isDominating(second));
        assertFalse(second.isDominating(first));

        assertFalse(first.isDominatedBy(second));
        assertFalse(second.isDominatedBy(first));
    }

    @Test
    public void name() throws Exception {
        final IntegerElement first = new IntegerElement(1, 2, 0);
        final IntegerElement second = new IntegerElement(2, 3, 0);

        assertTrue(first.isDominating(second));
        assertTrue(second.isDominatedBy(first));
    }
}