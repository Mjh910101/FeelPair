package com.feelpair.xy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.feelpair.xy.R;
import com.feelpair.xy.box.People;
import com.feelpair.xy.dialogs.ListDialog;
import com.feelpair.xy.handlers.ColorHandler;
import com.feelpair.xy.handlers.WinTool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<Integer, People> manMap;
    private Map<Integer, People> womanMap;
    private List<People> manList;
    private List<People> womanList;
    private List<People> peopleList;
    private LayoutInflater inflater;

    public PeopleAdapter() {
        this.manMap = new HashMap<>();
        this.womanMap = new HashMap<>();
        this.manList = new ArrayList<>();
        this.womanList = new ArrayList<>();
        this.peopleList = new ArrayList<>();
    }

    public void setAdapterContext(Context context){
        this.context = context;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addPeople(People obj) {
        if (obj.isMan()) {
            manList.add(obj);
            manMap.put(obj.getId(), obj);
        } else {
            womanList.add(obj);
            womanMap.put(obj.getId(), obj);
        }
        refreshPeopleList(manList, womanList);
    }

    public boolean deletePeople(People obj) {
        if (obj.getChooseSize() <= 1) {
            deleteItem(obj);
            return true;
        }
        showDeleteListDialog(obj);
        return false;
    }

    private void showDeleteListDialog(final People obj) {
        final ListDialog dialog = new ListDialog(context);
        dialog.setTitleGone();
        dialog.setList(obj.getChooseIdListForText());
        dialog.setItemListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                obj.deleteChooseForPosition(position);
                dialog.dismiss();
                notifyDataSetChanged();
            }
        });
    }

    private void deleteItem(People obj) {
        if (obj.isMan()) {
            manList.remove(obj);
            manMap.remove(obj.getId());
        } else {
            womanList.remove(obj);
            womanMap.remove(obj.getId());
        }
        refreshPeopleList(manList, womanList);
    }

    private void refreshPeopleList(List<People> manList, List<People> womanList) {
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
            holder.deleteBtn = (TextView) convertView.findViewById(R.id.peopleItem_deleteBtn);
            holder.scroll = (HorizontalScrollView) convertView.findViewById(R.id.peopleItem_scroll);
            holder.messageLayout = (LinearLayout) convertView.findViewById(R.id.peopleItem_messageLayout);

            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) holder.messageLayout.getLayoutParams();
            lp.width = WinTool.getWinWidth(context);

            convertView.setTag(holder);
        } else {
            holder = (HolderView) convertView.getTag();
        }

        initScroll(convertView, holder);
        People obj = peopleList.get(position);
        setView(holder, obj);
        serOnDeleteBotton(holder.deleteBtn, obj);
        return convertView;
    }

    private void serOnDeleteBotton(TextView btn, final People obj) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (deletePeople(obj)) {
                    notifyDataSetChanged();
                }
            }
        });
    }

    private void initScroll(View convertView, final HolderView holder) {
        if (holder.scroll.getScrollX() != 0) {
            holder.scroll.scrollTo(0, 0);
        }
        convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        //获得HorizontalScrollView滑动的水平方向值.
                        int scrollX = holder.scroll.getScrollX();
                        //获得操作区域的长度
                        int actionW = holder.deleteBtn.getWidth();
                        //注意使用smoothScrollTo,这样效果看起来比较圆滑,不生硬
                        //如果水平方向的移动值<操作区域的长度的一半,就复原
                        if (scrollX < actionW / 2) {
                            holder.scroll.smoothScrollTo(0, 0);
                        } else {//否则的话显示操作区域
                            holder.scroll.smoothScrollTo(actionW, 0);
                        }
                        return true;
                }
                return false;
            }
        });

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
        HorizontalScrollView scroll;
        LinearLayout messageLayout;
        TextView deleteBtn;
    }

}


