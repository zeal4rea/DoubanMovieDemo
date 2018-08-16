package com.zeal4rea.doubanmoviedemo;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void typeTest() {
        assertEquals(new TypeToken<List<String>>(){}.getType(), new TypeToken<List<String>>(){}.getType());
    }
}