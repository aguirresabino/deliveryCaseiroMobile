package io.github.aguirresabino.deliverycaseiro.application;

import android.app.Application;

import io.github.aguirresabino.deliverycaseiro.logs.MyLogger;
import io.github.aguirresabino.deliverycaseiro.model.entities.Usuario;

public class DeliveryApplication extends Application {

    // Atributos estáticos que são utilizados por diversas outras classes
    public static final String MY_TAG = "DELIVERY_CASEIRO_DEBUG";
    public static final String URL_BASE_API_DELIVERY_CASEIRO = "https://comida-caseira.herokuapp.com/comida-caseira/";
    public static Usuario usuarioLogado;

    @Override
    public void onCreate() {
        super.onCreate();
        //
        MyLogger.logInfo(MY_TAG, getClass(), "onCreate() chamado ");
    }
}
