package dxswifi_direct.com.wifidirectcommunication.main.ui.activity.social;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import dxswifi_direct.com.wifidirectcommunication.R;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;

/**
 * Created by Deepak Sharma on 7/7/16.
 * This is a class which has a list of the social medias to implement.
 */
public class SocialAppsActivity  extends BaseActivity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_apps);

        findViewById(R.id.btnFacepook).setOnClickListener(this);
        findViewById(R.id.btnGmail).setOnClickListener(this);
        findViewById(R.id.btnInstagram).setOnClickListener(this);
        findViewById(R.id.btnTwitter).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnFacepook:
                startActivity(new Intent(SocialAppsActivity.this, FBLoginActivity.class));
                break;

            case R.id.btnGmail:
                startActivity(new Intent(SocialAppsActivity.this, GPlusLoginActivity.class));
                break;

            case R.id.btnInstagram:
            //    startActivity(new Intent(SocialAppsActivity.this, .class));
                break;

            case R.id.btnTwitter:
                //    startActivity(new Intent(SocialAppsActivity.this, .class));
                break;

            default:
                break;
        }
    }

}