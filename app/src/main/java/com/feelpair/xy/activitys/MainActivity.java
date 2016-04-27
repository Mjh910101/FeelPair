package com.feelpair.xy.activitys;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.feelpair.xy.R;
import com.feelpair.xy.box.People;
import com.feelpair.xy.handlers.ColorHandler;
import com.feelpair.xy.handlers.MessageHandler;
import com.feelpair.xy.handlers.TextHandeler;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends BaseActivity {

    @ViewInject(R.id.main_manBtn)
    private TextView manBtn;
    @ViewInject(R.id.main_womanBtn)
    private TextView womanBtn;
    @ViewInject(R.id.main_peopleIdInput)
    private EditText peopleIdInput;
    @ViewInject(R.id.main_chooseIdInput)
    private EditText chooseIdInput;
    @ViewInject(R.id.main_addBtn)
    private TextView addBtn;
    @ViewInject(R.id.main_dataList)
    private ListView dataList;

    private boolean gender = People.MAN;

    private List<People> peopleList;
    private List<People> manList;
    private List<People> womanList;

    private PeopleAdapter mPeopleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        initActivity();
    }

    private void initActivity() {
        setGender(People.MAN);

        peopleList = new ArrayList<>();
        manList = new ArrayList<>();
        womanList = new ArrayList<>();

        mPeopleAdapter = new PeopleAdapter();
        dataList.setAdapter(mPeopleAdapter);
    }

    @OnClick({R.id.main_manBtn, R.id.main_womanBtn, R.id.main_addBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_manBtn:
                setGender(People.MAN);
                break;
            case R.id.main_womanBtn:
                setGender(People.WOMAN);
                break;
            case R.id.main_addBtn:
                operationPeople();
                break;
        }
    }

    private void cleanInput() {
        peopleIdInput.setText("");
        chooseIdInput.setText("");
    }

    private void operationPeople() {
        int peopleId = getPeopleId();
        People people = getHavePeople(peopleId);
        if (people != null) {
            if (!people.addChooseId(getChooseId())) {
                MessageHandler.showToast(context, "该参加者已选择了 " + getChooseId() + " 号");
                return;
            }
        } else {
            addPeople(createPeople(peopleId));
        }
        mPeopleAdapter.notifyDataSetChanged();
        cleanInput();
    }

    private void addPeople(People obj) {
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

    private People getHavePeople(int peopleId) {
        for (People obj : peopleList) {
            if (obj.equals(peopleId) && obj.equals(getGender())) {
                return obj;
            }
        }
        return null;
    }

    private People createPeople(int peopleId) {
        People obj = new People(peopleId, getGender());
        obj.addChooseId(getChooseId());
        return obj;
    }

    private boolean getGender() {
        return gender;
    }

    private void setGender(boolean gender) {
        this.gender = gender;
        initGender();
        if (gender == People.MAN) {
            manBtn.setTextColor(People.getManColor(context));
            addBtn.setBackgroundResource(R.color.man);
            peopleIdInput.setBackgroundResource(R.drawable.man_frame);
            chooseIdInput.setBackgroundResource(R.drawable.woman_frame);
        } else {
            womanBtn.setTextColor(People.getWomanColor(context));
            addBtn.setBackgroundResource(R.color.woman);
            peopleIdInput.setBackgroundResource(R.drawable.woman_frame);
            chooseIdInput.setBackgroundResource(R.drawable.man_frame);
        }
    }

    private void initGender() {
        manBtn.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));
        womanBtn.setTextColor(ColorHandler.getColorForID(context, R.color.text_gray_01));

        peopleIdInput.setBackgroundResource(R.drawable.blank_frame);
        chooseIdInput.setBackgroundResource(R.drawable.blank_frame);
    }

    private int getPeopleId() {
        try {
            return Integer.valueOf(TextHandeler.getText(peopleIdInput));
        } catch (Exception e) {
            return 0;
        }
    }

    private int getChooseId() {
        try {
            return Integer.valueOf(TextHandeler.getText(chooseIdInput));
        } catch (Exception e) {
            return 0;
        }
    }

    class PeopleAdapter extends BaseAdapter {

        private LayoutInflater inflater;

        public PeopleAdapter() {
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    }

    class HolderView {
        TextView peopleId;
        TextView chooseId;
        TextView sumText;
    }

}
