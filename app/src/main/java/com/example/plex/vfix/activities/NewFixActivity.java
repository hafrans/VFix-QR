package com.example.plex.vfix.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plex.vfix.R;
import com.example.plex.vfix.beans.SimpleResultBean;
import com.example.plex.vfix.utils.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * 新增修改界面
 *
 */
public class NewFixActivity extends AppCompatActivity {

    public static final int FINISH_ACT = 0x48;
    public static final int PROGRESSBAR_HIDE = 0x49;
    public ProgressDialog progressDialog;
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FINISH_ACT:
                    Log.i(Thread.currentThread().getClass().getSimpleName(),"finish");
                    finish();
                    break;
                case PROGRESSBAR_HIDE:
                    progressDialog.dismiss();
                    break;
            }

        }
    };
    OkHttpClient client = new OkHttpClient();

    public static void startActivity(AppCompatActivity appCompatActivity){
        Intent intent = new Intent(appCompatActivity,NewFixActivity.class);
        appCompatActivity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_fix);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

    }

    public void submit(View v){

        //check fields
        EditText problem = (EditText) findViewById(R.id.new_fix_problem);
        AppCompatSpinner BuildType = (AppCompatSpinner) findViewById(R.id.new_fix_building_type);
        AppCompatSpinner BuildNumber = (AppCompatSpinner) findViewById(R.id.new_fix_building_number);
        EditText Address  = (EditText) findViewById(R.id.new_fix_addr);
        EditText otherTel = (EditText) findViewById(R.id.new_fix_other_contact);
        EditText Description = (EditText) findViewById(R.id.new_fix_edit_details);

        if(problem.getText().length() == 0 || Address.getText().length() == 0 || Description.getText().length() == 0){
            Toast.makeText(this,"请将字段填写完整！",Toast.LENGTH_SHORT).show();
            return;
        }
        if(otherTel.getText().length() == 0){
            otherTel.setText(Configuration.myself.getMobile());
        }


        final FormBody body = new FormBody.Builder()
                .add("problem",problem.getText().toString())
                .add("build_type",BuildType.getSelectedItem().toString())
                .add("build_number",BuildNumber.getSelectedItem().toString())
                .add("desc",Description.getText().toString())
                .add("address",Address.getText().toString())
                .add("user_mobile",otherTel.getText().toString())
                .build();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("waiting");
        progressDialog.setMessage("提交中....");
        progressDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                final Request request = new Request.Builder()
                        .url(Configuration.SERVER_HOST+"/portal/service/offerService")
                        .post(body)
                        .headers(Configuration.headers)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Looper.prepare();
                        Toast.makeText(NewFixActivity.this,"网络请求失败",Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String result = response.body().string();
                        Message msg = Message.obtain();
                        msg.what = PROGRESSBAR_HIDE;
                        handler.sendMessage(msg);
                        try{
                            Gson gson = new GsonBuilder().create();
                            SimpleResultBean bean = gson.fromJson(result,SimpleResultBean.class);

                            if(bean.getCode() == 1){
                                Looper.prepare();
                                Toast.makeText(NewFixActivity.this,"提交成功！请刷新查看",Toast.LENGTH_LONG).show();
                                msg = Message.obtain();
                                msg.what = FINISH_ACT;
                                handler.sendMessage(msg);
                                Looper.loop();
                            }else{
                                Looper.prepare();
                                Toast.makeText(NewFixActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println(result);
                            Looper.prepare();
                            Toast.makeText(NewFixActivity.this,"数据解析失败",Toast.LENGTH_SHORT).show();
                            Looper.loop();

                        }

                    }
                });



            }
        },100);
    }
}
