package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

import java.util.List;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder> {

    private List<Appointment> appointments;
    private final OnItemClickListener listener;
    private final OnPhoneClickListener phoneListener;
    private Utils utils = new Utils();

    public AppointmentAdapter(List<Appointment> appointments, OnItemClickListener listener, OnPhoneClickListener phoneListener) {
        this.appointments = appointments;
        this.listener = listener;
        this.phoneListener = phoneListener;
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
        holder.clientName.setText(appointments.get(position).getClientName());

        if (appointments.get(position).getClientPhone().isEmpty()) {
            holder.clientPhoneIcon.setVisibility(View.GONE);
        } else {
            holder.clientPhone.setText(appointments.get(position).getClientPhone());
        }
        holder.service.setText(appointments.get(position).getService().getName());
        holder.sum.setText(appointments.get(position).getSum());
        holder.info.setText(appointments.get(position).getInfo());
        if (appointments.get(position).getInfo().isEmpty()) {
            holder.infoLayout.setVisibility(View.GONE);
        }
        holder.dateTime.setText(utils.convertDateToString(appointments.get(position).getDate(),
                Constants.DATE_TIME_FORMAT, MyApplication.getInstance().getApplicationContext()));
        // booleans {images}
        boolean done = appointments.get(position).isDone();
        if (!done) {
            holder.done.setVisibility(View.INVISIBLE);
        } else {
            holder.divider.setBackgroundColor(ContextCompat.getColor(
                    MyApplication.getInstance().getApplicationContext(), R.color.greenBackground));
        }
        boolean paid = appointments.get(position).isPaid();
        if (!paid){
            holder.paid.setVisibility(View.INVISIBLE);
            holder.sumStrike.setVisibility(View.GONE);
        }

        // SERVICES
        boolean hairColoring = appointments.get(position).getService().isHairColoring();
        if (!hairColoring) holder.hairColoring.setVisibility(View.GONE);
        boolean hairdo = appointments.get(position).getService().isHairdo();
        if (!hairdo) holder.hairdo.setVisibility(View.GONE);
        boolean haircut = appointments.get(position).getService().isHaircut();
        if (!haircut) holder.haircut.setVisibility(View.GONE);
        if (!hairColoring && !hairdo && !haircut) {
            holder.serviceLayout.setVisibility(View.GONE);
        }
        // TOOLS
        boolean brush = appointments.get(position).getTools().isBrush();
        if (!brush) holder.brush.setVisibility(View.GONE);
        boolean hairBrush = appointments.get(position).getTools().isHairBrush();
        if (!hairBrush) holder.hairBrush.setVisibility(View.GONE);
        boolean hairDryer = appointments.get(position).getTools().isHairDryer();
        if (!hairDryer) holder.hairDryer.setVisibility(View.GONE);
        boolean hairBand = appointments.get(position).getTools().isHairBand();
        if (!hairBand) holder.hairBand.setVisibility(View.GONE);
        boolean cutSet = appointments.get(position).getTools().isCutSet();
        if (!cutSet) holder.cutSet.setVisibility(View.GONE);
        boolean spray = appointments.get(position).getTools().isSpray();
        if (!spray) holder.spray.setVisibility(View.GONE);
        boolean oxy = appointments.get(position).getTools().isOxy();
        if (!oxy) holder.oxy.setVisibility(View.GONE);
        boolean tube = appointments.get(position).getTools().isTube();
        if (!tube) holder.tube.setVisibility(View.GONE);
        boolean trimmer = appointments.get(position).getTools().isTrimmer();
        if (!trimmer) holder.trimmer.setVisibility(View.GONE);
        if (!brush && !hairBrush && !hairDryer && !hairBand && !cutSet && !spray && !oxy &&
                !tube && !trimmer) {
            holder.toolsLayout.setVisibility(View.GONE);
        }
        // set onClickListeners
        holder.bind(appointments.get(position), listener, phoneListener);
    }

    @Override
    public int getItemCount() {
        if (appointments != null) {
            return appointments.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class AppointmentViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView clientName;
        AppCompatTextView clientPhone;
        AppCompatTextView service;
        AppCompatTextView sum;
        AppCompatTextView info;
        AppCompatTextView dateTime;
        AppCompatImageView clientPhoneIcon;
        AppCompatImageView paid;
        AppCompatImageView done;
        AppCompatImageView hairColoring;
        AppCompatImageView hairdo;
        AppCompatImageView haircut;
        AppCompatImageView brush;
        AppCompatImageView hairBrush;
        AppCompatImageView hairDryer;
        AppCompatImageView hairBand;
        AppCompatImageView cutSet;
        AppCompatImageView spray;
        AppCompatImageView oxy;
        AppCompatImageView tube;
        AppCompatImageView trimmer;
        View divider;
        RelativeLayout sumStrike;
        RelativeLayout serviceLayout;
        RelativeLayout toolsLayout;
        RelativeLayout infoLayout;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            clientName = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_client_name);
            clientPhone = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_client_phone);
            service = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_service_name);
            sum = (AppCompatTextView) itemView.findViewById(R.id.main_cards_sum);
            paid = (AppCompatImageView) itemView.findViewById(R.id.main_cards_paid_icon);
            done = (AppCompatImageView) itemView.findViewById(R.id.main_cards_done_icon);
            info = (AppCompatTextView) itemView.findViewById(R.id.main_cards_info);
            dateTime = (AppCompatTextView) itemView.findViewById(R.id.main_appointment_date_time);
            clientPhoneIcon = (AppCompatImageView) itemView.findViewById(R.id.main_cards_phone_call_icon);
            hairColoring = (AppCompatImageView) itemView.findViewById(R.id.main_cards_services_hair_coloring);
            hairdo = (AppCompatImageView) itemView.findViewById(R.id.main_cards_services_hairdo);
            haircut = (AppCompatImageView) itemView.findViewById(R.id.main_cards_services_haircut);
            brush = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_brush);
            hairBrush = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_hair_brush);
            hairDryer = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_hair_dryer);
            hairBand = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_hair_band);
            cutSet = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_cut_set);
            spray = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_spray);
            oxy = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_oxy);
            tube = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_tube);
            trimmer = (AppCompatImageView) itemView.findViewById(R.id.main_cards_tools_trimmer);

            divider = itemView.findViewById(R.id.divider);

            sumStrike = (RelativeLayout) itemView.findViewById(R.id.main_cards_sum_strike);
            serviceLayout = (RelativeLayout) itemView.findViewById(R.id.main_appointment_services);
            toolsLayout = (RelativeLayout) itemView.findViewById(R.id.main_appointment_tools);
            infoLayout = (RelativeLayout) itemView.findViewById(R.id.main_cards_info_layout);
        }

        public void bind(final Appointment appointment, final OnItemClickListener listener, final OnPhoneClickListener phoneListener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(appointment);
                }
            });
            clientPhoneIcon.setOnClickListener(new View.OnClickListener() {
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
