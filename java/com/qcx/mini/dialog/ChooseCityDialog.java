package com.qcx.mini.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qcx.mini.R;
import com.qcx.mini.adapter.ItemCityAdapter;
import com.qcx.mini.entity.CityEntity;
import com.qcx.mini.utils.LogUtil;
import com.qcx.mini.utils.StatusBarUtil;
import com.qcx.mini.widget.LetterListView;

import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * Created by Administrator on 2018/4/14.
 */

public class ChooseCityDialog extends BaseDialog implements TextWatcher{
    private ItemCityAdapter adapter;
    private List<CityEntity> cityEntities;
    private ItemCityAdapter.OnItemClickListener listener;

    RecyclerView rv_list;
    EditText et_input;
    LetterListView lv_letter;
    @Override
    public int getLayoutId() {
        return R.layout.dialog_choose_city;
    }

    @Override
    public void initView(View view) {
        StatusBarUtil.setColor(getActivity(), Color.WHITE, 0);
        rv_list=view.findViewById(R.id.choose_city_list);
        adapter=new ItemCityAdapter(getContext());
        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_list.setLayoutManager(manager);
        rv_list.setAdapter(adapter);
        et_input=view.findViewById(R.id.dialog_chooses_city_input);
        view.findViewById(R.id.dialog_chooses_city_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        et_input.addTextChangedListener(this);
        adapter.setDatas(cityEntities);
        adapter.setListener(listener);
        lv_letter=view.findViewById(R.id.choose_city_letter);
        lv_letter.setOnTouchingLetterChangedListener(new LetterListView.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String s) {
                if(adapter.getLettersPosition()!=null&&adapter.getLettersPosition().get(s)!=null){
                    rv_list.smoothScrollToPosition(adapter.getLettersPosition().get(s));
                }
            }

            @Override
            public void onTouch(boolean isTouch) {
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        StatusBarUtil.setColor(getActivity(), Color.TRANSPARENT, 0);
    }

    private String getFirstLetter(String str){
        if(TextUtils.isEmpty(str)){
            return "#";
        }
        String firstStr=str.substring(0,1);
        if(firstStr.matches("^[0-9]$")){
            return "#";
        }
        if(firstStr.matches("^[a-z]$")){
            return firstStr.toUpperCase();
        }
        if(firstStr.matches("^[A-Z]$")){
            return firstStr;
        }
        if(isChinese(firstStr.charAt(0))){
            String[] strings= PinyinHelper.toHanyuPinyinStringArray(firstStr.charAt(0));
            if(strings!=null&&strings.length>0){
                return getFirstLetter(strings[0]);
            }else {
                return "#";
            }
        }
        return "#";
    }

    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return true;
    }


    private List<CityEntity> formatData(List<String> data){
        List<CityEntity> cities=new ArrayList<>();
        for(int i=0;i<data.size();i++){
            CityEntity entity=new CityEntity();
            String str= TextUtils.isEmpty(data.get(i))?"#":data.get(i);
            entity.setLetter(getFirstLetter(str));
            entity.setName(data.get(i));
            cities.add(entity);
        }
        ComparatorCity comparator=new ComparatorCity();
        Collections.sort(cities, comparator);
        for(CityEntity entity:cities){
            Log.i("ddddd","paixu  name="+entity.getName()+"  letter="+entity.getLetter());
        }
        return cities;
    }

    public ChooseCityDialog setData( List<String> data){
        this.cityEntities=formatData(data);
        return this;
    }

    public ChooseCityDialog setListener(ItemCityAdapter.OnItemClickListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        adapter.setDatas(getMatchData(s.toString()));
    }

    private List<CityEntity> getMatchData(String s){
        if(TextUtils.isEmpty(s)){
            return cityEntities;
        }
        List<CityEntity> matchData=new ArrayList<>();
        for(int i=0;i<cityEntities.size();i++){
            if(cityEntities.get(i).getName().contains(s)){
                matchData.add(cityEntities.get(i));
            }
        }
        return matchData;
    }

    public class ComparatorCity implements Comparator {
        public int compare(Object obj0, Object obj1) {
            CityEntity user0=(CityEntity)obj0;
            CityEntity user1=(CityEntity)obj1;
            int f=user0.getLetter().compareTo(user1.getLetter());
            if(f==0){
                return user0.getName().compareTo(user1.getName());
            }else{
                return f;
            }
        }

    }

    @Override
    public float getDimAmount() {
        return 0;
    }

    public interface OnChooseCityDialogListener{
        void onItemClick(String s);

    }
}
