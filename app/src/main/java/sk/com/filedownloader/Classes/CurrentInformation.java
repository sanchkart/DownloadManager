package sk.com.filedownloader.Classes;

/**
 * Created by sanchirkartiev on 03/04/15.
 */
public class CurrentInformation {

    public CurrentInformation(String currentMinistro, String currentMobyLock, String currentTaxiIndigo) {
        this.currentMinistro = currentMinistro;
        this.currentMobyLock = currentMobyLock;
        this.currentTaxiIndigo = currentTaxiIndigo;
    }
    public String currentTaxiIndigo;
    public String currentMobyLock;
    public String currentMinistro;


    public String getCurrentMinistro() {
        return currentMinistro;
    }

    public void setCurrentMinistro(String currentMinistro) {
        this.currentMinistro = currentMinistro;
    }

    public String getCurrentTaxiIndigo() {
        return currentTaxiIndigo;
    }

    public void setCurrentTaxiIndigo(String currentTaxiIndigo) {
        this.currentTaxiIndigo = currentTaxiIndigo;
    }

    public String getCurrentMobyLock() {
        return currentMobyLock;
    }

    public void setCurrentMobyLock(String currentMobyLock) {
        this.currentMobyLock = currentMobyLock;
    }




}
