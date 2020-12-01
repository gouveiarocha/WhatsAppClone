package com.example.whatsappclone.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.whatsappclone.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseUtils {

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;

    private static FirebaseStorage storage;
    private static StorageReference storageReference;

    /**
     * Ret. Instancias...
     */

    //Retorna a instancia do FirebaseAuth
    public static FirebaseAuth getAuth(){
        if (auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }

    //Retorna a instancia do FirebaseDatabase
    public static FirebaseDatabase getDatabase(){
        if (database == null){
            database = FirebaseDatabase.getInstance();
        }
        return database;
    }

    //Retorna a instancia do FirebaseStorage
    public static FirebaseStorage getStorage(){
        if (storage == null){
            storage = FirebaseStorage.getInstance();
        }
        return storage;
    }

    //Retorna a instancia do FirebaseStorage
    public static StorageReference getStorageReference(){
        if (storageReference == null){
            storageReference = FirebaseStorage.getInstance().getReference();
        }
        return storageReference;
    }

    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth auth = FirebaseUtils.getAuth();
        return auth.getCurrentUser();
    }

    /**
     * Ret. Referencias...
     */

    //Retorna a referencia para o nó Usuarios.
    public static DatabaseReference refUsuarios(){
        database = FirebaseUtils.getDatabase();
        return database.getReference("usuarios");
    }

    /**
     * Ret. Dados...
     */

    //Retorna (Email/ID) do Usuario Logado
    public static String getIdUsuario(){
        auth = FirebaseUtils.getAuth();
        return Base64Custom.codificarBase64(auth.getCurrentUser().getEmail());
    }

    //Retorna o Usuario logado.
    public static Usuario getDadosUsuarioLogado(){

        FirebaseUser firebaseUser = FirebaseUtils.getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setEmail(firebaseUser.getEmail());

        if (firebaseUser.getPhotoUrl() == null){
            usuario.setFoto("");
        }else{
            usuario.setFoto(firebaseUser.getPhotoUrl().toString());
        }

        return usuario;
    }


    /**
     * Manipular Dados FirebaseUser...
     */

    //Atualiza o nome do Usuario no FirbaseUser.
    public static boolean atualizarNomeUsuario(String nome){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar o nome de perfil...");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

    //Manipula a foto de perfil interna do Firebase no FirebaseUser
    public static boolean atualizarFotoUsuario(Uri url){

        try {

            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()){
                        Log.d("Perfil", "Erro ao atualizar a foto de perfil...");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }

    }

}
