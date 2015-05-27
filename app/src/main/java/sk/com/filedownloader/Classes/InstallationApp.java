package sk.com.filedownloader.Classes;

import android.net.Uri;

import java.net.URL;

/**
 * Created by sanchirkartiev on 01/04/15.
 */
public class InstallationApp {

    private String version;
    private String url;
    private String futureVersion;
    private Boolean isWait;
    private Uri packageName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
    public Uri getPackageName()
    {
        return packageName;
    }

    public Boolean isWait() {
        return isWait;
    }

    public void setIsWait(Boolean isWait) {
        this.isWait = isWait;
    }


    public InstallationApp(String url, String version, String pack)
    {
        this.url = url;
        this.version = version;
        packageName = Uri.parse(pack);
    }
    public InstallationApp(String  url)
    {
        this.url = url;
        this.version = "";
    }


}
