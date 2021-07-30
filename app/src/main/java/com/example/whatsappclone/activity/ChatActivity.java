package com.example.whatsappclone.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.example.whatsappclone.model.Mensagem;
import com.example.whatsappclone.model.Usuario;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private CircleImageView circleImageViewFoto;
    private TextView textViewNome;
    private Usuario usuarioDestinatario;
    private EditText editMensagem;

    //Identificador usuario remetente e destinatario.
    private String idUsuarioRemetente;
    private String idUsuarioDestinatario;

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
        editMensagem = findViewById(R.id.editMensagem);

        //Recuperar dados do usuário remetente
        idUsuarioRemetente = FirebaseUtils.getIdUsuario();

        //Recuperar dados do usuário destinatário
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

        //Recuperar dados do usuário destinatário
        idUsuarioDestinatario = Base64Custom.codificarBase64(usuarioDestinatario.getEmail());

    }

    public void enviarMensagem(View view){

        String textoMensagem = editMensagem.getText().toString();

        if (!textoMensagem.isEmpty()){

            Mensagem mensagem = new Mensagem();
            mensagem.setIdUsuario(idUsuarioRemetente);
            mensagem.setMensagem(textoMensagem);

            //Salvar a mensagem para o remetente
            salvarMensagem(idUsuarioRemetente, idUsuarioDestinatario, mensagem);

        }else{
            Toast.makeText(this, "Digite uma mensagem para enviar", Toast.LENGTH_SHORT).show();
        }

    }

    private void salvarMensagem(String idRemetente, String idDestinatario, Mensagem msg){

        FirebaseUtils.refMensagens()
                .child(idRemetente)
                .child(idDestinatario)
                .push()
                .setValue(msg);

        editMensagem.setText("");

    }

}
