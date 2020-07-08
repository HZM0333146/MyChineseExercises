package com.example.chineseexercises.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.annotation.RequiresApi;

import java.util.List;

/**
 * Created by J on 2017/9/6.
 */

public class DragGridViewAdapter extends BaseAdapter {
    private Context context;
    private List<String> strList;
    private int hidePosition = AdapterView.INVALID_POSITION;
    public DragGridViewAdapter(Context context,List<String> strList){
        this.context=context;
        this.strList=strList;
    }
    @Override
    public int getCount() {
        return strList.size();
    }

    @Override
    public Object getItem(int i) {
        return strList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        TextView textView;
        if(view == null) {
            textView = new TextView(context);
        }else{
            textView = (TextView)view;
        }

        //hide时隐藏Text
        if(i != hidePosition) {
            textView.setText(strList.get(i));
            textView.setTag(strList.get(i));
        }else {
            textView.setText("");
        }
        textView.setId(i);
        textView.setTextSize(30);
        textView.setTextColor(Color.BLACK);
        textView.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
        return textView;
    }
    public void hideView(int pos) {
        hidePosition = pos;
        notifyDataSetChanged();
    }

    public void showHideView() {
        hidePosition = AdapterView.INVALID_POSITION;
        notifyDataSetChanged();
    }

    public void removeView(int pos) {
        strList.remove(pos);
        notifyDataSetChanged();
    }

    //更新拖动时的gridView
    public void swapView(int draggedPos, int destPos) {
        //从前向后拖动，其他item依次前移
        if(draggedPos < destPos) {
            strList.add(destPos+1, (String) getItem(draggedPos));
            strList.remove(draggedPos);
        }
        //从后向前拖动，其他item依次后移
        else if(draggedPos > destPos) {
            strList.add(destPos, (String) getItem(draggedPos));
            strList.remove(draggedPos+1);
        }
        hidePosition = destPos;
        notifyDataSetChanged();
    }
}

