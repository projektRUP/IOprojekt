package whatsapp;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;

import exceptions.AppDoesntHaveNecessaryPermissionsException;
import exceptions.NoWhatsappInstalledException;
import phone.Permissions;

public class WhatsappAccesser {

    // methods

    /**
     * Makes a video, or voip call using whatsapp.
     * @param context context of the application
     * @param actionId the id for performing an action - either videoCallId, or voipCallId from WhatsappContact class
     * @throws NoWhatsappInstalledException If whatsapp isn't installed an exception will be thrown.
     */
    public static void makeACall(AppCompatActivity context, String actionId) throws NoWhatsappInstalledException, AppDoesntHaveNecessaryPermissionsException {
        if (!isWhatsappInstalled(context))
            throw new NoWhatsappInstalledException("Whatsapp seems not to be istalled");

        if(!Permissions.doesApplicationHaveMakeCallPermission(context))
            throw new AppDoesntHaveNecessaryPermissionsException();

        Intent intent = new Intent();

        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("content://com.android.contacts/data/" + actionId),
                "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
        intent.setPackage("com.whatsapp");

        context.startActivity(intent);
    }

    /**
     * Gets a list of WhatsappContact class instances from users' contacts.
     * @param context context of the application
     * @return an ArrayList of WhatsappContact instances filled with data
     * @throws NoWhatsappInstalledException if whatsapp isn't installed an exception will be thrown
     * @throws AppDoesntHaveNecessaryPermissionsException if the application doesn't have "READ_CONTACTS" permission an exception will be thrown
     */
    public static ArrayList<WhatsappContact> getWhatsappContacts(AppCompatActivity context) throws NoWhatsappInstalledException, AppDoesntHaveNecessaryPermissionsException {
        if (!isWhatsappInstalled(context))
            throw new NoWhatsappInstalledException("Whatsapp seems not to be istalled");

        if (!Permissions.doesApplicationHaveReadContactsPermission(context))
            throw new AppDoesntHaveNecessaryPermissionsException("the app needs 'READ_CONTACTS' permission to perform the action");

        ArrayList<WhatsappContact> myWhatsappContacts = new ArrayList<WhatsappContact>();
        HashSet<String> primaryKeySet = new HashSet<String>();

        ContentResolver resolver = context.getContentResolver();
        Cursor cursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME);

        while (cursor.moveToNext()) {
            String primaryKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.LOOKUP_KEY));
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Data._ID));
            String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String mimeType = cursor.getString(cursor.getColumnIndex(ContactsContract.Data.MIMETYPE));

            WhatsappContact whatsappContact = new WhatsappContact(primaryKey, displayName, phoneNumber);

            int AtCharacterLocation = 0;

            if (phoneNumber != null)
                AtCharacterLocation = phoneNumber.indexOf("@");

            if (AtCharacterLocation > 0)
                phoneNumber = "+" + phoneNumber.substring(0, AtCharacterLocation);

            final String videoCall = "vnd.android.cursor.item/vnd.com.whatsapp.video.call";
            final String voipCall = "vnd.android.cursor.item/vnd.com.whatsapp.voip.call";
            boolean isOfRightMimetype = (mimeType.equals(videoCall) || mimeType.equals(voipCall));

            if (isOfRightMimetype && !primaryKeySet.contains(primaryKey)) {
                myWhatsappContacts.add(new WhatsappContact(primaryKey, displayName, phoneNumber));
                primaryKeySet.add(primaryKey);

                if (mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.video.call"))
                    myWhatsappContacts.get(myWhatsappContacts.size() - 1).setVideoCallId(id);

                if (mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call"))
                    myWhatsappContacts.get(myWhatsappContacts.size() - 1).setVoipCallId(id);
            } else if (isOfRightMimetype) {
                int l = 0;

                while (l < myWhatsappContacts.size() && !(myWhatsappContacts.get(l).getPrimaryKey().equals(primaryKey)))
                    l++;

                if (l != myWhatsappContacts.size() && mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.video.call"))
                    myWhatsappContacts.get(myWhatsappContacts.size() - 1).setVideoCallId(id);

                if (l != myWhatsappContacts.size() && mimeType.equals("vnd.android.cursor.item/vnd.com.whatsapp.voip.call"))
                    myWhatsappContacts.get(myWhatsappContacts.size() - 1).setVoipCallId(id);
            }
        }

        cursor.close();

        return myWhatsappContacts;
    }

    /**
     * Checks whether whatsapp is installed or not.
     * @param context context of the application
     * @return true if whatsapp installed, false otherwise
     */
    public static boolean isWhatsappInstalled(AppCompatActivity context) {
        PackageManager packageManager = context.getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
}