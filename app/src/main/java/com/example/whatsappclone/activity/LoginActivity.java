package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.config.FirebaseUtils;
import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText editEmail, editSenha;
    private FirebaseAuth auth = FirebaseUtils.getAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editEmail = findViewById(R.id.login_etxt_email);
        editSenha = findViewById(R.id.login_etxt_senha);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //Verificar e manter o login.
        FirebaseUser usuarioAtual = auth.getCurrentUser();
        if (usuarioAtual != null){
            abrirPrincipalActivity();
        }

    }

    public void validarUsuario(View view) {

        String txtEmail = editEmail.getText().toString();
        String txtSenha = editSenha.getText().toString();

        if (!txtEmail.isEmpty()) {
            if (!txtSenha.isEmpty()) {

                Usuario usuario = new Usuario();
                usuario.setEmail(txtEmail);
                usuario.setSenha(txtSenha);

                logarUsuario(usuario);

            } else {
                Toast.makeText(LoginActivity.this, "Atenção, Preencher Senha!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(LoginActivity.this, "Atenção, Preencher Email!", Toast.LENGTH_SHORT).show();
        }

    }

    public void logarUsuario(Usuario usuario) {

        auth.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            abrirPrincipalActivity();

                        } else {

                            String excecao = "";
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthInvalidUserException e) {
                                excecao = "Atenção: Usuário não cadastrado!";
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                excecao = "Atenção: Email e/ou senha incorreto!";
                            } catch (Exception e) {
                                excecao = "Erro ao Logar: " + e.getMessage();
                                e.printStackTrace();
                            }
                            Toast.makeText(LoginActivity.this, excecao, Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }

    public void abrirCadastroActivity(View view) {
        startActivity(new Intent(LoginActivity.this, CadastroActivity.class));

    }

    public void abrirPrincipalActivity() {
        startActivity(new Intent(LoginActivity.this, PrincipalActivity.class));
    }


}
