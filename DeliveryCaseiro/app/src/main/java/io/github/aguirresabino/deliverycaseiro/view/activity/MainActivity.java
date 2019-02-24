package io.github.aguirresabino.deliverycaseiro.view.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import io.github.aguirresabino.deliverycaseiro.R;
import io.github.aguirresabino.deliverycaseiro.view.activity.base.BaseActivity;
import io.github.aguirresabino.deliverycaseiro.view.fragments.InitialFragment;

public class MainActivity extends BaseActivity {

    private Fragment fragmentChild;

    @BindView(R.id.toolbar) @Nullable Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Inicializando o fragment InitialFragment
        if(savedInstanceState == null){
            fragmentChild = new InitialFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainActivityfragmentContainer, fragmentChild)
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpNavigationDrawer(toolbar);
    }
}