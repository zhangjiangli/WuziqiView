package com.gdcp.wuziqiview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class RoomActivity extends AppCompatActivity implements OnlickListener{
    private static final int BACK =1002 ;
    private ListView rooms;
    private RoomsAdapter roomsAdapter;
    private List<Match>matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        rooms= (ListView) findViewById(R.id.rooms);
        matchList=new ArrayList<>();
        roomsAdapter=new RoomsAdapter(matchList,this);
        roomsAdapter.setOnlickListener(this);
        rooms.setAdapter(roomsAdapter);
        query();
    }

    private void query() {
        BmobQuery<Match> query=new BmobQuery<>();
        query.findObjects(new FindListener<Match>() {
            @Override
            public void done(List<Match> list, BmobException e) {
                 if (e==null){
                     matchList.addAll(list);
                     roomsAdapter.notifyDataSetChanged();
                 }else{

                 }
            }
        });

    }

    @Override
    public void select(Match match) {
        Intent intent=new Intent();
        intent.putExtra("match",match);
        setResult(BACK,intent);
        finish();
    }
}
