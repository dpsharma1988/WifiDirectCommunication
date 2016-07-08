package dxswifi_direct.com.wifidirectcommunication.main.application;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import dxswifi_direct.com.wifidirectcommunication.base.application.BaseApplication;

/**
 * Created by Deepak Sharma on 7/7/16.
 */
public class MainApplication extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize the SDK before executing any other operations,
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }
}
