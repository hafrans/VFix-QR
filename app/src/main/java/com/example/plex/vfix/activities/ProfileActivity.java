package com.example.plex.vfix.activities;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.plex.vfix.R;
import com.example.plex.vfix.utils.Configuration;

import org.w3c.dom.Text;

/**
 * 用户信息界面
 *
 */
public class ProfileActivity extends AppCompatActivity {

    public static final int FINISH_ACT = 0x48;

    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case FINISH_ACT:
                    finish();
                    break;
            }

        }
    };

    public static void startActivity(AppCompatActivity appCompatActivity,String name,String phone){

        Intent intent  = new Intent(appCompatActivity,ProfileActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("phone",phone);

        appCompatActivity.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView tv_name = (TextView) findViewById(R.id.profile_name);
        tv_name.setText(Configuration.myself.getNickname());
        TextView tv_phone= (TextView) findViewById(R.id.profile_phone);
        tv_phone.setText(Configuration.myself.getMobile());
    }
}
