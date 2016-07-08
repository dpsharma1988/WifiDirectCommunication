package dxswifi_direct.com.wifidirectcommunication.main.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Deepak Sharma on 8/7/16.
 */
public class AppUtil {

    private static String TAG = "AppUtil";

    public static void printHashKey(Context pContext) {
        try {
        //    PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext, PackageManager.GET_SIGNATURES);

        PackageInfo info=pContext.getPackageManager().getPackageInfo(pContext.getPackageName(),PackageManager.GET_META_DATA);

            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i(TAG, "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "printHashKey()", e);
        } catch (Exception e) {
            Log.e(TAG, "printHashKey()", e);
        }
    }
}
