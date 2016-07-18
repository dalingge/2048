package com.dby.game;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.ktplay.open.KTAccountManager;
import com.ktplay.open.KTError;
import com.ktplay.open.KTLeaderboard;
import com.ktplay.open.KTLeaderboardPaginator;
import com.ktplay.open.KTPlay;
import com.ktplay.open.KTUser;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.tv_target_score)
    TextView tvTargetScore;
    @Bind(R.id.tv_goal_score)
    TextView tvGoalScore;
    @Bind(R.id.tv_height_score)
    TextView tvHeightScore;
    @Bind(R.id.game_view)
    GameView gameView;

    //最高分数
    private int mHighScore;
    // 目标分数
    private int mGoal;
    // Activity的引用
    private static MainActivity mMainActivity;

    public MainActivity() {
        mMainActivity = this;
    }

    /**
     * 获取当前Activity的引用
     *
     * @return Activity.this
     */
    public static MainActivity getmMainActivity() {
        return mMainActivity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        // 初始化View
        initView();

        KTPlay.startWithAppKey(this, AppConfig.APP_KEY, AppConfig.APP_SECRET);

        login();
    }

    private void initView() {
        mHighScore = AppConfig.mSp.getInt(AppConfig.KEY_HIGH_SCROE, 0);
        mGoal = AppConfig.mSp.getInt(AppConfig.KEY_GAME_GOAL, 2048);
        tvHeightScore.setText("最高得分\n\n" + mHighScore);
        tvTargetScore.setText("" + mGoal);
        tvGoalScore.setText("得分\n\n0");
        setScore(0, 0);
    }

    public void setGoal(int num) {
        tvTargetScore.setText(String.valueOf(num));
    }

    /**
     * 修改得分
     *
     * @param score score
     * @param flag  0 : score 1 : high score
     */
    public void setScore(int score, int flag) {
        switch (flag) {
            case 0:
                tvGoalScore.setText("得分\n\n" + score);

                if(1024==score){
                    KTPlay.showInterstitialNotification("1024",null);
                }
                break;
            case 1:
                tvHeightScore.setText("最高得分\n\n" + score);

                if(KTAccountManager.isLoggedIn()) {
                    KTLeaderboard.reportScore(score, "1", "", onReportScoreListener);
                }
                break;
            default:
                break;
        }
    }
    KTLeaderboard.OnReportScoreListener onReportScoreListener =
            new KTLeaderboard.OnReportScoreListener(){

                @Override
                public void onReportScoreResult(boolean isSuccess,
                  String leaderboardId, long score, String scoreTag, KTError error) {
                    if(isSuccess){
                        Toast.makeText(MainActivity.this, "report success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(MainActivity.this, "report failed", Toast.LENGTH_SHORT).show();
                    }
                }
            };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /**
     * 获取最高记录
     */
    private void getHighScore() {
        int score = AppConfig.mSp.getInt(AppConfig.KEY_HIGH_SCROE, 0);
        setScore(score, 1);
    }
    KTLeaderboard.OnGetLeaderboardListener onGetLeaderboardListener = new KTLeaderboard.OnGetLeaderboardListener(){
        @Override
        public void onGetLeaderboardResult(boolean isSuccess, String leaderboardId, KTLeaderboardPaginator leaderboard, KTError error) {
            if(isSuccess){
                String[] strings = new String[leaderboard.getUsers().size()];
                for (int i=0;i<leaderboard.getUsers().size();i++){
                    KTUser user= leaderboard.getUsers().get(i);
                    strings[i] = new String(user.getScore());
                }
                new MaterialDialog.Builder(MainActivity.this)
                        .items(strings)
                        .title("分数排行").show();
            }else{
                Toast.makeText(MainActivity.this, error.description,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    @OnClick({R.id.bt_score_list, R.id.bt_Restart,R.id.bt_setting})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_score_list:
                //获取游戏玩家排行榜数据
                KTLeaderboard.globalLeaderboard("1", 0, 10,onGetLeaderboardListener);
                break;
            case R.id.bt_Restart:
                gameView.startGame();
                setScore(0, 0);
                break;
            case R.id.bt_setting:
                new MaterialDialog.Builder(this)
                        .items("登录","登出","社区","分享游戏截图","分享游戏视频")
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                 switch (which){
                                     case 0:
                                         login();
                                         break;
                                     case 1:
                                         KTAccountManager.logout();
                                         break;
                                     case 2:
                                         KTPlay.show();
                                         break;
                                     case 3:
                                         KTPlay.shareScreenshotToKT("截图",
                                                 "噢噢噢噢哦哦哦哦哦哦");
                                         break;
                                     case 4:
                                         KTPlay.shareVideoToKT("",
                                                 "Game predfined title put in here",
                                                 "Game predfined meesage put in here");
                                         break;
                                 }
                            }
                        }).show();
                break;
        }
    }

    private void setKTListeners() {

        KTPlay.setOnActivityStatusChangedListener(new KTPlay.OnActivityStatusChangedListener() {
            @Override
            public void onActivityChanged(final boolean hasNewActivity) {
                new Handler(Looper.getMainLooper(), new Handler.Callback() {

                    @Override
                    public boolean handleMessage(Message msg) {
                        if (hasNewActivity) {
                           // showNewMsgHint();
                        } else {
                            //hideNewMsgHint();
                        }
                        return false;
                    }
                }).sendEmptyMessage(0);
            }
        });
    }

    private void login(){
        boolean isLoggedIn = KTAccountManager.isLoggedIn();
        if(isLoggedIn){
            Toast.makeText(this,"登录状态",Toast.LENGTH_SHORT).show();
        }else {
            KTAccountManager.showLoginView(true,
                    new KTAccountManager.KTLoginListener(){
                        @Override
                        public void onLoginResult(boolean isSuccess, KTUser user,
                                                  KTError error) {
                            if(isSuccess){
                                //login success

                            }else{
                                //login failed
                            }
                        }
                    });

        }

    }

    @Override
    public void onPause(){
        KTPlay.onPause(this);
        super.onPause();
    }

    @Override
    public void onResume(){
        KTPlay.onResume(this);
        super.onResume();
    }
}
