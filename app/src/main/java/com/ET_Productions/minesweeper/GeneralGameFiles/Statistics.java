/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.GeneralGameFiles;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.minesweeper.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Statistics extends Activity {

    private Player user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getActionBar().hide();

        Intent in = getIntent();
        user = (Player) in.getExtras().get("user");

        String gameD = user.getGameD();
        gameD = user.getGameD().substring(0, 1).toUpperCase() + user.getGameD().substring(1).toLowerCase();

        ListView resultsListView = (ListView) findViewById(R.id.listView1);

        LinkedHashMap<String, String> userStats = new LinkedHashMap<String, String>();

        userStats.put("Player Name", user.getName());
        //userStats.put("Duration Of Game", user.getTime());

        userStats.put("Duration Of Game", user.getTime());
        //userStats.put("Player Name", user.getName().toString());

        userStats.put("Game Difficulty", gameD);

        userStats.put("Number Of Flags", String.valueOf(user.getFlagNum() ) );

        userStats.put("Number Of Moves", String.valueOf(user.getMoves()));

        userStats.put("Date Of Game", user.getDate());

        userStats.put("Battery Level", String.valueOf(getBatteryPercentage(this)));

        List<HashMap<String, String>> listItems = new ArrayList<HashMap<String, String>>();
        SimpleAdapter adapter = new SimpleAdapter(this, listItems, R.layout.list_item,
                new String[]{"First Line", "Second Line"},
                new int[]{R.id.text1, R.id.text2});

        Iterator it = userStats.entrySet().iterator();
        while (it.hasNext()) {
            HashMap<String, String> resultsMap = new HashMap<String, String>();
            Map.Entry pair = (Map.Entry) it.next();
            resultsMap.put("First Line", pair.getKey().toString());
            resultsMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultsMap);
        }

        resultsListView.setAdapter(adapter);

    }
    public Statistics getThis(){
        return this;
    }

    public static int getBatteryPercentage(Context context) {

        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, iFilter);

        int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
        int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

        float batteryPct = level / (float) scale;

        return (int) (batteryPct * 100);
    }
}
