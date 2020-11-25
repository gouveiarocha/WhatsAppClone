package com.example.whatsappclone.helper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {

    private static FirebaseAuth auth;
    private static FirebaseDatabase database;

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

    /**
     * Ret. Referencias...
     */

    //Retorna a referencia para o nó Usuarios.
    public static DatabaseReference refUsuarios(){
        database = FirebaseUtils.getDatabase();
        return database.getReference("usuarios");
    }

}
