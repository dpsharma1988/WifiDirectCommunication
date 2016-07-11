package dxswifi_direct.com.wifidirectcommunication.main.ui.activity.social;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.internal.ImageRequest;
import com.facebook.internal.ImageResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import dxswifi_direct.com.wifidirectcommunication.R;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;
import dxswifi_direct.com.wifidirectcommunication.main.model.response.facebook.UserModel;

/**
 * Created by Deepak Sharma on 8/7/16.
 */
public class FBLoginActivity extends BaseActivity{

    public String TAG = this.getClass().getSimpleName();
    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackManager;

    private LoginButton btnFBLogin;
    private ImageView mImgProfilePic;
    private TextView userName;
    private Button btnPostImage;
    private Button btnUpdateStatus;

 //   private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static List<String> PERMISSIONS = Arrays.asList("public_profile","user_photos", "email","user_likes","user_posts",
                                                "user_hometown", "user_location","user_about_me","user_birthday",
                                                "user_friends","user_relationship_details");

    private static String message = "Sample status posted from android app";
    private AccessTokenTracker mAccessTokenTracker;
    private AccessToken mCurrentAccessToken;
    private ProfileTracker mProfileTracker;
    private Profile mCurrentProfile;

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String FIELDS = "fields";
    private static final String REQUEST_FIELDS = TextUtils.join(",", new String[] {ID, NAME, PICTURE});
    public JSONObject user;
    private Drawable userProfilePic;
    private String userProfilePicID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_login);

        callbackManager = CallbackManager.Factory.create();
        btnFBLogin = (LoginButton)findViewById(R.id.btnFBLogin);
    //    btnFBLogin.setReadPermissions("email");
        btnFBLogin.setReadPermissions(PERMISSIONS);

        userName = (TextView) findViewById(R.id.user_name);
        mImgProfilePic= (ImageView) findViewById(R.id.imgProfile);
        btnUpdateStatus = (Button)findViewById(R.id.update_status);
        btnPostImage = (Button)findViewById(R.id.post_image);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            if (!accessToken.isExpired()) {
                mCurrentAccessToken = accessToken;
            }
        }

        // Callback registration
        /*btnFBLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.i(TAG,"onSuccess registerCallback");
            }

            @Override
            public void onCancel() {
                // App code
                Log.i(TAG,"onCancel registerCallback");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.i(TAG,"onError registerCallback");
                exception.printStackTrace();
            }
        });*/

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                    // App code
                        Log.i(TAG,"onSuccess registerCallback");
                    }

                    @Override
                    public void onCancel() {
                    // App code
                        Log.i(TAG,"onCancel registerCallback");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Log.i(TAG,"onError registerCallback");
                    }
         });

        tokenTracker();
        profileTracker();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
                super.onActivityResult(requestCode, resultCode, data);
                        Log.i(TAG,"onActivityResult : "+data);
                        callbackManager.onActivityResult(requestCode, resultCode, data);
                        /*Log.i(TAG,"onActivityResult : "+ AccessToken.getCurrentAccessToken() );
                        Log.i(TAG,"onActivityResult : "+ Profile.getCurrentProfile());*/
                        fetchUserInfo();

    }

    //If you want your app to keep up with the current access token, you need to implement onCurrentAccessTokenChanged() abstract method of AccessTokenTracker abstract class
    public void tokenTracker()
    {
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {
                // Set the access token using currentAccessToken when it's loaded or set.
                mCurrentAccessToken = currentAccessToken;
                mImgProfilePic.setImageDrawable(getResources().getDrawable(R.drawable.gift_icon));
            }
        };
        // If the access token is available already assign it.
        mCurrentAccessToken = AccessToken.getCurrentAccessToken();
    }


    // If you want your app to keep up with the current profile, you need to implement onCurrentProfileChanged() abstract method of ProfileTracker classe.
    public void profileTracker()
    {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile,Profile currentProfile) {
                // TODO
                mCurrentProfile = currentProfile;
            }
        };
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAccessTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchUserInfo();
    //    updateUI();
    }

    private void fetchUserInfo() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            user = me;
                            UserModel userModel = new Gson().fromJson(me.toString(), UserModel.class);
                            /*URL url = null;
                            try {
                                url = new URL(userModel.getPicture().getData().getUrl());
                                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                                mImgProfilePic.setImageBitmap(bmp);
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }*/
                                new DownloadImageTask().execute(userModel.getPicture().getData().getUrl());
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString(FIELDS, REQUEST_FIELDS);
            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);
        } else {
            user = null;
        }
    }


    private ImageRequest getImageRequest() {
        ImageRequest request = null;
        ImageRequest.Builder requestBuilder = new ImageRequest.Builder(this, ImageRequest.getProfilePictureUri(
                        user.optString("id"),
                        getResources().getDimensionPixelSize(
                                R.dimen.usersettings_fragment_profile_picture_width),
                        getResources().getDimensionPixelSize(
                                R.dimen.usersettings_fragment_profile_picture_height)));

        request = requestBuilder.setCallerTag(this)
                .setCallback(
                        new ImageRequest.Callback() {
                            @Override
                            public void onCompleted(ImageResponse response) {
                                processImageResponse(user.optString("id"), response);
                            }
                        })
                .build();
        return request;
    }

    private void processImageResponse(String id, ImageResponse response) {
        if (response != null) {
            Bitmap bitmap = response.getBitmap();
            if (bitmap != null) {
                /*BitmapDrawable drawable = new BitmapDrawable(UserSettingsFragment.this.getResources(), bitmap);
                drawable.setBounds(0, 0,
                        getResources().getDimensionPixelSize(R.dimen.usersettings_fragment_profile_picture_width),
                        getResources().getDimensionPixelSize(R.dimen.usersettings_fragment_profile_picture_height));
                BitmapDrawable userProfilePic = drawable;
                String userProfilePicID = id;
                btnUpdateStatus.setCompoundDrawables(null, drawable, null, null);
                btnUpdateStatus.setTag(response.getRequest().getImageUri());*/
            }
        }
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            mImgProfilePic.setImageBitmap(result);
        }
    }
}
