package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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

    public AppointmentAdapter(List<Appointment> appointments, OnItemClickListener listener,
                              OnPhoneClickListener phoneListener) {
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
        holder.client.setText(appointments.get(position).getClient().getName());
        if (appointments.get(position).getClientContactPhone().isEmpty())
            holder.phoneIcon.setVisibility(View.INVISIBLE);
        holder.service.setText(appointments.get(position).getService().getName());
        if (appointments.get(position).getSum().isEmpty()) {
            holder.currency.setVisibility(View.INVISIBLE);
            holder.paid.setVisibility(View.GONE);
        } else {
            holder.sum.setText(appointments.get(position).getSum());
            holder.paid.setImageDrawable(ContextCompat.getDrawable(
                    MyApplication.getInstance().getApplicationContext(), R.mipmap.ic_money_paid_no));
        }
        holder.dateTime.setText(Utils.convertDateToString(appointments.get(position).getDate(),
                Constants.DATE_TIME_FORMAT, MyApplication.getInstance().getApplicationContext()));
        if (appointments.get(position).isPaid()) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(
                    MyApplication.getInstance().getApplicationContext(), R.color.light_yellow));
            holder.paid.setImageDrawable(ContextCompat.getDrawable(
                    MyApplication.getInstance().getApplicationContext(), R.mipmap.ic_money_paid_yes));
        }
        if (appointments.get(position).isDone() ||
                (appointments.get(position).isPaid() && appointments.get(position).isDone())) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(
                    MyApplication.getInstance().getApplicationContext(), R.color.light_green));
        }
        if (appointments.get(position).getClientContactAddress().isEmpty())
            holder.addressLayout.setVisibility(View.GONE);
        else holder.address.setText(appointments.get(position).getClientContactAddress());

        if (appointments.get(position).getInfo().isEmpty())
            holder.infoLayout.setVisibility(View.GONE);
        else holder.info.setText(appointments.get(position).getInfo());

        if (!appointments.get(position).getService().isHairColoring())
            holder.hairColoring.setVisibility(View.GONE);
        if (!appointments.get(position).getService().isHairdo())
            holder.hairdo.setVisibility(View.GONE);
        if (!appointments.get(position).getService().isHaircut())
            holder.haircut.setVisibility(View.GONE);
        if (!appointments.get(position).getService().isHairColoring()
                && !appointments.get(position).getService().isHairdo()
                && !appointments.get(position).getService().isHaircut())
            holder.serviceLayout.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isBrush()) holder.brush.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isHairBrush())
            holder.hairBrush.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isHairDryer())
            holder.hairDryer.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isHairBand())
            holder.hairBand.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isCutSet())
            holder.cutSet.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isSpray()) holder.spray.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isOxy()) holder.oxy.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isTube()) holder.tube.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isTrimmer())
            holder.trimmer.setVisibility(View.GONE);
        if (!appointments.get(position).getTools().isBrush()
                && !appointments.get(position).getTools().isHairBrush()
                && !appointments.get(position).getTools().isHairDryer()
                && !appointments.get(position).getTools().isHairBand()
                && !appointments.get(position).getTools().isCutSet()
                && !appointments.get(position).getTools().isSpray()
                && !appointments.get(position).getTools().isOxy()
                && !appointments.get(position).getTools().isTube()
                && !appointments.get(position).getTools().isTrimmer())
            holder.toolsLayout.setVisibility(View.GONE);

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
        AppCompatTextView client;
        AppCompatTextView phone;
        AppCompatTextView address;
        AppCompatTextView service;
        AppCompatTextView sum;
        AppCompatTextView currency;
        AppCompatTextView info;
        AppCompatTextView dateTime;
        AppCompatImageView paid;
        AppCompatImageView phoneIcon;
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
        AppCompatImageView infoIcon;
        AppCompatImageView addressIcon;
        ViewGroup serviceLayout;
        ViewGroup toolsLayout;
        ViewGroup addressLayout;
        ViewGroup infoLayout;
        CardView cardView;

        AppointmentViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.recycler);
            client = (AppCompatTextView) itemView.findViewById(R.id.card_client_name);
            phone = (AppCompatTextView) itemView.findViewById(R.id.card_client_phone);
            address = (AppCompatTextView) itemView.findViewById(R.id.card_client_address);
            addressIcon = (AppCompatImageView) itemView.findViewById(R.id.card_address_icon);
            service = (AppCompatTextView) itemView.findViewById(R.id.card_service_name);
            sum = (AppCompatTextView) itemView.findViewById(R.id.card_sum);
            currency = (AppCompatTextView) itemView.findViewById(R.id.card_sum_currency);
            paid = (AppCompatImageView) itemView.findViewById(R.id.card_paid_icon);
            infoIcon = (AppCompatImageView) itemView.findViewById(R.id.card_info_icon);
            info = (AppCompatTextView) itemView.findViewById(R.id.card_info);
            dateTime = (AppCompatTextView) itemView.findViewById(R.id.card_date_time);
            phoneIcon = (AppCompatImageView) itemView.findViewById(R.id.card_phone_call_icon);
            hairColoring = (AppCompatImageView) itemView.findViewById(R.id.card_services_coloring);
            hairdo = (AppCompatImageView) itemView.findViewById(R.id.card_services_hairdo);
            haircut = (AppCompatImageView) itemView.findViewById(R.id.card_services_haircut);
            brush = (AppCompatImageView) itemView.findViewById(R.id.card_tools_brush);
            hairBrush = (AppCompatImageView) itemView.findViewById(R.id.card_tools_hair_brush);
            hairDryer = (AppCompatImageView) itemView.findViewById(R.id.card_tools_hair_dryer);
            hairBand = (AppCompatImageView) itemView.findViewById(R.id.card_tools_hair_band);
            cutSet = (AppCompatImageView) itemView.findViewById(R.id.card_tools_cut_set);
            spray = (AppCompatImageView) itemView.findViewById(R.id.card_tools_spray);
            oxy = (AppCompatImageView) itemView.findViewById(R.id.card_tools_oxy);
            tube = (AppCompatImageView) itemView.findViewById(R.id.card_tools_tube);
            trimmer = (AppCompatImageView) itemView.findViewById(R.id.card_tools_trimmer);
            serviceLayout = (ViewGroup) itemView.findViewById(R.id.services);
            toolsLayout = (ViewGroup) itemView.findViewById(R.id.tools);
            addressLayout = (ViewGroup) itemView.findViewById(R.id.address);
            infoLayout = (ViewGroup) itemView.findViewById(R.id.info);
        }

        public void bind(final Appointment appointment, final OnItemClickListener listener,
                         final OnPhoneClickListener phoneListener) {
            itemView.setOnClickListener(v -> listener.onItemClick(appointment));
            phoneIcon.setOnClickListener(v -> phoneListener.onPhoneClick(
                    appointment.getClient().getContact().getPhone()));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Appointment appointment);
    }

    public interface OnPhoneClickListener {
        void onPhoneClick(String phoneNumber);
    }
}
