package com.example.chineseexercises;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chineseexercises.ui.CutTextView;

import java.util.HashMap;

//漢字的分解
public class ChineseWordDismantlingActivity extends AppCompatActivity {
    private String answer="e";
    private int idi;//資料代號
    private static String[] str_item_word,str_translation,str_item_Part,str_item_Structure,str_item_Structure_S;//存資料
    /*頁面物件*/
    private CutTextView cutTextView;
    /*頁面數據*/
    String levelCWD="1";
    int tx,ty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chinese_word_dismantling);
        cutTextView=(CutTextView)findViewById(R.id.text_cut);
        getPackage();
    }

    public void getPackage() {
        str_item_word=new String[]{"明"};
        str_item_Part=new String[]{"日#月"};
        str_item_Structure=new String[]{"1"};
        startGame();
    }
    public String getStructure(final String answer) {
        Log.v("User_answer------", answer);
        String resule="";
       for(int p=0;p<str_item_word.length;p++){
           Log.v("User_answer------", answer);
           if(str_item_word[p].equals(answer)){
               resule=str_item_Structure_S[p];
               Log.v("User_answer------", answer);
           }
       }
       return resule;
    }

    public  static  HashMap<String, Integer> computeCount(String[] array){
        // HashMap 統整
        HashMap<String, Integer> map = new HashMap<>(array.length);
        for (int i = 0;i<array.length;i++){
            String pid = array[i];
            Integer count = map.get(pid);
            if(count == null){
                map.put(pid,1);
            }else {
                map.put(pid, count+1);
            }
        }
        return map;
    }
    int count = 0;
    ImageView IconImageView;
    private void startGame(){
        //開始遊戲
        cutTextView.addCutTextArray(levelCWD,str_item_word,str_item_Part,str_item_Structure);
        //設定模式並啟動(1.限定結構2.限定部件)(限定的東西)
        cutTextView.setModeAndLimit(2,"日");
        cutTextView.start();
    }
    int img;
    private void img_class(String i) {
        switch (i) {
            // 獨體
            case "0":
                img = R.drawable.bg_single;
                break;
            //左右結構
            case "1":
                img = R.drawable.bg_left_right;
                break;
            //上下結構
            case "2":
                img = R.drawable.bg_up_down;
                break;
            //左中右結構
            case "3":
                img = R.drawable.bg_left_right_3;
                break;
            //上中下結構
            case "4":
                img = R.drawable.bg_up_down_3;
                break;
            //左上至右下結構
            case "5":
                img = R.drawable.bg_right_down;
                break;
            //左下至右上結構
            case "6":
                img = R.drawable.bg_right_up;
                break;
            //上一下二結構
            case "7":
                img = R.drawable.bg_up_down2;
                break;
            //左一右二結構
            case "8":
                img = R.drawable.bg_left_right2;
                break;
            //包圍字結構
            case "9":
                img = R.drawable.bg_arround;
                break;
            //特殊結構
            case "10":
                break;
            //上二下一
            case "11":
                img = R.drawable.bg_down_up2;
                break;
            case "12":
                img = R.drawable.bg_left_up;
                break;
            case "13":
                img = R.drawable.bg_left_down;
                break;
            default:
                img = R.drawable.bg_single;
                break;
        }
    }

}
