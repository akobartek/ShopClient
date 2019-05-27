package com.barteksokolowski.shopclient.ui.activities

import android.app.Activity
import android.content.ContentValues
import android.database.Cursor
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.app.NavUtils
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.data.BookContract.ShoppingHistoryEntry
import com.barteksokolowski.shopclient.data.BookContract.CartEntry
import com.barteksokolowski.shopclient.ui.adapters.CartRecyclerAdapter
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.MenuItem
import android.widget.Toast
import com.barteksokolowski.shopclient.data.BookDbHelper
import com.barteksokolowski.shopclient.utils.DatabaseUtils
import kotlinx.android.synthetic.main.activity_cart.*
import java.util.*

class CartActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private var BOOK_LOADER_ID = 44
//        private val LOG_TAG = CartActivity::class.java.simpleName
    }

    private lateinit var mAdapter: CartRecyclerAdapter
    private var mPosition = RecyclerView.NO_POSITION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        val layoutManager = LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)
        shoppingList.layoutManager = layoutManager
        shoppingList.setHasFixedSize(true)

        mAdapter = CartRecyclerAdapter(context = this@CartActivity)
        shoppingList.adapter = mAdapter

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                val id = viewHolder.itemView.tag as Int

                val dbHelper = BookDbHelper(this@CartActivity)
                val sharedPrefs = getSharedPreferences("cartPrefs", Activity.MODE_PRIVATE)
                val cartValue = (sharedPrefs.getFloat("cartValue", 0F)).toDouble()
                val preferencesEditor = sharedPrefs.edit()
                preferencesEditor.putFloat("cartValue", (cartValue - dbHelper.getPriceOfBook(id)).toFloat())
                preferencesEditor.apply()


                val stringId = Integer.toString(id)
                var uri = CartEntry.CONTENT_URI
                uri = uri.buildUpon().appendPath(stringId).build()

                contentResolver.delete(uri, null, null)

                Toast.makeText(this@CartActivity, getString(R.string.deleted), Toast.LENGTH_SHORT).show()

                supportLoaderManager.restartLoader(BOOK_LOADER_ID, null, this@CartActivity)

            }
        }).attachToRecyclerView(shoppingList)

        showLoading()

        supportLoaderManager.initLoader(BOOK_LOADER_ID, null, this@CartActivity)

        makeOrderBtn.setOnClickListener { makeOrder() }
    }

    private fun setCartValue() {
        orderSummary.text = getString(R.string.current_cart_value, DatabaseUtils.getCartValue(this@CartActivity))
    }

    private fun makeOrder() {
        saveOrderHistory()
        contentResolver.delete(CartEntry.CONTENT_URI, null, null)
        Toast.makeText(this@CartActivity, getString(R.string.order_made), Toast.LENGTH_SHORT).show()

        NavUtils.navigateUpFromSameTask(this@CartActivity)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            android.R.id.home -> {
                NavUtils.navigateUpFromSameTask(this@CartActivity)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
        shoppingList.visibility = View.INVISIBLE
        summaryLayout.visibility = View.INVISIBLE
        emptyCartLayout.visibility = View.INVISIBLE
    }

    private fun showDataView() {
        loading.visibility = View.INVISIBLE
        shoppingList.visibility = View.VISIBLE
        summaryLayout.visibility = View.VISIBLE
        emptyCartLayout.visibility = View.INVISIBLE
    }

    private fun showEmptyLayout() {
        loading.visibility = View.INVISIBLE
        shoppingList.visibility = View.INVISIBLE
        summaryLayout.visibility = View.INVISIBLE
        emptyCartLayout.visibility = View.VISIBLE
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> {
        return CursorLoader(this@CartActivity, CartEntry.CONTENT_URI,
                null, null, null, null)
    }

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor) {
        mAdapter.swapCursor(data)
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0
        shoppingList.smoothScrollToPosition(mPosition)
        if (data.count != 0) {
            showDataView()
            setCartValue()
        } else
            showEmptyLayout()
    }

    override fun onLoaderReset(loader: Loader<Cursor>) {
        mAdapter.swapCursor(null)
    }


    private fun saveOrderHistory() {
        val cartList = DatabaseUtils.getListOfBooksInCart(this@CartActivity)

        cartList.forEach {
            val values = ContentValues()
            values.put(ShoppingHistoryEntry.COLUMN_BOOK_ID, it._ID)
            values.put(ShoppingHistoryEntry.COLUMN_BOOK_TITLE, it.title)
            values.put(ShoppingHistoryEntry.COLUMN_BOOK_AUTHOR, it.authors)
            values.put(ShoppingHistoryEntry.COLUMN_BOOK_CATEGORY, it.category)
            values.put(ShoppingHistoryEntry.COLUMN_ORDER_DATE, Date().time)

            contentResolver.insert(ShoppingHistoryEntry.CONTENT_URI, values)
        }
    }
}
