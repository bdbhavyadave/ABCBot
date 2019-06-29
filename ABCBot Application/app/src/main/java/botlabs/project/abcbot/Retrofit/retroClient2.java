package botlabs.project.abcbot.Retrofit;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class retroClient2 {

    private static final String ROOT_URL = "http://vesaithonapi.000webhostapp.com";

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
