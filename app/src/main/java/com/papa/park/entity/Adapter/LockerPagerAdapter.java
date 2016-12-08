package com.papa.park.entity.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.papa.park.R;
import com.papa.park.ui.activity.ReserveLockerActivity;
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
                Intent intent = new Intent(mContext, ReserveLockerActivity.class);
                intent.putExtra(KeyConstant.KEY_DATA, cloudPoiInfo.uid);
                mContext.startActivity(intent);
            }
        });
        TextView nameTv = (TextView) view.findViewById(R.id.name_tv);
        nameTv.setText(cloudPoiInfo.title);
        TextView infoTv = (TextView) view.findViewById(R.id.info_tv);
        Map<String, Object> extras = cloudPoiInfo.extras;
        if (extras != null && extras.containsKey("firstStopPrice")) {
            Object firstStopPrice = extras.get("firstStopPrice");
            infoTv.setText(mContext.getString(R.string.price_of, firstStopPrice));
        }

        container.addView(view);
        return view;
    }

}
