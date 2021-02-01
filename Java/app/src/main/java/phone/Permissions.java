package phone;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Permissions {

    /**
     * Asks user to give permissions required for this application to work.
     * @param context context of the application
     */
    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void askForPermissions(AppCompatActivity context) {
        ArrayList<String> permissionsRequired = new ArrayList<>();

        if(ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED)
            permissionsRequired.add(Manifest.permission.READ_CONTACTS);

        String[] permissionsRequiredArray = new String[permissionsRequired.size()];
        permissionsRequired.toArray(permissionsRequiredArray);

        if(permissionsRequiredArray.length != 0)
            context.requestPermissions(permissionsRequiredArray, 1);
    }

    /**
     * Returns true/false depending on if the app has the "READ_CONTACTS" permission.
     * @param context context of the application
     * @return true if application has the "READ_CONTACTS" permission, or false otherwise
     */
    public static boolean doesApplicationHaveReadContactsPermission(AppCompatActivity context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }
}
