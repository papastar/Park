package com.papa.park.entity.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.papa.park.R;

import com.papa.park.ui.activity.RentDetailActivity;
import com.papa.park.utils.KeyConstant;

import java.util.List;
import java.util.Map;

/**
 * User: PAPA
 * Date: 2016-12-06
 */

public class LockerPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<CloudPoiInfo> mInfoList;

    public LockerPagerAdapter(Context context, List<CloudPoiInfo> infoList) {
        mContext = context;
        mInfoList = infoList;
    }

    public List<CloudPoiInfo> getCloudPoiInfoList() {
        return mInfoList;
    }

    public int getPosition(CloudPoiInfo info) {
        if (mInfoList != null)
            return mInfoList.indexOf(info);
        return 0;

    }


    @Override
    public int getCount() {
        return mInfoList != null ? mInfoList.size() : 0;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final CloudPoiInfo cloudPoiInfo = mInfoList.get(position);
        View view = LayoutInflater.from(mContext).inflate(R.layout.locker_pager_item, container,
                false);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RentDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(KeyConstant.KEY_TYPE, 1);
                bundle.putInt(KeyConstant.KEY_DATA, cloudPoiInfo.uid);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        TextView nameTv = (TextView) view.findViewById(R.id.locker_name_tv);
        nameTv.setText(cloudPoiInfo.title);
        TextView infoTv = (TextView) view.findViewById(R.id.info_tv);
        Map<String, Object> extras = cloudPoiInfo.extras;
        if (extras != null) {
            String rentFirstHourPrice = (String) extras.get("rentFirstHourPrice");
            String rentPerHourPrice = (String) extras.get("rentPerHourPrice");
            infoTv.setText(mContext.getString(R.string.rent_price_of,
                    rentFirstHourPrice, rentPerHourPrice));
        }

        container.addView(view);
        return view;
    }

}
