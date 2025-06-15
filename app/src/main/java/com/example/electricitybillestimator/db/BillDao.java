package com.example.electricitybillestimator.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Delete;
import com.example.electricitybillestimator.model.Bill;

import java.util.List;

@Dao
public interface BillDao {
    @Insert
    void insert(Bill bill);

    @Query("SELECT * FROM Bill ORDER BY id DESC")
    List<Bill> getAll();

    @Query("SELECT * FROM Bill WHERE id = :id")
    Bill getById(int id);

    @Delete
    void delete(Bill bill);

}
