package com.example.electricitybillestimator;

import android.os.Bundle;
import android.widget.*;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.electricitybillestimator.db.AppDatabase;
import com.example.electricitybillestimator.model.Bill;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth;
    EditText editUnits;
    RadioGroup radioGroupRebate;
    TextView totalChargesText, finalCostText;
    Button calculateBtn, saveBtn, viewBtn;

    AppDatabase db;
    double totalCharges = 0, finalCost = 0;
    int units = 0;
    double rebate = -1; // initially unset

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Electricity Bill Estimator");

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "billDB")
                .allowMainThreadQueries().build();

        spinnerMonth = findViewById(R.id.spinnerMonth);
        editUnits = findViewById(R.id.editTextUnits);
        radioGroupRebate = findViewById(R.id.radioGroupRebate);
        totalChargesText = findViewById(R.id.textViewTotalCharges);
        finalCostText = findViewById(R.id.textViewFinalCost);
        calculateBtn = findViewById(R.id.buttonCalculate);
        saveBtn = findViewById(R.id.buttonSave);
        viewBtn = findViewById(R.id.buttonViewHistory);

        // Populate month spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.months_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerMonth.setAdapter(adapter);

        // Listen for rebate radio selection
        radioGroupRebate.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio0) rebate = 0.0;
            else if (checkedId == R.id.radio1) rebate = 0.01;
            else if (checkedId == R.id.radio2) rebate = 0.02;
            else if (checkedId == R.id.radio3) rebate = 0.03;
            else if (checkedId == R.id.radio4) rebate = 0.04;
            else if (checkedId == R.id.radio5) rebate = 0.05;
        });

        // Calculate button
        calculateBtn.setOnClickListener(v -> {
            String unitsStr = editUnits.getText().toString().trim();
            if (unitsStr.isEmpty()) {
                editUnits.setError("Please enter electricity units.");
                return;
            }

            try {
                units = Integer.parseInt(unitsStr);
                totalCharges = calculateCharges(units);
                if (rebate == -1) {
                    Toast.makeText(this, "Please select a rebate percentage.", Toast.LENGTH_SHORT).show();
                    return;
                }
                finalCost = totalCharges - (totalCharges * rebate);
                totalChargesText.setText("Total Charges: RM " + String.format("%.2f", totalCharges));
                finalCostText.setText("Final Cost: RM " + String.format("%.2f", finalCost));
            } catch (NumberFormatException e) {
                editUnits.setError("Invalid number");
            }
        });

        // Save button
        saveBtn.setOnClickListener(v -> {
            String selectedMonth = spinnerMonth.getSelectedItem().toString();
            String unitsStr = editUnits.getText().toString().trim();

            // Check month (if first item is "Select a month")
            if (selectedMonth.equals("Select a month")) {
                Toast.makeText(this, "Please select a month.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check units
            if (unitsStr.isEmpty()) {
                editUnits.setError("Please enter electricity units.");
                return;
            }

            // Check rebate
            if (rebate == -1) {
                Toast.makeText(this, "Please select a rebate percentage.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Parse units
            units = Integer.parseInt(unitsStr);
            totalCharges = calculateCharges(units);
            finalCost = totalCharges - (totalCharges * rebate);

            // Save to database
            Bill bill = new Bill();
            bill.month = selectedMonth;
            bill.units = units;
            bill.rebate = rebate;
            bill.totalCharges = totalCharges;
            bill.finalCost = finalCost;

            db.billDao().insert(bill);
            Toast.makeText(this, "Saved to DB", Toast.LENGTH_SHORT).show();
        });

        viewBtn.setOnClickListener(v -> startActivity(new Intent(this, HistoryActivity.class)));
    }

    private double calculateCharges(int u) {
        double charge = 0;
        if (u <= 200) charge = u * 0.218;
        else if (u <= 300) charge = 200 * 0.218 + (u - 200) * 0.334;
        else if (u <= 600) charge = 200 * 0.218 + 100 * 0.334 + (u - 300) * 0.516;
        else charge = 200 * 0.218 + 100 * 0.334 + 300 * 0.516 + (u - 600) * 0.546;
        return charge;
    }
}