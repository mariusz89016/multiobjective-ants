package pl.agh.edu.intobl.ants.helpers;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class ParetoSetImplTest {
    @Test
    public void shouldContainsAllAddedElements() throws Exception {
        //given
        ParetoSet<Integer> set = new ParetoSetImpl();
        final IntegerElement first = new IntegerElement(1, 2, 0);
        final IntegerElement second = new IntegerElement(2, 1, 0);

        set.addParetoElementToSet(first);

        //when
        boolean result = set.addParetoElementToSet(second);

        //then
        Set<ParetoElement<Integer>> set1 = new HashSet<>();
        set1.add(first);
        set1.add(second);

        assertTrue(result);
        assertEquals(set.getNonDominatedSet(), set1);
    }

    @Test
    public void addingDominatedElement() throws Exception {
        //given
        ParetoSet<Integer> set = new ParetoSetImpl();
        final IntegerElement first = new IntegerElement(1, 2, 0);
        final IntegerElement second = new IntegerElement(2, 3, 0);

        set.addParetoElementToSet(first);

        //when
        boolean result = set.addParetoElementToSet(second);

        //then
        Set<ParetoElement<Integer>> set1 = new HashSet<>();
        set1.add(first);

        assertFalse(result);
        assertEquals(set.getNonDominatedSet(), set1);
    }

    @Test
    public void addingDominatingElement() throws Exception {
        //given
        ParetoSet<Integer> set = new ParetoSetImpl();
        final IntegerElement first = new IntegerElement(1, 2, 0);
        final IntegerElement second = new IntegerElement(3, 2, 0);
        final IntegerElement third = new IntegerElement(2, 3, 0);

        set.addParetoElementToSet(second);
        set.addParetoElementToSet(third);

        //when
        boolean result = set.addParetoElementToSet(first);

        //then
        Set<ParetoElement<Integer>> set1 = new HashSet<>();
        set1.add(first);

        assertTrue(result);
        assertEquals(set.getNonDominatedSet(), set1);
    }
}