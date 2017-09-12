package com.example.plex.vfix.activities;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.plex.vfix.R;
import com.example.plex.vfix.beans.OrdinaryResultBean;
import com.example.plex.vfix.utils.Configuration;
import com.example.plex.vfix.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 注册界面
 */
public class RegisterActivity extends AppCompatActivity {


    public static final int TOAST_TIP = 0x16;
    public static final int PROGRESSBAR_HIDE = 0x45;
    public Button handlerButton = null;
    private OkHttpClient client = new OkHttpClient();

    private ProgressBar handlerProgressBar = null;

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = null;
            switch (msg.what) {
                case 1:
                    bundle = msg.getData();
                    handlerButton.setText(bundle.getString("msg"));
                    break;
                case 2:
                    RegisterActivity.this.handlerButton.setText(R.string.get_validate_code);
                    RegisterActivity.this.handlerButton.setClickable(true);
                    break;
                case 3:
                    RegisterActivity.this.handlerButton.setClickable(false);
                    break;
                case TOAST_TIP:
                    bundle = msg.getData();
                    Toast.makeText(RegisterActivity.this,bundle.get("msg").toString(),Toast.LENGTH_SHORT).show();
                    break;
                case PROGRESSBAR_HIDE:
                    handlerProgressBar.setVisibility(View.INVISIBLE);
                default:
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        overridePendingTransition(R.anim.anim_enter_simple, R.anim.anim_exit_simple);

        Toolbar toolbar = (Toolbar) findViewById(R.id.register_toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });
    }

    public void register(View v) {
        //Toast.makeText(RegisterActivity.this, "注册动作", Toast.LENGTH_LONG);
        //show progressbar
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.register_progressbar);
        handlerProgressBar = progressBar;

        //fields

        EditText nickname = (EditText) findViewById(R.id.register_edit_name);
        EditText phone    = (EditText) findViewById(R.id.register_edit_phone);
        EditText validate_code = (EditText) findViewById(R.id.register_edit_validate);
        EditText passwd   = (EditText) findViewById(R.id.register_edit_password);
        EditText pwdCheck = (EditText) findViewById(R.id.register_edit_password_confirm);

        //check some fields

        if(nickname.getText().length() == 0
                || phone.getText().length() == 0
                || validate_code.getText().length() == 0
                || passwd.getText().length() == 0){
            Toast.makeText(this,"先把字段填好:)",Toast.LENGTH_SHORT).show();
            return;
        }


        //check two passwords is the same

        if(! passwd.getText().toString().equals(pwdCheck.getText().toString())){
            Toast.makeText(this,"两次密码不一致！",Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String,String> map = new HashMap<String,String>();
        map.put("username",phone.getText().toString());
        map.put("password",passwd.getText().toString());
        map.put("verification_code",validate_code.getText().toString());
        map.put("user_nickname",nickname.getText().toString());

        final String string = Utils.generateUrlString(map);

        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    Request request = new Request.Builder().url(Configuration.SERVER_HOST+"/user/public/register"+string).build();
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message msg = Message.obtain();
                            msg.what = PROGRESSBAR_HIDE;
                            handler.sendMessage(msg);
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"服务器请求失败！",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message msg = Message.obtain();
                            msg.what = PROGRESSBAR_HIDE;
                            handler.sendMessage(msg);
                            if(response.code() != 200){
                                Looper.prepare();
                                Toast.makeText(RegisterActivity.this,"服务器返回数据异常",Toast.LENGTH_SHORT).show();
                                Looper.loop();
                                return;
                            }else{
                                String result = response.body().string();
                                Gson gson = new GsonBuilder().create();
                                OrdinaryResultBean obj = gson.fromJson(result,OrdinaryResultBean.class);
                                if(obj.getCode() == 1){//good
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this,obj.getMsg(),Toast.LENGTH_SHORT).show();
                                    RegisterActivity.this.finish();
                                    Looper.loop();

                                }else{
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this,obj.getMsg(),Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }

                            }
                        }
                    });
                }catch (Exception e){
                    Message msg = Message.obtain();
                    msg.what = PROGRESSBAR_HIDE;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"鉴权失败！",Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }
            }
        }).start();
     }

    public void getValidateCode(final View v) {
        Log.i(Thread.currentThread().getClass().getSimpleName(), "send message");
        new Thread(new Runnable() {
            @Override
            public void run() {
                //network operations
                EditText phoneNumber = (EditText) findViewById(R.id.register_edit_phone);
                if(phoneNumber.getText().length() != 11){
                    Looper.prepare();
                    Toast.makeText(RegisterActivity.this,"手机号",Toast.LENGTH_SHORT).show();
                    Looper.loop();
                    return ;
                }


                try{
                    Request request = new Request.Builder().url(Configuration.SERVER_HOST+"user/verification_code/send?username="+phoneNumber.getText().toString()).build();

                    Response response = null;
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Looper.prepare();
                            Toast.makeText(RegisterActivity.this,"请求有误",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Looper.prepare();
                            if(response.code() != 200){
                                Toast.makeText(RegisterActivity.this,"服务器传值有问题",Toast.LENGTH_SHORT).show();
                            }else{
                                Gson gson = new GsonBuilder().create();
                                OrdinaryResultBean bean = gson.fromJson(response.body().string(),OrdinaryResultBean.class);
                                Log.i(Thread.currentThread().getClass().getSimpleName(),bean.getMsg());
                                if(bean.getCode() == 1){
                                    Toast.makeText(RegisterActivity.this,"验证码已经发送至您的手机",Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(RegisterActivity.this,bean.getMsg(),Toast.LENGTH_SHORT).show();
                                }
                            }
                            Looper.loop();
                        }
                    });

                }catch (Exception e){
                    e.printStackTrace();
                    return;
                }

                int sec = 30;
                RegisterActivity.this.handlerButton = (Button) v;
                //Looper.prepare();
                Message msg = Message.obtain();
                msg.what = 3;
                handler.sendMessage(msg);

                while (sec >= 0) {

                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "还剩" + sec-- + "秒");
                    message.setData(bundle);
                    handler.sendMessage(message);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                msg = Message.obtain();
                msg.what = 2;
                handler.sendMessage(msg);
                // Looper.loop();


            }
        }).start();
    }
}
