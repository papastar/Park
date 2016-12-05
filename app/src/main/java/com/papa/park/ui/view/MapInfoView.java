package com.papa.park.ui.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.papa.park.R;

/**
 * User: PAPA
 * Date: 2016-12-05
 */

public class MapInfoView extends RelativeLayout {

    private TextView mNameTv;

    public MapInfoView(Context context) {
        super(context);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.map_info_view, this);
        mNameTv = (TextView) findViewById(R.id.name_tv);
    }

    public void setData(String title) {
        mNameTv.setText(title);
    }
}
