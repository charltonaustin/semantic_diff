package com.charltonaustin;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppTest {

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
    public void testMoveAndAddToBodyShouldBeDifferent() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void goodbye(){}\n" +
                "public void hello(){int b = 1;}}\n";

        assertEquals(HowDifferent.NOT_IDENTICAL, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingMethodIsDifferent() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void goodbye(){}\n" +
                "public void hello(){}" +
                "public void different(){}}\n";

        assertEquals(HowDifferent.NOT_IDENTICAL, semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingMethodParameterIsDifferent() {
        String from = "public class Something {public void hello(int a){}}\n";
        String to = "public class Something {public void hello(){}}\n";

        assertEquals(HowDifferent.NOT_IDENTICAL, semanticDiff.itIsTheSame(from, to));
    }

}
