package com.barteksokolowski.shopclient.ui.activities

import android.annotation.SuppressLint
import android.app.LoaderManager
import android.content.ContentValues
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_book_details.*

import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.model.Book
import com.barteksokolowski.shopclient.data.BookContract.CartEntry
import com.barteksokolowski.shopclient.data.BookDbHelper
import android.content.Intent
import android.view.Menu
import com.barteksokolowski.shopclient.utils.DatabaseUtils


class BookDetailsActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor> {

    companion object {
        private const val DETAILS_LOADER_ID = 21
    }

    private var mCurrentPersonUri: Uri? = null
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        mCurrentPersonUri = intent.data
        loaderManager.initLoader(DETAILS_LOADER_ID, null, this@BookDetailsActivity)
    }

    override fun onResume() {
        super.onResume()
        invalidateOptionsMenu()
    }

    private fun setCartValue() {
        val cartMenuItem = menu.findItem(R.id.action_cart)
        val currentValueText = getString(R.string.current_cart_value, DatabaseUtils.getCartValue(this@BookDetailsActivity))
        cartMenuItem.title = currentValueText
        currentCartValueTV.text = currentValueText
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        this.menu = menu
        setCartValue()
        menu.findItem(R.id.action_search).isVisible = false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item!!.itemId) {
            R.id.action_cart -> {
                startActivity(Intent(this@BookDetailsActivity, CartActivity::class.java))
                true
            }
            android.R.id.home -> {
                startActivity(Intent(this@BookDetailsActivity, MainActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor>? {
        if (mCurrentPersonUri == null) {
            return null
        }
        return CursorLoader(this@BookDetailsActivity, mCurrentPersonUri, null, null, null, null)
    }

    @SuppressLint("SetTextI18n")
    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        if (data == null || data.count < 1) {
            return
        }

        if (data.moveToFirst()) {
            val book = Book.fromCursor(data)

            Picasso.with(this@BookDetailsActivity)
                    .load(book.photoURL)
                    .into(bookCoverImage)

            bookTitle.text = book.title
            bookAuthors.text = book.authors
            bookPrice.text = "${book.price}PLN"
            bookNote.text = book.note
            bookCategory.text = when (book.category) {
                0 -> "Klasyka"
                1 -> "Fantastyka"
                2 -> "Dramat"
                3 -> "Ekonomia"
                4 -> "Informatyka"
                else -> "Nieznane"
            }

            addToCartBtn.setOnClickListener {
                val contentValues = ContentValues()
                contentValues.put(CartEntry.COLUMN_BOOK_ID, book._ID)

                val dbHelper = BookDbHelper(this@BookDetailsActivity)
                dbHelper.writableDatabase.insert(CartEntry.TABLE_NAME, null, contentValues)

                setCartValue()

                Toast.makeText(this@BookDetailsActivity, getString(R.string.added_to_cart), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        Picasso.with(this@BookDetailsActivity).load(R.drawable.imagenotavailable).into(bookCoverImage)
        bookTitle.text = ""
        bookAuthors.text = ""
        bookPrice.text = ""
        bookNote.text = ""
    }
}
