package com.barteksokolowski.shopclient.ui.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.model.Book
import com.barteksokolowski.shopclient.ui.adapters.RecommendationRecyclerAdapter
import com.barteksokolowski.shopclient.utils.DatabaseUtils
import kotlinx.android.synthetic.main.activity_search_and_recommendation.*

class RecommendationActivity : AppCompatActivity() {

    private lateinit var mAdapter: RecommendationRecyclerAdapter
    private var mBookList = arrayListOf<Book>()
//    private val LOG_TAG = RecommendationActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_and_recommendation)

        title = getString(R.string.recommendations)

        showLoading()
        mBookList = DatabaseUtils.getRecommendationList(this@RecommendationActivity)

        recyclerview.layoutManager = LinearLayoutManager(this@RecommendationActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)

        mAdapter = RecommendationRecyclerAdapter(this@RecommendationActivity, mBookList)
        recyclerview.adapter = mAdapter

        if (mBookList.size == 0)
            showEmptyLayout()
        else
            showDataView()
    }

    private fun showLoading() {
        loadingIndicator.visibility = View.VISIBLE
        recyclerview.visibility = View.INVISIBLE
        noBooksLayout.visibility = View.INVISIBLE
    }

    private fun showDataView() {
        loadingIndicator.visibility = View.INVISIBLE
        recyclerview.visibility = View.VISIBLE
        noBooksLayout.visibility = View.INVISIBLE
    }

    private fun showEmptyLayout() {
        loadingIndicator.visibility = View.INVISIBLE
        recyclerview.visibility = View.INVISIBLE
        noBooksLayout.visibility = View.VISIBLE
    }
}
