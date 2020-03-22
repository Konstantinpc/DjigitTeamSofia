package com.example.djigitteamsofia;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.example.djigitteamsofia", appContext.getPackageName());
    }

   /* @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(email.validate("name@email.com"));
    }
    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(email.validate("name@abv.bg"));
    }
    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(email.validate("name@email"));
    }
    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(email.validate("name@email..com"));
    }
    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(email.validate("@email.com"));
    }
    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(email.validate(""));
    }
    @Test
    public void emailValidator_NullEmail_ReturnsFalse() {
        assertFalse(email.validate(null));
    }*/
}