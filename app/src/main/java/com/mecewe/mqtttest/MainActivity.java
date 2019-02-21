package com.mecewe.mqtttest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences rememberpref;
    private SharedPreferences.Editor remembereditor;
    private TextView log_1;
    private Button button1;
    private Button button2;
    private ImageButton setting;
    private EditText lower_text1;
    private EditText heigher_text1;
    private EditText lower_text2;
    private EditText heigher_text2;
    private String[] key;
    private int Numbers = 18;
    private int count = 0;
    private int lower1;
    private int heigher1;
    private int lower2;
    private int heigher2;
    private String Acess_token = "ywjc000";
    private String address;
    private String value;
    private boolean flag=false;
    private boolean No2_isabled=false;
    private String token,test2,test3;
    private boolean[] key_1;
    private boolean[] key_2;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private final MyHandler mHandler = new MyHandler(this);

    static class MyHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

        //子线程传递回主线程
        public MyHandler(MainActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            System.out.println(msg);
            if (mActivity.get() == null) {
                return;
            }
            MainActivity activity = mActivity.get();
            switch (msg.what) {
                case 0:
//                    activity.submit_but.setVisibility(View.VISIBLE);
                    activity.log_1.append(msg.obj.toString());
                    break;
                default:
                    break;
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu){
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.layout.activity_main, menu);
//        return true;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //EditText默认不弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        lower_text1 = (EditText)findViewById(R.id.text_1);
        heigher_text1 = (EditText)findViewById(R.id.text_2);
        lower_text2 = (EditText)findViewById(R.id.text_3);
        heigher_text2 = (EditText)findViewById(R.id.text_4);
        rememberpref = PreferenceManager.getDefaultSharedPreferences(this);
        log_1 = (TextView)findViewById(R.id.log_1);
        log_1.setMovementMethod(ScrollingMovementMethod.getInstance());
        button1 =(Button)findViewById(R.id.button_1);
        setting =(ImageButton)findViewById(R.id.ic_setting);
//        lower_text1.clearFocus();

        iniData();
//        lower_text1.setCursorVisible(false);
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow( lower_text1.getWindowToken(),0);
        //发送按钮
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    lower1 = Integer.parseInt(lower_text1.getText().toString());
                    heigher1 = Integer.parseInt(heigher_text1.getText().toString());
                    if (lower_text2.getText().toString().trim().length() != 0 && heigher_text2.getText().toString().trim().length() != 0){
                        lower2 = Integer.parseInt(lower_text2.getText().toString());
                        heigher2 = Integer.parseInt(heigher_text2.getText().toString());
                        No2_isabled = true;
                    }
                }catch(Exception e){
                    return;
                }
                remembereditor=rememberpref.edit();
                remembereditor.putString("lower_text1",lower_text1.getText().toString());
                remembereditor.putString("heigher_text1",heigher_text1.getText().toString());
                remembereditor.putString("lower_text2",lower_text2.getText().toString());
                remembereditor.putString("heigher_text2",heigher_text2.getText().toString());
                remembereditor.putBoolean("isNull",true);
                remembereditor.apply();
                if (lower_text2.getText().toString().trim().length() != 0 && heigher_text2.getText().toString().trim().length() != 0){
                    log_1.setText("Start to send No.1 group["+String.valueOf(lower1)+","+ String.valueOf(heigher1)+ ") and No.2 group" +
                            "["+String.valueOf(lower2)+","+ String.valueOf(heigher2)+ ")!\n");
                }else{
                    log_1.setText("Start to send No.1 group["+String.valueOf(lower1)+","+ String.valueOf(heigher1)+ ")!\n");
                    Log.e("111111111111111","111");
                    No2_isabled = false;
                }

//                log_1.append(Arrays.toString(key_1)+"\n");
//                log_1.append(Arrays.toString(key_2)+"\n");
//                log_1.append(rememberpref.getString("key_1", "[]"));

                flag = false;
                sendHttp();
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
        button2 =(Button)findViewById(R.id.button_2);
        //停止发送按钮
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSending();
            }
        });
    }

    //初始化数据
    private void iniData(){

        key = new String[]{"H0900","H0904","H0908","H090C","H0910","H0914","H0918","H091C","H0920","H0924",
                "H0928","H092C","H0930","H0934","H0938","H093C","H0940","H0944"};

        if (getIntent().getStringExtra("token") != null){
            Acess_token = getIntent().getStringExtra("token");
        }else{
            Acess_token = rememberpref.getString("access_token", "");
        }
        address = "http://140.143.23.199:8080/api/v1/"+Acess_token+"/telemetry";
        boolean isNull=rememberpref.getBoolean("isNull",false);
        if (isNull) {
            lower_text1.setText(rememberpref.getString("lower_text1", ""));
            heigher_text1.setText(rememberpref.getString("heigher_text1", ""));
            lower_text2.setText(rememberpref.getString("lower_text2", ""));
            heigher_text2.setText(rememberpref.getString("heigher_text2", ""));
        }
        Bundle b1=this.getIntent().getExtras(); //打开为空时Bundle[{profile=0}]
        Bundle b2=this.getIntent().getExtras();
//        if (b1 != null) {
//            key_1= b1.getBooleanArray("key_1");
//            Log.e("b1", b1.toString());
//            log_1.setText(b1.toString()+"1111\n");
//            log_1.setText(Arrays.toString(key_1)+"1111\n");
//        }else{
            try {
                JSONArray jsonArray1 = new JSONArray(rememberpref.getString("key_1", "[]"));
                Log.e("jsonArray1", jsonArray1.toString());
//                Arrays.fill(key_1, false);
                if (jsonArray1.length() > 0){
                    key_1=new boolean[Numbers];
                    Arrays.fill(key_1, false);
                    for (int i = 0; i < Numbers; i++) {
                        key_1[i] = jsonArray1.getBoolean(i);
                    }
                    Log.e("key1___", Arrays.toString(key_1));
                }
//                log_1.setText(Arrays.toString(key_1)+"\n");
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        }
//        if (b2 != null){
//            key_2= b2.getBooleanArray("key_2");
//            Log.e("key2", Arrays.toString(key_2));
//        }else{
            try {
                JSONArray jsonArray2 = new JSONArray(rememberpref.getString("key_2", "[]"));
                Log.e("jsonArray2", jsonArray2.toString());
//                Arrays.fill(key_2, false);
                if (jsonArray2.length() > 0){
                    key_2=new boolean[Numbers];
                    Arrays.fill(key_2, false);
                    for (int i = 0; i < Numbers; i++) {
                        key_2[i] = jsonArray2.getBoolean(i);
                    }
                    Log.e("key2___", Arrays.toString(key_2));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        }
//        if (getIntent().getStringExtra("token") != null){
//            Log.e("Access_token***",getIntent().getStringExtra("token"));
//        }

    }

    private void sendHttp(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!flag) {
                        Log.e("star sending","111");
                        if (key_1[count]){
                            Random r = new Random();
                            double d = r.nextDouble() * (heigher1-lower1)+lower1;
//                        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
//                        df.format(r.nextDouble() * 5);
                            DecimalFormat df1 = new DecimalFormat("0.00");
                            final String total1 =df1.format(d);
                            HttpUtil.sendOkHttpPost(address, key[count], total1, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("Sending*******", key[count]+":"+total1);
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = "Sending******* "+key[count]+" : "+total1+"\n";
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }

                        if (key_2[count] && No2_isabled){
                            Random r = new Random();
                            double d = r.nextDouble() * (heigher2-lower2)+lower2;
//                        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
//                        df.format(r.nextDouble() * 5);
                            DecimalFormat df1 = new DecimalFormat("0.00");
                            final String total2 =df1.format(d);
                            HttpUtil.sendOkHttpPost(address, key[count], total2, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    Log.e("Sending*******", key[count]+":"+total2);
                                    Message msg = new Message();
                                    msg.what = 0;
                                    msg.obj = "Sending******* "+key[count]+" : "+total2+"\n";
                                    mHandler.sendMessage(msg);
                                }
                            });
                        }

                        if(key_1[count] || (key_2[count] && No2_isabled)){
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                return;
                            }
                        }
                        count = (count + 1) % 18;

                        }



                } catch (Exception e) {
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }
        });
        t.start();
    }

//    Handler mHander = new Handler(){
//        @Override
//        public void handleMessage(Message msg){
//
//        }
//    }


    private void stopSending(){
        flag=true;
        Log.e("Stopping","sending");
        log_1.append("Stop sending!");
    }
}
