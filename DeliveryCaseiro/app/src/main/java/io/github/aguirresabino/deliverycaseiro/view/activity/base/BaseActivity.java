package io.github.aguirresabino.deliverycaseiro.view.activity.base;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.github.aguirresabino.deliverycaseiro.application.DeliveryApplication;
import io.github.aguirresabino.deliverycaseiro.logs.MyLogger;
import io.github.aguirresabino.deliverycaseiro.view.fragments.base.BaseFragment;

/**
 * BaseActivity é um objeto que implementa o LifeCycle da Activity e outras lógicas
 * que serão herdadas por Activities da aplicação (reuso).
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    public void onAttachedToWindow() {
        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onAttachedToWindow() chamado");
        super.onAttachedToWindow();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onCreate() chamado " + savedInstanceState);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();

        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onStart() chamado ");
    }

    @Override
    protected void onResume() {
        super.onResume();

        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onResume() chamado ");
    }

    @Override
    protected void onPause() {
        super.onPause();

        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onPause() chamado ");
    }

    @Override
    protected void onStop() {
        super.onStop();

        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onStop() chamado ");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onDestroy() chamado ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onRestart() chamado ");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), " onSaveInstanceState() chamado " + outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        MyLogger.logInfo(DeliveryApplication.MY_TAG, getClass(), "onRestoreInstanceState() chamado " + savedInstanceState);

        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * Este método auxilia a troca de fragments dentro de uma Activity.
     * @param fragment Fragment que deve ser exibido na tela.
     */
    protected void replaceFragment(BaseFragment fragment, int containerViewId){

        getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    /**
     * Este método adiciona uma toolbar a activity do contexto.
     * @param toolbar O layout da toolbar
     */
    public void setUpToolbar(Toolbar toolbar){
        if(toolbar != null) {
            this.setSupportActionBar(toolbar);
        }
    }
}
