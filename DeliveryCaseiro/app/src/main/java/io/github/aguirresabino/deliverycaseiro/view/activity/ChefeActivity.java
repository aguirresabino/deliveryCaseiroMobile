package io.github.aguirresabino.deliverycaseiro.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.aguirresabino.deliverycaseiro.R;
import io.github.aguirresabino.deliverycaseiro.view.activity.base.BaseActivity;

public class ChefeActivity extends BaseActivity {

    @BindView(R.id.activityChefeRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.activityChefeCollapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chefe);
        //
        ButterKnife.bind(this);
        //
        setUpToolbar(toolbar);
        //
        collapsingToolbarLayout.setTitle("Nome do Chefe");
        //collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        //utilizando botão voltar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //configurando recyclerview
        setUpRecyclerView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //se clicar no botão voltar, a activity atual é finalizada
        if(id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ListCardAdapter(getResources().getStringArray(R.array.testeChefeActivity), this.onClickPrato()));
    }

    private ListCardAdapter.CardOnClickListener onClickPrato(){
        return new ListCardAdapter.CardOnClickListener() {
            @Override
            public void onClickCard(View view, int idx) {
                //String item = getResources().getStringArray(R.array.teste)[idx];
                //Toast.makeText(getBaseContext(), "Clicou em " + item, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getBaseContext(), PratoPedidoActivity.class);
                startActivity(intent);
            }
        };
    }

    static class ListCardAdapter extends RecyclerView.Adapter<ListCardAdapter.ListCardViewHolder> {

        private String[] dataSet;
        private ListCardAdapter.CardOnClickListener cardOnClickListener;

        public ListCardAdapter(String[] dataSet, ListCardAdapter.CardOnClickListener cardOnClickListener){
            this.dataSet = dataSet;
            this.cardOnClickListener = cardOnClickListener;
        }

        //Cria novas views
        @NonNull
        @Override
        public ListCardAdapter.ListCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //criando nova view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_line, parent, false);

            ListCardAdapter.ListCardViewHolder listCardViewHolder = new ListCardAdapter.ListCardViewHolder(view);
            return listCardViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ListCardAdapter.ListCardViewHolder holder, final int position) {
            String elem = dataSet[position];

            holder.nome.setText(elem);
            holder.descricao.setText("Descrição do " + elem);
            //holder.image.setImageResource(Integer.parseInt(holder.itemView.getResources().getResourceName(R.mipmap.ic_launcher_round)));

            if(cardOnClickListener != null){
                holder.itemView.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        cardOnClickListener.onClickCard(holder.itemView, position);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return dataSet != null ? dataSet.length : 0;
        }

        public static class ListCardViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.card_img) ImageView image;
            @BindView(R.id.card_nome) TextView nome;
            @BindView(R.id.card_descricao) TextView descricao;
            @BindView(R.id.card_view) CardView card;

            public ListCardViewHolder(View itemView) {
                super(itemView);
                //
                ButterKnife.bind(this, itemView);
            }
        }

        public interface CardOnClickListener {
            void onClickCard(View view, int idx);
        }
    }
}
