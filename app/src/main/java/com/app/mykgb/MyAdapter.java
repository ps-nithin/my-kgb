package com.app.mykgb;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by inspiron3000 on 08-Oct-17.
 */

public class MyAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<RowData> mDataSource;

    public MyAdapter (Context context,List<RowData> items){
        mContext = context;
        mDataSource = (ArrayList<RowData>) items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        // TODO Auto-generated method stub
        return mDataSource.size();
    }

    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mDataSource.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        mInflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView=mInflater.inflate(R.layout.list_item,parent,false);
        TextView title=(TextView) rowView.findViewById(R.id.title);
        TextView subtitle=(TextView) rowView.findViewById(R.id.subtitle);
        RowData rowData=(RowData) getItem(position);
        title.setText(rowData.title);
        subtitle.setText(rowData.subtitle);

        return rowView;

    }
}
