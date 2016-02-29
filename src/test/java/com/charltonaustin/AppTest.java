package com.charltonaustin;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.google.common.collect.Sets;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

        assertTrue(semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testSameIsTheSame() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        
        assertTrue(semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testMoveAndAddToBodyShouldBeDifferent() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void goodbye(){}\n" +
                "public void hello(){int b = 1;}}\n";

        assertFalse(semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingMethodIsDifferent() {
        String from = "public class Something {public void hello(){}\n" +
                "public void goodbye(){}}\n";
        String to = "public class Something {public void goodbye(){}\n" +
                "public void hello(){}" +
                "public void different(){}}\n";

        assertFalse(semanticDiff.itIsTheSame(from, to));
    }

    @Test
    public void testAddingMethodParameterIsDifferent() {
        String from = "public class Something {public void hello(int a){}}\n";
        String to = "public class Something {public void hello(){}}\n";

        assertFalse(semanticDiff.itIsTheSame(from, to));
    }

}
