package com.charltonaustin;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SemanticDiffTest {

    private SemanticDiff semanticDiff;

    @Before
    public void setUp() throws Exception {
        semanticDiff = new SemanticDiff();
    }

    @Test
    public void testMethodMoveIsNotDiff() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void goodbye(){}\n" +
                "public void hello(){}}\n";

        assertEquals(HowDifferent.IDENTICAL, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testSameIsTheSame() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";

        assertEquals(HowDifferent.IDENTICAL, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingStatementIsAddition() {
        String from = "public class Something {public void hello(){}}\n";
        String to = "public class Something {public void hello(){int b = 1;}}\n";

        assertEquals(HowDifferent.ADDITION, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testRemovingStatementIsDeletion() {
        String from = "public class Something {public void hello(){int b = 1;}}\n";
        String to = "public class Something {public void hello(){}}\n";

        assertEquals(HowDifferent.DELETION, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingMethodIsAddition() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void goodbye(){}\n" +
                "public void hello(){}" +
                "public void different(){}}\n";

        assertEquals(HowDifferent.ADDITION, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testRemovingMethodIsDeletion() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void hello(){}}\n";

        assertEquals(HowDifferent.DELETION, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testRemovingParameterIsDeletion() {
        String from = "public class Something {public void hello(int a){}}\n";
        String to = "public class Something {public void hello(){}}\n";

        assertEquals(HowDifferent.DELETION, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingParameterIsAddition() {
        String from = "public class Something {public void hello(){}}\n";
        String to = "public class Something {public void hello(int a){}}\n";

        assertEquals(HowDifferent.ADDITION, semanticDiff.itIsTheSame(from, to));
    }

}
