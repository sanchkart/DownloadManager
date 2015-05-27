package sk.com.filedownloader.Classes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sanchirkartiev on 04/04/15.
 */
public class MyList {
    private static MyList instance;
    public static HashMap<String,File> list;

    public MyList()
    {
        list = new HashMap<String,File>();
    }
    public static synchronized MyList getInstance() {
        if (instance == null)
        {
            instance = new MyList();
        }
        return instance;
    }
}
