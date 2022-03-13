package com.example.whatsappclone.activity;

import android.os.Bundle;

import com.example.whatsappclone.adapter.GrupoSelecionadoAdapter;
import com.example.whatsappclone.model.Usuario;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.whatsappclone.R;

import java.util.ArrayList;
import java.util.List;

public class CadastroGrupoActivity extends AppCompatActivity {

    private List<Usuario> listaMembrosSelecionados = new ArrayList<>();
    private TextView txtTotalParticipantes;
    private GrupoSelecionadoAdapter grupoSelecionadoAdapter;
    private RecyclerView recyclerMembrosSelecionados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_grupo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Novo Grupo");
        toolbar.setSubtitle("Defina o nome");
        setSupportActionBar(toolbar);

        txtTotalParticipantes = findViewById(R.id.textTotalParticipantes);
        recyclerMembrosSelecionados = findViewById(R.id.recyclerMembrosGrupo);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Recuperar a lista de membros selecionados.
        if (getIntent().getExtras() != null) {
            List<Usuario> membros = (List<Usuario>) getIntent().getExtras().getSerializable("membros");
            listaMembrosSelecionados.addAll(membros);

            txtTotalParticipantes.setText("Participantes: " + listaMembrosSelecionados.size());

        }

        //Configurar recyclcerview
        //Configurar adapter Lista Membros Selecionados
        grupoSelecionadoAdapter = new GrupoSelecionadoAdapter(listaMembrosSelecionados, getApplicationContext());

        //Configurar RecyclerView Lista Membros Selecionados
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(
                getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerMembrosSelecionados.setLayoutManager(layoutManager2);
        recyclerMembrosSelecionados.setHasFixedSize(true);
        recyclerMembrosSelecionados.setAdapter(grupoSelecionadoAdapter);

    }

}