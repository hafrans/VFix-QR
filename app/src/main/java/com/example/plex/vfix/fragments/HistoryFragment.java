package com.example.plex.vfix.fragments;


import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.plex.vfix.R;
import com.example.plex.vfix.adapters.FixListAdaptor;
import com.example.plex.vfix.beans.FixDetailBean;
import com.example.plex.vfix.beans.FixListResultBean;
import com.example.plex.vfix.utils.Configuration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 *历史维修查询
 */
public class HistoryFragment extends Fragment {

    public static final int PROGRESSBAR_HIDE = 0x10;
    public static final int PROGRESSBAR_SHOW = 0x15;
    public static final int REFRESH_START = 0x12;
    public static final int REFRESH_END   = 0x1a;
    public static final int ADAPTOR_NOTIFY = 0x56;
    public static final int ADAPTOR_REFRESH = 0x55;
    public static final int TOAST = 0x1d;
    private OkHttpClient client = new OkHttpClient();
    private FrameLayout frameLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FixListAdaptor adaptor;
    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESSBAR_HIDE:
                    frameLayout.setVisibility(View.INVISIBLE);
                    break;
                case PROGRESSBAR_SHOW:
                    frameLayout.setVisibility(View.VISIBLE);
                    break;
                case REFRESH_END:
                    swipeRefreshLayout.setRefreshing(false);
                    break;
                case REFRESH_START:
                    swipeRefreshLayout.setRefreshing(true);
                    break;
                case TOAST:
                    Bundle bundle = msg.getData();
                    Toast.makeText(HistoryFragment.this.getContext(),bundle.get("msg").toString(),Toast.LENGTH_SHORT).show();
                    break;
                case ADAPTOR_REFRESH:
                    adaptor.notifyDataSetChanged();
                    break;

            }

        }
    };

    private List<ContentValues> list = new ArrayList<>();


    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        swipeRefreshLayout = getActivity().findViewById(R.id.history_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });

        ListView listView = getActivity().findViewById(R.id.history_list);
        adaptor = new FixListAdaptor(list,this.getContext());
        listView.setAdapter(adaptor);

        frameLayout = getActivity().findViewById(R.id.history_tip);
        frameLayout.setVisibility(View.INVISIBLE);

        refresh();

    }

    public void refresh(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //fetch data


                try{
                    Request request = new Request.Builder().headers(Configuration.headers).url(Configuration.SERVER_HOST+"portal/service/getsolved").build();
                    Log.i(Thread.currentThread().getClass().getSimpleName(),Configuration.TOKEN.trim());
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Message msg = Message.obtain();
                            msg.what = REFRESH_END;
                            handler.sendMessage(msg);
                            Looper.prepare();
                            Toast.makeText(getActivity(),"服务器繁忙，请稍后重试",Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message msg = Message.obtain();
                            msg.what = REFRESH_END;
                            handler.sendMessage(msg);
                            Looper.prepare();

                            if(response.code() != 200){

                                Toast.makeText(getActivity(),"服务器繁忙，请稍后重试",Toast.LENGTH_SHORT).show();
                                msg = Message.obtain();
                                msg.what = PROGRESSBAR_SHOW;
                                handler.sendMessage(msg);


                            }else{
                                Gson gson = new GsonBuilder().create();
                                String str = response.body().string();
                                Log.i(Thread.currentThread().getClass().getSimpleName(),str);
                                FixListResultBean bean;
                                try{
                                    bean = gson.fromJson(str,FixListResultBean.class);
                                }catch (Exception e){
                                    Log.i(Thread.currentThread().getClass().getSimpleName(),"无法读取！！");
                                    msg = Message.obtain();
                                    msg.what = PROGRESSBAR_SHOW;
                                    handler.sendMessage(msg);
                                    msg = Message.obtain();
                                    msg.what = REFRESH_END;
                                    handler.sendMessage(msg);
                                    Looper.loop();
                                    return;
                                }

                                if(bean.getData() == null){
                                    msg = Message.obtain();
                                    msg.what = PROGRESSBAR_SHOW;
                                    handler.sendMessage(msg);
                                }else{
                                    list.clear();
                                    for(FixDetailBean bb : bean.getData()){
                                        ContentValues contentValues = new ContentValues();
                                        contentValues.put("title",bb.getTitle());
                                        contentValues.put("startTime",bb.getStartTime());
                                        contentValues.put("updateTime",bb.getUpdateTime());
                                        contentValues.put("status",bb.getStatus());
                                        contentValues.put("id",bb.getId());
                                        contentValues.put("repairer_phone",bb.getRepairerMobile());
                                        contentValues.put("repairer_name",bb.getRepairerName());
                                        contentValues.put("resolve",bb.getResolve());
                                        contentValues.put("desc",bb.getDescription());
                                        contentValues.put("build_type",bb.getBuildType());
                                        contentValues.put("build_num",bb.getBuildNumber());
                                        contentValues.put("address",bb.getAddress());
                                        contentValues.put("operator",bb.getRepairerName() == null ? "等待中":bb.getRepairerName());
                                        list.add(contentValues);
                                    }

                                    msg = Message.obtain();
                                    msg.what = ADAPTOR_REFRESH;
                                    handler.sendMessage(msg);
                                    //notify signal
                                }

                                Looper.loop();




                            }

                        }
                    });

                }catch (Exception e){
                    Message msg = Message.obtain();
                    msg.what = REFRESH_END;
                    handler.sendMessage(msg);
                    e.printStackTrace();
                    Looper.prepare();
                    Toast.makeText(getActivity(),"网络出现问题，请稍后重试",Toast.LENGTH_SHORT).show();
                    Looper.loop();

                }


            }
        },0);
    }

}
