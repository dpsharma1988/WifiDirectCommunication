package dxswifi_direct.com.wifidirectcommunication.main.activity;

/**
 * Created by deepaks on 5/7/16.
 */

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;
import dxswifi_direct.com.wifidirectcommunication.main.listeners.ICallback;
import dxswifi_direct.com.wifidirectcommunication.main.network.UpdateListener;

public class FragmentBaseActivity extends BaseActivity implements ICallback,
        OnClickListener, UpdateListener.onUpdateViewListener/*
											 * , ResultCallback<People.
											 * LoadPeopleResult>
											 */{

    private DrawerLayout mDrawerLayout;

    View mDrawer;
    public static String currentFragment = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (View) findViewById(R.id.right_drawer);

        // set a custom shadow that overlays the main content when the drawer
        // opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        ((TextView) mDrawer.findViewById(R.id.user_name)).setText("Hi, "
                + ShipToMyidPref.getUserFirstName(getApplicationContext()));

        // initialize drawer components and set clickListener to them.
        ((TextView) mDrawer.findViewById(R.id.sign_out))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.dashboard))
                .setOnClickListener(this);

        ((TextView) mDrawer.findViewById(R.id.address_book))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.add_contact))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.import_contact))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.manage_group))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.profile))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.view_address))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.view_email))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.incoming))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.outgoing))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.notification))
                .setOnClickListener(this);
        ((TextView) mDrawer.findViewById(R.id.invite)).setOnClickListener(this);

        if (savedInstanceState == null) {
            Bundle bundle = getIntent().getExtras();

            if (bundle != null
                    && bundle.getBoolean(AppConstants.IS_GCM_DATA_RECEIVED,
                    false)) {
                Bundle args = new Bundle();
                args.putString(AppConstants.GCM_DATA_STRING,
                        bundle.getString(AppConstants.GCM_DATA_STRING));
                args.putBoolean(AppConstants.IS_GCM_DATA_RECEIVED, true);
                DashBoardFragment dashboardFragment = new DashBoardFragment();
                dashboardFragment.setArguments(args);
                onFragmentChange(new DashBoardFragment(), false);
            } else {
                onFragmentChange(new DashBoardFragment(), true);
            }
        }
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            onNewIntent(intent);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // TODO Auto-generated method stub
        super.onNewIntent(intent);
        Uri data = intent.getData();
        if (data != null) {

            Log.e("Ship2myID", "call-backresponse:" + data.toString());

            if (data.toString().contains("success")) {
                Toast.makeText(getApplicationContext(), "login successful",
                        Toast.LENGTH_SHORT).show();

                if (data.toString().contains("facebook"))
                    ShipToMyidPref.SetUserFBmapped(getApplicationContext(),
                            true);

                if (data.toString().contains("linkedin"))
                    ShipToMyidPref.SetUserLinkedMapped(getApplicationContext(),
                            true);

                if (data.toString().contains("outlook"))
                    ShipToMyidPref.SetUserFBmapped(getApplicationContext(),
                            true);

                if (data.toString().contains("google")) {
                    String dataArray[] = data.toString().split("access_token=");
                    String accessToken = dataArray[1];
                    Fragment fragment = new ContactListFragment();
                    Bundle args = new Bundle();

                    args.putString(AppConstants.FRAGMENT_TITLE,
                            "IMPORT CONTACT FROM GMAIL");
                    args.putString(AppConstants.CONTACT_MODE,
                            AppConstants.FRAGMENT_GMAIL);
                    fragment.setArguments(args);
                    ShipToMyidPref.setGmailAuthToken(getApplicationContext(),
                            accessToken);
                    onFragmentChange(fragment, false);

                }

                if (data.toString().contains("yahoo")) {

                    // need to split on the basis of & and get access token and
                    // oauth

                    String dataArray[] = data.toString().split("&");

                    for (int i = 0; i < dataArray.length; i++) {
                        if (dataArray[i].contains("oauth_token")) {
                            ShipToMyidPref.setYahooAuthToken(
                                    getApplicationContext(),
                                    (dataArray[i]).split("=")[1]);
                        } else if (dataArray[i].contains("access_token")) {
                            ShipToMyidPref.setYahooAccessToken(
                                    getApplicationContext(),
                                    (dataArray[i]).split("=")[1]);
                        }
                    }

                    Fragment fragment = new ContactListFragment();
                    Bundle args = new Bundle();
                    args.putString(AppConstants.FRAGMENT_TITLE,
                            "IMPORT CONTACT FROM YAHOO");
                    args.putString(AppConstants.CONTACT_MODE,
                            AppConstants.FRAGMENT_YAHOO);
                    fragment.setArguments(args);
                    onFragmentChange(fragment, false);
                }


                if (data.toString().contains("outlook")) {

                    String dataArray[] = data.toString().split("&");

                    for (int i = 0; i < dataArray.length; i++) {
                        if (dataArray[i].contains("access_token")) {
                            ShipToMyidPref.setOutlookAccessToken(
                                    getApplicationContext(),
                                    (dataArray[i]).split("=")[1]);
                        }
                    }

                    Fragment fragment = new ContactListFragment();
                    Bundle args = new Bundle();
                    args.putString(AppConstants.FRAGMENT_TITLE,
                            "IMPORT CONTACT FROM OUTLOOK");
                    args.putString(AppConstants.CONTACT_MODE,
                            AppConstants.FRAGMENT_OUTLOOK);
                    fragment.setArguments(args);
                    onFragmentChange(fragment, false);
                }

                if (currentFragment.equalsIgnoreCase("DashBoardFragment"))
                    onFragmentChange(new DashBoardFragment(), true);
                else if (currentFragment.equalsIgnoreCase("InviteFragment")) {
                    onFragmentChange(new InviteFragment(), false);
                }

            } else {
                Fragment fragment = null;
                if (data.toString().contains("facebook")
                        || data.toString().contains("linkedin")) {
                    if (currentFragment.equalsIgnoreCase("DashBoardFragment"))
                        fragment = new DashBoardFragment();
                    else if (currentFragment.equalsIgnoreCase("InviteFragment")) {
                        fragment = new InviteFragment();
                    }

                    Bundle args = new Bundle();
                    args.putBoolean(AppConstants.SUCCESS, false);

                    String dataArray[] = data.toString().split("reason=");
                    String reason = dataArray[1];

                    args.putString("ERROR", reason);
                    onFragmentChange(fragment, false);

                }

                if (data.toString().contains("google")
                        || data.toString().contains("yahoo")) {
                    fragment = new ImportContactFragment();
                    Bundle args = new Bundle();
                    args.putBoolean(AppConstants.SUCCESS, false);

                    String dataArray[] = data.toString().split("error=");
                    String error = dataArray[1];

                    args.putString("ERROR", error);
                    onFragmentChange(fragment, false);

                }

            }
        }
    }

    @Override
    public void setCurrentFragment(String fragmentName) {
        currentFragment = fragmentName;
    }

    @Override
    public void onFragmentChange(Fragment fragment, boolean flag) {
        currentFragment = fragment.getClass().getName();
        FragmentManager fragmentManager = getSupportFragmentManager();

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        transaction.replace(R.id.content_frame, fragment, fragment.getClass()
                .getName());
        if (!flag)
            transaction.addToBackStack(null);
        transaction.commit();

    }

    /**
     * In this method we creating a url on the basis of reqType and hit the
     * request to server.
     */
    private void hitApiRequest() throws JSONException {

        // to check the Internet connectivity.
        if (!ConnectivityUtils.isNetworkEnabled(FragmentBaseActivity.this)) {

            // show a dialog with a custom message.
            DialogFragment dFragment = new DialogFragment(
                    getString(R.string.network_disable_msg), false,
                    getApplicationContext(), false);
            dFragment.show(getSupportFragmentManager(), "Dialog Fragment");
            return;
        }

        // et request type of request for update view .
        int reqType = ApiConstants.SIGN_OUT;
        String progressMessage;
        String url = ApiConstants.USER_SIGN_OUT + "?"
                + ApiConstants.ACCESS_TOKEN + "="
                + ShipToMyidPref.getApiAuthTokenResult(getApplicationContext());
        progressMessage = "Loading...";

        // show progress dialog.
        ProgressUtils.showCustomProgressDialog(progressMessage,
                FragmentBaseActivity.this, false);

        // create a POST request with help of volley Lib.
        VolleyJsonRequest request = VolleyJsonRequest.dodelete(url,
                new UpdateListener1(this, reqType));

        // add that request to volley queue to process.
        VolleyManager.getInstance(getApplicationContext()).addToRequestQueue(
                request, url);

    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();

        switch (v.getId()) {
            case R.id.dashboard:
                if (!currentFragment.contains("DashBoardFragment"))
                    onFragmentChange(new DashBoardFragment(), true);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.address_book:
                if (!currentFragment.contains("AddressBookFragment"))
                    onFragmentChange(new AddressBookFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.add_contact:
                if (!currentFragment.contains("AddContactFragment"))
                    onFragmentChange(new AddContactFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.import_contact:
                if (!currentFragment.contains("ImportContactFragment"))
                    onFragmentChange(new ImportContactFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.manage_group:
                if (!currentFragment.contains("ManageGroupFragment"))
                    onFragmentChange(new ManageGroupFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.profile:
                if (!currentFragment.contains("ProfileFragment"))
                    onFragmentChange(new ProfileFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.view_address:
                if (!currentFragment.contains("ViewAddressFragment"))
                    onFragmentChange(new ViewAddressFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.view_email:
                if (!currentFragment.contains("ViewEmailFragment"))
                    onFragmentChange(new ViewEmailFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.incoming:
                if (!currentFragment.contains("IncomingFragment")) {
                    bundle.putString(AppConstants.ORDER_MODE, AppConstants.INCOMING);
                    IncomingFragment incomingFragment = new IncomingFragment();
                    incomingFragment.setArguments(bundle);
                    onFragmentChange(incomingFragment, false);
                }
                // onFragmentChange(new ShipmentAcceptedFragment(), false);

                mDrawerLayout.closeDrawer(mDrawer);
                break;
            case R.id.outgoing:
                if (!currentFragment.contains("OutgoingFragment")) {
                    bundle.putString(AppConstants.ORDER_MODE, AppConstants.OUTGOING);
                    IncomingFragment inOutFragment = new IncomingFragment();
                    inOutFragment.setArguments(bundle);
                    onFragmentChange(inOutFragment, false);
                    mDrawerLayout.closeDrawer(mDrawer);
                }
                break;
            case R.id.notification:
                if (!currentFragment.contains("NotificationFragment")) {
                    onFragmentChange(new NotificationFragment(), false);
                }
                mDrawerLayout.closeDrawer(mDrawer);
                break;

            case R.id.invite:
                if (!currentFragment.contains("InviteFragment"))
                    onFragmentChange(new InviteFragment(), false);
                mDrawerLayout.closeDrawer(mDrawer);
                break;

            case R.id.sign_out:

                try {
                    mDrawerLayout.closeDrawer(mDrawer);
                    hitApiRequest();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            default:
                break;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see com.mapMyId.ship2myId.listener.UpdateListener1.onUpdateViewListener#
     * updateView(java.lang.String, boolean, int)
     *
     * set a update view for every request response.
     */
    @Override
    public void updateView(String responseString, boolean isSuccess, int reqType) {

        // remove progress bar.
        ProgressUtils.removeProgressDialog();

        // get a instance of Gson class to parse the Json response.
        Gson gson = new GsonBuilder().create();
        DialogFragment dFragment = null;

        if (isSuccess) {

            // check response for any failure status.
            if (!responseString.contains("failure")
                    && !responseString.contains("Error")) {

                // clear all shared preference data
                ShipToMyidPref.setUserFirstName(getApplicationContext(), "");
                ShipToMyidPref.setUserLastName(getApplicationContext(), "");
                ShipToMyidPref.saveApiAuthTokenResult(getApplicationContext(),
                        "");
                ShipToMyidPref.saveUserId(getApplicationContext(), "");
                ShipToMyidPref.setRefralScore(getApplicationContext(), 0);

                ShipToMyidPref.SetUserSignIndetails(getApplicationContext(),
                        false);

                Intent intent = new Intent(FragmentBaseActivity.this,
                        LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

            } else {

                if (responseString.contains("Error")) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseString);
                        JSONObject object;
                        object = jsonObject.getJSONObject("Error");
                        String message = object.getString("message");

                        // show dialog with Error message.
                        dFragment = new DialogFragment(message, false,
                                getApplicationContext(), false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // parse failure response to statusErrorModel.
                    StatusErrorModel errorModel = gson.fromJson(responseString,
                            StatusErrorModel.class);

                    // show dialog with failure reason.
                    dFragment = new DialogFragment(errorModel.getReason(),
                            false, getApplicationContext(), false);
                    dFragment.show(getSupportFragmentManager(),
                            "Dialog Fragment");

                } else {
                    dFragment = new DialogFragment(responseString, false,
                            getApplicationContext(), false);
                    dFragment.show(getSupportFragmentManager(),
                            "Dialog Fragment");
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("inside BaseActivity", "onActivityResult()");
        // super.onActivityResult(requestCode, resultCode, data);
        Log.i("inside BaseActivity", "onActivityResult()");

        Fragment topFragment = getSupportFragmentManager().findFragmentById(
                R.id.content_frame);
        if (topFragment instanceof ImportContactFragment) {
			/*
			 * Fragment topInnerFragment = ((ComposeFragment)
			 * topFragment).getTopInnerFragmentInstance(); if (topInnerFragment
			 * instanceof ComposeDoubtFragment) {
			 */
            ((ImportContactFragment) topFragment).onActivityResult(requestCode,
                    resultCode, data);
			/* } */
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

	/*
	 * @Override public void onResult(LoadPeopleResult loadPeopleResult) {
	 * Log.i("inside BaseActivity", "onResult()"); Fragment topFragment =
	 * getSupportFragmentManager().findFragmentById(R.id.content_frame); if
	 * (topFragment instanceof ContactListFragment) { Fragment topInnerFragment
	 * = ((ComposeFragment) topFragment).getTopInnerFragmentInstance(); if
	 * (topInnerFragment instanceof ComposeDoubtFragment) {
	 * ((ContactListFragment) topFragment).onResult(loadPeopleResult); } } }
	 */

    public void sendAGift(String url) {
        // String url = "http://www.nextgengifting.com/";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

}
