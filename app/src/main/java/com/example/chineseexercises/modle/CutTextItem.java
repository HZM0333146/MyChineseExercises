package com.example.chineseexercises.modle;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class CutTextItem {
    private Context cutTextContext;
    private RelativeLayout.LayoutParams params;
    private TextView ctiTextView;
    private CutTextBean cutTextBean;
    private boolean cutStatus=false;
    int mContextWidth,mContextHigh;

    public CutTextItem(Context mCutTextContext, CutTextBean cutTextData) {
        this.cutTextContext = mCutTextContext;
        this.cutTextBean=cutTextData;
        this.mContextWidth=mContextWidth;
        this.mContextHigh=mContextHigh;
        initTextView();
    }
    public void initTextView(){
        ctiTextView=new TextView(cutTextContext);
        ctiTextView.setText(cutTextBean.getCtiName());
        ctiTextView.setTextSize(cutTextBean.getTextSize());
    }
    public TextView getCutTextView(){
        return ctiTextView;
    }
    public CutTextBean getCutTextBean(){
        return cutTextBean;
    }
    //碰撞狀態
    void setCutStatus(boolean cutStatus) {
        this.cutStatus = cutStatus;
    }
    boolean getCutStatus() {
        return cutStatus;
    }
    //顯示部件散落彈跳的方式
    public Map<String,int[]> getTextMoves(CutTextItem cutTextItem, int startTextX, int startTextY) {
        CutTextBean cutTextData=cutTextItem.getCutTextBean();
        int textSizeContext=cutTextData.getTextSize();
        char[] partArray=cutTextData.getCtiNamePoje();
        Map<String,int[]> stringMap=new HashMap<>();
        switch (cutTextData.getCtiStructureCode()) {
            //左右結構
            case LeftAndRight:
                stringMap.put(String.valueOf(partArray[0]),lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]),lowerRightCurve(startTextX, startTextY));
                break;
            //上下結構
            case UpAndDown:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerRightCurve(startTextX, startTextY + (textSizeContext / 2)));
                break;
            //左中右結構
            case LeftMiddleRight:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), middleCurve(startTextX + (textSizeContext / 3), startTextY));
                stringMap.put(String.valueOf(partArray[2]), lowerRightCurve(startTextX + (textSizeContext / 3 * 2), startTextY));
                break;
            //上中下結構
            case UpDown:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), middleCurve(startTextX, startTextY + (textSizeContext / 3)));
                stringMap.put(String.valueOf(partArray[2]), lowerRightCurve(startTextX, startTextY + (textSizeContext / 3 * 2)));
                break;
            //左上至右下結構
            case TopLeftToBottomRight:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerRightCurve(startTextX, startTextY));
                break;
            //左下至右上結構
            case BottomLeftToTopRight:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerRightCurve(startTextX, startTextY));
                break;
            //上一下二結構
            case LastTwo:
                stringMap.put(String.valueOf(partArray[0]), middleCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerLeftCurve(startTextX, startTextY + (textSizeContext)));
                stringMap.put(String.valueOf(partArray[2]), lowerRightCurve(startTextX + (textSizeContext), startTextY + (textSizeContext)));
                break;
            //左一右二結構
            case LeftOneRightTwo:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerRightCurve(startTextX + (textSizeContext), startTextY));
                stringMap.put(String.valueOf(partArray[2]), lowerRightCurve(startTextX + (textSizeContext), startTextY + (textSizeContext)));
                break;
            //包圍字結構
            case SurroundWord:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerRightCurve(startTextX, startTextY));
                break;
            //特殊結構
            case  Special:
                int[][] randomPath = new int[partArray.length][6];
                int randomPathStarX = startTextX;
                Random random=new Random();
                for (int i = 0; i < randomPath.length; i++) {
                    switch (random.nextInt(3)) {
                        case 0:
                            randomPath[i] = lowerLeftCurve(randomPathStarX, startTextY);
                            break;
                        case 1:
                            randomPath[i] = middleCurve(randomPathStarX, startTextY);
                            break;
                        case 2:
                            randomPath[i] = lowerRightCurve(randomPathStarX, startTextY);
                            break;
                    }
                    randomPathStarX += textSizeContext;
                }
                for (int i = 0; i < partArray.length; i++) {
                    stringMap.put(String.valueOf(partArray[i]), randomPath[i]);
                }
                break;
            //上二下一
            case PreviousTwoNext:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                stringMap.put(String.valueOf(partArray[1]), lowerRightCurve(startTextX + (textSizeContext), startTextY));
                stringMap.put(String.valueOf(partArray[2]), middleCurve(startTextX, startTextY + (textSizeContext)));
                break;
            default:
                stringMap.put(String.valueOf(partArray[0]), lowerLeftCurve(startTextX, startTextY));
                break;
        }
        return stringMap;
    }
    //上拋曲線
    private int[] throwOnTheMove(int textSizeContext) {
        int[] throwOnTheMove = new int[6];
        //1起點值
        throwOnTheMove[0] = mContextWidth-textSizeContext;
        throwOnTheMove[1] = mContextHigh;
        //1終點值
        throwOnTheMove[4] = 0;
        throwOnTheMove[5] = mContextHigh;
        //1中間值
        throwOnTheMove[2] = mContextWidth / 2;
        throwOnTheMove[3] = -(mContextHigh / 2);
        return throwOnTheMove;
    }

    //左下曲線
    private int[] lowerLeftCurve(int startTextX, int startTextY) {
        int[] lowerLeftCurve = new int[6];
        //1起點值
        lowerLeftCurve[0] = startTextX;
        lowerLeftCurve[1] = startTextY;
        //1終點值
        lowerLeftCurve[4] = startTextX;
        lowerLeftCurve[5] = mContextHigh;
        //1中間值
        lowerLeftCurve[2] = startTextX / 2;
        lowerLeftCurve[3] = startTextY + ((mContextHigh - startTextY) / 2);
        return lowerLeftCurve;
    }

    //中下曲線
    private int[] middleCurve(int startTextX, int startTextY) {
        int[] middleCurve = new int[6];
        //1起點值
        middleCurve[0] = startTextX;
        middleCurve[1] = startTextY;
        //1終點值
        middleCurve[4] = startTextX;
        middleCurve[5] = mContextHigh;
        //1中間值
        middleCurve[2] = startTextX;
        middleCurve[3] = startTextY + ((mContextHigh - startTextY) / 2);
        return middleCurve;
    }

    //右下曲線
    private int[] lowerRightCurve(int startTextX, int startTextY) {
        int[] lowerRightCurve = new int[6];
        //起點值x,y
        lowerRightCurve[0] = startTextX;
        lowerRightCurve[1] = startTextY;
        //終點值x,y
        lowerRightCurve[4] = lowerRightCurve[0];
        lowerRightCurve[5] = mContextHigh;
        //中間值x,y
        lowerRightCurve[2] = lowerRightCurve[0] + ((mContextWidth - lowerRightCurve[0]) / 2);
        lowerRightCurve[3] = startTextY + ((mContextHigh - startTextY) / 2);
        return lowerRightCurve;
    }
}
