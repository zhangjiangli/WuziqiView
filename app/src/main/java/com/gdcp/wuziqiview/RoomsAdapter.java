package com.gdcp.wuziqiview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by asus- on 2017/6/17.
 */

public class RoomsAdapter extends BaseAdapter{
    private List<Match>matchList;
    private Context context;
    private OnlickListener onlickListener;
    public RoomsAdapter(List<Match>matchList, Context context){
        this.matchList=matchList;
        this.context=context;
    }

    public void setOnlickListener(OnlickListener onlickListener) {
        this.onlickListener = onlickListener;
    }

    @Override
    public int getCount() {
        return matchList.size();
    }

    @Override
    public Object getItem(int position) {
        return matchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.roomview,parent,false);
        }
        TextView roomnum= (TextView) convertView.findViewById(R.id.num_room);
        TextView roomuser= (TextView) convertView.findViewById(R.id.user_room);
        final Button btnjoin= (Button) convertView.findViewById(R.id.join_room);
        final Match match= matchList.get(position);
        roomnum.setText(match.getRoom()+"");
        roomuser.setText(match.getUserA());
        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlickListener.select(match);
            }
        });
        return convertView;
    }
}
