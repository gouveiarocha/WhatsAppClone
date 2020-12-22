package com.example.whatsappclone.model;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Usuario implements Serializable {

    private String id, nome, email, senha, foto;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.id = Base64Custom.codificarBase64(email);
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void salvar(){
        FirebaseUtils.refUsuarios().child(this.id).setValue(this);
    }

    public void atualizar(){

        Map<String, Object> valoresUsuario = converterParaMap();
        FirebaseUtils.refUsuarios().child(FirebaseUtils.getIdUsuario()).updateChildren(valoresUsuario);

    }

    @Exclude
    public Map<String, Object> converterParaMap(){
        HashMap <String, Object> usuarioMap = new HashMap<>();

        usuarioMap.put("nome", getNome());
        usuarioMap.put("email", getEmail());
        usuarioMap.put("foto", getFoto());

        return usuarioMap;
    }

}
