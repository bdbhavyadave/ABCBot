package botlabs.project.abcbot.Retrofit;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("qid")
    @Expose
    private String qId;

    @SerializedName("marks")
    @Expose
    private int marks;

    public Result(String qId, int marks) {
        this.qId = qId;
        this.marks = marks;
    }

    public String getqId() {
        return qId;
    }

    public void setqId(String qId) {
        this.qId = qId;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }
}
