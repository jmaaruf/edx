package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class Editpage extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editpage);

        SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        String time = (sharedpreferences.getString("time", ""));

        Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.field));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner timespinner = (Spinner) findViewById(R.id.spinner8);

        ArrayAdapter<String> adapters = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.time));
        adapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timespinner.setAdapter(adapters);

        int spinnerPosition = adapters.getPosition(time);

        timespinner.setSelection(spinnerPosition);

        String name = (sharedpreferences.getString("name", ""));
        String school = (sharedpreferences.getString("school", ""));
        String field = sharedpreferences.getString("field", "");

        int spinnerPos = adapters.getPosition(field);

        spinner.setSelection(spinnerPos);

        ((TextView)findViewById(R.id.textView23)).setText(name);
        ((TextView)findViewById(R.id.textView25)).setText(school);

        Button save = (Button)findViewById(R.id.button2);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newname = ((TextView)findViewById(R.id.textView23)).getText().toString();
                String newschool = ((TextView)findViewById(R.id.textView25)).getText().toString();
                String newtime = timespinner.getSelectedItem().toString();
                String newfield = spinner.getSelectedItem().toString();

                SharedPreferences sharedpreferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                SharedPreferences.Editor editor = sharedpreferences.edit();


                editor.putString("name", newname);
                editor.putString("field", newfield);
                editor.putString("school", newschool);
                editor.putInt("alarm", Integer.parseInt(newtime));
                editor.putString("time", newtime);
                editor.commit();

                int times = sharedpreferences.getInt("alarm", 0);

                Calendar calendar = Calendar.getInstance();

                calendar.set(Calendar.HOUR_OF_DAY, times);
                calendar.set(Calendar.MINUTE, 20);
                calendar.set(Calendar.SECOND, 0);

                Intent intent = new Intent(getApplicationContext(), Notreceiver.class);
                PendingIntent pendingintent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingintent);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                String userid = user.getUid();

                reference.child(userid).child("fname").setValue(newname);
                reference.child(userid).child("schoolname").setValue(newschool);
                reference.child(userid).child("field").setValue(newfield);


                Toast.makeText(Editpage.this, "Please restart the app to apply all changes",Toast.LENGTH_LONG).show();

                startActivity(new Intent(Editpage.this, Editprofile.class));
                finish();
            }
        });
    }
}