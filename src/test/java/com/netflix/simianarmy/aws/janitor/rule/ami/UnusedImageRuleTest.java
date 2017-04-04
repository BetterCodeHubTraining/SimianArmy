package com.netflix.simianarmy.aws.janitor.rule.ami;

import org.testng.annotations.Test;

import static org.testng.AssertJUnit.*;

/**
 * Created by Erwin on 12/01/2017.
 */
public class UnusedImageRuleTest {

    @Test
    public void testUnusedImageRule() {
        //Assemble
        String name = "test";
        String expected = "TEST";

        //Act
        String result = name.toUpperCase();

        //Assert
        assertNotNull(name);
        assertEquals(expected,result);
    }

    @Test
    public void testMath(){
        int a = 4;
        int b = 3;
        int expected = 12;

        assertEquals(expected, a*b);
    }
}
