package dxswifi_direct.com.wifidirectcommunication.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Deepak Sharma on 10/6/16.
 */

public class CustomListAdapter<T> extends BaseAdapter {

        private List<T> mListData;
        private LayoutInflater inflater;
        private int resources;
        private AdapterCommand<T> listener;
        private ViewHolder h;

        public CustomListAdapter(Context context, List<T> objects,int resources, AdapterCommand<T> listener, ViewHolder h) {
            this.h = h;
            this.listener = listener;
            this.resources = resources;
            this.mListData = objects;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mListData.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return mListData.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        /*@Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View v = convertView;
            if (v == null) {
                v = inflater.inflate(resources, parent, false);
                listener.init(v, h);
                v.setTag(h);
            } else {
                h = (ViewHolder) v.getTag();
            }
            T object = (T) getItem(position);
            listener.execute(object, h);
            return v;
        }*/

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        ViewHolder h;
        if (v == null) {
            v = inflater.inflate(resources, parent, false);
            h = new ViewHolder();
            listener.init(v, h);
            v.setTag(h);
        } else {
            h = (ViewHolder) v.getTag();
        }
        T object = (T) getItem(position);
        listener.execute(object, h);
        return v;
    }

    public interface AdapterCommand<T> {
            public void init(View v, ViewHolder h);
            public void execute(T object, ViewHolder h);
        }

        public static class ViewHolder {

        }
}
