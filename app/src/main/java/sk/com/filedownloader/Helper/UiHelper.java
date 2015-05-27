package sk.com.filedownloader.Helper;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
public class UiHelper {
    public static void showToast(Context context, CharSequence text) {
        if(context != null) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
            toast.show();
        }
    }
}
