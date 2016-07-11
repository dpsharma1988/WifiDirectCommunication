package dxswifi_direct.com.wifidirectcommunication.main.model.response.facebook;

/**
 * Created by Deepak Sharma on 8/7/16.
 */
public class UserModel {

    private String id;
    private String name;
    private Picture picture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
