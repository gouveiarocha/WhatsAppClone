package com.example.whatsappclone.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.example.whatsappclone.model.Mensagem;

import java.util.List;

public class MensagensAdapter extends RecyclerView.Adapter<MensagensAdapter.MyViewHolder> {

    private List<Mensagem> mensagens;
    private Context context;

    private static final int TIPO_REMETENTE = 0;
    private static final int TIPO_DESTINATARIO = 1;

    public MensagensAdapter(List<Mensagem> lista, Context c) {
        this.mensagens = lista;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View item = null;
        if (viewType == TIPO_REMETENTE){
            item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_mensagem_remetente, viewGroup, false);
        }else if (viewType == TIPO_DESTINATARIO){
            item = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_mensagem_destinatario, viewGroup, false);
        }

        return new MyViewHolder(item);

    }

    @Override
    public void onBindViewHolder(@NonNull MensagensAdapter.MyViewHolder myViewHolder, int position) {

        Mensagem mensagem = mensagens.get(position);

        String msg = mensagem.getMensagem();
        String imagem = mensagem.getImagem();

        if (imagem != null){
            Uri url = Uri.parse(imagem);
            Glide.with(context).load(url).into(myViewHolder.imagem);
            //Esconder Texto
            myViewHolder.mensagem.setVisibility(View.GONE);
        }else{
            myViewHolder.mensagem.setText(msg);
            //Esconder imagem
            myViewHolder.imagem.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mensagens.size();
    }

    @Override
    public int getItemViewType(int position) {

        Mensagem mensagem = mensagens.get(position);
        String idUsuario = FirebaseUtils.getIdUsuario();

        if (idUsuario.equals(mensagem.getIdUsuario())){
            return TIPO_REMETENTE;
        }
        return TIPO_DESTINATARIO;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView mensagem;
        private ImageView imagem;
        public MyViewHolder(View itemView){
            super(itemView);
            mensagem = itemView.findViewById(R.id.textMensagemTexto);
            imagem = itemView.findViewById(R.id.imageMensagemFoto);
        }
    }

}
