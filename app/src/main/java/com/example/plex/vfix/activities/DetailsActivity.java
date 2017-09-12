package com.example.plex.vfix.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plex.vfix.R;
import com.example.plex.vfix.beans.FixDetailBean;

/**
 * 维修详情
 */
public class DetailsActivity extends AppCompatActivity {


    private final static int PERMISSION_CALL_REQUEST = 0x15;
    public FloatingActionButton fab;
    private int status;
    private String title;
    private String startTime;
    private String updateTime;
    private String desc;
    private String repairerName;
    private String repairerPhone;
    private String resolve;
    private String buildType;
    private String buildNum;
    private String addr;
    private String[] permissions = new String[]{Manifest.permission.CALL_PHONE};

    public static void startActivity(ContentValues values, Context context) {

        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("values", values);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.details_toolbar);

        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(DetailsActivity.this.getClass().getSimpleName(), "RUN CLICK");
                DetailsActivity.this.finish();
            }
        });


        //fill with infos

        ContentValues values = getIntent().getParcelableExtra("values");

        title = values.getAsString("title") == null ? "没有数据" : values.getAsString("title");
        startTime = values.getAsString("startTime") == null ? "2017-01-01 01:01:02" : values.getAsString("startTime");
        updateTime = values.getAsString("updateTime") == null ? "2017-01-01 01:01:03" : values.getAsString("updateTime");
        status = values.getAsInteger("status") == null ? 0 : values.getAsInteger("status");
        repairerName = values.getAsString("repairer_name") == null ? "未知" : values.getAsString("repairer_name");
        repairerPhone = values.getAsString("repairer_phone") == null ? "1234567890" : values.getAsString("repairer_phone");
        resolve = values.getAsString("resolve") == null ? "" : values.getAsString("resolve");
        desc = values.getAsString("desc") == null ? "" : values.getAsString("desc");
        buildType = values.getAsString("build_type") == null ? "--" : values.getAsString("build_type");
        buildNum = values.getAsString("build_num") == null ? "--" : values.getAsString("build_num");
        addr = values.getAsString("address") == null ? "--" : values.getAsString("address");


        TextView _problem = (TextView) findViewById(R.id.details_problem);
        TextView _start = (TextView) findViewById(R.id.details_start_time);
        TextView _update = (TextView) findViewById(R.id.details_update_time);
        TextView _addr = (TextView) findViewById(R.id.details_address);
        TextView _status = (TextView) findViewById(R.id.details_status);
        TextView _name = (TextView) findViewById(R.id.details_repairer_name);
        TextView _desc = (TextView) findViewById(R.id.details_desc);
        TextView _resolve = (TextView) findViewById(R.id.details_resolve);

        _problem.setText(title);
        _start.setText(startTime);
        _update.setText(updateTime);
        _name.setText(repairerName);
        _desc.setText(desc);
        _resolve.setText(resolve);
        _addr.setText(buildType + " " + buildNum + " " + addr);


        switch (status) {
            case FixDetailBean.FIX_STATUS_NOT_ACCEPTED:
                toolbar.setTitle("等待处理");
                _status.setText("等待处理");


                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(DetailsActivity.this, "维护员没有确定！无法拨打", Toast.LENGTH_LONG).show();
                    }
                });


                break;
            case FixDetailBean.FIX_STATUS_PENDDING:
                toolbar.setTitle("处理中");
                _status.setText("处理中");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // 检查该权限是否已经获取
                            int i = ContextCompat.checkSelfPermission(DetailsActivity.this, permissions[0]);

                            if (i != PackageManager.PERMISSION_GRANTED) {
                                // 如果没有授予该权限，就去提示用户请求
                                ActivityCompat.requestPermissions(DetailsActivity.this, permissions, PERMISSION_CALL_REQUEST);
                            }else{
                                try {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairerPhone));
                                    DetailsActivity.this.startActivity(intent);
                                } catch (Exception e) {
                                    Toast.makeText(DetailsActivity.this, "拨打电话权限已被禁止，请检查权限！", Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            try {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairerPhone));
                                DetailsActivity.this.startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(DetailsActivity.this, "拨打电话权限已被禁止，请检查权限！", Toast.LENGTH_LONG).show();
                            }
                        }

                    }
                });

                break;
            case FixDetailBean.FIX_STATUS_SUCCESS:
                toolbar.setTitle("处理成功");
                _status.setText("处理成功");
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            // 检查该权限是否已经获取
                            int i = ContextCompat.checkSelfPermission(DetailsActivity.this, permissions[0]);

                            if (i != PackageManager.PERMISSION_GRANTED) {
                                // 如果没有授予该权限，就去提示用户请求
                                ActivityCompat.requestPermissions(DetailsActivity.this, permissions, PERMISSION_CALL_REQUEST);
                            }else{
                                try {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairerPhone));
                                    DetailsActivity.this.startActivity(intent);
                                } catch (Exception e) {
                                    Toast.makeText(DetailsActivity.this, "拨打电话权限已被禁止，请检查权限！", Toast.LENGTH_LONG).show();
                                }
                            }
                        }else{
                            try {
                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairerPhone));
                                DetailsActivity.this.startActivity(intent);
                            } catch (Exception e) {
                                Toast.makeText(DetailsActivity.this, "拨打电话权限已被禁止，请检查权限！", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                break;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_CALL_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + repairerPhone));
                    try {
                        DetailsActivity.this.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(DetailsActivity.this, "拨打电话权限已被禁止，请检查权限！", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(DetailsActivity.this, "拨打电话权限已被禁止，请检查权限！", Toast.LENGTH_LONG).show();
                }


                break;
        }

    }
}
