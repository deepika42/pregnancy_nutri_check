package com.example.pregnancynutricheck;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private ArrayList<Model> recordList;

    public RecordAdapter(Context context, int layout, ArrayList<Model>recordList){
        this.context=context;
        this.layout=layout;
        this.recordList=recordList;
    }

    @Override
    public int getCount() {
        return recordList.size();
    }

    @Override
    public Object getItem(int i) {
        return recordList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private class ViewHolder{
        TextView txtName,txtDetail,txtDate,txtTime,txtNote;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        View row=view;
        ViewHolder holder=new ViewHolder();

        if(row==null){
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(layout,null);
            holder.txtName= row.findViewById(R.id.txtName);
            holder.txtDetail= row.findViewById(R.id.txtDetail);
            holder.txtDate= row.findViewById(R.id.txtDate);
            holder.txtTime= row.findViewById(R.id.txtTime);
            holder.txtNote= row.findViewById(R.id.txtNote);
            row.setTag(holder);

        }
        else{
            holder=(ViewHolder)row.getTag();
        }

        Model model= recordList.get(i);
        holder.txtName.setText(model.getName());
        holder.txtDetail.setText(model.getDetail());
        holder.txtDate.setText(model.getDate());
        holder.txtTime.setText(model.getTime());
        holder.txtNote.setText(model.getNote());

        return row;
    }


}
