package com.feelpair.xy.box;

import android.content.Context;

import com.feelpair.xy.R;
import com.feelpair.xy.handlers.ColorHandler;

import java.util.ArrayList;
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
 * Created by Hua on 16/4/27.
 */
public class People implements Comparable {

    public final static boolean MAN = true;
    public final static boolean WOMAN = false;

    private int id;
    private int sum;
    private boolean gender;
    private List<Integer> chooseIdList;

    public People(int id, boolean gender) {
        this.id = id;
        this.gender = gender;
        this.chooseIdList = new ArrayList<Integer>();
        this.sum = 0;
    }

    public String getSumText() {
        try {
            return String.valueOf(sum);
        } catch (Exception e) {
            return "";
        }
    }

    public String getIdText() {
        StringBuffer sb = new StringBuffer();
        sb.append(id + "号 ");
        if (isMan()) {
            sb.append("先生");
        } else {
            sb.append("小姐");
        }
        return sb.toString();
    }

    public String getChooseText() {
        StringBuffer sb = new StringBuffer();
        for (Integer c : chooseIdList) {
            sb.append(c + "号 ");
        }
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public boolean isMan() {
        return gender == MAN;
    }

    public boolean isWoman() {
        return gender == WOMAN;
    }

    public boolean addChooseId(int id) {
        for (int i : chooseIdList) {
            if (i == id) {
                return false;
            }
        }
        chooseIdList.add(id);
        return true;
    }

    public boolean isChooseId(int id) {
        if (chooseIdList.isEmpty()) {
            return false;
        }
        return chooseIdList.contains(id);
    }

    public boolean equals(int peopleId) {
        return id == peopleId;
    }

    public boolean equals(boolean gender) {
        return this.gender == gender;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(getIdText());
        sb.append("，选择了 ");
        sb.append(getChooseText());
        sb.append("。");
        return sb.toString();
    }

    public final static int getManColor(Context context) {
        return ColorHandler.getColorForID(context, R.color.man);
    }

    public final static int getWomanColor(Context context) {
        return ColorHandler.getColorForID(context, R.color.woman);
    }

    @Override
    public int compareTo(Object obj) {
        People p = (People) obj;
        return this.getId() - p.getId();
    }


}