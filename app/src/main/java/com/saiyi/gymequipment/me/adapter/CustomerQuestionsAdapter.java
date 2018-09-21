package com.saiyi.gymequipment.me.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.saiyi.gymequipment.R;
import com.saiyi.gymequipment.me.model.bean.Question;
import com.saiyi.libfast.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomerQuestionsAdapter extends BaseAdapter {
    private Context context;
    private List<Question> datas;
    private LayoutInflater layoutInflater;
    private Question question;

    public CustomerQuestionsAdapter(List<Question> datas, Context context) {
        this.datas = datas;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v = layoutInflater.inflate(R.layout.adapter_questions, null);
        ImageView checkedIv = (ImageView) v.findViewById(R.id.checked_iv);
        TextView questionTv = (TextView) v.findViewById(R.id.question_tv);
        Question question = datas.get(i);
        if(question.isChecked()){
            checkedIv.setImageResource(R.drawable.select);
        }else{
            checkedIv.setImageResource(R.drawable.reveal);
        }
        questionTv.setText(question.getQdescription());
        return v;
    }

    public void updateData(List<Question> list) {
        this.datas.clear();
        this.datas.addAll(list);
        notifyDataSetChanged();
    }

    public void setClickItme(int postion) {
        if (postion > datas.size()) return;
        for (int i = 0; i < datas.size(); i++) {
            Question q = datas.get(i);
            ;
            if (i == postion) {
                q.setChecked(true);
                datas.set(i, q);
                question = q;
            } else {
                q.setChecked(false);
                datas.set(i, q);
            }
        }
        notifyDataSetChanged();
    }

    public Question getQuestion() {
        return question;
    }
}
