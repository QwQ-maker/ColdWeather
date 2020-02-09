package com.coldweather.android.city_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.coldweather.android.R;
import com.coldweather.android.bean.WeatherBean;
import com.coldweather.android.db.DatabaseBean;
import com.google.gson.Gson;

import java.util.List;

public class CityManagerAdapter extends BaseAdapter {
    Context context;
    List<DatabaseBean> databaseBeans;

    public CityManagerAdapter(Context context, List<DatabaseBean> databaseBeans) {
        this.context = context;
        this.databaseBeans = databaseBeans;
    }

    @Override
    public int getCount() {
        return databaseBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return databaseBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=null;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_city_manager,null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        else{
            holder= (ViewHolder) convertView.getTag();

        }
        DatabaseBean databaseBean = databaseBeans.get(position);
        holder.cityTv.setText(databaseBean.getCity());
        WeatherBean weatherBean = new Gson().fromJson(databaseBean.getContent(), WeatherBean.class);
        WeatherBean.ResultsBean.WeatherDataBean weatherDataBean = weatherBean.getResults().get(0).getWeather_data().get(0);
        holder.conTv.setText("天气"+weatherDataBean.getWeather());
        String[] split = weatherDataBean.getDate().split("：");
        String todayTemp = split[1].replace(")", "");
        holder.currentTempTv.setText(todayTemp);
        holder.windTv.setText(weatherDataBean.getWind());
        holder.tempRangeTv.setText(weatherDataBean.getTemperature());
        return convertView;
    }

    class ViewHolder{
        TextView cityTv;
        TextView conTv;
        TextView currentTempTv;
        TextView windTv;
        TextView tempRangeTv;

        public ViewHolder(View view) {
            cityTv=view.findViewById(R.id.item_city_tv_city);
            conTv=view.findViewById(R.id.item_city_tv_condition);
            currentTempTv=view.findViewById(R.id.item_city_tv_temp);
            windTv=view.findViewById(R.id.item_city_wind);
            tempRangeTv=view.findViewById(R.id.item_city_temprange);

        }
    }
}
