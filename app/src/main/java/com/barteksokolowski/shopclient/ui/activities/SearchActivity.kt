package com.barteksokolowski.shopclient.ui.activities

import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.app.LoaderManager
import android.content.CursorLoader
import android.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View

import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.data.BookContract
import com.barteksokolowski.shopclient.ui.adapters.BookRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_category.*

class SearchActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    private lateinit var mAdapter: BookRecyclerAdapter
    private var mPosition = RecyclerView.NO_POSITION
    private val LOG_TAG = SearchActivity::class.java.simpleName

    private var BOOK_LOADER_ID: Int = 911

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_and_recommendation)

        title = getString(R.string.search_activity_title, intent.extras.getString("search_query", ""))

        recyclerview.layoutManager = LinearLayoutManager(this@SearchActivity, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)

        mAdapter = BookRecyclerAdapter(context = this@SearchActivity)
        recyclerview.adapter = mAdapter

        loaderManager.initLoader(BOOK_LOADER_ID, null, this@SearchActivity)

        showLoading()
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

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        val query = intent.extras.getString("search_query", "")
        val selection = "${BookContract.BookEntry.COLUMN_TITLE} LIKE '%$query%' OR " +
                "${BookContract.BookEntry.COLUMN_AUTHOR} LIKE '%$query%'"
        Log.i(LOG_TAG, selection)

        return CursorLoader(this@SearchActivity, BookContract.BookEntry.CONTENT_URI, null, selection, null, null)
    }

    override fun onLoadFinished(loader: android.content.Loader<Cursor>?, data: Cursor?) {
        mAdapter.swapCursor(data)
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0
        recyclerview.smoothScrollToPosition(mPosition)
        if (data!!.count != 0)
            showDataView()
        else
            showEmptyLayout()
    }

    override fun onLoaderReset(loader: android.content.Loader<Cursor>?) {
        mAdapter.swapCursor(null)
    }
}
