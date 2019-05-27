package com.barteksokolowski.shopclient.ui.activities

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem

import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.data.BookContract
import com.barteksokolowski.shopclient.ui.adapters.UserPagerAdapter
import com.barteksokolowski.shopclient.utils.DatabaseUtils

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViewPager()

        DatabaseUtils.checkIfDatabaseIsEmpty(this@MainActivity)
        DatabaseUtils.isRecommendationAvailable(this@MainActivity)
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    private fun initViewPager() {
        viewPager.adapter = UserPagerAdapter(supportFragmentManager)
        viewPager.currentItem = 0
        tabLayout.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        this.menu = menu
        setCartValue()

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val queryIntent = Intent(this@MainActivity, SearchActivity::class.java)
                queryIntent.putExtra("search_query", query)
                startActivity(queryIntent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
                true
            }
            R.id.action_clear_history -> {
                showClearHistoryDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setCartValue() {
        val cartMenuItem = menu.findItem(R.id.action_cart)
        cartMenuItem.title = getString(R.string.current_cart_value, DatabaseUtils.getCartValue(this@MainActivity))
    }

    private fun showClearHistoryDialog() = AlertDialog.Builder(this@MainActivity)
            .setTitle(getString(R.string.clear_shopping_history))
            .setMessage(getString(R.string.clear_shopping_history_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.yes), { dialog, _ ->
                dialog.dismiss()
                contentResolver.delete(BookContract.ShoppingHistoryEntry.CONTENT_URI, null, null)
            })
            .setNegativeButton(getString(R.string.no), { dialog, _ ->
                dialog.dismiss()
            })
            .create()
            .show()
}
