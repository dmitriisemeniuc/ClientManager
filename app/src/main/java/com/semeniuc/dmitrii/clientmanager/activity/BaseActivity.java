package com.semeniuc.dmitrii.clientmanager.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.view.Menu;
import android.widget.Toast;

import com.semeniuc.dmitrii.clientmanager.MyApplication;
import com.semeniuc.dmitrii.clientmanager.OnTaskCompleted;
import com.semeniuc.dmitrii.clientmanager.R;
import com.semeniuc.dmitrii.clientmanager.db.DatabaseTaskHelper;
import com.semeniuc.dmitrii.clientmanager.model.Appointment;
import com.semeniuc.dmitrii.clientmanager.model.User;
import com.semeniuc.dmitrii.clientmanager.utils.Constants;
import com.semeniuc.dmitrii.clientmanager.utils.Utils;

public abstract class BaseActivity extends AppCompatActivity implements OnTaskCompleted {

    protected User user;
    protected Appointment appointment;
    protected DatabaseTaskHelper dbHelper;
    protected OnTaskCompleted listener;

    public abstract void initInstances();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initInstances();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(getMenuItem(), menu);
        return true;
    }

    protected int getMenuItem(){
        return R.menu.appointment_review_toolbar_menu;
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    protected Appointment setDataFromFields(Appointment appointment, String client, String phone,
                                         String address, String service, String sum, String info,
                                         String dateTime, Context context) {
        appointment.setUser(MyApplication.getInstance().getUser());
        appointment.getClient().setName(client);
        appointment.getClient().getContact().setPhone(phone);
        appointment.getClient().getContact().setAddress(address);
        appointment.getService().setName(service);
        appointment.setSum(sum);
        appointment.setInfo(info);
        appointment.setDate(Utils.convertStringToDate(dateTime, Constants.DATE_TIME_FORMAT, context));
        return appointment;
    }

    protected void changeImage(int image, Appointment appointment, AppCompatImageView imageView) {
        switch (image) {
            case Constants.PAID:
                if (appointment.isPaid()) imageView.setImageResource(R.mipmap.ic_money_paid_yes);
                else imageView.setImageResource(R.mipmap.ic_money_paid_no);
                break;
            case Constants.DONE:
                if (appointment.isDone()) imageView.setImageResource(R.mipmap.ic_ok_yes);
                else imageView.setImageResource(R.mipmap.ic_ok_no);
                break;
            case Constants.HAIR_COLORING:
                if (appointment.getService().isHairColoring())
                    imageView.setImageResource(R.mipmap.ic_paint_yes);
                else imageView.setImageResource(R.mipmap.ic_paint_no);
                break;
            case Constants.HAIRDO:
                if (appointment.getService().isHairdo())
                    imageView.setImageResource(R.mipmap.ic_womans_hair_yes);
                else imageView.setImageResource(R.mipmap.ic_womans_hair_no);
                break;
            case Constants.HAIR_CUT:
                if (appointment.getService().isHaircut())
                    imageView.setImageResource(R.mipmap.ic_scissors_yes);
                else imageView.setImageResource(R.mipmap.ic_scissors_no);
                break;
            case Constants.BRUSH:
                if (appointment.getTools().isBrush())
                    imageView.setImageResource(R.mipmap.ic_brush_yes);
                else imageView.setImageResource(R.mipmap.ic_brush_no);
                break;
            case Constants.HAIR_BRUSH:
                if (appointment.getTools().isHairBrush())
                    imageView.setImageResource(R.mipmap.ic_hair_brush_yes);
                else imageView.setImageResource(R.mipmap.ic_hair_brush_no);
                break;
            case Constants.HAIR_DRAYER:
                if (appointment.getTools().isHairDryer())
                    imageView.setImageResource(R.mipmap.ic_hair_dryer_yes);
                else imageView.setImageResource(R.mipmap.ic_hair_dryer_no);
                break;
            case Constants.OXY:
                if (appointment.getTools().isOxy())
                    imageView.setImageResource(R.mipmap.ic_soap_yes);
                else imageView.setImageResource(R.mipmap.ic_soap_no);
                break;
            case Constants.CUT_SET:
                if (appointment.getTools().isCutSet())
                    imageView.setImageResource(R.mipmap.ic_cutset_yes);
                else imageView.setImageResource(R.mipmap.ic_cutset_no);
                break;
            case Constants.HAIR_BAND:
                if (appointment.getTools().isHairBand())
                    imageView.setImageResource(R.mipmap.ic_hair_band_yes);
                else imageView.setImageResource(R.mipmap.ic_hair_band_no);
                break;
            case Constants.SPRAY:
                if (appointment.getTools().isSpray())
                    imageView.setImageResource(R.mipmap.ic_spray_yes);
                else imageView.setImageResource(R.mipmap.ic_spray_no);
                break;
            case Constants.TUBE:
                if (appointment.getTools().isTube())
                    imageView.setImageResource(R.mipmap.ic_tube_yes);
                else imageView.setImageResource(R.mipmap.ic_tube_no);
                break;
            case Constants.TRIMMER:
                if (appointment.getTools().isTrimmer())
                    imageView.setImageResource(R.mipmap.ic_trimmer_yes);
                else imageView.setImageResource(R.mipmap.ic_trimmer_no);
                break;
        }
    }
}
