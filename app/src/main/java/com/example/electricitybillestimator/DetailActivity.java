package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import com.example.electricitybillestimator.R;
import com.example.electricitybillestimator.db.AppDatabase;
import com.example.electricitybillestimator.model.Bill;

public class DetailActivity extends AppCompatActivity {

    TextView detailsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailsText = findViewById(R.id.textViewDetails);
        int billId = getIntent().getIntExtra("id", -1);

        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "billDB")
                .allowMainThreadQueries().build();

        Bill bill = db.billDao().getById(billId);

        String detail = "Month: " + bill.month +
                "\nUnits: " + bill.units +
                "\nRebate: " + (int)(bill.rebate * 100) + "%" +
                "\nTotal Charges: RM " + String.format("%.2f", bill.totalCharges) +
                "\nFinal Cost: RM " + String.format("%.2f", bill.finalCost);

        detailsText.setText(detail);
    }
}
