package dxswifi_direct.com.wifidirectcommunication.base.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Deepak Sharma on 10/6/16.
 * This is an abstract activity which is a base activity for all other activities in the project.
 * It contains common methods(requirements) for all activities. This activity contains concrete as
 * well as abstract methods and every of its Sub-activity must be implement all its abstract methods. This activity can not be intantiated.
 */

public abstract class BaseActivity extends AppCompatActivity{

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }
}
