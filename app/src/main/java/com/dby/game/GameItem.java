package com.dby.game;


import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * FileName:GameItem.java
 * Description:
 * Author:dingboyang
 * Email:445850053@qq.com
 * Date:16/7/18
 */
public class GameItem extends FrameLayout{

    private  int mCardShowNum;
    private TextView mTvNum;
    private LayoutParams mParams;
    public GameItem(Context context,int cardShowNum) {
        super(context);
        this.mCardShowNum=cardShowNum;
        initCardItem();
    }

    private void initCardItem(){
        setBackgroundResource(R.color.gameBg);
        mTvNum=new TextView(getContext());
        setNum(mCardShowNum);
        mTvNum.getPaint().setFakeBoldText(true);
        mTvNum.setGravity(Gravity.CENTER);
        mTvNum.setTextSize(28);
        mParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mParams.setMargins(5,5,5,5);
        addView(mTvNum,mParams);
    }

    public void setNum(int num) {
        this.mCardShowNum = num;
        if(num==0){
            mTvNum.setText("");
        }else {
            mTvNum.setText(" " + num);
        }

        switch (num){
            case 0:
                mTvNum.setBackgroundResource(R.color.card0);
                break;
            case 2:
                mTvNum.setBackgroundResource(R.color.card2);
                break;
            case 4:
                mTvNum.setBackgroundResource(R.color.card4);
                break;
            case 8:
                mTvNum.setBackgroundResource(R.color.card8);
                break;
            case 16:
                mTvNum.setBackgroundResource(R.color.card16);
                break;
            case 32:
                mTvNum.setBackgroundResource(R.color.card32);
                break;
            case 64:
                mTvNum.setBackgroundResource(R.color.card64);
                break;
            case 128:
                mTvNum.setBackgroundResource(R.color.card128);
                break;
            case 256:
                mTvNum.setBackgroundResource(R.color.card256);
                break;
            case 512:
                mTvNum.setBackgroundResource(R.color.card512);
                break;
            case 1024:
                mTvNum.setBackgroundResource(R.color.card1024);
                break;
            case 2048:
                mTvNum.setBackgroundResource(R.color.card2048);
                break;
            default:
                mTvNum.setBackgroundResource(R.color.card);
                break;
        }
    }

    public View getItemView() {
        return mTvNum;
    }

    public int getNum() {
        return mCardShowNum;
    }
}
