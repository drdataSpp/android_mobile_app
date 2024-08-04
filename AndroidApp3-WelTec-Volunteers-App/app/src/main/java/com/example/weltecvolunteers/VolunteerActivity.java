package com.example.weltecvolunteers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class VolunteerActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        recyclerView = findViewById(R.id.volunteerRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DatabaseHelperClass databaseHelperClass = new DatabaseHelperClass(this);
        List<VolunteerClass> volunteerClassList = databaseHelperClass.getVolunteerList();

        if( volunteerClassList.size() > 0 )
        {
           VolunteerAdapterClass volunteerAdapter = new VolunteerAdapterClass(volunteerClassList,
                   VolunteerActivity.this);
           recyclerView.setAdapter(volunteerAdapter);
        }

        else
        {
            Toast.makeText(VolunteerActivity.this, "No Volunteers Available",
                    Toast.LENGTH_SHORT).show();
        }
    }
}