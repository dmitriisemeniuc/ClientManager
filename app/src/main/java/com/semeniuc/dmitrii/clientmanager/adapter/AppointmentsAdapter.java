package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.AppointmentViewHolder> {

    private List<Appointment> mAppointments;
    private final OnItemClickListener mListener;
    private Utils mUtils = new Utils();

    public AppointmentsAdapter(List<Appointment> appointments, OnItemClickListener listener) {
        mAppointments = appointments;
        mListener = listener;
    }

    @Override
    public AppointmentsAdapter.AppointmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_cards_layout, parent, false);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppointmentsAdapter.AppointmentViewHolder holder, int position) {
        holder.mAppointmentTitle.setText(mAppointments.get(position).getTitle());
        holder.mClientName.setText(mAppointments.get(position).getClientName());
        holder.mClientPhone.setText(mAppointments.get(position).getClientPhone());
        holder.mService.setText(mAppointments.get(position).getService());
        holder.mInfo.setText(mAppointments.get(position).getInfo());
        holder.mDate.setText(mUtils.convertDateToString(mAppointments.get(position).getDate()));
        holder.bind(mAppointments.get(position), mListener);
    }

    @Override
    public int getItemCount() {
        if (mAppointments != null) {
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
        TextView mClientPhone;
        TextView mService;
        TextView mInfo;
        TextView mDate;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            mAppointmentTitle = (TextView) itemView.findViewById(R.id.main_appointment_title);
            mClientName = (TextView) itemView.findViewById(R.id.main_appointment_client_name);
            mClientPhone = (TextView) itemView.findViewById(R.id.main_appointment_client_phone);
            mService = (TextView) itemView.findViewById(R.id.main_appointment_service);
            mInfo = (TextView) itemView.findViewById(R.id.main_appointment_info);
            mDate = (TextView) itemView.findViewById(R.id.main_appointment_time);
        }

        public void bind(final Appointment appointment, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(appointment);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }
}


