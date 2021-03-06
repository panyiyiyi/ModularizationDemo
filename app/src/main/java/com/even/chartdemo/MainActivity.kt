package com.even.chartdemo

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.even.chart_view.bean.CircularRateBean
import com.even.common_utils.DisplayUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mCurrentValue = 20f
    var mIsShow = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.e("Even", "高度：" + DisplayUtils.getScreenHeight())
        Log.e("Even", "宽度：" + DisplayUtils.getScreenWidth())
        Log.e("Even", "状态栏高度：" + DisplayUtils.getStatusBarHeight(this))
        Log.e("Even", "转换：" + DisplayUtils.dip2px(20))

        btDialog.setOnClickListener {
            startActivity(Intent(this, ScrollPickerActivity::class.java))
//            val selectDialogFragment =
//                DataSelectorDialog(
//                    5,
//                    "标题",
//                    listOf("这是中文", "哈哈哈", "阿瑟东发阿达法阿瑟东发阿瑟东发", "单独", "大士大夫", "a答复")
//                )
//            supportFragmentManager.beginTransaction().add(selectDialogFragment, "f")
//                .commitAllowingStateLoss()
        }


        btReduce.setOnClickListener {
            mCurrentValue -= 20
            progressBarView.setValue(60f, 120f, mCurrentValue)
        }

        btAdd.setOnClickListener {
            mCurrentValue += 20
            progressBarView.setValue(60f, 120f, mCurrentValue)
//            progressBarView.setValueColor(
//                Color.parseColor("#ff0000"),
//                Color.parseColor("#ffFF00"),
//                Color.parseColor("#ffFFFF")
//            )
        }


        btShow.setOnClickListener {
            if (!mIsShow) {
                circularRateView.setIsShowRemind(true)
                mIsShow = true
            } else {
                circularRateView.setIsShowRemind(false)
                mIsShow = false
            }

        }

        btAddValue.setOnClickListener {
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
}
