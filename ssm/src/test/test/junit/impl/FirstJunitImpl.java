package test.junit.impl;

import org.junit.*;
import test.junit.BaseJunitTest;

import java.io.IOException;

public class FirstJunitImpl extends BaseJunitTest {

    static int i = 0;
    public FirstJunitImpl() {
        super();
    }

    @Before
    public void test() {
        System.out.println("before test");
    }

    @Test(timeout = 4000, expected = IOException.class)
    public void AT1() throws Exception{
        //Thread.sleep(3000);
        System.out.println("test " + ++i);
        throw new Exception();
    }

    @Test
    public void AT2() {
        System.out.println("test2 " + ++i);
    }

    @After
    public void name() {
        System.out.println("After test");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }
}
