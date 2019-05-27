package com.barteksokolowski.shopclient.ui.fragments

import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.data.BookContract.BookEntry
import com.barteksokolowski.shopclient.ui.adapters.BookRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private val LOG_TAG = CategoryFragment::class.java.simpleName

        fun newInstance(categoryId: Int): CategoryFragment {
            return CategoryFragment().apply {
                arguments = Bundle().apply {
                    putInt("category_id", categoryId)
                }
            }
        }
    }

    private lateinit var mAdapter: BookRecyclerAdapter

    private var BOOK_LOADER_ID: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        BOOK_LOADER_ID = arguments!!.getInt("category_id")

        recyclerview.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        recyclerview.setHasFixedSize(true)

        mAdapter = BookRecyclerAdapter(context = context)
        recyclerview.adapter = mAdapter

        loaderManager.initLoader(BOOK_LOADER_ID, null, this@CategoryFragment)

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
        val selection = "${BookEntry.COLUMN_CATEGORY} = $BOOK_LOADER_ID"
        Log.i(LOG_TAG, selection)

        return CursorLoader(context!!, BookEntry.CONTENT_URI, null, selection, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter.swapCursor(data)
        if (data.count != 0)
            showDataView()
        else
            showEmptyLayout()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }
}
