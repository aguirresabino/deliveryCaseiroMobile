package io.github.aguirresabino.deliverycaseiro.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import io.github.aguirresabino.deliverycaseiro.R;

public class PedidoDetailActivity extends AppCompatActivity {

    private final String TAG = getClass().getName() + " ESPECIFICA ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_detail);
    }
}