package com.feelpair.xy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.feelpair.xy.R;
import com.feelpair.xy.box.People;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * *
 * * ┏┓      ┏┓
 * *┏┛┻━━━━━━┛┻┓
 * *┃          ┃
 * *┃          ┃
 * *┃ ┳┛   ┗┳  ┃
 * *┃          ┃
 * *┃    ┻     ┃
 * *┃          ┃
 * *┗━┓      ┏━┛
 * *  ┃      ┃
 * *  ┃      ┃
 * *  ┃      ┗━━━┓
 * *  ┃          ┣┓
 * *  ┃         ┏┛
 * *  ┗┓┓┏━━━┳┓┏┛
 * *   ┃┫┫   ┃┫┫
 * *   ┗┻┛   ┗┻┛
 * Created by Hua on 16/4/28.
 */
public class PeopleAdapter extends BaseAdapter {

    private Context context;
    private List<People> manList;
    private List<People> womanList;
    private List<People> peopleList;
    private LayoutInflater inflater;

    public PeopleAdapter(Context context) {
        this.context = context;
        this.manList = new ArrayList<>();
        this.womanList = new ArrayList<>();
        this.peopleList = new ArrayList<>();
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addPeople(People obj) {
        if (obj.isMan()) {
            manList.add(obj);
        } else {
            womanList.add(obj);
        }
        Collections.sort(manList);
        Collections.sort(womanList);
        peopleList.removeAll(peopleList);
        peopleList.addAll(manList);
        peopleList.addAll(womanList);
    }

    public People getHavePeople(int peopleId, boolean gender) {
        if (peopleList.isEmpty()) {
            return null;
        }
        for (People obj : peopleList) {
            if (obj.equals(peopleId) && obj.equals(gender)) {
                return obj;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return peopleList.size();
    }

    @Override
    public Object getItem(int position) {
        return peopleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holder;
        if (convertView == null) {
            convertView = inflater.inflate(
                    R.layout.layout_people_item, null);
            holder = new HolderView();
            holder.peopleId = (TextView) convertView.findViewById(R.id.peopleItem_peopleId);
            holder.chooseId = (TextView) convertView.findViewById(R.id.peopleItem_chooesId);
            holder.sumText = (TextView) convertView.findViewById(R.id.peopleItem_sumText);
            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        People obj = peopleList.get(position);
        setView(holder, obj);

        return convertView;
    }

    private void setView(HolderView holder, People obj) {
        holder.peopleId.setText(obj.getIdText());
        holder.chooseId.setText(obj.getChooseText());
        holder.sumText.setText(obj.getSumText());

        if (obj.isMan()) {
            holder.peopleId.setTextColor(People.getManColor(context));
        } else {
            holder.peopleId.setTextColor(People.getWomanColor(context));
        }
    }

    class HolderView {
        TextView peopleId;
        TextView chooseId;
        TextView sumText;
    }

}


