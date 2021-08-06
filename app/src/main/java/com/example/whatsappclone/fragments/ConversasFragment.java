package com.example.whatsappclone.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.whatsappclone.R;
import com.example.whatsappclone.activity.ChatActivity;
import com.example.whatsappclone.adapter.ConversasAdapter;
import com.example.whatsappclone.helper.FirebaseUtils;
import com.example.whatsappclone.helper.RecyclerItemClickListener;
import com.example.whatsappclone.model.Conversa;
import com.example.whatsappclone.model.Mensagem;
import com.example.whatsappclone.model.Usuario;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {

    private RecyclerView recyclerViewConversas;

    private List<Conversa> listaConversas = new ArrayList<>();
    private ConversasAdapter adapter;

    private DatabaseReference conversasRef;

    private ChildEventListener childEventListenerConversas;

    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);

        //Configs Iniciais
        recyclerViewConversas = view.findViewById(R.id.recyclerListaConversas);

        //Configura ConversasRef
        String idUsuario = FirebaseUtils.getIdUsuario();
        conversasRef = FirebaseUtils.refConversas().child(idUsuario);

        //Config adapter
        adapter = new ConversasAdapter(listaConversas, getActivity());

        //Config recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewConversas.setLayoutManager(layoutManager);
        recyclerViewConversas.setHasFixedSize(true);
        recyclerViewConversas.setAdapter(adapter);

        //Configurar evento de clique
        recyclerViewConversas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getActivity(), recyclerViewConversas, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Conversa conversaSelecionada = listaConversas.get(position);
                        Intent intent = new Intent(getActivity(), ChatActivity.class);
                        intent.putExtra("chatContato", conversaSelecionada.getUsuarioExibicao());
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
                )
        );

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarConversas();
    }

    @Override
    public void onStop() {
        super.onStop();
        conversasRef.removeEventListener(childEventListenerConversas);
    }

    public void pesquisarConversas(String texto){
        List<Conversa> listaConversasBusca = new ArrayList<>();
        for (Conversa conversa : listaConversas){
            String nome = conversa.getUsuarioExibicao().getNome().toLowerCase();
            String ultimaMsg = conversa.getUltimaMensagem();
            if (nome.contains(texto) || ultimaMsg.contains(texto)){
                listaConversasBusca.add(conversa);
            }
        }

        adapter = new ConversasAdapter(listaConversasBusca, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recarregarConversas(){

        adapter = new ConversasAdapter(listaConversas, getActivity());
        recyclerViewConversas.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    public void recuperarConversas() {

        childEventListenerConversas = conversasRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded( DataSnapshot dataSnapshot,  String s) {

                Conversa conversa = dataSnapshot.getValue(Conversa.class);
                listaConversas.add(conversa);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onChildRemoved( DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved( DataSnapshot dataSnapshot,  String s) {

            }

            @Override
            public void onCancelled( DatabaseError databaseError) {

            }
        });

    }

}
