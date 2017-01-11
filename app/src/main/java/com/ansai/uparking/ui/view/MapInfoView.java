package com.ansai.uparking.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.ansai.uparking.R;

/**
 * User: PAPA
 * Date: 2016-12-05
 */

public class MapInfoView extends RelativeLayout {

    private TextView mNameTv;
    private TextView mInfoTv;


    public MapInfoView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.map_info_view, this);
        mNameTv = (TextView) findViewById(R.id.locker_name_tv);
        mInfoTv = (TextView) findViewById(R.id.info_tv);
    }

    public void setData(String title) {
        mNameTv.setText(title);
    }

    public void setData(CloudPoiInfo info) {
        mNameTv.setText(info.title);
        if (info.extras != null) {
            if (info.extras.containsKey("rentFirstHourPrice") && info.extras.containsKey
                    ("rentPerHourPrice")) {
                String rentFirstHourPrice = (String) info.extras.get("rentFirstHourPrice");
                String rentPerHourPrice = (String) info.extras.get("rentPerHourPrice");
                mInfoTv.setText(getResources().getString(R.string.rent_price_of,
                        rentFirstHourPrice, rentPerHourPrice));
            }
        }
    }

}
