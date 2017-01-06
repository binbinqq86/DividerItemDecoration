package com.binbin.divideritemdecoration;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.*;
import android.support.v7.widget.DividerItemDecoration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView recyclerView;
    private List<String> str = new ArrayList<>();
    private MyAdapter adapter;
    private Button btHas,btNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView= (RecyclerView) findViewById(R.id.rv);
        btHas= (Button) findViewById(R.id.has);
        btNo= (Button) findViewById(R.id.no);
        for (int i = 0; i < 11; i++) {
            str.add(i + "个");
        }
        //设置adapter
        adapter=new MyAdapter(str,this);
        final DividerItemDecoration2 dividerItemDecoration=new DividerItemDecoration2(this);
        dividerItemDecoration.setDrawBorderTopAndBottom(true);
        dividerItemDecoration.setDrawBorderLeftAndRight(true);
//        dividerItemDecoration.setOnlySetItemOffsetsButNoDraw(true);
        recyclerView.addItemDecoration(dividerItemDecoration);

        //设置布局管理器
//        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(5,1);
        GridLayoutManager layoutManager=new GridLayoutManager(this,3);
//        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        layoutManager.setOrientation(1);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (3 - position % 3);
//                return 1;
            }
        });
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        btHas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.removeItemDecoration(dividerItemDecoration);
//                dividerItemDecoration.setDrawBorderLine(true);
                recyclerView.addItemDecoration(dividerItemDecoration);
            }
        });
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.removeItemDecoration(dividerItemDecoration);
//                dividerItemDecoration.setDrawBorderLine(false);
                recyclerView.addItemDecoration(dividerItemDecoration);
            }
        });
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> datas;
        private Context mContext;
        public MyAdapter(List<String> datas,Context mContext){
            this.datas=datas;
            this.mContext=mContext;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item, parent,
                    false));
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.tv.setText(datas.get(holder.getAdapterPosition()));
//            if(holder.getAdapterPosition()==0){
//                holder.tv.setBackgroundColor(Color.BLUE);
//            }else if(holder.getAdapterPosition()==1){
//                holder.tv.setBackgroundColor(Color.MAGENTA);
//            }else if(holder.getAdapterPosition()==2){
//                holder.tv.setBackgroundColor(Color.GREEN);
//            }else{
//                holder.tv.setBackgroundColor(Color.WHITE);
//            }
            //手动更改高度，不同位置的高度有所不同
//            holder.tv.setHeight(100 + (position % 3) * 30);
        }

        @Override
        public int getItemCount() {
            return datas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public MyViewHolder(View view) {
                super(view);
                tv = (TextView) view.findViewById(R.id.tv);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
