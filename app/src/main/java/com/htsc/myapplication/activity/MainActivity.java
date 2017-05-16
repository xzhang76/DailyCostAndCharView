package com.htsc.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.htsc.myapplication.CostBean;
import com.htsc.myapplication.R;
import com.htsc.myapplication.db.MyDataBaseHelper;
import com.htsc.myapplication.view.CostRecyclerViewAdapter;
import com.htsc.myapplication.view.DividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<CostBean> mCostList = new ArrayList<>();
    private CostRecyclerViewAdapter recyclerViewAdapter;
    private MyDataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, AddCostDetailActivity.class);
                startActivity(intent);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewAdapter = new CostRecyclerViewAdapter(this, mCostList);
        mRecyclerView.setAdapter(recyclerViewAdapter);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        dataBaseHelper = MyDataBaseHelper.geInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initListData();
    }

    private void initListData() {
//        dataBaseHelper.clearCostDetail();
//        for (int i = 0; i < 10; i++) {
//            CostBean bean = new CostBean();
//            bean.setCostTitle(i + "moomoo");
//            bean.setCostDate(new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(new Date()));
//            bean.setCostMoney("$8.8");
//            dataBaseHelper.insertCostDetail(bean);
//        }
        mCostList = dataBaseHelper.queryAllCostDetail();
        recyclerViewAdapter.setDataList(mCostList);
        recyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_chart) {
            Intent intent = new Intent(MainActivity.this, ChartViewActivity.class);
            intent.putExtra("cost_list", (Serializable) mCostList);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
