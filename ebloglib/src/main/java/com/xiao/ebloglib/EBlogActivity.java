package com.xiao.ebloglib;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EBlogActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SmartRefreshLayout pullRefrestLayout;
    private TextView mtvView;

    private EBlogAdapter mEblogAdapter;
    private Handler mainHandler = new Handler();

    List<EBlogDataBean> lists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eblog_main);
        initView();

        mainHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                fillMockData();  //模拟进来之后拉取网络数据
            }
        },500);
    }

    Random ra =new Random();
    private void initView(){
        recyclerView = findViewById(R.id.rv_blog_list);
        mtvView = findViewById(R.id.tv_error);
        pullRefrestLayout = findViewById(R.id.refreshLayout);
        pullRefrestLayout.setEnableRefresh(true);
        pullRefrestLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //这里模拟一下
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int addCount = ra.nextInt(10)+ 1;
                        for (int i=0;i<addCount;i++)
                            lists.add(0,new EBlogDataBean("Load"+ra.nextInt(100),
                                    "loaded content",
                                    ra.nextInt(10),
                                    ra.nextInt(10))
                            );
                        mEblogAdapter.setData(lists);
                        pullRefrestLayout.finishRefresh();

                        if (lists.size() > 100)
                            mEblogAdapter.setFooterStatus(EBlogAdapter.FOOTER_STATE_NO_MORE);
                        else
                            mEblogAdapter.setFooterStatus(EBlogAdapter.FOOTER_STATE_MORE);
                    }
                },1000);
            }
        });


        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });


        mtvView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mtvView.setVisibility(View.GONE);
                pullRefrestLayout.autoRefresh();
            }
        });

        initRecycleView();

    }


    private void initRecycleView(){


        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        // 分割线
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        mEblogAdapter = new EBlogAdapter(this);

        // 点击底部的跳转
        mEblogAdapter.setOnFooterClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        int addCount = ra.nextInt(10)+ 1;
                        for (int i=0;i<addCount;i++)
                            lists.add(new EBlogDataBean("Load"+ra.nextInt(100),
                                    "loaded content",
                                    ra.nextInt(10),
                                    ra.nextInt(10))
                            );
                        mEblogAdapter.setData(lists);
                    }
                },1000);
            }
        });

        // 点击Item的跳转
        mEblogAdapter.setOnItemClickListener(new EBlogAdapter.OnItemClickListener() {

            @Override
            public void onFollowClick(View view,int position) {
                Toast.makeText(EBlogActivity.this,"Follow clicked",Toast.LENGTH_SHORT).show();
                EBlogDataBean bean = mEblogAdapter.getItemDataByIndex(position);
                if (bean != null){
                    bean.setmFollowTimes(bean.getmFollowTimes() + 1);
                    mEblogAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onZanClick(View view,int position) {
                Toast.makeText(EBlogActivity.this,"Zan clicked",Toast.LENGTH_SHORT).show();
                EBlogDataBean bean = mEblogAdapter.getItemDataByIndex(position);
                if (bean != null){
                    bean.setmZanTimes(bean.getmZanTimes() + 1);
                    mEblogAdapter.notifyItemChanged(position);
                }
            }

            @Override
            public void onItemClick(View v, int position) {
                EBlogDataBean bean = mEblogAdapter.getItemDataByIndex(position);
                if (bean != null) {
                    Intent intent = new Intent(EBlogActivity.this, EBlogDetailActivity.class);

                    intent.putExtra(EBlogConst.TITLE_KEY, bean.getmTitle());
                    intent.putExtra(EBlogConst.CONTENT_KEY, bean.getmContent());


                    startActivity(intent);
                }else{
                    Toast.makeText(EBlogActivity.this,"Click invalidate Data!",Toast.LENGTH_SHORT).show();
                }
            }
        });

        recyclerView.setAdapter(mEblogAdapter);

        // 初始化停止播放
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE: //滚动停止

                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING: //手指拖动
                    case RecyclerView.SCROLL_STATE_SETTLING: //惯性滚动

                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }


    private void fillMockData(){
        EBlogDataBean eBlogDataBean1 = new EBlogDataBean("Title1","xxxxxxxxx",0,0);
        EBlogDataBean eBlogDataBean2 = new EBlogDataBean("Title2","xxxxxxxxx",1,0);
        EBlogDataBean eBlogDataBean3 = new EBlogDataBean("Title3","The AudioRecord class manages the audio resources for Java applications to record audio from the audio input hardware of the platform. This is achieved by \"pulling\" (reading) the data from the AudioRecord object. The application is responsible for polling the AudioRecord object in time using one of the following three methods: read(byte[], int, int), read(short[], int, int) or read(java.nio.ByteBuffer, int). The choice of which method to use will be based on the audio data storage format that is the most convenient for the user of AudioRecord.",2,0);
        EBlogDataBean eBlogDataBean4 = new EBlogDataBean("Title4","xxxxxxxxx",3,0);
        EBlogDataBean eBlogDataBean5 = new EBlogDataBean("Title5","xxxxxxxxx",4,0);
        EBlogDataBean eBlogDataBean6 = new EBlogDataBean("Title6","xxxxxxxxx",0,1);
        EBlogDataBean eBlogDataBean7 = new EBlogDataBean("Title7","xxxxxxxxx",0,2);
        EBlogDataBean eBlogDataBean8 = new EBlogDataBean("Title8","xxxxxxxxx",0,3);
        EBlogDataBean eBlogDataBean9 = new EBlogDataBean("Title9","xxxxxxxxx",0,4);
        EBlogDataBean eBlogDataBean10 = new EBlogDataBean("Title10","xxxxxxxxx",0,5);
        EBlogDataBean eBlogDataBean11 = new EBlogDataBean("Title11","xxxxxxxxx",0,6);
        EBlogDataBean eBlogDataBean12 = new EBlogDataBean("Title12","xxxxxxxxx",7,7);


        lists.add(eBlogDataBean1);
        lists.add(eBlogDataBean2);
        lists.add(eBlogDataBean3);
        lists.add(eBlogDataBean4);
        lists.add(eBlogDataBean5);
        lists.add(eBlogDataBean6);
        lists.add(eBlogDataBean7);
        lists.add(eBlogDataBean8);
        lists.add(eBlogDataBean9);
        lists.add(eBlogDataBean10);
        lists.add(eBlogDataBean11);
        lists.add(eBlogDataBean12);

        mEblogAdapter.setData(lists);


    }
}
