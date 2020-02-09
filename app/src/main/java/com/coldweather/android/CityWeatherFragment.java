package com.coldweather.android;


import android.app.AlertDialog;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.coldweather.android.base.BaseFragment;
import com.coldweather.android.bean.WeatherBean;
import com.coldweather.android.db.DBManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;


public class CityWeatherFragment extends BaseFragment implements View.OnClickListener {
    TextView tempTv;
    TextView cityTv;
    TextView conditionTv;
    TextView windTv;
    TextView tempRangeTv;
    TextView dateTv;
    TextView clothIndexTv;
    TextView carIndexTv;
    TextView coldIndexTv;
    TextView sportIndexTv;
    TextView raysIndexTv;
    ImageView dayIv;
    LinearLayout futureLayout;
    String city;
    String url1="http://api.map.baidu.com/telematics/v3/weather?location=";
    String url2="&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    private List<WeatherBean.ResultsBean.IndexBean> indexList;

    @Override
    public void onSuccess(String result) {
        Log.w(TAG, "onSuccess: succeeded" );
        parseShowData(result);
        if (DBManager.updateInfoByCity(city,result)<=0){
            DBManager.addCityInfo(city,result);
        }
    }

    private void parseShowData(String result) {
        Log.w(TAG, "parseShowData: start" );
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        WeatherBean.ResultsBean resultsBean = weatherBean.getResults().get(0);
        indexList = resultsBean.getIndex();
        dateTv.setText(weatherBean.getDate());
        cityTv.setText(resultsBean.getCurrentCity());
        WeatherBean.ResultsBean.WeatherDataBean todayDataBean = resultsBean.getWeather_data().get(0);
        windTv.setText(todayDataBean.getWind());
        tempRangeTv.setText(todayDataBean.getTemperature());
        conditionTv.setText(todayDataBean.getWeather());
        String[] split = todayDataBean.getDate().split("：");
        String todayTemp = split[1].replace(")", "");
        tempTv.setText(todayTemp);
        Picasso.with(getActivity()).load(todayDataBean.getDayPictureUrl()).into(dayIv);
        List<WeatherBean.ResultsBean.WeatherDataBean> futureList = resultsBean.getWeather_data();
        futureList.remove(0);
        Log.w(TAG, "parseShowData: "+futureList.size() );
        for (int i=0;i<futureList.size();i++) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_main_center, null);
            view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            futureLayout.addView(view);
            TextView idateTv=view.findViewById(R.id.item_center_tv_date);
            TextView iconTv=view.findViewById(R.id.item_center_tv_con);
            TextView itemprangeTv=view.findViewById(R.id.item_center_tv_temp);
            ImageView iIv=view.findViewById(R.id.item_center_iv);
            WeatherBean.ResultsBean.WeatherDataBean dataBean = futureList.get(i);
            idateTv.setText(dataBean.getDate());
            iconTv.setText(dataBean.getWeather());
            itemprangeTv.setText(dataBean.getTemperature());
            Picasso.with(getActivity()).load(dataBean.getDayPictureUrl()).into(iIv);
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
        Log.w(TAG, "onError: onFail" );
    }

    @Override
    public void onCancelled(CancelledException cex) {
        super.onCancelled(cex);
    }

    @Override
    public void onFinished() {
        super.onFinished();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_city_weather,container,false);
        initView(view);
        Bundle bundle=getArguments();
        city=bundle.getString( "city");
        String url=url1+city+url2;
        Log.w(TAG, "onCreateView: "+url );
        loadData(url);
        return view;
    }
    private void initView(View view){
        tempTv = view.findViewById(R.id.frag_tv_currenttemp);
        cityTv = view.findViewById(R.id.frag_tv_city);
        conditionTv = view.findViewById(R.id.frag_tv_condition);
        windTv = view.findViewById(R.id.frag_tv_wind);
        tempRangeTv = view.findViewById(R.id.frag_tv_temprange);
        dateTv = view.findViewById(R.id.frag_tv_date);
        clothIndexTv = view.findViewById(R.id.frag_index_tv_dress);
        carIndexTv = view.findViewById(R.id.frag_index_tv_washcar);
        coldIndexTv = view.findViewById(R.id.frag_index_tv_cold);
        sportIndexTv = view.findViewById(R.id.frag_index_tv_sport);
        raysIndexTv = view.findViewById(R.id.frag_index_tv_rays);
        dayIv = view.findViewById(R.id.frag_iv_today);
        futureLayout = view.findViewById(R.id.frag_center_layout);
        clothIndexTv.setOnClickListener(this);
        carIndexTv.setOnClickListener(this);
        coldIndexTv.setOnClickListener(this);
        sportIndexTv.setOnClickListener(this);
        raysIndexTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.frag_index_tv_dress:
                builder.setTitle("穿衣指数");
                WeatherBean.ResultsBean.IndexBean indexBean = indexList.get(0);
                String msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_washcar:
                builder.setTitle("洗车指数");
                indexBean = indexList.get(1);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_cold:
                builder.setTitle("感冒指数");
                indexBean = indexList.get(2);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_sport:
                builder.setTitle("运动指数");
                indexBean = indexList.get(3);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
            case R.id.frag_index_tv_rays:
                builder.setTitle("紫外线指数");
                indexBean = indexList.get(4);
                msg = indexBean.getZs()+"\n"+indexBean.getDes();
                builder.setMessage(msg);
                builder.setPositiveButton("确定",null);
                break;
        }
        builder.create().show();
    }
}
