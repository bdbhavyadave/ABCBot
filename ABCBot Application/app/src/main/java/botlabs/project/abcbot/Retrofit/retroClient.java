package botlabs.project.abcbot.Retrofit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import botlabs.project.abcbot.MainActivity;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retroClient {

    private static final String ROOT_URL = "http://vesaithonapi2.000webhostapp.com";

    private static Retrofit getRetrofitInstance() {

        return new Retrofit.Builder()
                .baseUrl(ROOT_URL)
                .addConverterFactory(StringConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static API getApiService() {
        return getRetrofitInstance().create(API.class);
    }
}
