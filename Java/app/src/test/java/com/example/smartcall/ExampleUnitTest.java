package com.example.smartcall;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import exceptions.PassedArgumentIsNullException;
import whatsapp.RatingContainer;
import whatsapp.WhatsappContact;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    public void checkRatingContainer_getCallRatings() {
        float[] expected = new float[NUMBER_OF_CALLS_TO_REMEMBER];
        float[] actual = null;
        RatingContainer tested = new RatingContainer();

        Arrays.fill(expected, VALUE_FOR_EMPTY_RATING);

        actual = tested.getRatings();

        assertArrayEquals(expected, actual, 0);
    }

    @Test
    public void checkRatingContainer_addRating() {
        RatingContainer tested = new RatingContainer();
        float[] expected = new float[NUMBER_OF_CALLS_TO_REMEMBER];
        float[] actual = null;

        tested.addRating(0.45f);

        Arrays.fill(expected, VALUE_FOR_EMPTY_RATING);

        expected[0] = 0.45f;

        actual = tested.getRatings();

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
    public void checkRatingContainer_getAverage() {
        RatingContainer tested = new RatingContainer();
        float expected;

        tested.addRating(0.2f);
        expected = 0.2f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(0.f);
        expected = 0.1f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(1.f);
        expected = 0.4f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(0.8f);
        expected = 0.5f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(3.f);
        expected = 1.f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(7.0f);
        expected = 2.f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(2.0f);
        expected = 2.f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(10.f);
        expected = 3.f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(3.0f);
        expected = 3.f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(13.f);
        expected = 4.f;
        assertEquals(expected, tested.getAvarage(), 0.f);

        tested.addRating(0.2f);
        expected = 4.f;
        assertEquals(expected, tested.getAvarage(), 0.f);
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

    @Test
    public void checkWhatsappContact_findAndSetRatingsFromAList() {

        ArrayList<RatingContainer> ratingContainerList = new ArrayList<>();

        ratingContainerList.add(new RatingContainer("pk1"));
        ratingContainerList.add(new RatingContainer("pk2", 0.f));
        ratingContainerList.add(new RatingContainer("pk3", 0.001f));
        ratingContainerList.add(new RatingContainer("pk4", 1.2345f));
        ratingContainerList.add(new RatingContainer("pk5", -0.0033f));

        WhatsappContact tested1 = new WhatsappContact("pk1", "displayName", "phoneNumber");
        WhatsappContact tested2 = new WhatsappContact("pk2", "displayName", "phoneNumber");
        WhatsappContact tested3 = new WhatsappContact("pk3", "displayName", "phoneNumber");
        WhatsappContact tested4 = new WhatsappContact("pk4", "displayName", "phoneNumber");
        WhatsappContact tested5 = new WhatsappContact("pk5", "displayName", "phoneNumber");
        WhatsappContact tested6 = new WhatsappContact("pk12345", "displayName", "phoneNumber");

        try {
            tested1.findAndSetRatingsFromAList(ratingContainerList);
            tested2.findAndSetRatingsFromAList(ratingContainerList);
            tested3.findAndSetRatingsFromAList(ratingContainerList);
            tested4.findAndSetRatingsFromAList(ratingContainerList);
            tested5.findAndSetRatingsFromAList(ratingContainerList);
            tested6.findAndSetRatingsFromAList(ratingContainerList);
        } catch (PassedArgumentIsNullException e) {
            assertFalse(true);
        }

        assertEquals(VALUE_FOR_EMPTY_RATING, tested1.getUserRatings().getAvarage(), 0.f);
        assertEquals(0.f, tested2.getUserRatings().getAvarage(), 0.f);
        assertEquals(0.001f, tested3.getUserRatings().getAvarage(), 0.f);
        assertEquals(1.2345f, tested4.getUserRatings().getAvarage(), 0.f);
        assertEquals(-0.0033f, tested5.getUserRatings().getAvarage(), 0.f);
        assertEquals(VALUE_FOR_EMPTY_RATING, tested6.getUserRatings().getAvarage(), 0.f);
    }
}