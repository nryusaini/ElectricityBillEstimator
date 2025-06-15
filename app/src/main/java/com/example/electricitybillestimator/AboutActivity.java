package com.example.electricitybillestimator;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.electricitybillestimator.R;

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView aboutText = findViewById(R.id.textViewAbout);
        aboutText.setText("Name: Nur Yusaini Binti Yuzaimin\n" +
                "Student ID: 2024927207\n" +
                "Course: ICT602 - Mobile Technology And Development\n" +
                "© 2025 Electricity Bill Estimator\n" +
                "GitHub: https://github.com/nryusaini/ElectricityBillEstimator\n");
        Linkify.addLinks(aboutText, Linkify.WEB_URLS);
    }
}
