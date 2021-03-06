package vienan.app.gaodemap.activity.fragment;

import android.graphics.PixelFormat;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;

import vienan.app.gaodemap.R;

/**
 * Created by lenovo on 2015/7/31.
 */
public class MapFragment extends Fragment implements LocationSource,AMapLocationListener,RadioGroup.OnCheckedChangeListener {
    private LocationManagerProxy mLocationManagerProxy;
    private AMap aMap;
    MapView mapView;
    private OnLocationChangedListener mListener;
    private LocationManagerProxy mAMapLocationManager;
    private RadioGroup radioGroup;
    private int currentColor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.locationmodesource_activity,container,false);
        mapView= (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        radioGroup= (RadioGroup) rootView.findViewById(R.id.gps_radio_group);
        init();
        return rootView;
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            setUpMap();
        }

        radioGroup.setOnCheckedChangeListener(this);
    }
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // 设置定位的类型为定位模式：定位（AMap.LOCATION_TYPE_LOCATE）、跟随（AMap.LOCATION_TYPE_MAP_FOLLOW）
        // 地图根据面向方向旋转（AMap.LOCATION_TYPE_MAP_ROTATE）三种模式
        //设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
        // 设置定位的类型为定位模式

        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation.getAMapException().getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mAMapLocationManager == null) {
            mAMapLocationManager = LocationManagerProxy.getInstance(getActivity());
            //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
            //在定位结束后，在合适的生命周期调用destroy()方法
            //其中如果间隔时间为-1，则定位只定一次
            mAMapLocationManager.requestLocationData(
                    LocationProviderProxy.AMapNetwork, 60*1000, 10, this);
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mAMapLocationManager != null) {
            mAMapLocationManager.removeUpdates(this);
            mAMapLocationManager.destroy();
        }
        mAMapLocationManager = null;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch(checkedId){
            case R.id.gps_locate_button:
                //设置定位的类型为定位模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
                break;
            case R.id.gps_follow_button:
                //设置定位的类型为 跟随模式
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
                break;
            case R.id.gps_rotate_button:
                //设置定位的类型为根据地图面向方向旋转
                aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
                break;
        }
    }
}
