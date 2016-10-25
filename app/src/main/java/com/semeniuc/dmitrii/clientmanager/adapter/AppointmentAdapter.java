package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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
        // fields
        holder.mClientName.setText(mAppointments.get(position).getClientName());
        holder.mClientPhone.setText(mAppointments.get(position).getClientPhone());
        holder.mService.setText(mAppointments.get(position).getService().getName());
        holder.mSum.setText(mAppointments.get(position).getSum());
        holder.mInfo.setText(mAppointments.get(position).getInfo());
        if(mAppointments.get(position).getInfo().isEmpty()){
            holder.mInfoLayout.setVisibility(View.GONE);
        }
        holder.mDateTime.setText(mUtils.convertDateToString(mAppointments.get(position).getDate(),
                Constants.DATE_TIME_FORMAT));
        // booleans {images}
        boolean done = mAppointments.get(position).isDone();
        if(!done) holder.mDone.setVisibility(View.GONE);
        boolean paid = mAppointments.get(position).isPaid();
        if(!paid) holder.mPaid.setVisibility(View.GONE);
        boolean hairColoring = mAppointments.get(position).getService().isHairColoring();
        if(!hairColoring) holder.mHairColoring.setVisibility(View.GONE);
        boolean hairdo = mAppointments.get(position).getService().isHairdo();
        if(!hairdo) holder.mHairdo.setVisibility(View.GONE);
        boolean haircut = mAppointments.get(position).getService().isHaircut();
        if(!haircut) holder.mHaircut.setVisibility(View.GONE);
        if(!hairColoring && !hairdo && !haircut){
            holder.mServiceLabel.setVisibility(View.GONE);
        }
        // set onClickListeners
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
        AppCompatTextView mServiceLabel;
        AppCompatTextView mSum;
        AppCompatTextView mInfo;
        RelativeLayout mInfoLayout;
        AppCompatTextView mDateTime;
        AppCompatImageView mClientPhoneIcon;
        AppCompatImageView mPaid;
        AppCompatImageView mDone;
        AppCompatImageView mHairColoring;
        AppCompatImageView mHairdo;
        AppCompatImageView mHaircut;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            mClientName = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_client_name);
            mClientPhone = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_client_phone);
            mService = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_service_name);
            mServiceLabel = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_services_label);
            mSum = (AppCompatTextView) itemView.findViewById(R.id.main_cards_sum_et);
            mPaid = (AppCompatImageView) itemView.findViewById(R.id.main_cards_paid_icon);
            mDone = (AppCompatImageView) itemView.findViewById(R.id.main_cards_done_icon);
            mInfo = (AppCompatTextView) itemView.findViewById(R.id.main_cards_info);
            mInfoLayout = (RelativeLayout) itemView.findViewById(R.id.main_cards_info_layout);
            mDateTime = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_date_time);
            mClientPhoneIcon = (AppCompatImageView) itemView.findViewById(R.id.main_cards_phone_call_icon);
            mHairColoring = (AppCompatImageView) itemView.findViewById(R.id.main_cards_services_hair_coloring);
            mHairdo = (AppCompatImageView) itemView.findViewById(R.id.main_cards_services_hairdo);
            mHaircut = (AppCompatImageView) itemView.findViewById(R.id.main_cards_services_haircut);
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


