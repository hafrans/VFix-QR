package com.example.plex.vfix.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.plex.vfix.R;
import com.example.plex.vfix.activities.DetailsActivity;
import com.example.plex.vfix.beans.FixDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Plex on 2017/9/9.
 */

public class FixListAdaptor extends BaseAdapter {

    private List<ContentValues> contentValues = new ArrayList<>();
    private Context mContext;
    private ContentValues data;

    public FixListAdaptor(List<ContentValues> dataSet,Context context) {
        super();
        this.contentValues = dataSet;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return contentValues.size();
    }

    @Override
    public ContentValues getItem(int i) {
        return contentValues.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        FixListViewHolder viewHolder = null;

        if(view == null){
            view = layoutInflater.inflate(R.layout.list_fix_details,null,false);
            viewHolder = new FixListViewHolder();
            viewHolder.background = view.findViewById(R.id.list_fix_status_bk);
            viewHolder.btn = view.findViewById(R.id.list_fix_btn);
            viewHolder.operator = view.findViewById(R.id.list_fix_operator);
            viewHolder.process = view.findViewById(R.id.list_fix_status);
            viewHolder.startTime = view.findViewById(R.id.list_fix_start_time);
            viewHolder.updateTime = view.findViewById(R.id.list_fix_update_time);
            viewHolder.title = view.findViewById(R.id.list_fix_title);
            view.setTag(viewHolder);
        }else{
            viewHolder = (FixListViewHolder) view.getTag();
        }

        data = contentValues.get(i);

        viewHolder.title.setText((String) data.get("title"));
        viewHolder.startTime.setText((String) data.get("startTime"));
        viewHolder.updateTime.setText((String) data.get("updateTime"));
        viewHolder.process.setText((String) data.get("process"));
        viewHolder.operator.setText((String) data.get("operator"));

        //status
        int status = data.getAsInteger("status");
        switch (status){
            case FixDetailBean.FIX_STATUS_NOT_ACCEPTED:
                viewHolder.process.setText("未受理");
                viewHolder.background.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_fix_not_accpeted));
                break;
            case FixDetailBean.FIX_STATUS_PENDDING:
                viewHolder.process.setText("处理中");
                viewHolder.background.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_fix_pending));
                break;
            case FixDetailBean.FIX_STATUS_SUCCESS:
                viewHolder.process.setText("处理成功");
                viewHolder.background.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.list_fix_success));
                break;

        }



        viewHolder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("values",contentValues.get(i));
                mContext.startActivity(intent);
            }
        });


        return view;

    }
}
