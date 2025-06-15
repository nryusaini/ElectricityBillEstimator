package com.example.electricitybillestimator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.appcompat.app.AlertDialog;

import com.example.electricitybillestimator.db.AppDatabase;
import com.example.electricitybillestimator.model.Bill;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView listView;
    List<Bill> bills;
    AppDatabase db;
    BillAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setTitle("History");

        listView = findViewById(R.id.listViewHistory);
        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "billDB")
                .allowMainThreadQueries().build();

        loadBills();

        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", bills.get(position).id);
            startActivity(intent);
        });
    }

    private void loadBills() {
        bills = db.billDao().getAll();
        adapter = new BillAdapter();
        listView.setAdapter(adapter);
    }

    class BillAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return bills.size();
        }

        @Override
        public Object getItem(int position) {
            return bills.get(position);
        }

        @Override
        public long getItemId(int position) {
            return bills.get(position).id;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                row = LayoutInflater.from(HistoryActivity.this)
                        .inflate(R.layout.history_list_item, parent, false);
            }

            TextView textBill = row.findViewById(R.id.textBillItem);
            Button deleteBtn = row.findViewById(R.id.buttonDelete);

            Bill bill = bills.get(position);
            textBill.setText(bill.month + ": RM " + String.format("%.2f", bill.finalCost));

            deleteBtn.setOnClickListener(v -> {
                new AlertDialog.Builder(HistoryActivity.this)
                        .setTitle("Delete Confirmation")
                        .setMessage("Are you sure you want to delete this bill?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.billDao().delete(bill);
                            loadBills(); // refresh the list
                            Toast.makeText(HistoryActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            });


            return row;
        }
    }
}
