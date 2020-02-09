package com.coldweather.android.city_manager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.coldweather.android.MainActivity;
import com.coldweather.android.R;
import com.coldweather.android.base.BaseActivity;
import com.coldweather.android.db.DBManager;
import com.coldweather.android.db.DatabaseBean;

import java.util.ArrayList;
import java.util.List;

public class CityManagerActivity extends BaseActivity implements OnClickListener {
    ImageView addIv;
    ImageView backIv;
    ListView cityLv;
    List<DatabaseBean> databaseBeans;
    CityManagerAdapter cityManagerAdapter;
    List<String>cityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        addIv=findViewById(R.id.city_iv_add);
        backIv=findViewById(R.id.city_iv_back);
        cityLv=findViewById(R.id.city_lv);
        databaseBeans=new ArrayList<>();
        addIv.setOnClickListener(this);
        backIv.setOnClickListener(this);
        cityManagerAdapter = new CityManagerAdapter(this, databaseBeans);
        cityLv.setAdapter(cityManagerAdapter);
        cityList=DBManager.queryAllCityName();
        cityLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(CityManagerActivity.this);
                builder.setTitle("提示信息").setMessage("是否要删除").setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String cityName=cityList.get(position);
                        DBManager.deleteInfoByCity(cityName);
                        Log.w("Test", "onClick: click" );
                        Intent intent=new Intent(CityManagerActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("取消",null);
                builder.create().show();
                return true;
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        List<DatabaseBean>list=DBManager.queryAllInfo();
        databaseBeans.clear();
        databaseBeans.addAll(list);
        cityManagerAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.city_iv_add:
                if (DBManager.getCityCount()<5) {
                    Intent intent = new Intent(this, SearchCityActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this, "Too much city", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.city_iv_back:
                finish();
                break;
        }
    }
}
