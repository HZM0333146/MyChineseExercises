package com.example.chineseexercises.modle;

import android.graphics.Point;

public class CutTextBean {
    private char ctiName;
    private char[] ctiNamePoj;
    private CutTextStructure ctiStructureCode;
    private int textSize;
    private Point startPosition, centerPosition, endPosition;
    CutTextBean(char ctiName, char[] ctiNamePoj, CutTextStructure ctiStructureCode){
        this(ctiName,ctiNamePoj,ctiStructureCode,18);

    }
    CutTextBean(char ctiName, char[] ctiNamePoj, CutTextStructure ctiStructureCode, int textSize){
        this.ctiName=ctiName;
        this.ctiNamePoj=ctiNamePoj;
        this.ctiStructureCode=ctiStructureCode;
        this.textSize=textSize;
    }
    void setCtiName(char ctiName){
        this.ctiName=ctiName;
    }
    void setCtiNamePoje(char[] ctiNamePoj){
        this.ctiNamePoj=ctiNamePoj;
    }
    void setCtiStructureCode(CutTextStructure ctiStructureCode){
        this.ctiStructureCode=ctiStructureCode;
    }
    void setTextSize(int textSize){
        this.textSize=textSize;
    }
    char getCtiName(){
        return ctiName;
    }
    char[] getCtiNamePoje(){
        return ctiNamePoj;
    }
    CutTextStructure getCtiStructureCode(){
        return ctiStructureCode;
    }
    int getTextSize(){
        return textSize;
    }
    //設定起點值
    void setStartPosition(int startPositionX, int startPositionY) {
        this.startPosition = new Point(startPositionX, startPositionY);
    }
    //設定中間值
    void setCenterPosition(int centerPositionX, int centerPositionY) {
        this.centerPosition = new Point(centerPositionX, centerPositionY);
    }
    //設定終點值
    void setEndPosition(int endPositionX, int endPositionY) {
        this.endPosition = new Point(endPositionX, endPositionY);
    }
    void setPosition(int[] positionArray) {
        setStartPosition(positionArray[0], positionArray[1]);
        setCenterPosition(positionArray[2], positionArray[3]);
        setEndPosition(positionArray[4], positionArray[5]);
    }
}
