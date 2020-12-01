package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.example.whatsappclone.helper.Permissao;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class ConfiguracoesActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageView imageViewProfile;
    private ImageView imgEditarNomeUsuario;
    private EditText editNomeUsuario;
    private ImageButton btnProfileCamera, btnProfileGaleria;
    private static final int SELECAO_CAMERA = 100, SELECAO_GALERIA = 200;
    private StorageReference storageReference;
    private String identificadorUsuario;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        //Configurações Iniciais
        storageReference = FirebaseUtils.getStorageReference();
        identificadorUsuario = FirebaseUtils.getIdUsuario();
        usuarioLogado = FirebaseUtils.getDadosUsuarioLogado();

        //Validar permissoes
        Permissao.valiarPermissoes(permissoesNecessarias, this, 1);

        imageViewProfile = findViewById(R.id.image_profile);
        imgEditarNomeUsuario = findViewById(R.id.img_editar_nome);
        btnProfileCamera = findViewById(R.id.btn_profile_camera);
        btnProfileGaleria = findViewById(R.id.btn_profile_galeria);
        editNomeUsuario = findViewById(R.id.etxt_nome_usuario);

        Toolbar toolbar = findViewById(R.id.toolbar_principal);
        toolbar.setTitle("Configurações");
        setSupportActionBar(toolbar);

        //Habilita o botão voltar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar dados do Usuario
        final FirebaseUser usuario = FirebaseUtils.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();
        if (url != null){
            Glide.with(ConfiguracoesActivity.this)
                    .load(url)
                    .into(imageViewProfile);
        }else{
            imageViewProfile.setImageResource(R.drawable.default_profile);
        }

        editNomeUsuario.setText(usuario.getDisplayName());

        //Botão de ação para capturar a foto com a camera.
        btnProfileCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) { //aula 227 minuto 19.
                    startActivityForResult(intent, SELECAO_CAMERA);
                }

            }
        });

        //Botão de ação para escolher a foto a partir da galeria.
        btnProfileGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (intent.resolveActivity(getPackageManager()) != null) { //aula 227 minuto 19.
                    startActivityForResult(intent, SELECAO_GALERIA);
                }

            }
        });
        
        imgEditarNomeUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                String nome = editNomeUsuario.getText().toString();
                boolean retorno = FirebaseUtils.atualizarNomeUsuario(nome);
                if (retorno) {

                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();

                    Toast.makeText(ConfiguracoesActivity.this, "Nome alterado com sucesso...", Toast.LENGTH_SHORT).show();
                }
                
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                        break;
                }
                if (imagem != null) {

                    //Setar a imagem na interface.
                    imageViewProfile.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o Firebas
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Salvar imagem no Firebase AULA
                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(identificadorUsuario)
                            .child("perfil.jpeg");

                    UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this, "Erro ao gravar imagem no Firebase", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ConfiguracoesActivity.this, "Sucesso ao gravar imagem no Firebase", Toast.LENGTH_SHORT).show();

                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    atualizaFotoUsuario(url);
                                }
                            });

                        }
                    });
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    public void atualizaFotoUsuario(Uri url) {
        boolean retorno = FirebaseUtils.atualizarFotoUsuario(url);
        if (retorno){
            usuarioLogado.setFoto(url.toString());
            usuarioLogado.atualizar();
            Toast.makeText(this, "Sua Foto foi alterada...", Toast.LENGTH_SHORT).show();
        }
        
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o App, é necessário aceitar as permissões...");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
