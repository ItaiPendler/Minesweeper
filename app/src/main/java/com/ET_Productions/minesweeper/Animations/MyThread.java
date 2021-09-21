/*
 * Copyright (c) 2019. Belongs To Itai Pendler
 */

package com.ET_Productions.minesweeper.Animations;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;

public class MyThread extends Thread
{

    private boolean stop;
    private MyHandler myHandler;
    private String WINLOSE;

    public MyThread (MyHandler myHandler,String WINLOSE)
    {
        this.stop = true;
        this.myHandler = myHandler;
        this.WINLOSE = WINLOSE;
    }
    public void setStop(boolean stop){
        this.stop = stop;
    }

    public void run(String WINLOSE){
        //Log.d("C: MT, F: Run","");
        Log.d("C: MT, F: Run","Just Started");

        stop = false;
        int i = 1;
        int rounds = 5;
        while(rounds!=0&&!stop)
        {
            i++;
            if(WINLOSE.equals("WIN"))
            {
                if (i == 41)
                {
                    i = 1;
                    rounds--;
                }
                sendCounterToActivity(i,"WIN");
                try
                {
                    sleep(50,0);
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
            else
                {
                if(i==6)
                {
                    rounds--;

                    i = 1;
                }
                Log.d("C: MT, F: Run","just before sending the i to the activity for lose.");
                sendCounterToActivity(i,"LOSE");
                try {
                    Log.d("C: MT, F: Run","just before sleep for lose.");
                    sleep(50,0);
                }
                catch (InterruptedException e){
                        e.printStackTrace();
                }

            }
        }
        Log.d("C: MT, F: Run","finished run");

    }
    public void sendCounterToActivity(int i, String WINLOSE){
//        Log.d("C: MT, F: SCTA","");
        Log.d("C: MT, F: SCTA","just started");
        Message msg = myHandler.obtainMessage();
        Log.d("C: MT, F: SCTA","got message");
        Bundle data = msg.getData();
        data.putInt("count", i);
        Log.d("C: MT, F: SCTA","put i in data");
        data.putString("WINLOSE", WINLOSE);
        Log.d("C: MT, F: SCTA","put winlose in data");
        myHandler.sendMessage(msg);
        Log.d("C: MT, F: SCTA","messageSent");
    }


    @Override
    public void run(){
        run(WINLOSE);
    }
}
