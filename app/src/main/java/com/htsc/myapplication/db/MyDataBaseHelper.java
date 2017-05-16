package com.htsc.myapplication.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.htsc.myapplication.CostBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 数据库帮助类
 * Created by zhangxiaoting.
 */
public class MyDataBaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "daily_cost";
    public static final String COST_TITLE = "cost_title";
    public static final String COST_DATE = "cost_date";
    public static final String COST_MONEY = "cost_money";
    private static MyDataBaseHelper sHelper;
    private static final int VERSION = 1;
    private static final String SQL_CREATE = "create table if not exists daily_cost(" +
            "_id integer primary key autoincrement," +
            "cost_title varchar,cost_date varchar,cost_money varchar)";
    private static final String SQL_DROP = "drop table if exists daily_cost";


    //单例模式，将构造函数私有化
    private MyDataBaseHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    //单例模式，只有在实例未创建时才会new一个，new过之后会直接返回它
    public static MyDataBaseHelper geInstance(Context context) {
        if (sHelper == null) {
            //sHelper是static，所有它只会有一个（单例）
            sHelper = new MyDataBaseHelper(context);
        }
        return sHelper;
    }

    /*
     * onCreate()中完成创建数据库的操作
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

    public synchronized void deleteThread(String costTitle) {
        SQLiteDatabase db = sHelper.getWritableDatabase();
        db.execSQL("delete from daily_cost where cost_title = ?", new Object[]{costTitle});
        db.close();
    }

    public boolean isExist(String costTitle) {
        SQLiteDatabase db = sHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from daily_cost where cost_title = ?", new String[]{costTitle});
        boolean exist = cursor.moveToNext();
        cursor.close();
        db.close();
        return exist;
    }

    // 插入数据
    public synchronized void insertCostDetail(CostBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        // 使用ContentValues来插入表信息
        ContentValues values = new ContentValues();
        values.put(COST_TITLE, bean.getCostTitle());
        values.put(COST_DATE, bean.getCostDate());
        values.put(COST_MONEY, bean.getCostMoney());
        db.insert(DB_NAME, null, values);
        db.close();
    }

    // 查询所有数据
    public synchronized List<CostBean> queryAllCostDetail() {
        List<CostBean> costBeans = new ArrayList<>();
        SQLiteDatabase database = getWritableDatabase();
        Cursor cursor = database.query(DB_NAME, null, null, null, null, null, COST_DATE + " " + "ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CostBean bean = new CostBean();
                bean.setCostTitle(cursor.getString(cursor.getColumnIndex(COST_TITLE)));
                bean.setCostDate(cursor.getString(cursor.getColumnIndex(COST_DATE)));
                bean.setCostMoney(cursor.getString(cursor.getColumnIndex(COST_MONEY)));
                costBeans.add(bean);
            }
            cursor.close();
        }

        return costBeans;
    }

    public synchronized void clearCostDetail() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(DB_NAME, null, null);
    }

}
