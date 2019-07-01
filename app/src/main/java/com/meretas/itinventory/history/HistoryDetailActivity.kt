package com.meretas.itinventory.history

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meretas.itinventory.R
import com.meretas.itinventory.data.HistoryListData
import com.meretas.itinventory.utils.DATA_INTENT_DASHBOARD_DETAIL_HISTORY
import kotlinx.android.synthetic.main.activity_history_detail.*

class HistoryDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history_detail)

        val intent = intent.getParcelableExtra<HistoryListData.Result>(DATA_INTENT_DASHBOARD_DETAIL_HISTORY)

        tv_historydetail_komputer.text = intent.computer
        tv_historydetail_branch.text = intent.branch
        tv_historydetail_status.text = intent.statusHistory
        tv_historydetail_note.text = intent.note
        tv_historydetail_author.text = intent.author
        tv_historydetail_date.text = intent.createdAt
        tv_historydetail_id.text = "#"+ intent.id.toString()

        iv_historydetail_close.setOnClickListener {
            onBackPressed()
        }

    }
}
