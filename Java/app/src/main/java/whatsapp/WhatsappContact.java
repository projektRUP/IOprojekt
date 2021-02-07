package whatsapp;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import exceptions.AppDoesntHaveNecessaryPermissionsException;
import exceptions.PassedArgumentIsNullException;
import phone.ImageRetriever;

import static predefined.Values.MAX_NUMBER_OF_STARS;
import static predefined.Values.VALUE_FOR_EMPTY_RATING;
import static predefined.Values.VALUE_FOR_NEXT_STAR;

public class WhatsappContact {
    private final String primaryKey;
    private String videoCallId;
    private String voipCallId;
    private String displayName;
    private String phoneNumber;
    private RatingContainer userRatings;
    private Bitmap contactPhoto;

    public WhatsappContact(AppCompatActivity context, String primaryKey, String displayName, String phoneNumber) {
        this.primaryKey = primaryKey;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;

        userRatings = new RatingContainer(primaryKey);

        try {
            contactPhoto = ImageRetriever.getContactPhoto(context, phoneNumber);
        } catch (AppDoesntHaveNecessaryPermissionsException e) {
            e.printStackTrace();
        }
    }

    public WhatsappContact(String primaryKey, String displayName, String phoneNumber) {
        this.primaryKey = primaryKey;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;

        userRatings = new RatingContainer(primaryKey);
    }

    /**
     * Adds a new rating to the float[] ratings array existing in RatingContainer class.
     * Once the array is full and a new value is added the oldest value is overwritten.
     * The newest value has the highest index among all added values.
     * If array isn't full then:
     * 1: element is added at the first place where the value doesn't equal predefined.Values.VALUE_FOR_EMPTY_RATING. (so the place is "empty")
     * Once the array is full and new value is added:
     * 1: all elements except the first one (index 0) have their index decremented.
     * 2: new element is added at the end. (index userRatings.size() - 1)
     * @param rating the added new rating value
     */
    public void addANewRating(float rating) {
        userRatings.addRating(rating);
    }

    /**
     * Looks for a RatingContainer instance from an ArrayList and assigns it to userRatings
     * The method looks for RatingContainer instance with primaryKey matching the WhatsappContact's primaryKey.
     * If such RatingContainer instance is not found, then userRatings isn't overwritten.
     * @param ratings ArrayList of ratings from which the method will try to find and assign RatingContainer instances.
     * @throws PassedArgumentIsNullException if passed argument ratings is null an exception will be thrown.
     */
    public void findAndSetRatingsFromAList(ArrayList<RatingContainer> ratings) throws PassedArgumentIsNullException{
        if(ratings == null)
            throw new PassedArgumentIsNullException();

        int l = 0;

        while(l < ratings.size() && !ratings.get(l).getPrimaryKey().equals(this.primaryKey))
            l ++;

        if(l < ratings.size())
            this.userRatings = ratings.get(l);
    }

    /**
     * Returns number of stars depending on:
     * 1. average of userRatings
     * 2. Predefined.MAX_NUMBER_OF_STARS (maximal value returned)
     * 3. Predefined.VALUE_FOR_NEXT_STAR
     * Returns -1 if average value is out of range.
     * Stars are returned based on such algorithm:
     * 1. i=1
     * 2. check if (i-1)*VALUE_FOR_NEXT_STAR <= value <= i*VALUE_FOR_NEXT_STAR -
     * if true: return i | otherwise: go to step 3.
     * 3. check if i<MAX_NUMBER_OF_STARS+1 -
     * if true: return -1 (invalid value) | if false: go to step 2.
     * @return positive value on success, -1 if value is not in good range.
     */
    public int getStars() {
        float average = userRatings.getAvarage();

        if(average != VALUE_FOR_EMPTY_RATING)
            for(int i = 1; i <= MAX_NUMBER_OF_STARS; i ++)
                if(average >= (i - 1) * VALUE_FOR_NEXT_STAR && average <= i * VALUE_FOR_NEXT_STAR)
                    return i;

        return -1;
    }

    /**
     * Returns contacts' display name concatenated with a new line character and the contacts' phone number.
     * @return displayName(new line)phoneNumber
     */
    public String displayShort() {
        return displayName + "\n" + phoneNumber;
    };

    // getters/setters
    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getVideoCallId() {
        return videoCallId;
    }

    public String getVoipCallId() {
        return voipCallId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public RatingContainer getUserRatings() {
        return userRatings;
    }

    public Bitmap getContactPhoto() {
        return contactPhoto;
    }

    public void setVideoCallId(String videoCallId) {
        this.videoCallId = videoCallId;
    }

    public void setVoipCallId(String voipCallId) {
        this.voipCallId = voipCallId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserRatings(RatingContainer userCallRatings) {
        this.userRatings = userCallRatings;
    }

    public void setContactPhoto(Bitmap contactPhoto) {
        this.contactPhoto = contactPhoto;
    }

    // overrides
    @Override
    public String toString() {
        return displayName+ "\n(video: " + videoCallId + ", voip: " + voipCallId + ")\n" + phoneNumber + "\n" + primaryKey;
    }
}
