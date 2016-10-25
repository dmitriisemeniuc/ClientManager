package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> mAppointments;
    private final OnItemClickListener mListener;
    private final OnPhoneClickListener mPhoneListener;
    private Utils mUtils = new Utils();

    public AppointmentAdapter(List<Appointment> appointments, OnItemClickListener listener, OnPhoneClickListener phoneListener) {
        mAppointments = appointments;
        mListener = listener;
        mPhoneListener = phoneListener;
    }

    @Override
    public AppointmentAdapter.AppointmentViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.main_cards_layout, parent, false);
        return new AppointmentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AppointmentAdapter.AppointmentViewHolder holder, int position) {
        holder.mClientName.setText(mAppointments.get(position).getClientName());
        holder.mClientPhone.setText(mAppointments.get(position).getClientPhone());
        holder.mService.setText(mAppointments.get(position).getService().getName());
        holder.mInfo.setText(mAppointments.get(position).getInfo());
        holder.mDateTime.setText(mUtils.convertDateToString(mAppointments.get(position).getDate(),
                Constants.DATE_TIME_FORMAT));
        holder.bind(mAppointments.get(position), mListener, mPhoneListener);
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
        AppCompatTextView mClientName;
        AppCompatTextView mClientPhone;
        AppCompatTextView mService;
        AppCompatTextView mInfo;
        AppCompatTextView mDateTime;
        AppCompatImageView mClientPhoneIcon;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            mClientName = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_client_name);
            mClientPhone = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_client_phone);
            mService = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_service);
            mInfo = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_info);
            mDateTime = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_date_time);
            mClientPhoneIcon = (AppCompatImageView) itemView.findViewById(R.id.main_cards_phone_call_icon);
        }

        public void bind(final Appointment appointment, final OnItemClickListener listener, final OnPhoneClickListener phoneListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(appointment);
                }
            });
            mClientPhoneIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneListener.onPhoneClick(appointment.getClientPhone());
                }
            });

        }
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public interface OnPhoneClickListener {
        void onPhoneClick(String phoneNumber);
    }
}


