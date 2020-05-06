package com.even.chartdemo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.even.chart_view.bean.CircularRateBean
import com.even.common_utils.DisplayUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mCurrentValue = 20f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("Even", "高度：" + DisplayUtils.getScreenHeight())
        Log.e("Even", "宽度：" + DisplayUtils.getScreenWidth())
        Log.e("Even", "状态栏高度：" + DisplayUtils.getStatusBarHeight(this))
        Log.e("Even", "转换：" + DisplayUtils.dip2px(20))


        btReduce.setOnClickListener {
            mCurrentValue -= 20
            progressBarView.setValue(60f, 120f, mCurrentValue)
        }

        btAdd.setOnClickListener {
            mCurrentValue += 20
            progressBarView.setValue(60f, 120f, mCurrentValue)
            progressBarView.setValueColor(
                Color.parseColor("#ff0000"),
                Color.parseColor("#ffFF00"),
                Color.parseColor("#ffFFFF")
            )
        }


        circularRateView.setRateValue(
            arrayListOf(
                CircularRateBean(
                    300f,
                    Color.parseColor("#32BCB8"),
                    "物业费"
                ), CircularRateBean(
                    100f,
                    Color.parseColor("#FFF265"),
                    "管理费"
                ),
                CircularRateBean(
                    50f,
                    Color.parseColor("#F26782"),
                    "药事服务费"
                ), CircularRateBean(
                    100f,
                    Color.parseColor("#FBC014"),
                    "诊金"
                )
            )
        )
    }
}
