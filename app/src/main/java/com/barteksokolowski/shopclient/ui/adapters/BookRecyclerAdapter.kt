package com.barteksokolowski.shopclient.ui.adapters

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.barteksokolowski.shopclient.R
import com.barteksokolowski.shopclient.data.BookContract
import com.barteksokolowski.shopclient.model.Book
import com.barteksokolowski.shopclient.ui.activities.BookDetailsActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.book_list_item.view.*

class BookRecyclerAdapter(context: Context?) : RecyclerView.Adapter<BookRecyclerAdapter.BookRecyclerAdapterViewHolder>() {

    private val mContext = context
    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookRecyclerAdapterViewHolder =
            BookRecyclerAdapterViewHolder(LayoutInflater.from(mContext).inflate(R.layout.book_list_item, parent, false))

    override fun getItemCount(): Int {
        if (mCursor == null) return 0
        return mCursor!!.count
    }

    override fun onBindViewHolder(holder: BookRecyclerAdapterViewHolder, position: Int) {
        mCursor!!.moveToPosition(position)
        holder.bindBook(Book.fromCursor(mCursor!!))
    }

    fun swapCursor(newCursor: Cursor?) {
        mCursor = newCursor
        notifyDataSetChanged()
    }


    inner class BookRecyclerAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun bindBook(book: Book) {
            Picasso.with(itemView.context)
                    .load(book.photoURL)
                    .into(itemView.itemBookCoverImage)

            itemView.itemBookTitle.text = book.title
            itemView.itemBookAuthors.text = book.authors
            itemView.itemBookPrice.text = "${book.price}PLN"

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, BookDetailsActivity::class.java)
                intent.data = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, book._ID)
                itemView.context.startActivity(intent)
            }
        }
    }
}
