package dxswifi_direct.com.wifidirectcommunication.main.ui.activity.social;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import dxswifi_direct.com.wifidirectcommunication.R;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;

/**
 * Created by Deepak Sharma on 11/7/16.
 */
public class GPlusLoginActivity  extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gplus_login);

        // Configure sign-in to request the user's ID, email address, and basic// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

    }
}
