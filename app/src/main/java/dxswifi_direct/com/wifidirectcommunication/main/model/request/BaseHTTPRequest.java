package dxswifi_direct.com.wifidirectcommunication.main.model.request;

import java.util.List;

/**
 * Created by Deepak Sharma on 4/7/16.
 * This is a HTTP request class which has the basic parameters.
 * If you wants to add some more parameters, please make a subclass of that class
 * and add with your subclass. Don't modify this class.
 */
public class BaseHTTPRequest<T> {

    private String URL;
    private int requestCode;
    private List<T> listParameters;
    private String header;

    public void setURL(String URL) {
        this.URL = URL;
    }
    public String getURL() {
        return URL;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }

    public List<T> getListParameters() {
        return listParameters;
    }

    public void setListParameters(List<T> listParameters) {
        this.listParameters = listParameters;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}
