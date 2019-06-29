package botlabs.project.abcbot.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Doc {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("msg")
    @Expose
    private String message;

    @SerializedName("userid")
    @Expose
    private String userId;

    public Doc(String status, String message, String userId) {
        this.status = status;
        this.message = message;
        this.userId = userId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
