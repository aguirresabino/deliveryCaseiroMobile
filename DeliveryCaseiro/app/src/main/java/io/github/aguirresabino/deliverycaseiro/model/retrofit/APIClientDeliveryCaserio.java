package io.github.aguirresabino.deliverycaseiro.model.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClientDeliveryCaserio {
    private static Retrofit retrofit = null;

    private static final String BASE_URL = "https://comida-caseira.herokuapp.com/comida-caseira/";

    public static void initClient() {

        if(retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder().build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }

//        return retrofit;
    }

    public static APIDeliveryCaseiroUsuario getApiDeliveryCaseiroUsuario() {
        initClient();
        return retrofit.create(APIDeliveryCaseiroUsuario.class);
    }

    public static APIDeliveryCaseiroChefe getApiDeliveryCaseiroChefe() {
        initClient();
        return retrofit.create(APIDeliveryCaseiroChefe.class);
    }
}
