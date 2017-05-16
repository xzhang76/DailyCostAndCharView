package com.htsc.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;

import com.htsc.myapplication.CostBean;
import com.htsc.myapplication.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * Created by zhangxiaoting on 2017/5/16.
 */
public class ChartViewActivity extends AppCompatActivity {
    private LineChartView mLineChartView;
    // char view的数据 横坐标是时间 纵坐标是金额
    LineChartData mLineCharData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.char_view_activity);
        mLineChartView = (LineChartView) findViewById(R.id.line_chart_view);
        Intent intent = getIntent();
        List<CostBean> allData = (List<CostBean>) intent.getSerializableExtra("cost_list");
        if (allData != null && allData.size() > 0) {
            Map<String, Integer> chartValues = generateValues(allData);
            mLineCharData = generatePoints(chartValues);
            mLineCharData.setBaseValue(Float.NEGATIVE_INFINITY);
            mLineChartView.setLineChartData(mLineCharData);
        }
    }

    private LineChartData generatePoints(Map<String, Integer> chartValues) {
        List<Line> lines = new ArrayList<>();
        List<PointValue> pointValues = new ArrayList<>();
        int indexX = 0;
        for (Integer value : chartValues.values()) {
            pointValues.add(new PointValue(indexX, value));
            indexX ++;
        }
        Line line = new Line(pointValues);
        line.setColor(ChartUtils.COLOR_ORANGE);
        line.setShape(ValueShape.CIRCLE);
        line.setPointColor(ChartUtils.COLOR_BLUE);
        lines.add(line);
        LineChartData lineChartData = new LineChartData(lines);
        return lineChartData;
    }

    // 根据原始数据获取图表的原始数据
    private Map<String, Integer> generateValues(List<CostBean> allData) {
        Map<String, Integer> table = new TreeMap<>();
        for (int i = 0; i < allData.size(); i++) {
            CostBean bean = allData.get(i);
            String costDate = bean.getCostDate();
            int costMoney = Integer.parseInt(bean.getCostMoney());
            if (!table.containsKey(costDate)) {
                table.put(costDate, costMoney);
            } else {
                int originMoney = table.get(costDate);
                table.put(costDate, costMoney + originMoney);
            }
        }
        return table;

    }
}
