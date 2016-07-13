package dxswifi_direct.com.wifidirectcommunication.main.ui.activity.social;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.model.ShareVideoContent;
import com.facebook.share.widget.ShareDialog;
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
public class FBLoginActivity extends BaseActivity implements View.OnClickListener{

    public String TAG = this.getClass().getSimpleName();
    // Creating Facebook CallbackManager Value
    public static CallbackManager callbackManager;

    private LoginButton btnFBLogin;
    private ImageView mImgProfilePic;
    private TextView userName;
    private Button btnPostImage;
    private Button btnUpdateStatus;

    private ShareDialog shareDialog;

 //   private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static List<String> PERMISSIONS = Arrays.asList("public_profile","user_photos","user_videos", "email","user_likes","user_posts",
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
    private Button btnShare;
    private Button btnShareMultimedia;
    private Button btnShareLink;
    private Button btnSharePhoto;
    private Button btnShareVideo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fb_login);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        callbackManager = CallbackManager.Factory.create();
        btnFBLogin = (LoginButton)findViewById(R.id.btnFBLogin);
    //    btnFBLogin.setReadPermissions("email");
        btnFBLogin.setReadPermissions(PERMISSIONS);

        userName = (TextView) findViewById(R.id.user_name);
        mImgProfilePic= (ImageView) findViewById(R.id.imgProfile);
        btnShare = (Button)findViewById(R.id.btnShare);
        btnShare.setOnClickListener(this);
        btnShareLink = (Button)findViewById(R.id.btnShareLink);
        btnShareLink.setOnClickListener(this);
        btnSharePhoto = (Button)findViewById(R.id.btnSharePhoto);
        btnSharePhoto.setOnClickListener(this);
        btnShareVideo = (Button)findViewById(R.id.btnShareVideo);
        btnShareVideo.setOnClickListener(this);
        btnShareMultimedia = (Button)findViewById(R.id.btnShareMultimedia);
        btnShareMultimedia.setOnClickListener(this);
        btnUpdateStatus = (Button)findViewById(R.id.btnUpdateStatus);
        btnUpdateStatus.setOnClickListener(this);
        btnPostImage = (Button)findViewById(R.id.btnPostImage);
        btnPostImage.setOnClickListener(this);

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

        LoginManager.getInstance().logInWithPublishPermissions(this,Arrays.asList("publish_actions"));
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



    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnShare:
                sharePost();
                break;

            case R.id.btnShareMultimedia:

                try {
                    /*Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setComponent(new ComponentName("com.facebook.katana",
                            "com.facebook.katana.activity.composer.ImplicitShareIntentHandler"));
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "http://i.huffpost.com/gen/3046086/images/o-TESS-HOLLIDAY-facebook.jpg");
                    startActivity(shareIntent);*/
                }
                catch(Exception exception)
                {
                    exception.printStackTrace();
                }
                break;

            case R.id.btnShareLink:
                shareLink();
                break;

            case R.id.btnUpdateStatus:
                sharePost();
                break;

            case R.id.btnSharePhoto:
                try {
                    sharePhoto();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btnShareVideo:
                shareVideo();
                break;

            case R.id.btnPostImage:
                sharePost();
                break;

            default:
                break;
        }
    }



    private void sharePost() {
        shareDialog = new ShareDialog(this);

        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Hello Facebook")
                    .setContentDescription(
                            "'Hello Facebook' Its a simple Facebook share integration")
                    .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                    .setShareHashtag(new ShareHashtag.Builder()// Optional if you wants to add hashtag
                            .setHashtag("#Professional share")
                            .build())
                    .build();

            shareDialog.show(linkContent);
        }

        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i(TAG, "shareDialog onSuccess()");

            }

            @Override
            public void onCancel() {
                Log.i(TAG, "shareDialog onCancel()");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG, "shareDialog onError()");
            }
        });
    }
    private void shareLink() {
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }


    /**
     * This is a method which can post an image to the facebook wall.
     * @throws IOException
     */
    private void sharePhoto() throws IOException {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;

        String imageUri = "drawable://" + R.drawable.gift_icon;
                                /*OR*/
        Uri path = Uri.parse("android.resource://dxswifi_direct.com.wifidirectcommunication/" + R.drawable.gift_icon);
        Uri otherPath = Uri.parse("android.resource://dxswifi_direct.com.wifidirectcommunication/drawable/gift_icon");
        String path1 = path.toString();
        String path2 = otherPath.toString();


        imageUri = "http://i.dailymail.co.uk/i/pix/2015/04/22/21/27DEB82000000578-3051045-image-a-25_1429735533586.jpg";
        URL url = new URL(imageUri);
        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        /*SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
        Bitmap image = BitmapFactory.decodeFile(imageUri, bmOptions);*/
        SharePhoto photo = new SharePhoto.Builder().setBitmap(image).build();
        SharePhotoContent content = new SharePhotoContent.Builder().addPhoto(photo).build();
        Toast.makeText(getApplicationContext(), getString(R.string.facebook_uploading), Toast.LENGTH_SHORT).show();

        ShareApi.share(content, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result)
            {
                Toast.makeText(getApplicationContext(), getString(R.string.facebook_successful), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel()
            {
                Log.v("FACEBOOK_TEST", "share api cancel");
            }

            @Override
            public void onError(FacebookException e)
            {
                Log.v("FACEBOOK_TEST", "share api error " + e);
            }
        });

        //******************************************
        /*Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.gift_icon);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        //    shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);*/
    }

    /**
     * This is a method which can post an image to the facebook wall.
     * @throws IOException
     */
    private void shareVideo() {
        Uri videoFileUri =  Uri.parse("https://www.youtube.com/watch?v=gsMHXeHroVU");
        ShareVideo shareVideo = new ShareVideo.Builder()
                .setLocalUrl(videoFileUri)
                .build();
        ShareVideoContent content = new ShareVideoContent.Builder()
                .setVideo(shareVideo)
                .build();
        ShareDialog shareDialog = new ShareDialog(this);
        shareDialog.show(content, ShareDialog.Mode.AUTOMATIC);
    }

    private void shareMumtimedia() {
        Bitmap icon1 = BitmapFactory.decodeResource(getResources(),R.drawable.gift_icon);
        Bitmap icon2 = BitmapFactory.decodeResource(getResources(),R.drawable.common_google_signin_btn_icon_dark_focused);
        SharePhoto sharePhoto1 = new SharePhoto.Builder()
                .setBitmap(icon1)
                .build();
        SharePhoto sharePhoto2 = new SharePhoto.Builder()
                .setBitmap(icon2)
                .build();
        /*ShareVideco shareVideo1 = new ShareVideo.Builder()
                .setLocalUrl(...)
        .build();
        ShareVideo shareVideo2 = new ShareVideo.Builder()
                .setLocalUrl(...)
        .build();*/

        ShareMediaContent shareMediaContent = new ShareMediaContent.Builder()
                .addMedium(sharePhoto1)
                .addMedium(sharePhoto2)
                                                /*.addMedium(shareVideo1)
                                                .addMedium(shareVideo2)*/
                .build();

        /*ShareContent shareContent = new ShareMediaContent.Builder()
                .addMedium(sharePhoto1)
                .addMedium(sharePhoto2)
                .addMedium(shareVideo1)
                .addMedium(shareVideo2)
                .build();*/

        ShareDialog shareDialog = new ShareDialog(this);
        //    shareDialog.show(shareContent, ShareDialog.Mode.AUTOMATIC);
        shareDialog.show(shareMediaContent, ShareDialog.Mode.AUTOMATIC);

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
