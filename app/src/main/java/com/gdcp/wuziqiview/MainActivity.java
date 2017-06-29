package com.gdcp.wuziqiview;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private WuziqiView wuziqiView;
    private Match match=new Match();
    private ProgressDialog mProgressDialog;
    private static final int BACK =1002 ;
    private static final int START =1001 ;
    private EditText phone;
    private BmobRealTimeData rtd;
    private String id=null;
    private Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what==100){
                wuziqiView.updateData();
                /*Toast.makeText(MainActivity.this, "已收到消息", Toast.LENGTH_SHORT).show();*/
            }

            super.handleMessage(msg);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wuziqiView= (WuziqiView) findViewById(R.id.wuziqiView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.start){
            wuziqiView.start();
            return true;
        }
        if (id==R.id.undo){
            wuziqiView.undo();
            return true;
        }

        if (id==R.id.newRoom){
            View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.view,null,false);
            phone= (EditText) view.findViewById(R.id.edt_phone);
            final EditText roomNub= (EditText) view.findViewById(R.id.edt_roomname);
            final EditText username= (EditText) view.findViewById(R.id.edt_username);
            new AlertDialog.Builder(MainActivity.this).setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String getphone=phone.getText().toString();
                    int getroomNub=Integer.parseInt(roomNub.getText().toString());
                    String getusername=username.getText().toString();
                    match.setPhone(getphone);
                    match.setRoom(getroomNub);
                    match.setUserA(getusername);
                    match.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                           if (e==null){
                               /*wuziqiView.queryId();*/
                               Toast.makeText(MainActivity.this, "创建成功", Toast.LENGTH_SHORT).show();
                               /*showProgressDialog("等待玩家加入. .....");*/
                               queryObjectId();

                           }else {

                               Toast.makeText(MainActivity.this, "创建失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }
            }).setNegativeButton("取消",null).create().show();
            return true;
        }


        if (id==R.id.joinRoom){
            //启动另一个activity
            Intent intent=new Intent(this,RoomActivity.class);
            startActivityForResult(intent,START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void queryObjectId() {
        BmobQuery<Match> query=new BmobQuery<>();
        query.addWhereEqualTo("phone",phone.getText().toString());
        query.findObjects(new FindListener<Match>() {
            @Override
            public void done(List<Match> list, BmobException e) {
                if (e==null){
                     wuziqiView.setObjectId(list.get(0).getObjectId());
                    id=list.get(0).getObjectId();
                    //开始监听
                    listenTo(list.get(0).getObjectId());

                }else{

                }
            }
        });
    }


    public void showProgressDialog(String msg) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setMessage(msg);
        mProgressDialog.show();

    }

    public void hideProgressDialog() {
        mProgressDialog.hide();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==START&&resultCode==BACK){
                 Match backmatch= (Match) data.getSerializableExtra("match");
            if (backmatch!=null){
                Toast.makeText(this,backmatch.getRoom()+":"+backmatch.getUserA(), Toast.LENGTH_SHORT).show();
                wuziqiView.setObjectId(backmatch.getObjectId());
                wuziqiView.setIsWhite(false);
                id=backmatch.getObjectId();
                //开始监听
                listenTo(backmatch.getObjectId());
                //fdfdfdfdfdfd
            }
        }
    }

   //开始监听行数据变化
    private void listenTo(final String objectId){
        rtd = new BmobRealTimeData();
        rtd.start(new ValueEventListener() {
            @Override
            public void onDataChange(JSONObject data) {
               /* Toast.makeText(MainActivity.this, "onDataChange", Toast.LENGTH_SHORT).show();*/
               //更新数据
                Message msg=new Message();
                msg.what=100;
                myHandler.sendMessage(msg);

            }


            @Override
            public void onConnectCompleted(Exception ex) {
                rtd.subRowUpdate("Match", objectId);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (id!=null){
            rtd.unsubRowUpdate("Match", id);
        }
    }
}
