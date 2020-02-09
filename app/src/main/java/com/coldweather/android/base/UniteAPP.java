package com.coldweather.android.base;

import android.app.Application;

import androidx.cardview.widget.CardView;

import com.coldweather.android.db.DBManager;

import org.xutils.x;

public class UniteAPP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        DBManager.initDB(this);
    }
}
