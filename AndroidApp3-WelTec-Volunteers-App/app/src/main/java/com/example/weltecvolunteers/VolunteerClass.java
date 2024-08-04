package com.example.weltecvolunteers;

public class VolunteerClass
{
        String studentID, studentContactNumber, studentName, studentEmail;

    public VolunteerClass(String studentID, String studentName, String studentEmail
                            , String studentContactNumber)
    {
        this.studentID = studentID;
        this.studentName = studentName;
        this.studentEmail = studentEmail;
        this.studentContactNumber = studentContactNumber;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getStudentContactNumber() {
        return studentContactNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }
}
