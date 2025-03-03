package com.example.womensafety;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Calender extends AppCompatActivity {

    private CalendarView calendarView;
    private ImageButton hamButton;
    private Dialog dialog;
    private Button setBtn, updateBtn;
    private String selectedDate;
    private DatabaseReference databaseReference;
    private String userId = "User123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        calendarView = findViewById(R.id.cal);
        hamButton = findViewById(R.id.ham);

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        dialog = new Dialog(this);
        dialog.setContentView(R.layout.calset_layout);
        dialog.setCancelable(true);

        setBtn = dialog.findViewById(R.id.setbtn);
        updateBtn = dialog.findViewById(R.id.updatebtn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                showDialogBox();
            }
        });
    }

    private void showDialogBox() {
        dialog.show();

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePeriodStartDate(selectedDate);
                dialog.dismiss();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePeriodDate(selectedDate);
                dialog.dismiss();
            }
        });
    }

    private void savePeriodStartDate(String date) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("periodStartDate", date);
        databaseReference.child(userId).setValue(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Calender.this, "Period date saved!", Toast.LENGTH_SHORT).show();
                schedulePeriodAlert(date);
            } else {
                Toast.makeText(Calender.this, "Failed to save data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updatePeriodDate(String newDate) {
        databaseReference.child(userId).child("periodStartDate").setValue(newDate).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(Calender.this, "Period date updated!", Toast.LENGTH_SHORT).show();
                schedulePeriodAlert(newDate);
            } else {
                Toast.makeText(Calender.this, "Failed to update data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void schedulePeriodAlert(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(sdf.parse(date));
            calendar.add(Calendar.DAY_OF_MONTH, 28 - 3);
            Toast.makeText(this, "Reminder set for: " + sdf.format(calendar.getTime()), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
