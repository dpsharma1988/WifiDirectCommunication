package dxswifi_direct.com.wifidirectcommunication.main.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import dxswifi_direct.com.wifidirectcommunication.R;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;
import dxswifi_direct.com.wifidirectcommunication.main.model.request.BaseHTTPRequest;
import dxswifi_direct.com.wifidirectcommunication.main.model.response.OKHttpModel;
import dxswifi_direct.com.wifidirectcommunication.main.network.UpdateListener;
import dxswifi_direct.com.wifidirectcommunication.main.preferences.MyPreferences;
import dxswifi_direct.com.wifidirectcommunication.main.ui.fragments.DialogFragment;
import dxswifi_direct.com.wifidirectcommunication.main.utils.ApiConstants;
import dxswifi_direct.com.wifidirectcommunication.main.utils.ConnectivityUtils;
import dxswifi_direct.com.wifidirectcommunication.main.utils.ProgressUtils;

/**
 * Created by Deepak Sharma on 4/7/16.
 * This is a test class which is responsible to get HTTPResponse.
 */
public class HitAPIActivity extends BaseActivity implements View.OnClickListener, UpdateListener.OnUpdateViewListener{

    private static final String TAG = "HitAPIActivity";
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.api_activity);
        findViewById(R.id.btnGetResponse).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnGetResponse:
                hitApiRequest(ApiConstants.GET_CONTACTS);
                break;

            default:
                break;
        }
    }

    /**
     * @param reqType
     *            - to check which API we are calling.
     *
     *            In this method we creating a url on the basis of reqType and
     *            hit the request to server.
     */
    private void hitApiRequest(int reqType) {

        // to check the Internet connectivity.
        if (!ConnectivityUtils.isNetworkEnabled(HitAPIActivity.this)) {

            // show a dialog with a custom message.
            DialogFragment dFragment = new DialogFragment(getString(R.string.network_disable_msg), false,
                                        HitAPIActivity.this, true);
            dFragment.show(getSupportFragmentManager(),"Dialog Fragment");
            return;
        }
        String url =  "";
        String progressMessage;
    //    VolleyJsonRequest request = null;
        BaseHTTPRequest baseHTTPRequest = null;

        // create a URL on the basis of request type.
        switch (reqType) {

            case ApiConstants.GET_CONTACTS:
                url = ApiConstants.GET_CONTACTS_URL
                        + MyPreferences.getAuthToken(this);

                /*// set Get request for particular request type
                request = VolleyJsonRequest.doget(url, new UpdateListener1(this,
                        reqType));*/

                baseHTTPRequest = new BaseHTTPRequest();
            //    baseHTTPRequest.setURL("https://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demohttps://api.geonames.org/citiesJSON?north=44.1&south=-9.9&east=-22.4&west=55.2&lang=de&username=demo");
                baseHTTPRequest.setURL("http://jsonparsing.parseapp.com/jsonData/moviesDemoItem.txt");
                baseHTTPRequest.setRequestCode(reqType);

                UpdateListener updateListener = new UpdateListener(HitAPIActivity.this, baseHTTPRequest);
                updateListener.getJsonData();

                break;

            /*case ApiConstants.GET_PROFILE:

                url = ApiConstants.GET_PROFILE_URL
                        + MyPreferences.getAuthToken(this);

                request = VolleyJsonRequest.doPostjson(url, new UpdateListener1(
                                this, ApiConstants.DELETE_CONTACT),
                        getParams(ApiConstants.DELETE_CONTACT));
                break;*/

            default:
                break;
        }
        progressMessage = "Loading...";

        // show progress dialog.
        ProgressUtils.showCustomProgressDialog(progressMessage, this,false);

        // add that request to volley queue to process.
        /*VolleyManager.getInstance(getActivity())
                .addToRequestQueue(request, url);*/
    }

    @Override
    public void updateView(final String responseString, boolean isSuccess, int reqType) {

        DialogFragment dFragment = null;

        if (isSuccess)
        {
            // get a instance of Gson class to parse the Json response.
            Gson gson = new GsonBuilder().create();

            // check response contains any failure status.
            if (!responseString.contains("failure")
                    && !responseString.contains("Error")) {
                // Handle request on the basis of Request Type.
                switch (reqType) {
                    case ApiConstants.GET_CONTACTS:
                        // remove progress bar.
                        ProgressUtils.removeProgressDialog();
                        Log.e(TAG, "RESPONSE:" + responseString);
                        OKHttpModel okHttpModel = new Gson().fromJson(responseString, OKHttpModel.class);
                        HitAPIActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                TextView txtResponse = (TextView)findViewById(R.id.txtResponse);
                                txtResponse.setText("");
                                txtResponse.setText(responseString);
                            }
                        });

                    //    dFragment = new DialogFragment("Contact Added Successfully.", true, this, true);
                    //    dFragment.show(fm, "Dialog Fragment");
                        break;

                    case ApiConstants.GET_PROFILE:

                        // remove progress bar.
                        ProgressUtils.removeProgressDialog();

                        // TODO convert responseString into correspondence object.

                        break;

                    default:
                        break;
                }
            }

        }

    }
}
