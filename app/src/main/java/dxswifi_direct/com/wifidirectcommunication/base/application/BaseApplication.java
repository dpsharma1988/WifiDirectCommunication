package dxswifi_direct.com.wifidirectcommunication.base.application;

import android.app.Application;
import dxswifi_direct.com.wifidirectcommunication.main.utils.AppUtil;

/**
 * Created by Deepak Sharma on 10/6/16.
 * This is the base application class and custom Application class must extend this class to
 * achieve common functionalities.
 */

public class BaseApplication extends Application{

    private static final String TAG = "BaseApplication";

    @Override
    public void onCreate() {
        super.onCreate();

        AppUtil.printHashKey(getApplicationContext());

    }
}
