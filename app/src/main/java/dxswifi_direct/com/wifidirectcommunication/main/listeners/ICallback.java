package dxswifi_direct.com.wifidirectcommunication.main.listeners;

import android.support.v4.app.Fragment;

public interface ICallback {

    void setCurrentFragment(String fragmentName);

    void onFragmentChange(Fragment fragment, boolean b);

}