package dxswifi_direct.com.wifidirectcommunication.main;

import com.firebase.client.Firebase;

import dxswifi_direct.com.wifidirectcommunication.base.application.BaseApplication;


/**
 * Created by deepaks on 10/6/16.
 */

public class ProjectApplication extends BaseApplication {



    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(ProjectApplication.this);


    }
}
