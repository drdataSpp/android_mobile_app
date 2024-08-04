package com.example.weltecvolunteers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VolunteerAdapterClass extends RecyclerView.Adapter<VolunteerAdapterClass.ViewHolder>
{

    List<VolunteerClass> volunteerList;
    Context context;
    DatabaseHelperClass databaseHelperClass;

    public VolunteerAdapterClass(List<VolunteerClass> volunteerList, Context context) {
        this.volunteerList = volunteerList;
        this.context = context;
        databaseHelperClass = new DatabaseHelperClass(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.voluteer_item_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        final VolunteerClass volunteer = volunteerList.get(position);

        holder.studentID.setText(volunteer.getStudentID());
        holder.studentName.setText(volunteer.getStudentName());
        holder.studentEmail.setText(volunteer.getStudentEmail());
        holder.studentContactNumber.setText(volunteer.getStudentContactNumber());

        holder.editData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String volunteerName = holder.studentName.getText().toString();
                String volunteerEmail = holder.studentEmail.getText().toString();
                String volunteerContactNumber = holder.studentContactNumber.getText().toString();

                databaseHelperClass.updateVolunteer(
                        new VolunteerClass(volunteer.getStudentID(), volunteerName,
                                volunteerEmail, volunteerContactNumber));

                notifyDataSetChanged();
                ((Activity) context).finish();

                context.startActivity(((Activity) context).getIntent());
            }
        });

        holder.deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseHelperClass.deleteVolunteer(Integer.parseInt(volunteer.getStudentID()));
                volunteerList.remove(position);
                notifyDataSetChanged();

            }
        });
    }

    @Override
    public int getItemCount() {
        return volunteerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView studentID;
        EditText studentName, studentEmail, studentContactNumber;
        Button editData, deleteData;

        public ViewHolder(@NonNull View ItemView)
        {
            super(ItemView);

            studentID = ItemView.findViewById(R.id.tv_studentID);
            studentName = ItemView.findViewById(R.id.et_studentNameItem);
            studentEmail = ItemView.findViewById(R.id.et_studentEmailItem);
            studentContactNumber = ItemView.findViewById(R.id.et_studentContactNoItem);

            editData = ItemView.findViewById(R.id.bt_editDataItem);
            deleteData = ItemView.findViewById(R.id.bt_deleteDataItem);
        }
    }
}
