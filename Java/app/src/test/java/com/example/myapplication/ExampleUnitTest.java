package com.example.myapplication;

import org.junit.Test;

import java.util.Arrays;

import whatsapp.CallRating;
import whatsapp.WhatsappAccesser;
import whatsapp.WhatsappContact;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static predefined.Values.MAX_NUMBER_OF_STARS;
import static predefined.Values.NUMBER_OF_CALLS_TO_REMEMBER;
import static predefined.Values.VALUE_FOR_EMPTY_RATING;
import static predefined.Values.VALUE_FOR_NEXT_STAR;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void checkCallRating_getCallRatings() {
        float[] expected = new float[NUMBER_OF_CALLS_TO_REMEMBER];
        float[] actual = null;
        CallRating tested = new CallRating();

        Arrays.fill(expected, VALUE_FOR_EMPTY_RATING);

        actual = tested.getCallRatings();

        assertArrayEquals(expected, actual, 0);
    }

    @Test
    public void checkCallRating_addRating() {
        CallRating tested = new CallRating();
        float[] expected = new float[NUMBER_OF_CALLS_TO_REMEMBER];
        float[] actual = null;

        tested.addRating(0.45f);

        Arrays.fill(expected, VALUE_FOR_EMPTY_RATING);

        expected[0] = 0.45f;

        actual = tested.getCallRatings();

        assertArrayEquals(expected, actual, 0);

        tested.addRating(0.013f);
        tested.addRating(0.5f);
        tested.addRating(123.456f);

        expected[1] = 0.013f;
        expected[2] = 0.5f;
        expected[3] = 123.456f;

        assertArrayEquals(expected, actual, 0);

        tested.addRating(1f);
        tested.addRating(2f);
        tested.addRating(3f);
        tested.addRating(4f);
        tested.addRating(5f);
        tested.addRating(6f);

        expected[4] = 1f;
        expected[5] = 2f;
        expected[6] = 3f;
        expected[7] = 4f;
        expected[8] = 5f;
        expected[9] = 6f;

        assertArrayEquals(expected, actual, 0);

        tested.addRating(7f);
        tested.addRating(8f);
        tested.addRating(9f);
        tested.addRating(10f);

        for(int i = 0; i<NUMBER_OF_CALLS_TO_REMEMBER; i ++)
            expected[i] = i + 1;

        assertArrayEquals(expected, actual, 0);

        tested.addRating(11f);

        for(int i = 0; i<NUMBER_OF_CALLS_TO_REMEMBER; i ++)
            expected[i] = i + 2;

        assertArrayEquals(expected, actual, 0);
    }

    @Test
    public void checkWhatsappContact_addANewRating() {
        WhatsappContact tested = new WhatsappContact("pk", "tested", "phoneNumber");
        WhatsappContact tested2 = new WhatsappContact("pk", "tested", "phoneNumber");
        int expected = -1;
        int actual;

        // negative value (improper)
        tested.addANewRating(-0.001f);
        actual = tested.getStars();
        assertEquals(expected, actual);

        // value still negative
        tested.addANewRating(0.f);
        actual = tested.getStars();
        assertEquals(expected, actual);

        // too big value
        tested.addANewRating(3 * MAX_NUMBER_OF_STARS * VALUE_FOR_NEXT_STAR + 1);
        actual = tested.getStars();
        assertEquals(expected, actual);

        // proper value
        tested.addANewRating(0.f);
        actual = tested.getStars();
        assert actual > 0;

        tested2.addANewRating(0.f);
        expected = 1;
        actual = tested2.getStars();
        assertEquals(expected, actual);

        tested2.addANewRating(2 * MAX_NUMBER_OF_STARS * VALUE_FOR_NEXT_STAR);
        expected = MAX_NUMBER_OF_STARS;
        actual = tested2.getStars();
        assertEquals(expected, actual);
    }
}