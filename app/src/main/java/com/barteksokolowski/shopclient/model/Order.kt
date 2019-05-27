package com.barteksokolowski.shopclient.model

import android.database.Cursor
import com.barteksokolowski.shopclient.data.BookContract.ShoppingHistoryEntry

data class Order(var _ID: Long,
                 var bookID: Long,
                 var bookTitle: String,
                 var bookAuthor: String,
                 var category: Int,
                 var orderDate: Long) {

    companion object {
        fun fromCursor(cursor: Cursor): Order {
            return Order(
                    cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingHistoryEntry._ID)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingHistoryEntry.COLUMN_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ShoppingHistoryEntry.COLUMN_BOOK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(ShoppingHistoryEntry.COLUMN_BOOK_AUTHOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(ShoppingHistoryEntry.COLUMN_BOOK_CATEGORY)),
                    cursor.getLong(cursor.getColumnIndexOrThrow(ShoppingHistoryEntry.COLUMN_ORDER_DATE))
            )
        }
    }
}