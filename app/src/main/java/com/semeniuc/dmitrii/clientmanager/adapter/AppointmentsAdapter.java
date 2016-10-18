package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    List<Appointment> mAppointments;

    public AppointmentsAdapter(List<Appointment> appointments){
        mAppointments = appointments;
    }

    @Override
    public AppointmentsAdapter.AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cards_layout, parent, false);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppointmentsAdapter.AppointmentViewHolder holder, int position) {
        holder.mAppointmentTitle.setText(mAppointments.get(position).getTitle());
        holder.mClientName.setText(mAppointments.get(position).getClientName());
    }

    @Override
    public int getItemCount() {
        if(mAppointments != null){
            return mAppointments.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        TextView mAppointmentTitle;
        TextView mClientName;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            mAppointmentTitle = (TextView) itemView.findViewById(R.id.main_appointment_title);
            mClientName = (TextView) itemView.findViewById(R.id.main_appointment_client_name);
        }
    }
}


