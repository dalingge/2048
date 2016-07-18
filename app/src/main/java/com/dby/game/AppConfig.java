package com.dby.game;

import android.app.Application;
import android.content.SharedPreferences;

/**
 * FileName:AppConfig.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/7/18
 */
public class AppConfig extends Application{

    public static SharedPreferences mSp;
    public static int mGameGoal;
    /**
     *  行数
     */
    public static int mGameLines;
    /**
     * item宽高
     */
    public static int mItemSize;

    /**
     * 记录分数
     */
    public static int SCROE = 0;

    public static String SP_HIGH_SCROE = "SP_HIGH_SCROE";

    public static String KEY_HIGH_SCROE = "KEY_HIGH_SCROE";

    public static String KEY_GAME_LINES = "KEY_GAME_LINES";

    public static String KEY_GAME_GOAL = "KEY_GAME_GOAL";

    //Replace the value to your app key.
    public static String APP_KEY = "UHpRhmtBU";

    //Replace the value to your app secret.
    public static String APP_SECRET = "ad01bf43ac2bcb024fa5046cf55c9379f270b1c9";

    @Override
    public void onCreate() {
        super.onCreate();
        mSp = getSharedPreferences(SP_HIGH_SCROE,0);
        mGameLines = mSp.getInt(KEY_GAME_LINES,4);
        mGameGoal = mSp.getInt(KEY_GAME_GOAL,2048);
        mItemSize = 4;


    }
}
