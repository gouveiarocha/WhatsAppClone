package com.example.whatsappclone.model;

import com.example.whatsappclone.helper.Base64Custom;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.google.firebase.database.Exclude;

public class Usuario {

    private String id, nome, email, senha;

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

    public void salvar(){
        FirebaseUtils.refUsuarios().child(this.id).setValue(this);
    }

}
