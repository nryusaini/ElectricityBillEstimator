package com.example.electricitybillestimator.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.example.electricitybillestimator.model.Bill;

@Database(entities = {Bill.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BillDao billDao();
}