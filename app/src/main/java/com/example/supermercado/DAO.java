package com.example.supermercado;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DAO {
    @Query("SELECT * FROM Produtos")
    List<Produto> getAll();

    /*
        @Query("SELECT * FROM Produto WHERE id IN (:userIds)")
        List<Produto> loadAllByIds(int[] userIds);

        @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
                "last_name LIKE :last LIMIT 1")
        User findByName(String first, String last);
     */
    @Insert
    void insert(Produto produto);

    @Update
    void update(Produto produto);

    @Delete
    void delete(Produto produto);
}
