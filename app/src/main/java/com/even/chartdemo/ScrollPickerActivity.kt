package com.even.chartdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.even.chart_view.dialog.adapter.DataScrollPickerAdapter
import com.even.commonrv.adapter.BaseRecyclerAdapter
import com.even.commonrv.adapter.BaseViewHolder
import kotlinx.android.synthetic.main.activity_scroll_picker.*

/**
 *  @author  Created by Even on 2020/6/19
 *  Email: emailtopan@163.com
 *
 */
class ScrollPickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroll_picker)
        var list = mutableListOf<String>(
            "sdfa1",
            "sdfa2",
            "sdfa3",
            "sdfa4",
            "sdfa5",
            "sdfa6",
            "sdfa7",
            "sdfa8",
            "sdfa9",
            "sdfa10",
            "sdfa11",
            "sdfa12",
            "sdfa13",
            "sdfa14",
            "sdfa",
            "sdfa",
            "sdfa"
        )
        val adapter = object : BaseRecyclerAdapter<String>(
            list,
            R.layout.item_text
        ) {
            override fun covert(holder: BaseViewHolder, item: String, position: Int) {
                holder.setText(R.id.tvTextView, item)
            }
        }
        val dataScrollPickerAdapter = DataScrollPickerAdapter(list)

        scrollPickerView1.layoutManager = LinearLayoutManager(this)
        scrollPickerView1.adapter = dataScrollPickerAdapter
        scrollPickerView2.layoutManager = LinearLayoutManager(this)
        scrollPickerView2.adapter = dataScrollPickerAdapter


    }
}