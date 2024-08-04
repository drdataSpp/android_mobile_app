//--------------------------------------------------------------------------------------------------
//Programmer/ID:    Soorya Parthiban / 2192681
//Date:             28 September 2020
//Reference:        SD6501 - Assignment 2
/*Description:      MainActivity has two buttons in it; the app users can use the "Add button" to
                    to add new volunteers; Or the users can use the "View Button"  to view the
                    existing volunteers.*/
//--------------------------------------------------------------------------------------------------

package com.example.weltecvolunteers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText et_StudentID ,et_StudentName, et_StudentEmail, et_StudentContactNumber;
    Button bt_addData, bt_viewData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_StudentID = findViewById(R.id.et_StudentID);
        et_StudentName = findViewById(R.id.et_StudentName);
        et_StudentEmail = findViewById(R.id.et_StudentEmail);
        et_StudentContactNumber = findViewById(R.id.et_StudentContactNo);

        bt_addData = findViewById(R.id.bt_addData);
        bt_viewData = findViewById(R.id.bt_viewData);

        //Adding data to the database and the listView.
        bt_addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name, email, id, contactNumber;

                name = et_StudentName.getText().toString();
                email = et_StudentEmail.getText().toString();
                id = et_StudentID.getText().toString();
                contactNumber = et_StudentContactNumber.getText().toString();

                //Informing users to fill all the FIELDS (edit text boxes).
                if ( name.length() <= 0 || email.length() <= 0
                        || id.length() <= 0 || contactNumber.length() <= 0)
                {
                    Toast.makeText(MainActivity.this, "Fill all the fields",
                            Toast.LENGTH_LONG).show();
                }

                //If all the fields are filled, new volunteer is created.
                else
                {
                    DatabaseHelperClass databaseHelperClass = new DatabaseHelperClass(MainActivity.this);

                    VolunteerClass volunteer = new VolunteerClass(id, name,
                            email, contactNumber);

                    databaseHelperClass.addVolunteer(volunteer);

                    Toast.makeText(MainActivity.this, "Volunteer successfully added",
                            Toast.LENGTH_LONG).show();

                    finish();
                    startActivity(getIntent());
                }

            }
        });

        //Invoking another activity to view the volunteers list.
        bt_viewData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(MainActivity.this,
                        VolunteerActivity.class);

                startActivity(intent);
            }
        });

    }
}