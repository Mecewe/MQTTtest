package com.mecewe.mqtttest;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Response;

import java.lang.ref.WeakReference;
import java.text.DecimalFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private TextView log_1;
    private Button button1;
    private Button button2;
    private String[] key = new String[20];
    private int Numbers = 8;
    private int count = 0;
    private int lower;
    private int heigher;
    private String Acess_token = "ywjc001";
    private String address;
    private String value;
    private boolean flag=false;

    public static final MediaType JSON=MediaType.parse("application/json; charset=utf-8");
    private final MyHandler mHandler = new MyHandler(this);

    static class MyHandler extends Handler {

        private final WeakReference<MainActivity> mActivity;

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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText lower_text = (EditText)findViewById(R.id.text_1);
        final EditText heigher_text = (EditText)findViewById(R.id.text_2);
        log_1 = (TextView)findViewById(R.id.log_1);
        log_1.setMovementMethod(ScrollingMovementMethod.getInstance());
        button1 =(Button)findViewById(R.id.button_1);
        iniData();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    lower = Integer.parseInt(lower_text.getText().toString());
                    heigher = Integer.parseInt(heigher_text.getText().toString());
                }catch(Exception e){
                    return;
                }
                log_1.setText("Start to send ["+String.valueOf(lower)+","+ String.valueOf(heigher)+ ") numbers!\n");
                flag = false;
                sendHttp();
            }
        });
        button2 =(Button)findViewById(R.id.button_2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSending();
            }
        });
    }

    private void iniData(){

//        for (int i = 0;i<Numbers;i++){
//            key[i]="H09"+
//        }
        key[0] = "H0900";
        key[1] = "H0904";
        key[2] = "H0908";
        key[3] = "H090C";
        key[4] = "H0910";
        key[5] = "H0914";
        key[6] = "H0918";
        key[7] = "H091C";

        address = "http://140.143.23.199:8080/api/v1/"+Acess_token+"/telemetry";
    }

    private void sendHttp(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!flag) {
                        Random r = new Random();
                        double d = r.nextDouble() * (heigher-lower)+lower;
//                        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
//                        df.format(r.nextDouble() * 5);
                        DecimalFormat df1 = new DecimalFormat("0.00");
                        final String total =df1.format(d);
                        HttpUtil.sendOkHttpPost(address, key[count], total, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.e("Sending*******", key[count]+":"+total);
                                Message msg = new Message();
                                msg.what = 0;
                                msg.obj = "Sending******* "+key[count]+" : "+total+"\n";
                                mHandler.sendMessage(msg);
                            }
                        });
//                        Message msg = new Message();
//                        msg.what = 0;
//                        msg.obj = "Sending******* "+key[count]+" : "+total+"\n";
//                        mHandler.sendMessage(msg);
//                        String msgq = "Sending*******"+key[count]+":"+total+"\n";
//                        log_1.append("Sending*******"+key[count]+":"+total+"\n");

                        count = (count + 1) % 8;
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            return;
                        }
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
