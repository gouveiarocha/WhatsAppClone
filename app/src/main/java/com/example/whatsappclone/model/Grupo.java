package com.example.whatsappclone.model;

import com.example.whatsappclone.helper.FirebaseUtils;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.List;

public class Grupo implements Serializable {
    private String id;
    private String nome;
    private String foto;
    private List<Usuario> membros;

    public Grupo() {

        String idGrupoFirebase = FirebaseUtils.refGrupos().push().getKey();
        setId(idGrupoFirebase);

    }

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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Usuario> getMembros() {
        return membros;
    }

    public void setMembros(List<Usuario> membros) {
        this.membros = membros;
    }

}
