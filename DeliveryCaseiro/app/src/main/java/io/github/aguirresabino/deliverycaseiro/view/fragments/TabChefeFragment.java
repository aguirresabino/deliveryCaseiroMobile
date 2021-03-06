package io.github.aguirresabino.deliverycaseiro.view.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.github.aguirresabino.deliverycaseiro.R;
import io.github.aguirresabino.deliverycaseiro.application.DeliveryApplication;
import io.github.aguirresabino.deliverycaseiro.logs.MyLogger;
import io.github.aguirresabino.deliverycaseiro.model.entities.Chefe;
import io.github.aguirresabino.deliverycaseiro.model.entities.Endereco;
import io.github.aguirresabino.deliverycaseiro.model.enums.ValuesApplicationEnum;
import io.github.aguirresabino.deliverycaseiro.model.retrofit.APIDeliveryCaseiroChefe;
import io.github.aguirresabino.deliverycaseiro.model.retrofit.APIDeliveryCaseiroRetrofitFactory;
import io.github.aguirresabino.deliverycaseiro.model.services.ChefeService;
import io.github.aguirresabino.deliverycaseiro.model.services.UsuarioService;
import io.github.aguirresabino.deliverycaseiro.view.activity.ChefeActivity;
import io.github.aguirresabino.deliverycaseiro.view.activity.UsuarioPerfilActivity;
import io.github.aguirresabino.deliverycaseiro.view.activity.LoginActivity;
import io.github.aguirresabino.deliverycaseiro.view.fragments.base.BaseFragment;
import io.github.aguirresabino.deliverycaseiro.view.helpers.ToastHelper;
import io.github.aguirresabino.deliverycaseiro.view.transform.CircleTransform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TabChefeFragment extends BaseFragment {

    @BindView(R.id.fragmentTabChefeRecyclerView) RecyclerView recyclerView;
    @BindView(R.id.fragmentTabChefeFab) FloatingActionButton fabButton;

    private LocalBroadcastReceiver localBroadcastReceiver;
    private ChefeService chefeService;
    private UsuarioService usuarioService;
    private List<Chefe> chefes;

    @Override
    public void onAttach(@NonNull Context context) {
        chefeService = new ChefeService(this.getActivity());
        usuarioService = new UsuarioService(this.getActivity());
        super.onAttach(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_chefe, container, false);
        // ButterKnife
        ButterKnife.bind(this, view);
        // O fragment define o menu da toolbar
        setHasOptionsMenu(true);
        //
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        localBroadcastReceiver = new LocalBroadcastReceiver();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(localBroadcastReceiver, new IntentFilter(LocalBroadcastReceiver.LOCAL_BROADCAST_TAB_CHEFE_FRAGMENT));
        // Carregando todos os chefes da localidade
        this.chefeService.readByCep(DeliveryApplication.usuarioLogado.getEndereco().getCep());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.fragment_tab_chefe, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_perfil:
                getActivity().startActivity(new Intent(getContext(), UsuarioPerfilActivity.class));
                break;
            case R.id.action_sair:
                DeliveryApplication.usuarioLogado = null;
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(localBroadcastReceiver);
    }

    @OnClick(R.id.fragmentTabChefeFab)
    public void fabClick() {

        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View inputsDialog = layoutInflater.inflate(R.layout.inputs_dialog_endereco, null);

        initInputs(inputsDialog);

        new AlertDialog.Builder(getContext())
                .setView(inputsDialog)
                .setTitle("Atualizar Endereço")
                .setCancelable(false)
                .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        atualizarEndereco(inputsDialog);
                        ToastHelper.toastShort(getContext(), "Endereço atualizado!");
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private ChefeRecyclerViewAdapter.CardOnClickListener onClickChefe() {
        return new ChefeRecyclerViewAdapter.CardOnClickListener() {
            @Override
            public void onClickCard(View view, int idx) {
                Intent intent = new Intent(getActivity(), ChefeActivity.class);
                intent.putExtra("chefe", chefes.get(idx));
                startActivity(intent);
            }
        };
    }

    private void initInputs(View view) {
        TextInputLayout estado = view.findViewById(R.id.inputsDialogEnderecoTextInputEstado);
        TextInputLayout cidade = view.findViewById(R.id.inputsDialogEnderecoTextInputCidade);
        TextInputLayout bairro = view.findViewById(R.id.inputsDialogEnderecoTextInputBairro);
        TextInputLayout rua = view.findViewById(R.id.inputsDialogEnderecoTextInputRua);
        TextInputLayout numero = view.findViewById(R.id.inputsDialogEnderecoTextInputNumero);
        TextInputLayout cep = view.findViewById(R.id.inputsDialogEnderecoTextInputCep);

        estado.getEditText().setText(DeliveryApplication.usuarioLogado.getEndereco().getEstado());
        cidade.getEditText().setText(DeliveryApplication.usuarioLogado.getEndereco().getCidade());
        bairro.getEditText().setText(DeliveryApplication.usuarioLogado.getEndereco().getBairro());
        rua.getEditText().setText(DeliveryApplication.usuarioLogado.getEndereco().getRua());
        numero.getEditText().setText(DeliveryApplication.usuarioLogado.getEndereco().getNumero());
        cep.getEditText().setText(DeliveryApplication.usuarioLogado.getEndereco().getCep());
    }

    private void atualizarEndereco(View view) {
        TextInputLayout estado = view.findViewById(R.id.inputsDialogEnderecoTextInputEstado);
        TextInputLayout cidade = view.findViewById(R.id.inputsDialogEnderecoTextInputCidade);
        TextInputLayout bairro = view.findViewById(R.id.inputsDialogEnderecoTextInputBairro);
        TextInputLayout rua = view.findViewById(R.id.inputsDialogEnderecoTextInputRua);
        TextInputLayout numero = view.findViewById(R.id.inputsDialogEnderecoTextInputNumero);
        TextInputLayout cep = view.findViewById(R.id.inputsDialogEnderecoTextInputCep);

        Endereco novoEndereco = new Endereco();
        novoEndereco.setEstado(estado.getEditText().getText().toString());
        novoEndereco.setCidade(cidade.getEditText().getText().toString());
        novoEndereco.setBairro(bairro.getEditText().getText().toString());
        novoEndereco.setRua(rua.getEditText().getText().toString());
        novoEndereco.setNumero(numero.getEditText().getText().toString());
        novoEndereco.setCep(cep.getEditText().getText().toString());

        DeliveryApplication.usuarioLogado.setEndereco(novoEndereco);

        this.usuarioService.update(DeliveryApplication.usuarioLogado);

        this.chefeService.readByCep(DeliveryApplication.usuarioLogado.getEndereco().getCep());
    }

    // Implementação do RecyclerView.Adapter

    static class ChefeRecyclerViewAdapter extends RecyclerView.Adapter<ChefeRecyclerViewAdapter.ListCardViewHolder> {

        private List<Chefe> chefes;
        private ChefeRecyclerViewAdapter.CardOnClickListener cardOnClickListener;

        public ChefeRecyclerViewAdapter(List<Chefe> chefes, ChefeRecyclerViewAdapter.CardOnClickListener cardOnClickListener){
            this.chefes = chefes;
            this.cardOnClickListener = cardOnClickListener;
        }

        //Cria novas views
        @NonNull
        @Override
        public ListCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            //criando nova view
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_line, parent, false);

            ListCardViewHolder listCardViewHolder = new ListCardViewHolder(view);
            return listCardViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ChefeRecyclerViewAdapter.ListCardViewHolder holder, final int position) {
            Chefe chefe = chefes.get(position);

            holder.nome.setText(chefe.getNome());
            holder.descricao.setText("Telefone: " + chefe.getTelefone());
            Picasso.get()
                    .load(chefe.getImagem())
                    .transform(new CircleTransform())
                    .into(holder.image);

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
            return chefes != null ? chefes.size() : 0;
        }

        public static class ListCardViewHolder extends RecyclerView.ViewHolder{
            @BindView(R.id.card_img) AppCompatImageView image;
            @BindView(R.id.card_nome) TextView nome;
            @BindView(R.id.card_descricao) TextView descricao;
            @BindView(R.id.card_view) CardView card;

            public ListCardViewHolder(View itemView) {
                super(itemView);
                // ButterKnife
                ButterKnife.bind(this, itemView);
            }
        }

        public interface CardOnClickListener {
            public void onClickCard(View view, int idx);
        }
    }

    public class LocalBroadcastReceiver extends BroadcastReceiver {

        public static final String LOCAL_BROADCAST_TAB_CHEFE_FRAGMENT = "local.broadcast.tab.chefe.fragment";

        @Override
        public void onReceive(Context context, Intent intent) {
            chefes = intent.getParcelableArrayListExtra("chefe.service.readByCep");
            if(chefes != null || !chefes.isEmpty()) {
                //TODO Como informar ao recyclerview que a view deve ser atualizada
                recyclerView.setAdapter(new TabChefeFragment.ChefeRecyclerViewAdapter(chefes, onClickChefe()));
                MyLogger.logInfo(ValuesApplicationEnum.MY_TAG.getValue(), TabChefeFragment.class, chefes.toString());
            } else
                ToastHelper.toastShort(getActivity(), "Nenhum chefe foi encontrado nesta localização!");
        }
    }
}
