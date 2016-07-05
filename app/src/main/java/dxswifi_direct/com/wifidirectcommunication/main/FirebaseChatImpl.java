package dxswifi_direct.com.wifidirectcommunication.main;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import dxswifi_direct.com.wifidirectcommunication.main.interfaces.FirebaseChatCallback;
import dxswifi_direct.com.wifidirectcommunication.main.model.Message;
import dxswifi_direct.com.wifidirectcommunication.main.utils.FirebaseConst;

/**
 * Created by Deepak Sharma on 10/6/16.
 */

public class FirebaseChatImpl {

    public static final Firebase mFirebase = new Firebase(FirebaseConst.FIREBASE_DB_PATH);
    public static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddmmss");
    public static final String TAG = "FirebaseChatImpl";
    public static final String MESSAGE = "MESSAGE";
    public static final String SENDER = "SENDER";
    private final String conversationID;

    public FirebaseChatCallback callback;

    public FirebaseChatImpl(String conversationId)
    {
        this.conversationID = conversationId;
    }


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap<String, String> msg = (HashMap<String, String>) dataSnapshot.getValue();
            Message message = new Message();
            message.setMessage(msg.get(MESSAGE));
            message.setSender(msg.get(SENDER));

            try {
                message.setMessageDate(simpleDateFormat.parse(dataSnapshot.getKey()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (callback!=null)
            {
                callback.onMessageAdded(message);
            }

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(FirebaseError firebaseError) {

        }
    };


    public void saveFirebasechatMessage(Message message, String conversationId)
    {
        Date date = message.getMessageDate();
        String key = simpleDateFormat.format(date);
        HashMap<String,String> map = new HashMap<>();
        map.put(MESSAGE, message.getMessage());
        map.put(SENDER,message.getSender());
        mFirebase.child(conversationId).child(key).setValue(message);

    }

}
