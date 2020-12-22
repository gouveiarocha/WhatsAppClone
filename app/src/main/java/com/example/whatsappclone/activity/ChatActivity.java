package com.example.whatsappclone.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView circleImageViewFoto;
    private TextView textViewNome;
    private Usuario usuarioDestinatario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Config. iniciais
        circleImageViewFoto = findViewById(R.id.circleImageFotoChat);
        textViewNome = findViewById(R.id.textViewNomeChat);

        //Recuperar dados do usuário selecionado
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            usuarioDestinatario = (Usuario) bundle.getSerializable("chatContato");

            String foto = usuarioDestinatario.getFoto();
            if (foto !=null ){
                Uri url = Uri.parse(usuarioDestinatario.getFoto());
                Glide.with(ChatActivity.this).load(url).into(circleImageViewFoto);
            }else{
                circleImageViewFoto.setImageResource(R.drawable.default_profile);
            }

            textViewNome.setText(usuarioDestinatario.getNome());
        }

    }

}
