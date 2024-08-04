//--------------------------------------------------------------------------------------------------
//Programmer/ID:    Soorya Parthiban / 2192681
//Date:             28 September 2020
//Reference:        SD6501 - Assignment 2
/*Description:      DataBaseHelperClass has the code of the CRUD database process*/
//--------------------------------------------------------------------------------------------------
package com.example.weltecvolunteers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperClass extends SQLiteOpenHelper
{
    //Database Infos.
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "VOLUNTEER.db";
    private static final String TABLE_NAME = "VOLUNTEER";

    //Columns in Volunteer table.
    public static final String STUDENT_ID = "STUDENT_ID";
    public static final String STUDENT_NAME = "STUDENT_NAME";
    public static final String STUDENT_EMAIL = "STUDENT_EMAIL";
    public static final String STUDENT_CONTACT_NO = "STUDENT_CONTACT_NO";
    private SQLiteDatabase sqLiteDatabase;


    //Create table SQL Query:
    String sqlQuery = "CREATE TABLE " + TABLE_NAME +
            "( " + STUDENT_ID + " INTEGER PRIMARY KEY, " +
            STUDENT_NAME + " TEXT," +
            STUDENT_EMAIL + " TEXT, " +
            STUDENT_CONTACT_NO + " LONG)";


    public DatabaseHelperClass (Context context)
    {
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //CREATE - DATABASE RECORDS
    public void addVolunteer(VolunteerClass newVolunteer)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelperClass.STUDENT_ID, newVolunteer.getStudentID());
        contentValues.put(DatabaseHelperClass.STUDENT_NAME, newVolunteer.getStudentName());
        contentValues.put(DatabaseHelperClass.STUDENT_EMAIL, newVolunteer.getStudentEmail());
        contentValues.put(DatabaseHelperClass.STUDENT_CONTACT_NO, newVolunteer.getStudentContactNumber());

        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.insert(DatabaseHelperClass.TABLE_NAME, null,contentValues);
    }

    //READ or VIEW - DATABASE RECORDS
    public List<VolunteerClass> getVolunteerList()
    {
        String sqlReadAllDataQuery = "SELECT * FROM " + TABLE_NAME;

        sqLiteDatabase = this.getReadableDatabase();

        List<VolunteerClass> volunteers = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery(sqlReadAllDataQuery,null);

        if (cursor.moveToFirst())
        {
            do
            {
                String studentID = cursor.getString(0);
                String studentName = cursor.getString(1);
                String studentEmail = cursor.getString(2);
                String studentContactNo = cursor.getString(3);

                volunteers.add(new VolunteerClass(studentID, studentName,
                        studentEmail, studentContactNo));

            } while (cursor.moveToNext());
        }

        cursor.close();
        return volunteers;
    }

    //UPDATE - DATABASE RECORDS
    public void updateVolunteer(VolunteerClass volunteer)
    {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelperClass.STUDENT_ID, volunteer.getStudentID());
        contentValues.put(DatabaseHelperClass.STUDENT_NAME, volunteer.getStudentName());
        contentValues.put(DatabaseHelperClass.STUDENT_EMAIL, volunteer.studentEmail);
        contentValues.put(DatabaseHelperClass.STUDENT_CONTACT_NO, volunteer.studentContactNumber);

        sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.update( TABLE_NAME, contentValues,STUDENT_ID + " = ?" , new String[]
                {String.valueOf(volunteer.getStudentID())});
    }

    //DELETE - DATABASE RECORDS
    public void deleteVolunteer(int id)
    {
        sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.delete(TABLE_NAME, STUDENT_ID + " = ? ", new String[]
                {String.valueOf(id)});
    }
}
