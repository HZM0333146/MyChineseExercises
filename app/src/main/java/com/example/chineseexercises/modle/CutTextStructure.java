package com.example.chineseexercises.modle;
/*紀錄畫過的結構紀錄
0.獨體字、1.左右、2.上下、  3.左中右、4.上中下
5.左上至右下、6.左下至右上、7.上一下二
8.左一右二、9.包圍字 10.特殊 11.上二下一
*/
public enum CutTextStructure {
    Solitary,
    LeftAndRight,
    UpAndDown,
    LeftMiddleRight,
    UpDown,
    TopLeftToBottomRight,
    BottomLeftToTopRight,
    LastTwo,
    LeftOneRightTwo,
    SurroundWord,
    Special,
    PreviousTwoNext
}
