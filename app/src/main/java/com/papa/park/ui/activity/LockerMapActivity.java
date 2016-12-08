package com.papa.park.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.cloud.CloudListener;
import com.baidu.mapapi.cloud.CloudManager;
import com.baidu.mapapi.cloud.CloudPoiInfo;
import com.baidu.mapapi.cloud.CloudRgcResult;
import com.baidu.mapapi.cloud.CloudSearchResult;
import com.baidu.mapapi.cloud.DetailSearchResult;
import com.baidu.mapapi.cloud.LocalSearchInfo;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.papa.libcommon.base.BaseAppCompatActivity;
import com.papa.park.R;
import com.papa.park.api.BaiduConfig;
import com.papa.park.data.LocationInfo;
import com.papa.park.data.LocationManager;
import com.papa.park.entity.adapter.LockerPagerAdapter;
import com.papa.park.ui.view.MapInfoView;
import com.papa.park.ui.view.ZoomOutPageTransformer;
import com.papa.park.utils.StringUtil;

import java.util.LinkedHashMap;
import java.util.List;

import butterknife.Bind;

public class LockerMapActivity extends BaseAppCompatActivity implements CloudListener, BaiduMap
        .OnMarkerClickListener {

    @Bind(R.id.toolBar)
    Toolbar mToolBar;

    @Bind(R.id.map_view)
    MapView mMapView;
    // 定位相关
    LocationClient mLocClient;
    BitmapDescriptor mCurrentMarker;
    boolean isFirstLoc = true; // 是否首次定位
    @Bind(R.id.pager_layout)
    FrameLayout mPagerLayout;

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    LockerPagerAdapter mLockerPagerAdapter;
    private BaiduMap mBaiduMap;
    private LocationListener mListener = new LocationListener();
    private LinkedHashMap<Marker, CloudPoiInfo> mPoiInfoLinkedHashMap = new LinkedHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void loadLBS() {
        LocalSearchInfo info = new LocalSearchInfo();
        info.ak = BaiduConfig.SERVER_AK;
        info.geoTableId = StringUtil.parseInt(BaiduConfig.GEOTABLE_ID);
        info.tags = "";
        info.filter = "lockerPhone:13418459758";
        info.region = LocationManager.getInstance().getLocationInfo().getCityName();
        CloudManager.getInstance().localSearch(info);
    }


    private String getLocation() {
        LocationInfo locationInfo = LocationManager.getInstance().getLocationInfo();
        return locationInfo.getLongitude() + "," + locationInfo.getLatitude();

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_locker_map;
    }

    @Override
    protected void initViewsAndEvents() {
        setToolbar(mToolBar, "寻找车位");
        initMap();
        CloudManager.getInstance().init(this);
        loadLBS();
    }

    private void initMap() {

        // 地图初始化
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setOnMarkerClickListener(this);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mCurrentMarker = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.COMPASS, true, null));


        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(mListener);
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }


    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        if (mMapView != null) {
            mMapView.onDestroy();
            mMapView = null;
        }


        CloudManager.getInstance().destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onGetSearchResult(CloudSearchResult result, int i) {
        if (result != null && result.poiList != null
                && result.poiList.size() > 0) {
            mPoiInfoLinkedHashMap.clear();
            mBaiduMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
            LatLng ll;
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (CloudPoiInfo info : result.poiList) {
                ll = new LatLng(info.latitude, info.longitude);
                OverlayOptions oo = new MarkerOptions().icon(bd).position(ll).animateType
                        (MarkerOptions.MarkerAnimateType.drop);
                Marker marker = (Marker) mBaiduMap.addOverlay(oo);
                mPoiInfoLinkedHashMap.put(marker, info);
                builder.include(ll);
            }
            LatLngBounds bounds = builder.build();
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds);
            mBaiduMap.animateMapStatus(u);

            bindViewPager(result.poiList);
        }
    }

    @Override
    public void onGetDetailSearchResult(DetailSearchResult detailSearchResult, int i) {

    }

    @Override
    public void onGetCloudRgcResult(CloudRgcResult cloudRgcResult, int i) {

    }

    private void bindViewPager(List<CloudPoiInfo> infoList) {
        mLockerPagerAdapter = new LockerPagerAdapter(this, infoList);
        mViewPager.setAdapter(mLockerPagerAdapter);
        mViewPager.setPageTransformer(false, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(infoList.size());
        mViewPager.setPageMargin(TypedValue.complexToDimensionPixelSize(20, getResources()
                .getDisplayMetrics()));
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int
                    positionOffsetPixels) {
                if (mPagerLayout != null) {
                    mPagerLayout.invalidate();
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (mPoiInfoLinkedHashMap.containsKey(marker)) {
            CloudPoiInfo cloudPoiInfo = mPoiInfoLinkedHashMap.get(marker);
            if (cloudPoiInfo != null) {
                MapInfoView view = new MapInfoView(this);
                view.setData(cloudPoiInfo.title);
                InfoWindow infoWindow = new InfoWindow(view, marker.getPosition(), -47);
                mBaiduMap.showInfoWindow(infoWindow);
                return true;
            }
        }

        return false;
    }


    /**
     * 定位SDK监听函数
     */
    private class LocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    //.direction(100)
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }
}
