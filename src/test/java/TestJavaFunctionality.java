import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

/**
 * Created by Erwin on 03/03/2017.
 */
public class TestJavaFunctionality {

    @Test
    public void multiplyTest(){
        //Assemble
        int a = 3;
        int b = 4;

        //Act
        int c = a * b;

        //Assert
        assertEquals(12,c);
    }

    @Test
    public void upperCaseTest(){
        //Assemble
        String name = "erwin";

        //Act
        String allUpper = name.toUpperCase();

        //Assert
        assertNotNull(allUpper);
        assertTrue(allUpper.length() == 5);
        assertEquals(allUpper,"ERWIN");

    }
}
