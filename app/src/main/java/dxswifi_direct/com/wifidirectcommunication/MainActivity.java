package dxswifi_direct.com.wifidirectcommunication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;
import dxswifi_direct.com.wifidirectcommunication.main.ui.activity.FirebaseChatActivity;
import dxswifi_direct.com.wifidirectcommunication.main.ui.activity.HitAPIActivity;
import dxswifi_direct.com.wifidirectcommunication.main.ui.activity.social.SocialAppsActivity;

/**
 * Created by Deepak Sharma on 10/6/16.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.btnSocialMedia).setOnClickListener(this);
        findViewById(R.id.btnFirebase).setOnClickListener(this);
        findViewById(R.id.btnWifi).setOnClickListener(this);
        findViewById(R.id.btnAPI).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSocialMedia:
                startActivity(new Intent(MainActivity.this, SocialAppsActivity.class));
                break;

            case R.id.btnFirebase:
                startActivity(new Intent(MainActivity.this, FirebaseChatActivity.class));
                break;

            case R.id.btnWifi:
            //    startActivity(new Intent(MainActivity.this, WiFiDirectActivity.class));
                startActivity(new Intent(MainActivity.this, dxswifi_direct.com.wifidirectcommunication.main.WiFiDirectActivity.class));
                break;

            case R.id.btnAPI:
                startActivity(new Intent(MainActivity.this, HitAPIActivity.class));
                break;

            default:
                break;
        }
    }
}