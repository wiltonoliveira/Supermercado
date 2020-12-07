package com.example.supermercado;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Produto.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DAO ProdutoDAO();
    private static AppDatabase instancia;

    public static AppDatabase getInstance(Context context) {
        if (instancia == null) {
            instancia = Room.databaseBuilder(context, AppDatabase.class, "lista-produtos-db")
                    .allowMainThreadQueries().fallbackToDestructiveMigration().build();
        }
        return instancia;
    }
}


