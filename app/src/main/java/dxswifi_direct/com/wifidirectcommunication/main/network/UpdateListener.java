package dxswifi_direct.com.wifidirectcommunication.main.network;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;
import java.util.List;
import dxswifi_direct.com.wifidirectcommunication.main.model.BaseHTTPRequest;
import dxswifi_direct.com.wifidirectcommunication.main.model.RequestParameter;

/**
 * Created by Deepak Sharma on 4/7/16.
 * This is a Simple java class which will help you for HTTP request/response and it will
 * throw the response to your correspondance activity.
 */

public class UpdateListener {

    private onUpdateViewListener onUpdateViewListener;

    OkHttpClient client = new OkHttpClient();
    BaseHTTPRequest requestModel;

    public interface onUpdateViewListener {
        public void updateView(String responseString, boolean isSuccess,
                               int reqType);
    }

    public UpdateListener(onUpdateViewListener onUpdateView, final BaseHTTPRequest requestModel) {
        this.requestModel = requestModel;
        this.onUpdateViewListener = onUpdateView;

        String url = null;
        if (requestModel.getListParameters()!=null && requestModel.getListParameters().size()>0)
        {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(requestModel.getURL()).newBuilder();
            /*urlBuilder.addQueryParameter("v", "1.0");
            urlBuilder.addQueryParameter("user", "deepak");*/
            List<RequestParameter> requestParameters = requestModel.getListParameters();
            for (int i=0; i<requestParameters.size();i++)
            {
                urlBuilder.addQueryParameter(requestParameters.get(i).getKey(),requestParameters.get(i).getValue());
            }
            url = urlBuilder.build().toString();
        }
        else
        {
            url = requestModel.getURL();
        }

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                e.printStackTrace();
                onUpdateViewListener.updateView(NetworkException.getErrorMessage(e), false, requestModel.getRequestCode());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    // You can also throw your own custom exception
                    throw new IOException("Unexpected code " + response);
                } else {
                    onUpdateViewListener.updateView(response.toString(),true,requestModel.getRequestCode());
                }
                    // do something wih the result
            }
        });
    }

    /*@Override
    public void onErrorResponse(VolleyError error) {
        onUpdateViewListener.updateView(NetworkException.getErrorMessage(error), false, reqType);
    }

    @Override
    public void onResponse(JSONObject response) {
        // TODO Auto-generated method stub
        if (BuildConfig.DEBUG) {
            Log.i("Respone-------------->", response.toString());
        }
        onUpdateViewListener.updateView(response.toString(), true, reqType);
    }*/

	/*
	 * @Override protected Response<Bitmap> parseNetworkResponse(NetworkResponse
	 * response) { Map<String, String> responseHeaders = response.headers; }
	 */

}
