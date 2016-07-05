package dxswifi_direct.com.wifidirectcommunication.main.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.client.ChildEventListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dxswifi_direct.com.wifidirectcommunication.R;
import dxswifi_direct.com.wifidirectcommunication.base.activity.BaseActivity;
import dxswifi_direct.com.wifidirectcommunication.main.FirebaseChatImpl;
import dxswifi_direct.com.wifidirectcommunication.main.adapter.CustomListAdapter;
import dxswifi_direct.com.wifidirectcommunication.main.interfaces.FirebaseChatCallback;
import dxswifi_direct.com.wifidirectcommunication.main.model.Message;

/**
 * Created by Deepak Sharma on 10/6/16.
 */
public class FirebaseChatActivity extends BaseActivity implements View.OnClickListener, FirebaseChatCallback, CustomListAdapter.AdapterCommand{

    private ListView mMessageList;
//    private CommonDataAdapter adapter;
    private CustomListAdapter adapter;
    private CustomListAdapter.AdapterCommand listener;
    private List<Message> mMessages;
    private EditText edtMessage;
    private Button btnSend;

    public ChildEventListener childEventListener = null;
    private String mConversationId;
    public FirebaseChatImpl firebaseChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.firebase_chat);

        edtMessage = (EditText)findViewById(R.id.edtMessage);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(this);

        String[] IDs = {"123","abc"};
        mConversationId = IDs[0]+IDs[1];
        firebaseChat = new FirebaseChatImpl(mConversationId);

        mMessageList = (ListView)findViewById(R.id.message_list);
        mMessages = new ArrayList<>();
        setAdapter();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnSend:
            {
                Message message = new Message();
                message.setMessage(edtMessage.getText().toString());
                message.setMessageDate(new Date());
                message.setSender("Deepak");
                firebaseChat.saveFirebasechatMessage(message, mConversationId);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onMessageAdded(Message message) {
        if (message!=null)
        {
            // TODO add message into the list and notify listview with modification
            adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(FirebaseChatActivity.this, "Its a buggy message.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAdapter()
    {
        /*adapter = new CommonDataAdapter(FirebaseChatActivity.this);
        adapter.setData(mMessages, R.layout.message_item, new CommonDataAdapter.IClickable() {
            TextView txtMessage;
            @Override
            public void init(View view) {
                txtMessage = (TextView)view.findViewById(R.id.txtMessage);
            }

            @Override
            public void execute(View view, Object object) {
                Message message = (Message) object;
                txtMessage.setText(message.getMessage());
            }
        });

        mMessageList.setAdapter((ListAdapter) adapter);*/

        adapter = new CustomListAdapter(FirebaseChatActivity.this,mMessages, R.layout.message_item,this, new CustomListAdapter.ViewHolder());
        mMessageList.setAdapter(adapter);
    }

    @Override
    public void init(View v, CustomListAdapter.ViewHolder h) {
        Toast.makeText(this, "Init", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void execute(Object object, CustomListAdapter.ViewHolder h) {
        Toast.makeText(this, "Execute", Toast.LENGTH_SHORT).show();
    }
}
