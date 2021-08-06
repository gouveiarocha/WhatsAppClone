package com.example.whatsappclone.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.example.whatsappclone.fragments.ContatosFragment;
import com.example.whatsappclone.fragments.ConversasFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import java.util.ArrayList;
import java.util.List;

public class PrincipalActivity extends AppCompatActivity {

    private FirebaseAuth auth = FirebaseUtils.getAuth();
    private MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        
        Toolbar toolbar = findViewById(R.id.toolbar_principal);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        //Configurar abas
        final FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Conversas", ConversasFragment.class)
                .add("Contatos", ContatosFragment.class)
                .create()
        );

        ViewPager viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagertab = findViewById(R.id.viewPagerTab);
        viewPagertab.setViewPager(viewPager);

        //Config SearchView
        searchView = findViewById(R.id.materialSeachPrincipal);

        //Listener para o SearchView
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

            }

            @Override
            public void onSearchViewClosed() {
                ConversasFragment conversasFragment = (ConversasFragment) adapter.getPage(0);
                conversasFragment.recarregarConversas();

            }
        });

        //Listener para caixa de texto
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(PrincipalActivity.this, "entrei na pesquisa", Toast.LENGTH_SHORT).show();
                ConversasFragment fragment = (ConversasFragment) adapter.getPage(0);
                if (newText != null && !newText.isEmpty()){
                    fragment.pesquisarConversas(newText.toLowerCase());
                }
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        //Configurar botao de pesquisa
        MenuItem item = menu.findItem(R.id.menu_pesquisa);
        searchView.setMenuItem(item);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_pesquisa:
                break;
            case R.id.menu_config:
                abrirConfiguracoes();
                break;
            case R.id.menu_sair:
                deslogarUsuario();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void deslogarUsuario(){
        try {
            auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void abrirConfiguracoes(){
        startActivity(new Intent(PrincipalActivity.this, ConfiguracoesActivity.class));
    }

}
