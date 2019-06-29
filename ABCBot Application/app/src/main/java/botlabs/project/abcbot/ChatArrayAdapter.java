package botlabs.project.abcbot;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import botlabs.project.abcbot.Retrofit.Doc;
import botlabs.project.abcbot.Retrofit.Result;
import botlabs.project.abcbot.Retrofit.retroClient;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import botlabs.project.abcbot.Retrofit.API;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by yashkothari on 12/03/18.
 */

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private YouTubePlayerView video;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;
    public static final String API_KEY = "AIzaSyChxMb4B2Gs99IEAcV3rhHAQEFMemcM0y8";

    List<Result> intraResult = new ArrayList<Result>();
    JSONArray jArray = new JSONArray();



    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final ChatMessage chatMessageObj = getItem(position);
        View row = convertView;

        LayoutInflater inflater;


        if(chatMessageObj.video != null)
        {
            inflater = (LayoutInflater) chatMessageObj.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }
        else {
            inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right, parent, false);
            chatText = row.findViewById(R.id.msgr);
            chatText.setText(chatMessageObj.message);

        }else{
            if(chatMessageObj.video != null) {
                row = inflater.inflate(R.layout.left_video, parent, false);
                video = row.findViewById(R.id.Youtubeplayer);
                video.initialize(API_KEY, new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer youTubePlayer, boolean b) {

                        youTubePlayer.setPlaybackEventListener(playbackEventListener);

                        //start buffering
                        if(!b)
                        {

                            youTubePlayer.cueVideo(chatMessageObj.video);

                        }

                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

                    }
                });

                Button opt1, opt2, opt3, opt4;
                TextView question;

                question = row.findViewById(R.id.question);
                opt1 = row.findViewById(R.id.opt1);
                opt2 = row.findViewById(R.id.opt2);
                opt3 = row.findViewById(R.id.opt3);
                opt4 = row.findViewById(R.id.opt4);

                question.setText(chatMessageObj.options.get(0));
                opt1.setText(chatMessageObj.options.get(1));
                opt2.setText(chatMessageObj.options.get(2));
                opt3.setText(chatMessageObj.options.get(3));
                opt4.setText(chatMessageObj.options.get(4));


                opt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(1));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(2));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(3));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(4));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

            }
            else if(chatMessageObj.image != 0) {
                row = inflater.inflate(R.layout.left_image, parent, false);

                ImageView image = row.findViewById(R.id.image);
                Button opt1, opt2, opt3, opt4;
                TextView question;

                image.setImageDrawable(context.getResources().getDrawable(chatMessageObj.image));

                question = row.findViewById(R.id.question);
                opt1 = row.findViewById(R.id.opt1);
                opt2 = row.findViewById(R.id.opt2);
                opt3 = row.findViewById(R.id.opt3);
                opt4 = row.findViewById(R.id.opt4);

                question.setText(chatMessageObj.options.get(0));
                opt1.setText(chatMessageObj.options.get(1));
                opt2.setText(chatMessageObj.options.get(2));
                opt3.setText(chatMessageObj.options.get(3));
                opt4.setText(chatMessageObj.options.get(4));


                opt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(1));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(2));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(3));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(4));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

            }
            else if(chatMessageObj.message.equals("")&&chatMessageObj.options.get(0)!= null)
            {
                row = inflater.inflate(R.layout.left_text, parent, false);

                Button opt1, opt2, opt3, opt4;
                TextView question;

                question = row.findViewById(R.id.question);
                opt1 = row.findViewById(R.id.opt1);
                opt2 = row.findViewById(R.id.opt2);
                opt3 = row.findViewById(R.id.opt3);
                opt4 = row.findViewById(R.id.opt4);

                question.setText(chatMessageObj.options.get(0));
                opt1.setText(chatMessageObj.options.get(1));
                opt2.setText(chatMessageObj.options.get(2));
                opt3.setText(chatMessageObj.options.get(3));
                opt4.setText(chatMessageObj.options.get(4));

                opt1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(1));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(2));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(3));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

                opt4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONObject jObject = new JSONObject();
                        try {
                            jObject.put("qid",chatMessageObj.zone);
                            jObject.put("marks",chatMessageObj.marks.get(4));
                            jArray.put(jObject);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((ChatBot)chatMessageObj.context).callFromAdapter();

                    }
                });

            }
            else if(chatMessageObj.message.equals("Submit"))
            {
                row = inflater.inflate(R.layout.left_button, parent, false);

                Button submit = row.findViewById(R.id.buttonSubmit);

                submit.setOnClickListener(new View.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.KITKAT)
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onClick(View v) {
                        //call api

                        String skill = chatMessageObj.skillId;
                        int userId = Integer.parseInt(MainActivity.id);

                        try {
                            JSONArray tempArray = jArray;

                            Log.d("array1",tempArray.toString());

                            makeConnection(skill, userId, tempArray);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        jArray = new JSONArray(new ArrayList<String>());


                        Log.d("array",jArray.toString());

                        ((ChatBot)chatMessageObj.context).callFromAdapter();
                    }
                });
            }
            else
            {
                row = inflater.inflate(R.layout.left, parent, false);
                chatText = row.findViewById(R.id.msgr);
                chatText.setText(chatMessageObj.message);

            }
        }

        return row;
    }

    YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener()
    {

        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };


    private void makeConnection(String skill, int user,JSONArray result) throws JSONException {


        API api = retroClient.getApiService();
        api.sendResponse(skill, user, result).enqueue(new Callback<Doc>() {

            @Override
            public void onResponse(Call<Doc> call, Response<Doc> response) {

                if (response.isSuccessful()) {

                    if (response.code() != 200) {
                        Toast.makeText(context, "Server not responding, try again later", Toast.LENGTH_LONG).show();
                    } else {
                        Doc doc = response.body();
                        String status = doc.getStatus();
                        Toast.makeText(context, doc.getMessage(), Toast.LENGTH_SHORT).show();


                        if(status.equals("success")) {
                            Toast.makeText(context, "success", Toast.LENGTH_SHORT).show();
                            Log.d("hohoooooo", doc.getMessage());
                            //next skill

                        }
                    }
                }

                else
                {
                    Toast.makeText(context, "Server not responding, contact the admin.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<Doc> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("aaayii", t.getMessage()+"");

            }

        });

    }
}
