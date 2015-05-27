package sk.com.filedownloader.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import sk.com.filedownloader.Classes.CurrentInformation;
import sk.com.filedownloader.Classes.ServerInformation;

/**
 * Created by sanchirkartiev on 03/04/15.
 */
public class GlobalHelper {

   private static ServerInformation serverInformation = null;

    public static void setServerInformation(ServerInformation information)
    {
        serverInformation = information;
    }
    public static ServerInformation getServerInformation()
    {
        return serverInformation;
    }

    public static void savePreferences(String preference, String application, Context context) {

        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor ed = sPref.edit();
        switch (application) {
            case Constants.MOBI_LOCK:
                ed.putString(Constants.MOBI_LOCK, preference);
                break;
            case Constants.TAXI_INDIGO:
                ed.putString(Constants.TAXI_INDIGO, preference);
                break;
            case Constants.MINISTRO:
                ed.putString(Constants.MINISTRO,preference);
                break;
            default:
                break;
        }
        ed.commit();
    }
    public static CurrentInformation getPreferences(Context context)
    {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        CurrentInformation currentInformation;
        if(settings != null)
        {
            String currentTaxiIndigo = settings.getString(Constants.TAXI_INDIGO,"");
            String currentMobyLock = settings.getString(Constants.MOBI_LOCK,"");
            String currentMinistro = settings.getString(Constants.MINISTRO,"");
            currentInformation = new CurrentInformation(currentMinistro,currentMobyLock,currentTaxiIndigo);
        }
        else
        {
            currentInformation = new CurrentInformation("","","");
        }
        return currentInformation;

    }
}
