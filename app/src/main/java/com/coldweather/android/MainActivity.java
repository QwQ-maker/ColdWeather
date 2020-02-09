package com.coldweather.android;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.coldweather.android.base.ActivityCollector;
import com.coldweather.android.base.BaseActivity;
import com.coldweather.android.city_manager.CityManagerActivity;
import com.coldweather.android.db.DBManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    ImageView addCityIv;
    ImageView moreIv;
    LinearLayout pointLayout;
    ViewPager mainVp;
    List<Fragment>fragmentList;
    List<String>cityList;
    List<ImageView>imageViewList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addCityIv=findViewById(R.id.main_iv_add);
        moreIv=findViewById(R.id.main_iv_more);
        pointLayout=findViewById(R.id.main_layout_point);
        mainVp=findViewById(R.id.main_vp);
        fragmentList=new ArrayList<>();
        cityList= DBManager.queryAllCityName();
        imageViewList=new ArrayList<>();
        if (cityList.size()==0){
            cityList.add("南宁");
        }
        Intent intent=getIntent();
        String city = intent.getStringExtra("city");
        if (!cityList.contains(city)&&!TextUtils.isEmpty(city)) {
            cityList.add(city);

        }
        initPaper();
        CityFragmentPaperAdapter cityFragmentPaperAdapter = new CityFragmentPaperAdapter(getSupportFragmentManager(),fragmentList);
        mainVp.setAdapter(cityFragmentPaperAdapter);
        initPoint();
        mainVp.setCurrentItem(fragmentList.size()-1);
        setPagerListener();
        addCityIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this, "长按卡片可删除城市", Toast.LENGTH_SHORT).show();
            }
        });

        moreIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCollector.finishAll();
            }
        });
    }

    private void setPagerListener() {
        mainVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i <imageViewList.size() ; i++) {
                    if (i==position){
                        imageViewList.get(i).setImageResource(R.mipmap.a2);
                    }
                    else {
                        imageViewList.get(i).setImageResource(R.mipmap.a1);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void initPoint() {
        for (int i = 0; i < fragmentList.size(); i++) {
            ImageView pIv=new ImageView(this);
            pIv.setImageResource(R.mipmap.a1);
            pIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            LinearLayout.LayoutParams lp= (LinearLayout.LayoutParams) pIv.getLayoutParams();
            lp.setMargins(0,0,20,0);
            imageViewList.add(pIv);
            pointLayout.addView(pIv);

        }
        imageViewList.get(imageViewList.size()-1).setImageResource(R.mipmap.a2);
    }

    private void initPaper() {
        for (int i = 0; i < cityList.size(); i++) {
            CityWeatherFragment cityWeatherFragment = new CityWeatherFragment();
            Bundle bundle = new Bundle();
            bundle.putString("city",cityList.get(i));
            cityWeatherFragment.setArguments(bundle);
            fragmentList.add(cityWeatherFragment);
        }
    }
}
