package com.example.plex.vfix.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plex.vfix.R;
import com.example.plex.vfix.beans.OrdinaryResultBean;
import com.example.plex.vfix.beans.ProfileResultBean;
import com.example.plex.vfix.utils.Configuration;
import com.example.plex.vfix.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 登陆界面
 */
public class LoginActivity extends AppCompatActivity {

    public static final int REQUEST_REG = 0x5;
    public static final int PROGRESSBAR_HIDE = 0x10;
    public static final int PROGRESSBAR_SHOW = 0x16;


    private ProgressBar handler_progressbar = null;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESSBAR_HIDE:
                    handler_progressbar.setVisibility(View.INVISIBLE);
                    break;
            }

        }
    };


    private OkHttpClient client = new OkHttpClient();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.anim_enter_simple,R.anim.anim_exit_simple);

        //fetch server

        SharedPreferences sp = getSharedPreferences("server_cfg",MODE_PRIVATE);
        Configuration.SERVER_HOST = sp.getString("host",Configuration.SERVER_HOST);

        EditText editText_phone = (EditText) findViewById(R.id.login_edit_tel);
        EditText editText_pwd   = (EditText) findViewById(R.id.login_edit_pwd);

        editText_phone.setText(sp.getString("usr",""));
        editText_pwd.setText(sp.getString("pwd",""));


    }


    public void forgetPassword(View v){
        Toast.makeText(LoginActivity.this,"目前请联系管理员进行重置",Toast.LENGTH_LONG).show();
    }

    public void register(View v){

        startActivityForResult(new Intent(LoginActivity.this,RegisterActivity.class),REQUEST_REG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void login(View v){

        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.firstBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);
        this.handler_progressbar = progressBar;
        final EditText phone = (EditText) findViewById(R.id.login_edit_tel);
        final EditText pwd   = (EditText) findViewById(R.id.login_edit_pwd);

        if(phone.getText().length() != 11 || pwd.getText().length() == 0){
            progressBar.setVisibility(ProgressBar.INVISIBLE);
            Toast.makeText(LoginActivity.this,"账号/密码输入不合法",Toast.LENGTH_SHORT).show();
            return;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Map<String,String> map = new HashMap<>();
                map.put("username",phone.getText().toString());
                map.put("password",pwd.getText().toString());
                map.put("device_type","mobile");

                String string = Utils.generateUrlString(map);

                Log.i(Thread.currentThread().getClass().getSimpleName(),string);
                try{
                    Request request = new Request.Builder().url(Configuration.SERVER_HOST+"user/public/login"+string).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message msg = Message.obtain();
                            msg.what = PROGRESSBAR_HIDE;
                            handler.sendMessage(msg);
                            Looper.prepare();
                            Toast.makeText(LoginActivity.this,"网络异常",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if(response.code() == 200){
                                String result = response.body().string();
                                Log.e(Thread.currentThread().getClass().getSimpleName(),result);
                                Gson gson = new GsonBuilder().create();
                               try{
                                   OrdinaryResultBean obj = gson.fromJson(result, OrdinaryResultBean.class);

                                   if(obj.getCode() == 1){
                                       Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                       //save this

                                       SharedPreferences.Editor editor = getSharedPreferences("server_cfg",MODE_PRIVATE).edit();

                                       editor.putString("usr",phone.getText().toString());
                                       editor.putString("pwd",pwd.getText().toString());
                                       editor.apply();

                                       //save token;

                                       Configuration.TOKEN = obj.getData().getToken();
                                       //write header to configuration file for later usage

                                       Configuration.headers = new Headers.Builder().add("XX-Token",Configuration.TOKEN.trim()).add("XX-Device-Type","mobile").build();

                                       String url = Configuration.SERVER_HOST +"user/profile/userInfo";

                                       OkHttpClient client = new OkHttpClient();
                                       Request request = new Request.Builder().url(url).headers(Configuration.headers).build();
                                       response = client.newCall(request).execute();
                                       gson = new GsonBuilder().create();
                                       ProfileResultBean b;
                                       try{
                                          b  = gson.fromJson(response.body().string(),ProfileResultBean.class);
                                       }catch (Exception e){
                                           Message msg = Message.obtain();
                                           msg.what = PROGRESSBAR_HIDE;
                                           handler.sendMessage(msg);
                                           e.printStackTrace();
                                           Looper.prepare();
                                           Toast.makeText(LoginActivity.this,"用户信息解析失败",Toast.LENGTH_SHORT).show();
                                           Looper.loop();

                                           return;
                                       }
                                       System.out.println(b.getData().getNickname()+"登陆系统");

                                       Configuration.myself = b.getData();

                                       Message msg = Message.obtain();
                                       msg.what = PROGRESSBAR_HIDE;
                                       handler.sendMessage(msg);
                                       //###########################################################
                                       startActivity(intent);
                                   }else{
                                       Looper.prepare();
                                       Toast.makeText(LoginActivity.this,obj.getMsg(),Toast.LENGTH_SHORT).show();
                                       Looper.loop();
                                   }
                               }catch (Exception e){
                                   e.printStackTrace();
                                   Looper.prepare();
                                   Toast.makeText(LoginActivity.this,"信息解析失败",Toast.LENGTH_SHORT).show();
                                   Looper.loop();
                                   return;

                               }
                            }else{
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this,"服务器返回信息有误",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }

                        }
                    });
                }catch (Exception e){
                    Message msg = Message.obtain();
                    msg.what = PROGRESSBAR_HIDE;
                    handler.sendMessage(msg);
                    Looper.prepare();
                    Toast.makeText(LoginActivity.this,"服务器通信鉴权失败",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    e.printStackTrace();
                }
            }
        },100);

    }

    public void provision(View v){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://ibytes.cn/copyright")));
    }

    public void setting(View v){

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View  view  =View.inflate(this,R.layout.login_setting_prompt,null);
        EditText info_edit = view.findViewById(R.id.prompt_servername);
        info_edit.setText(Configuration.SERVER_HOST);
        builder.setTitle("设置服务器地址");
        builder.setView(view);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog body = (AlertDialog) dialogInterface;
                EditText infoedit = (EditText) body.findViewById(R.id.prompt_servername);
                if(infoedit.getText().toString().startsWith("http") && infoedit.getText().length() > 8){
                    SharedPreferences.Editor editor = getSharedPreferences("server_cfg",MODE_PRIVATE).edit();
                    editor.putString("host",infoedit.getText().toString());
                    editor.apply();
                    Configuration.SERVER_HOST = infoedit.getText().toString();
                    Toast.makeText(LoginActivity.this,"服务器地址设置成功！",Toast.LENGTH_SHORT).show();
                    body.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this,"服务器地址设置失败！！",Toast.LENGTH_SHORT).show();
                    body.dismiss();
                }


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();


    }

}
