package com.mecewe.mqtttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONArray;

import java.util.Arrays;

public class SettingActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private SharedPreferences rememberpref;
    private SharedPreferences.Editor remembereditor;
    private ImageButton arrow_back;
    private EditText access_token;
    private Integer num = 18;
    private CheckBox checkBox1_1,checkBox1_2,checkBox1_3,checkBox1_4,checkBox1_5,checkBox1_6;
    private CheckBox checkBox1_7,checkBox1_8,checkBox1_9,checkBox1_10,checkBox1_11,checkBox1_12;
    private CheckBox checkBox1_13,checkBox1_14,checkBox1_15,checkBox1_16,checkBox1_17,checkBox1_18;
    private CheckBox checkBox2_1,checkBox2_2,checkBox2_3,checkBox2_4,checkBox2_5,checkBox2_6;
    private CheckBox checkBox2_7,checkBox2_8,checkBox2_9,checkBox2_10,checkBox2_11,checkBox2_12;
    private CheckBox checkBox2_13,checkBox2_14,checkBox2_15,checkBox2_16,checkBox2_17,checkBox2_18;
    private CheckBox[] checkBoxes_1;
    private CheckBox[] checkBoxes_2;
    private boolean key_1[] = new boolean[18];
    private boolean key_2[] = new boolean[18];
    private boolean isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Integer[] id_1 = new Integer[]{R.id.checkbox1_1,R.id.checkbox1_2,R.id.checkbox1_3,R.id.checkbox1_4,R.id.checkbox1_5,R.id.checkbox1_6,
                R.id.checkbox1_7, R.id.checkbox1_8,R.id.checkbox1_9,R.id.checkbox1_10,R.id.checkbox1_11,R.id.checkbox1_12,
                R.id.checkbox1_13,R.id.checkbox1_14,R.id.checkbox1_15, R.id.checkbox1_16,R.id.checkbox1_17,R.id.checkbox1_18};
        Integer[] id_2 = new Integer[]{R.id.checkbox2_1,R.id.checkbox2_2,R.id.checkbox2_3,R.id.checkbox2_4,R.id.checkbox2_5,R.id.checkbox2_6,
                R.id.checkbox2_7, R.id.checkbox2_8,R.id.checkbox2_9,R.id.checkbox2_10,R.id.checkbox2_11,R.id.checkbox2_12,
                R.id.checkbox2_13,R.id.checkbox2_14,R.id.checkbox2_15, R.id.checkbox2_16,R.id.checkbox2_17,R.id.checkbox2_18};
        checkBoxes_1 = new CheckBox[]{checkBox1_1,checkBox1_2,checkBox1_3,checkBox1_4,checkBox1_5,checkBox1_6,
                checkBox1_7,checkBox1_8,checkBox1_9,checkBox1_10,checkBox1_11,checkBox1_12,checkBox1_13,checkBox1_14,
                checkBox1_15,checkBox1_16,checkBox1_17,checkBox1_18};
        checkBoxes_2 = new CheckBox[]{checkBox2_1,checkBox2_2,checkBox2_3,checkBox2_4,checkBox2_5,checkBox2_6,
                checkBox2_7,checkBox2_8,checkBox2_9,checkBox2_10,checkBox2_11,checkBox2_12,checkBox2_13,checkBox2_14,
                checkBox2_15,checkBox2_16,checkBox2_17,checkBox2_18};
        for (int i=0;i<18;i++){
            checkBoxes_1[i] =(CheckBox)findViewById(id_1[i]);
            checkBoxes_2[i] =(CheckBox)findViewById(id_2[i]);
            checkBoxes_1[i].setTag(i);
            checkBoxes_2[i].setTag(i+18);
            checkBoxes_1[i].setOnCheckedChangeListener(this);
            checkBoxes_2[i].setOnCheckedChangeListener(this);
        }
        access_token=(EditText)findViewById(R.id.text_token);
        arrow_back =(ImageButton)findViewById(R.id.ic_back);
        final ImageView isLocked =(ImageView)findViewById(R.id.locked);

//        Log.e("key1 name",String.valueOf(key_1));[Z@202a8fe2
//        Log.e("key1 name",String.valueOf(key_2));[Z@14c24f73

        //读取上次设置内容
        rememberpref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isNull=rememberpref.getBoolean("isNull",false);
        if (isNull) {
            String rem_access_token = rememberpref.getString("access_token", "");
            Log.e("access_token---",rem_access_token);

            boolean[] rem_key_1=new boolean[num];
            boolean[] rem_key_2=new boolean[num];
            Arrays.fill(rem_key_1, false);
            Arrays.fill(rem_key_2, false);
            try {
                JSONArray jsonArray1 = new JSONArray(rememberpref.getString("key_1", "[]"));
                JSONArray jsonArray2 = new JSONArray(rememberpref.getString("key_2", "[]"));
                for (int i = 0; i < num; i++) {
                    rem_key_1[i] = jsonArray1.getBoolean(i);
                    rem_key_2[i] = jsonArray2.getBoolean(i);
                    if(rem_key_1[i]){
                        checkBoxes_1[i].setChecked(true);
                        checkBoxes_2[i].setEnabled(false);
                    }
                    if(rem_key_2[i]){
                        checkBoxes_2[i].setChecked(true);
                        checkBoxes_1[i].setEnabled(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            access_token.setText(rem_access_token);
        }
        //返回到主活动界面
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                remembereditor=rememberpref.edit();
//                remembereditor.putString("access_token",access_token.getText().toString());
//                remembereditor.apply();
                JSONArray jsonArray1 = new JSONArray();
                JSONArray jsonArray2 = new JSONArray();
                for (boolean b : key_1) {
                    jsonArray1.put(b);
                }
                for (boolean b : key_2) {
                    jsonArray2.put(b);
                }
                remembereditor=rememberpref.edit();
                remembereditor.putString("key_1",jsonArray1.toString());
                remembereditor.putString("key_2",jsonArray2.toString());
                remembereditor.apply();

                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                intent.putExtra("token",access_token.getText().toString());
                Bundle b1=new Bundle();
                Bundle b2=new Bundle();
                b1.putBooleanArray("key_1",key_1);
                b2.putBooleanArray("key_2",key_2);
                intent.putExtras(b1);
                intent.putExtras(b2);
                Log.e("token|||",access_token.getText().toString());
                Log.e("key_1|||", Arrays.toString(key_1));
                startActivity(intent);
            }
        });
        //默认锁住Access token
        isLocked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == isLocked){
                    if (isChanged){
                        isLocked.setImageDrawable(getResources().getDrawable(R.drawable.outline_lock_black_24));
                        access_token.setEnabled(false);
                    }else{
                        isLocked.setImageDrawable(getResources().getDrawable(R.drawable.baseline_lock_open_black_24));
                        access_token.setEnabled(true);
                    }
                    isChanged = !isChanged;
                    remembereditor=rememberpref.edit();
                    remembereditor.putBoolean("isNull",true);
                    remembereditor.putString("access_token",access_token.getText().toString());
                    remembereditor.apply();
                }
            }
        });


    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int pos=(int)buttonView.getTag();
        if(buttonView.isChecked()){
            if(pos<18){
                key_1[pos] = true;
                key_2[pos] = false;
                checkBoxes_2[pos].setEnabled(false);
            }else{
                key_2[pos-18] = true;
                key_1[pos-18] = false;
                checkBoxes_1[pos-18].setEnabled(false);
            }
        }else {
            if(pos<18){
                key_1[pos] = false;
                checkBoxes_2[pos].setEnabled(true);
            }else{
                key_2[pos-18] = false;
                checkBoxes_1[pos-18].setEnabled(true);
            }
        }
    }

//    public void saveBooleanArray(boolean[] booleanArray){
//        JSONArray jsonArray = new JSONArray();
//        for (boolean b : booleanArray) {
//            jsonArray.put(b);
//        }
//        remembereditor=rememberpref.edit();
//        remembereditor.putString(String.valueOf(booleanArray),jsonArray.toString());
//        remembereditor.apply();
//    }
}
