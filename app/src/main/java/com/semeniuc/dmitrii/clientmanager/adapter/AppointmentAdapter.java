package com.semeniuc.dmitrii.clientmanager.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.CardView;
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
        // CLIENT
        holder.client.setText(appointments.get(position).getClient().getName());
        // PHONE ICON
        if (appointments.get(position).getClient().getContact().getPhone().isEmpty()) {
            holder.phoneIcon.setVisibility(View.INVISIBLE);
        }
        // SERVICE
        holder.service.setText(appointments.get(position).getService().getName());
        // SUM
        if (appointments.get(position).getSum().isEmpty()) {
            holder.currency.setVisibility(View.INVISIBLE);
            holder.paid.setVisibility(View.GONE);
        } else {
            holder.sum.setText(appointments.get(position).getSum());
            holder.paid.setImageDrawable(ContextCompat.getDrawable(
                    MyApplication.getInstance().getApplicationContext(), R.mipmap.ic_money_paid_no));
        }
        // DATE
        holder.dateTime.setText(utils.convertDateToString(appointments.get(position).getDate(),
                Constants.DATE_TIME_FORMAT, MyApplication.getInstance().getApplicationContext()));
        // PAID
        boolean paid = appointments.get(position).isPaid();
        if (paid) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(
                    MyApplication.getInstance().getApplicationContext(), R.color.light_yellow));
            holder.paid.setImageDrawable(ContextCompat.getDrawable(
                    MyApplication.getInstance().getApplicationContext(), R.mipmap.ic_money_paid_yes));
        }
        // DONE
        boolean done = appointments.get(position).isDone();
        if (done || (paid && done)) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(
                    MyApplication.getInstance().getApplicationContext(), R.color.light_green));
        }
        // ADDRESS
        if (appointments.get(position).getClient().getContact().getAddress().isEmpty()) {
            holder.addressLayout.setVisibility(View.GONE);
        } else {
            holder.address.setText(appointments.get(position).getClient().getContact().getAddress());
        }
        // INFO
        if (appointments.get(position).getInfo().isEmpty()) {
            holder.infoLayout.setVisibility(View.GONE);
        } else {
            holder.info.setText(appointments.get(position).getInfo());
        }
        // SERVICES
        boolean hairColoring = appointments.get(position).getService().isHairColoring();
        if (!hairColoring) holder.hairColoring.setVisibility(View.GONE);
        boolean hairdo = appointments.get(position).getService().isHairdo();
        if (!hairdo) holder.hairdo.setVisibility(View.GONE);
        boolean haircut = appointments.get(position).getService().isHaircut();
        if (!haircut) holder.haircut.setVisibility(View.GONE);
        if (!hairColoring && !hairdo && !haircut) holder.serviceLayout.setVisibility(View.GONE);
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
                !tube && !trimmer) holder.toolsLayout.setVisibility(View.GONE);
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
        AppCompatTextView client, phone, address, service, sum, currency, info, dateTime;
        AppCompatImageView paid, phoneIcon, hairColoring, hairdo, haircut, brush, hairBrush,
                hairDryer, hairBand, cutSet, spray, oxy, tube, trimmer, infoIcon, addressIcon;
        RelativeLayout serviceLayout, toolsLayout, addressLayout, infoLayout;
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
            serviceLayout = (RelativeLayout) itemView.findViewById(R.id.services);
            toolsLayout = (RelativeLayout) itemView.findViewById(R.id.tools);
            addressLayout = (RelativeLayout) itemView.findViewById(R.id.address);
            infoLayout = (RelativeLayout) itemView.findViewById(R.id.info);
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
