package com.example.oldiary;

import android.util.Log;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class AgeCalculator {
    private static final String TAG = "Age";
    //メンバ
    public int year, month, date, age;


    public boolean leap(int year) {
        return (year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0));
    }

    public AgeCalculator(){}; //初期化用
    public AgeCalculator(int y, int m, int d, int a) {
        this.year = y;
        this.month = m;
        this.date = d;
        this.age = a;
    }

    public boolean monthCheck30(int month) {
        int[] mList30 = {4, 6, 9, 11};
        for (int i = 0; i < mList30.length; i++) {
            if (mList30[i] == month) {
                return true;
            }
        }
        //Log.d(TAG, "contains{4, 6, 9, 11}" + Arrays.asList(mList30).contains(month));
        return false;
    }

    public boolean monthCheck31(int month) {
        int[] mList31 = {1, 3, 5, 7, 8, 10, 12};
        for (int i = 0; i < mList31.length; i++) {
            if (mList31[i] == month) {
                return true;
            }
        }
        //Log.d(TAG, "contains{4, 6, 9, 11}" + Arrays.asList(mList30).contains(month));
        return false;
    }

    public int getAge(int year, int month, int date) {
        Calendar c = Calendar.getInstance();
        TimeZone tz = TimeZone.getTimeZone("Asia/Tokyo");
        c.setTimeZone(tz); //日本時間に設定
        int nowY = c.get(Calendar.YEAR);
        int nowM = c.get(Calendar.MONTH) + 1;
        int nowD = c.get(Calendar.DATE);
        int result = nowY - year;

        //debug
        Log.d(TAG, "30month? " + (monthCheck30(month)));
        Log.d(TAG, "31month? " + (monthCheck31(month)));


        if ((result < 0) || (year < 1900) || (month < 1) || (month > 12) || (date < 1)) {
            Log.e(TAG, "input date is wrong code:1");
            return -1;
        }
        if ((monthCheck30(month) && (date > 30)) || (monthCheck31(month) && (date > 31))) {
            Log.e(TAG, "input date is wrong code:2");
            return -1;
        }
        if (((leap(year) && month == 2) && (date > 29)) || (!(leap(year)) && (month == 2) && (date > 28))) {
            Log.e(TAG, "input date is wrong code:3");
            return -1;
        }

        if (month > nowM) {
            result--;
        }
        if ((month == nowM) && (date > nowD)) {
            result--;
        }
        return result;
    }
}




