package sk.com.filedownloader.Classes;

/**
 * Created by sanchirkartiev on 03/04/15.
 */
public class ServerInformation {

    public String mobyLock;
    public String ministro;
    public String taxiIndigo;

    public ServerInformation(String mobyLock, String ministro, String taxiIndigo)
    {
        this.mobyLock = mobyLock;
        this.ministro = ministro;
        this.taxiIndigo = taxiIndigo;
    }

    public String getTaxiIndigo() {
        return taxiIndigo;
    }

    public void setTaxiIndigo(String taxiIndigo) {
        this.taxiIndigo = taxiIndigo;
    }

    public String getMinistro() {
        return ministro;
    }

    public void setMinistro(String ministro) {
        this.ministro = ministro;
    }

    public String getMobyLock() {

        return mobyLock;
    }

    public void setMobyLock(String mobyLock) {
        this.mobyLock = mobyLock;
    }
}
