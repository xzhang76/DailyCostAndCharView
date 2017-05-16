package com.htsc.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.htsc.myapplication.CostBean;
import com.htsc.myapplication.R;
import com.htsc.myapplication.db.MyDataBaseHelper;

/**
 *
 * Created by zhangxiaoting on 2017/3/29.
 */
public class AddCostDetailActivity extends AppCompatActivity {
    private MyDataBaseHelper dataBaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cost_detail_add_layout);

        final EditText addCostTitle = (EditText) findViewById(R.id.add_cost_title);
        final EditText addCostMoney = (EditText) findViewById(R.id.add_cost_money);
        final DatePicker datePicker = (DatePicker) findViewById(R.id.add_cost_date);
        Button addCostDetail = (Button) findViewById(R.id.add_cost_button);

        addCostDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CostBean bean  = new CostBean();
                bean.setCostTitle(addCostTitle.getText().toString());
                bean.setCostMoney(addCostMoney.getText().toString());
                bean.setCostDate(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                dataBaseHelper.insertCostDetail(bean);
                finish();
            }
        });
        findViewById(R.id.cancel_cost_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dataBaseHelper = MyDataBaseHelper.geInstance(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
