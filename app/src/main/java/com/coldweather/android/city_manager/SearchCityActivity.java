package com.coldweather.android.city_manager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.coldweather.android.MainActivity;
import com.coldweather.android.R;
import com.coldweather.android.base.BaseActivity;
import com.coldweather.android.bean.WeatherBean;
import com.google.gson.Gson;

public class SearchCityActivity extends BaseActivity implements View.OnClickListener {
    private EditText searchEt;
    private ImageView submitIv;
    private String city;
    private ArrayAdapter adapter;
    private String url1="http://api.map.baidu.com/telematics/v3/weather?location=";
    private String url2="&output=json&ak=FkPhtMBK0HTIQNh7gG4cNUttSTyr0nzo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_city);
        searchEt=findViewById(R.id.search_et);
        submitIv=findViewById(R.id.search_iv_submit);
        submitIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_iv_submit:
                city = searchEt.getText().toString();
                if (!TextUtils.isEmpty(city)) {
                    String url=url1+city+url2;
                    loadData(url);
                }else {
                    Toast.makeText(this, "Input cannot be empty", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onSuccess(String result) {
        Log.w("search", "onSuccess: " );
        WeatherBean weatherBean = new Gson().fromJson(result, WeatherBean.class);
        if (weatherBean.getError()==0) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("city",city);
            startActivity(intent);
        }else{
            Toast.makeText(this, "The city is not exist", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError(Throwable ex, boolean isOnCallback) {
        super.onError(ex, isOnCallback);
    }

    @Override
    public void onCancelled(CancelledException cex) {
        super.onCancelled(cex);
    }

    @Override
    public void onFinished() {
        super.onFinished();
    }
}
