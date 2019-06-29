package botlabs.project.abcbot;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.ibm.watson.developer_cloud.assistant.v1.Assistant;
import com.ibm.watson.developer_cloud.assistant.v1.model.InputData;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageOptions;
import com.ibm.watson.developer_cloud.assistant.v1.model.MessageResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChatBot extends YouTubeBaseActivity {


    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ImageView sendButton,mic;
    private EditText chatText;
    private ListView messageListView;
    private boolean side = true;
    final int REQ_CODE_SPEECH_INPUT=0;
    Assistant service;
    MessageOptions options;
    MessageResponse response;
    int flag =0, otFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);

        mic = findViewById(R.id.mic);
        chatText =  findViewById(R.id.chatText);
        sendButton =  findViewById(R.id.sendButton);
        messageListView =  findViewById(R.id.messageListView);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        messageListView.setAdapter(chatArrayAdapter);

        service = new Assistant("2018-02-16");
        service.setUsernameAndPassword("459a58fb-1f1c-4299-b6ef-c0007fcad29d", "ybIAJLeUxqwW");

            sendChatMessageFromTheCall("Hi! \nI'm ABC Bot and we are going to proceed in a fun way!","");
            sendChatMessageFromTheCall("We will play some fun games to find out the best in you.","");

        {
            InputData input = new InputData.Builder("intra ques1").build();
            options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                    .input(input)
                    .build();
            new AsyncCaller().execute();
            flag = 1;
        }






        chatText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(chatText.getText().toString().length()>0){
                    mic.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                }
                else{
                    mic.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(chatText.getText().toString().length()>0){
                    mic.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                }
                else{
                    mic.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(chatText.getText().toString().length()>0){
                    mic.setVisibility(View.GONE);
                    sendButton.setVisibility(View.VISIBLE);
                }
                else{
                    mic.setVisibility(View.VISIBLE);
                    sendButton.setVisibility(View.GONE);
                }
            }

        });

        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessageToTheScreen();
                }
                return false;
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(chatText.getText().toString().length()>0) {
                    sendChatMessageToTheScreen();
                }
            }
        });

        messageListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        messageListView.setAdapter(chatArrayAdapter);


        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptSpeechInput();
            }
        });

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                messageListView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

    }

    private boolean sendChatMessageToTheScreen() {

        otFlag = 1;
        chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString(),ChatBot.this,"",""));
        String data = chatText.getText().toString().trim();

        InputData input = new InputData.Builder(data).build();
        options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                .input(input)
                .build();
        new AsyncCaller().execute();
        chatText.setText("");

        return true;
    }





    private boolean sendChatMessageFromTheCall(String reply, String skillId) {
        chatArrayAdapter.add(new ChatMessage(!side,reply, ChatBot.this, "",skillId ));
        return true;
    }

    private boolean sendChatMessageFromTheCall(String reply, String video, List<String> options, List<Integer> marks, String zone) {
        chatArrayAdapter.add(new ChatMessage(!side,reply,video, ChatBot.this, options,marks,zone));
        return true;
    }

    private boolean sendChatMessageFromTheCall(String reply, int image, List<String> options, List<Integer> marks, String zone) {
        chatArrayAdapter.add(new ChatMessage(!side,reply,image, ChatBot.this, options,marks,zone));
        return true;
    }

    private boolean sendChatMessageFromTheCall(String reply, List<String> options, List<Integer> marks, String zone) {
        chatArrayAdapter.add(new ChatMessage(!side,reply, ChatBot.this, options,marks, zone));
        return true;
    }

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak Up");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),"speech_not_supported", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if ((resultCode == RESULT_OK) && (null != data)) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    chatText.setText(result.get(0));
                    if(chatText.getText().toString().length()>0){
                        sendChatMessageToTheScreen();
                        chatText.setText("");
                    }
                }
                break;

            }
        }

    }

    @Override
    public void onBackPressed() {

    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Void doInBackground(Void... params) {

            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here
            try {
                response = service.message(options).execute();

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread

            for(int i = 0; i< response.getOutput().getText().size(); i++) {
                String res = response.getOutput().getText().get(i);
                System.out.println(res);
                sendChatMessageFromTheCall(res,"");
            }

            if(flag == 1 && otFlag == 0)
            {
                {
                    sendChatMessageFromTheCall("Let's watch a video and then answer some questions.","");
                    List<String> options = new ArrayList<String>();
                    List<Integer> marks = new ArrayList<Integer>();

                    marks.add(0);
                    marks.add(5);
                    marks.add(10);
                    marks.add(2);
                    marks.add(7);

                    options.add("What will be your reaction on such scenario?");
                    options.add("Lie");
                    options.add("Tell truth");
                    options.add("will not feel guilty about it");
                    options.add("will feel guilty but will not tell truth");
                    sendChatMessageFromTheCall("", ("dlflr5b5VgQ"), options, marks, "intra1");

                }

                {
                    InputData input = new InputData.Builder("intra ques2").build();
                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                            .input(input)
                            .build();
                    flag = 2;
                }
            }

            else if(flag == 2 && otFlag == 0) {

                {
                    List<String> options = new ArrayList<String>();
                    List<Integer> marks = new ArrayList<Integer>();

                marks.add(0);
                marks.add(5);
                marks.add(7);
                marks.add(10);
                marks.add(2);

                options.add("John has 3 cookies but two of them are poisoned, choose the good one.");
                options.add("1");
                options.add("2");
                options.add("3");
                options.add("all are poisoned.");
                sendChatMessageFromTheCall("", R.drawable.cookie, options, marks, "intra2");
            }

                {
                    InputData input = new InputData.Builder("intra ques3").build();
                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                            .input(input)
                            .build();
                    flag = 3;
                }
            }

            else if(flag == 3 && otFlag == 0) {

                {
                    List<String> options = new ArrayList<String>();
                    List<Integer> marks = new ArrayList<Integer>();

                    marks.add(0);
                    marks.add(10);
                    marks.add(5);
                    marks.add(7);
                    marks.add(2);

                    options.add("Ram is new in your class and you want to make him your friend. What will be your action?");
                    options.add("you will go and talk");
                    options.add("you will not go fearing of spoiling first impression");
                    options.add("you will prepare first then go to talk to him");
                    options.add("you decide to cancel the idea and wait for him to talk");
                    sendChatMessageFromTheCall("", options, marks, "intra3");
                }

                {
                    InputData input = new InputData.Builder("intra ques6").build();
                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                            .input(input)
                            .build();
                    flag = 4;
                }
            }
            else if(flag == 4 && otFlag == 0) {


                sendChatMessageFromTheCall("Submit","intrapersonal_score");

                InputData input = new InputData.Builder("natural ques1").build();
                options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                        .input(input)
                        .build();

                flag = 5;


            }
            else if(flag == 5 && otFlag == 0)
            {
                {
                    List<String> options = new ArrayList<String>();
                    List<Integer> marks = new ArrayList<Integer>();

                    marks.add(0);
                    marks.add(7);
                    marks.add(5);
                    marks.add(10);
                    marks.add(2);

                    options.add("where you want to go out?");
                    options.add("beach");
                    options.add("mall");
                    options.add("camping");
                    options.add("movie");
                    sendChatMessageFromTheCall("", options, marks, "natural1");
                }

                {
                    InputData input = new InputData.Builder("natural ques2").build();
                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                            .input(input)
                            .build();
                    flag = 6;
                }
            }
            else if(flag == 6 && otFlag == 0)
            {

                {
                    List<String> options = new ArrayList<String>();
                    List<Integer> marks = new ArrayList<Integer>();

                    marks.add(0);
                    marks.add(10);
                    marks.add(7);
                    marks.add(5);
                    marks.add(2);

                    options.add("What are your first thought about this image?");
                    options.add("Dogs should be in every house.");
                    options.add("Having a dog is good but not necessary.");
                    options.add("Buying a video game is much better.");
                    options.add("Buying a dog is waste of money.");
                    sendChatMessageFromTheCall("", R.drawable.dog, options, marks, "natural2");
                }

                InputData input = new InputData.Builder("natural ques3").build();
                options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                        .input(input)
                        .build();
                flag = 7;
            }
            else
                if(flag == 7 && otFlag == 0)
                {
                    {
                        List<String> options = new ArrayList<String>();
                        List<Integer> marks = new ArrayList<Integer>();

                        marks.add(0);
                        marks.add(2);
                        marks.add(7);
                        marks.add(5);
                        marks.add(10);

                        options.add(" do you want to play holi with animals?");
                        options.add("yes it will be fun");
                        options.add("does not sounds much great");
                        options.add("no animals are scary");
                        options.add("no animals might feel allurgic after it");
                        sendChatMessageFromTheCall("", options, marks, "natural1");
                    }

                    {
                        InputData input = new InputData.Builder("natural ques6").build();
                        options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                                .input(input)
                                .build();
                        flag = 8;
                    }
                }
                else if(flag == 8 && otFlag == 0)
                {
                    sendChatMessageFromTheCall("Submit","naturalistic_score");

                    InputData input = new InputData.Builder("logical ques1").build();
                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                            .input(input)
                            .build();

                    flag = 9;
                }
                else if(flag == 9 && otFlag == 0)
                {
                    {
                        List<String> options = new ArrayList<String>();
                        List<Integer> marks = new ArrayList<Integer>();

                        marks.add(0);
                        marks.add(2);
                        marks.add(2);
                        marks.add(10);
                        marks.add(2);

                        options.add("What is this shape?");
                        options.add("Rectangle");
                        options.add("Rhombus");
                        options.add("Triangle");
                        options.add("Circle");
                        sendChatMessageFromTheCall("", R.drawable.triangle, options, marks, "logical1");
                    }

                    InputData input = new InputData.Builder("logical ques2").build();
                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                            .input(input)
                            .build();
                    flag = 10;
                }
                else if(flag == 10 && otFlag == 0)
                {
                    {
                        List<String> options = new ArrayList<String>();
                        List<Integer> marks = new ArrayList<Integer>();

                        marks.add(0);
                        marks.add(2);
                        marks.add(10);
                        marks.add(2);
                        marks.add(2);

                        options.add("Select the odd one out.");
                        options.add("Football");
                        options.add("Carrom");
                        options.add("Hockey");
                        options.add("Cricket");
                        sendChatMessageFromTheCall("", options, marks, "logical2");
                    }

                    {
                        InputData input = new InputData.Builder("logical ques3").build();
                        options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                                .input(input)
                                .build();
                        flag = 11;
                    }
                }  else if(flag == 11 && otFlag == 0)
                {
                    {
                        List<String> options = new ArrayList<String>();
                        List<Integer> marks = new ArrayList<Integer>();

                        marks.add(0);
                        marks.add(2);
                        marks.add(2);
                        marks.add(2);
                        marks.add(10);

                        options.add("Statements :(i) : Earth is smaller than Moon. \n" +
                                "            (ii) : Moon is bigger than Sun.\n" +
                                "\n" +
                                "Conclusions:   (i) : Sun is bigger than Earth.\n" +
                                "            (ii) : Earth and Sun are equal.");
                        options.add(" Only conclusion I follows");
                        options.add("Only conclusion II follows");
                        options.add("Both conclusion I and II follow");
                        options.add("Neither conclusion I nor II follows");

                        sendChatMessageFromTheCall("", options, marks, "logical2");
                    }

                    {
                        InputData input = new InputData.Builder("logical ques6").build();
                        options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
                                .input(input)
                                .build();
                        flag = 12;
                    }
                }else if(flag == 12 && otFlag == 0)
                {
                    sendChatMessageFromTheCall("Submit","logical_score");

//                    InputData input = new InputData.Builder("logical ques1").build();
//                    options = new MessageOptions.Builder("bcde8362-24ea-4a0b-a6dc-1ff4b7936a2a")
//                            .input(input)
//                            .build();

                    flag = 9;
                }



                    otFlag = 0;
        }

        }

        public void callFromAdapter()
        {
            new AsyncCaller().execute();
        }

    }

