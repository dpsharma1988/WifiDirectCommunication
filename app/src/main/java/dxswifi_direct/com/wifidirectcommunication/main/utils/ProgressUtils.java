package dxswifi_direct.com.wifidirectcommunication.main.utils;

/**
 * Created by Deepak Sharma on 5/7/16.
 * This is a common utility class for a roundig circular dialog during network transactions.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

public class ProgressUtils {

    private static ProgressDialog mProgressDialog;

    /**
     * Progress dialog to be used in all projects
     *
     * @param bodyText
     * @param context
     * @param isCancelable
     */
    public static void showCustomProgressDialog(String bodyText,Context context, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = new ProgressDialog(context);
                mProgressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                mProgressDialog.setCancelable(false);
            }
            mProgressDialog.setMessage(bodyText);
            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes the progress dialog
     */
    public static void removeProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.hide();
                mProgressDialog.dismiss();
                mProgressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isProgressShowing() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            return true;
        }
        return false;
    }
}
