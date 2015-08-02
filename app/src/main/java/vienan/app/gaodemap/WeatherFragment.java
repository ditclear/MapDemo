package vienan.app.gaodemap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocalDayWeatherForecast;
import com.amap.api.location.AMapLocalWeatherForecast;
import com.amap.api.location.AMapLocalWeatherListener;
import com.amap.api.location.AMapLocalWeatherLive;
import com.amap.api.location.LocationManagerProxy;

import java.util.List;

/**
 * Created by lenovo on 2015/8/2.
 */
public class WeatherFragment extends Fragment implements AMapLocalWeatherListener {

    private LocationManagerProxy mLocationManagerProxy;
    private AMapLocalWeatherForecast aMapLocalWeatherForecast;
    private TextView mWeatherLocationTextView;// 天气预报地点
    private TextView mTodayTimeTextView;//
    private TextView mTomorrowTimeTextView;//
    private TextView mNextDayTimeTextView;//

    private TextView mTodayWeatherTextView;// 今天天气状况
    private TextView mTomorrowWeatherTextView;// 明天天气状况
    private TextView mNextDayWeatherTextView;// 后天天气状况
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.activity_future_weather_report,container,false);
        init(rootView);
        return rootView;
    }

    private void init(View rootView) {
        mLocationManagerProxy = LocationManagerProxy.getInstance(getActivity());
        //获取未来天气预报
        mLocationManagerProxy.requestWeatherUpdates(
                LocationManagerProxy.WEATHER_TYPE_FORECAST, this);
        mWeatherLocationTextView = (TextView) rootView.findViewById(R.id.future_weather_location_text);

        mTodayTimeTextView = (TextView) rootView.findViewById(R.id.today_time_text);
        mTodayWeatherTextView = (TextView) rootView.findViewById(R.id.today_weather_des_text);
        mTomorrowTimeTextView = (TextView) rootView.findViewById(R.id.tomorrow_time_text);
        mTomorrowWeatherTextView = (TextView) rootView.findViewById(R.id.tomorrow_weather_des_text);
        mNextDayTimeTextView = (TextView) rootView.findViewById(R.id.netx_day_time_text);
        mNextDayWeatherTextView = (TextView) rootView.findViewById(R.id.netx_day_des_text);
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationManagerProxy.destroy();
    }

    @Override
    public void onWeatherLiveSearched(AMapLocalWeatherLive aMapLocalWeatherLive) {
    }

    @Override
    public void onWeatherForecaseSearched(
            AMapLocalWeatherForecast aMapLocalWeatherForecast) {
        // 未来天气预报回调
        if (aMapLocalWeatherForecast != null
                &&aMapLocalWeatherForecast.getAMapException().getErrorCode() == 0) {
            this.aMapLocalWeatherForecast=aMapLocalWeatherForecast;
            List<AMapLocalDayWeatherForecast> forcasts = aMapLocalWeatherForecast
                    .getWeatherForecast();
            for (int i = 0; i < forcasts.size(); i++) {
                AMapLocalDayWeatherForecast forcast = forcasts.get(i);
                switch (i) {
                    case 0:
                        Log.i("address",forcast.getCity());
                        mWeatherLocationTextView.setText(forcast.getCity());
                        mTodayTimeTextView.setText("今天 ( " + forcast.getDate()
                                + " )");
                        mTodayWeatherTextView.setText(forcast.getDayWeather()
                                + "    " + forcast.getDayTemp() + "℃/"
                                + forcast.getNightTemp() + "℃    "
                                + forcast.getDayWindPower()+"级");

                        break;
                    case 1:
                        mTomorrowTimeTextView.setText("明天 ( " + forcast.getDate()
                                + " )");
                        mTomorrowWeatherTextView.setText(forcast.getDayWeather()
                                + "    " + forcast.getDayTemp() + "℃/"
                                + forcast.getNightTemp() + "℃    "
                                + forcast.getDayWindPower()+"级");
                        break;
                    case 2:
                        mNextDayTimeTextView.setText("后天 ( " + forcast.getDate()
                                + " )");
                        mNextDayWeatherTextView.setText(forcast.getDayWeather()
                                + "    " + forcast.getDayTemp() + "℃/"
                                + forcast.getNightTemp() + "℃    "
                                + forcast.getDayWindPower()+"级");
                        break;
                }
            }
        } else {

            // 获取天气预报失败
            Toast.makeText(
                    getActivity(),
                    "获取天气预报失败:"
                            + aMapLocalWeatherForecast.getAMapException()
                            .getErrorMessage(), Toast.LENGTH_SHORT)
                    .show();
        }
    }
}
