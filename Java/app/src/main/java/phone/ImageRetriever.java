package phone;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

import java.io.FileNotFoundException;
import java.io.IOException;

import exceptions.AppDoesntHaveNecessaryPermissionsException;

public class ImageRetriever {

    // methods

    /**
     * Retrieves an image associated with a given phone number.
     * If there is no image associated with a contact a default image will be returned.
     * The default image must be stored in the "drawable" directory.
     * The default image must be named: "default_contact_photo".
     * @param context context of the application
     * @param phoneNumber phone number of the contact from which you want to retrieve an image.
     * @return an image associated with a contact in a Bitmap form
     * @throws AppDoesntHaveNecessaryPermissionsException if the application doesn't have "READ_CONTACTS" permission an exception will be thrown
     */
    public static Bitmap getContactPhoto(AppCompatActivity context, String phoneNumber) throws AppDoesntHaveNecessaryPermissionsException {
        if(!Permissions.doesApplicationHaveReadContactsPermission(context))
            throw new AppDoesntHaveNecessaryPermissionsException();

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.az_s);

        Uri contactUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        
        Cursor cursor = context.getContentResolver().query(contactUri, null, null, null, null);
        
        while (cursor.moveToNext()) {
            String imageUri = cursor.getString(cursor.getColumnIndex(
                    ContactsContract.CommonDataKinds.Phone.PHOTO_URI));

            if (imageUri != null) {
                try {
                    bitmap = MediaStore.Images.Media
                            .getBitmap(context.getContentResolver(),
                                    Uri.parse(imageUri));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        cursor.close();

        return   bitmap;
    }
}
