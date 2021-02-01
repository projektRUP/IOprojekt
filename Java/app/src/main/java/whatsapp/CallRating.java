package whatsapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import exceptions.PassedArgumentIsNullException;

import static android.content.Context.MODE_PRIVATE;
import static predefined.Strings.NAME_OF_A_FILE_WITH_CALL_RATINGS;
import static predefined.Values.NUMBER_OF_CALLS_TO_REMEMBER;
import static predefined.Values.VALUE_FOR_EMPTY_RATING;

public class CallRating implements java.io.Serializable {
    private String primaryKey;
    private float[] callRatings = new float[NUMBER_OF_CALLS_TO_REMEMBER];

    public CallRating() {
        Arrays.fill(callRatings, VALUE_FOR_EMPTY_RATING);
    }

    public CallRating(String primaryKey) {
        Arrays.fill(callRatings, VALUE_FOR_EMPTY_RATING);
        this.primaryKey = primaryKey;
    }

    public CallRating(String primaryKey, float initialValue) {
        Arrays.fill(callRatings, initialValue);
        this.primaryKey = primaryKey;
    }

    public CallRating(float initialValue) {
        Arrays.fill(callRatings, initialValue);
    }

    // methods

    /**
     * Returns the average of the ratings stored in this classes' instance.
     * @return average of all elements which value doesn't equal "predefined.Values.VALUE_FOR_EMPTY_RATING"
     */
    public float getAvarage() {
        int l = 0;
        float result = 0;

        while(l < callRatings.length && callRatings[l] != VALUE_FOR_EMPTY_RATING) {
            result += callRatings[l];
            l ++;
        }

        if(l != 0) {
            result /= l;
            return result;
        }

        return VALUE_FOR_EMPTY_RATING;
    }

    /**
     * Saves given ClassRating instances to a file specified in "predefined.Strings.NAME_OF_A_FILE_WITH_CALL_RATINGS".
     * Overwrites ClassRating instances based on "primaryKey".
     * Doesn't delete previously created ClassRating instances, that do not have primaryKey's identical to the ones of "saved".
     * @param context context of the application
     * @param ratingsToSave CallRating calss instances' which you want to save
     * @throws PassedArgumentIsNullException if passed argument "ratingsToSave" is null an exception will be thrown.
     */
    public static void saveRatingsToAFile(AppCompatActivity context, ArrayList<CallRating> ratingsToSave) throws PassedArgumentIsNullException {
        if(ratingsToSave == null)
            throw new PassedArgumentIsNullException();

        String storageFilePath = context.getFilesDir() + "/" + NAME_OF_A_FILE_WITH_CALL_RATINGS;

        HashSet<String> primaryKeysOfRatingsToSave = new HashSet<>();

        File storageFile = new File(storageFilePath);
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<CallRating> ratingsAlreadyExisting = getRatingsFromAFile(context);

        for(CallRating rating : ratingsToSave)
            primaryKeysOfRatingsToSave.add(rating.primaryKey);

        if(ratingsAlreadyExisting != null)
            for(CallRating rating : ratingsAlreadyExisting)
                if(!primaryKeysOfRatingsToSave.contains(rating.primaryKey))
                    ratingsToSave.add(rating);

        try {
            fileOutputStream = context.openFileOutput(NAME_OF_A_FILE_WITH_CALL_RATINGS, MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(ratingsToSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileOutputStream != null)
                    fileOutputStream.close();

                if(objectOutputStream != null)
                    objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves a single instance of ClassRating to a file specified in "predefined.Strings.NAME_OF_A_FILE_WITH_CALL_RATINGS".
     * Overwrites ClassRating instance based on primaryKey.
     * Doesn't delete previously created ClassRating instances that do not have the same primaryKey as the "ratingToSave".
     * @param context context of the application
     * @param ratingToSave CallRating instance to save
     * @throws PassedArgumentIsNullException if passed argument "ratingsToSave" is null an exception will be thrown.
     */
    public static void saveASingleRatingToAFile(AppCompatActivity context, CallRating ratingToSave) throws PassedArgumentIsNullException {
        if(ratingToSave == null)
            throw new PassedArgumentIsNullException();

        String storageFilePath = context.getFilesDir() + "/" + NAME_OF_A_FILE_WITH_CALL_RATINGS;
        ArrayList<CallRating> ratingsToSave = new ArrayList<>();

        File storageFile = new File(storageFilePath);
        FileOutputStream fileOutputStream = null;
        ObjectOutputStream objectOutputStream = null;

        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ArrayList<CallRating> ratingsAlreadyExisting = getRatingsFromAFile(context);;

        if(ratingsAlreadyExisting != null)
            for(CallRating rating : ratingsAlreadyExisting)
                if(!rating.primaryKey.equals(ratingToSave.primaryKey))
                    ratingsToSave.add(rating);

        ratingsToSave.add(ratingToSave);

        try {
            fileOutputStream = context.openFileOutput(NAME_OF_A_FILE_WITH_CALL_RATINGS, MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(ratingsToSave);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileOutputStream != null)
                    fileOutputStream.close();

                if(objectOutputStream != null)
                    objectOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns an ArrayList of CallRating instances from a file.
     * The file is specified in "predefined.Strings.NAME_OF_A_FILE_WITH_CALL_RATINGS"
     * @param context context of the application
     * @return ArrayList of CallRating instances saved in a file.
     */
    public static ArrayList<CallRating> getRatingsFromAFile(AppCompatActivity context) {
        String storageFilePath = context.getFilesDir() + "/" + NAME_OF_A_FILE_WITH_CALL_RATINGS;

        File storageFile = new File(storageFilePath);
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        ArrayList<CallRating> ratings = null;

        try {
            if(storageFile.createNewFile())
                Log.i(null, "A file was created:\n" + storageFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileInputStream = context.openFileInput(NAME_OF_A_FILE_WITH_CALL_RATINGS);
            objectInputStream = new ObjectInputStream(fileInputStream);
            ratings = (ArrayList<CallRating>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null)
                    fileInputStream.close();

                if(objectInputStream != null)
                    objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return ratings;
    }

    /**
     * Returns a single CallRating instance from a file.
     * The file is specified in "predefined.Strings.NAME_OF_A_FILE_WITH_CALL_RATINGS"
     * @param context context of the application
     * @param primaryKey key of the retrieved CallRating instance
     * @return CallRating instance with given "primaryKey", or null on failure.
     */
    public static CallRating getASingleRatingFromAFile(AppCompatActivity context, String primaryKey) {
        String storageFilePath = context.getFilesDir() + "/" + NAME_OF_A_FILE_WITH_CALL_RATINGS;
        CallRating outputRating = null;

        File storageFile = new File(storageFilePath);
        FileInputStream fileInputStream = null;
        ObjectInputStream objectInputStream = null;

        ArrayList<CallRating> ratings = null;

        try {
            if(storageFile.createNewFile())
                Log.i(null, "A file was created:\n" + storageFile.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fileInputStream = context.openFileInput(NAME_OF_A_FILE_WITH_CALL_RATINGS);
            objectInputStream = new ObjectInputStream(fileInputStream);
            ratings = (ArrayList<CallRating>) objectInputStream.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fileInputStream != null)
                    fileInputStream.close();

                if(objectInputStream != null)
                    objectInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for(CallRating rating : ratings)
            if(rating.getPrimaryKey().equals(primaryKey))
                outputRating = rating;

        return outputRating;
    }

    /**
     * Adds a new rating to the "float[] callRatings" array.
     * Once the array is full and a new value is added the oldest value is overwritten.
     * The newest value has the highest index among all added values.
     * If array isn't full then:
     * 1: element is added at the first place where the value doesn't equal "predefined.Values.VALUE_FOR_EMPTY_RATING". (so the place is "empty")
     * Once the array is full and new value is added:
     * 1: all elements except the first one (index 0) have their index decremented.
     * 2: new element is added at the end. (index callRating.size() - 1)
     * @param rating the added new rating value
     */
    public void addRating(float rating) {
        int l = 0;

        while (l < callRatings.length && callRatings[l] != -1 * Float.MAX_VALUE)
            l++;

        if (l < callRatings.length) {
            this.callRatings[l] = rating;
        }

        if (l == callRatings.length) {
            System.arraycopy(callRatings, 1, callRatings, 0, callRatings.length - 1);

            this.callRatings[callRatings.length - 1] = rating;
        }
    }

    // getters/setters
    public float[] getCallRatings() {
        return callRatings;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setCallRatings(float[] callRatings) {
        this.callRatings = callRatings;
    }

    // overrides
    @NonNull
    @Override
    public String toString() {
        String output = primaryKey + ": {";

        for (float callRating : callRatings)
            output += callRating + ", ";

        // deleting last ", " part
        output = output.substring(0, output.length() - 2);
        output += "}";

        return output;
    }
}
