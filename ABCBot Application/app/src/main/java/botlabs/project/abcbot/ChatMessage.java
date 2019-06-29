package botlabs.project.abcbot;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import java.util.List;

import botlabs.project.abcbot.Retrofit.StringConverterFactory;

/**
 * Created by yashkothari on 12/03/18.
 */

public class ChatMessage {
    public boolean left;
    public String message;
    public String video;
    public Context context;
    List<String> options;
    List<Integer> marks;
    public int image;
    public String zone;
    public String skillId;

    public ChatMessage(boolean left, String message, String video, Context context, List<String> options,  List<Integer> marks, String zone) {
        super();
        this.left = left;
        this.message = message;
        this.video = video;
        this.context = context;
        this.options = options;
        this.marks = marks;
        this.zone = zone;
    }

    public ChatMessage(boolean left, String message, int image, Context context, List<String> options,  List<Integer> marks, String zone) {
        super();
        this.left = left;
        this.message = message;
        this.image = image;
        this.context = context;
        this.options = options;
        this.marks = marks;
        this.zone = zone;

    }

    public ChatMessage(boolean left, String message, Context context, String zone, String skillId) {
        super();
        this.left = left;
        this.message = message;
        this.context = context;
        this.zone = zone;
        this.skillId = skillId;

    }

    public ChatMessage(boolean left, String message, Context context,List<String> options, List<Integer> marks, String zone) {
        super();
        this.left = left;
        this.message = message;
        this.context = context;
        this.options = options;
        this.marks = marks;
        this.zone = zone;
    }

}

