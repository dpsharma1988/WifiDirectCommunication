package dxswifi_direct.com.wifidirectcommunication.main.ui.fragments;

/**
 * Created by Deepak Sharma on 5/7/16.
 */

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import dxswifi_direct.com.wifidirectcommunication.R;

/**
 * @author manish.verma
 *
 */
public class DialogFragment extends android.support.v4.app.DialogFragment {

    TextView messageContainer;
    String message;
    boolean isFinishActivity;
    Context mContext;
    boolean isFragment;

    public DialogFragment() {
        // Empty Constructor.
    }

    /**
     * @param message
     */
    public DialogFragment(String message, boolean isFinish, Context mContext,boolean isFragment) {
        super();
        this.message = message;
        this.isFinishActivity = isFinish;
        this.mContext = mContext;
        this.isFragment = isFragment;
        this.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate the dailog View
        View rootView = inflater.inflate(R.layout.dialog_fragment, container,
                false);
        // Set the feature to See No title in dialog box.
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

		/* Depricated method for Blur */
        // getDialog().getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);

        // initialize message component
        messageContainer = (TextView) rootView
                .findViewById(R.id.message_container);

        messageContainer.setText(message);

        ((TextView) rootView.findViewById(R.id.ok_button))
                .setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        getDialog().cancel();
                        if (isFinishActivity) {

                            if (isFragment) {

                                getActivity().getSupportFragmentManager()
                                        .popBackStack();

                            } else {
                                ((Activity) mContext).finish();
                            }
                        }
                    }
                });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);

        // Set Animation for the Dialog Box.
        getDialog().getWindow().getAttributes().windowAnimations = R.style.AppTheme;
    }

}