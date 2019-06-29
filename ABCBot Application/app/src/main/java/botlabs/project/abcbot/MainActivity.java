package botlabs.project.abcbot;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import botlabs.project.abcbot.Retrofit.API;
import botlabs.project.abcbot.Retrofit.LogInResponse;
import botlabs.project.abcbot.Retrofit.retroClient2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    Button signIn, signUp;
    EditText phoneEt, passwordEt;
    String password;
    static String id, name, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phoneEt = findViewById(R.id.phoneNumberSignIn);
        passwordEt = findViewById(R.id.passwordSignIn);
        signIn = findViewById(R.id.signInBtn);
        signUp = findViewById(R.id.signUpBtn);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = phoneEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                if(phone.equals("")) {
                    Toast.makeText(MainActivity.this, "Email can't be empty.", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(password.equals(""))
                    {
                        Toast.makeText(MainActivity.this, "Password can't be empty.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        //call api
                        makeConnection(phone,password);
                    }
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
    }

    private void makeConnection(final String phone, String password)
    {

        API api = retroClient2.getApiService();
        api.logIn(phone, password).enqueue(new Callback<LogInResponse>() {

            @Override
            public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {

                if (response.isSuccessful()) {

                    if (response.code() != 200) {
                        Toast.makeText(MainActivity.this, "Server not responding, try again later", Toast.LENGTH_LONG).show();
                    } else {
                        LogInResponse doc = response.body();
                        id = doc.getUserId();
                        try {
                            if(doc.getStatus().equals("success")) {

                                Toast.makeText(MainActivity.this, "Success signing In.", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, ChatBot.class);
                                startActivity(i);
                            }
                            else {

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                else
                {
                    Toast.makeText(MainActivity.this, "Server not responding, contact the admin.", Toast.LENGTH_LONG).show();

                }
            }

            @Override
            public void onFailure(Call<LogInResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Connection failed!!", Toast.LENGTH_SHORT).show();
            }

        });

    }

}
