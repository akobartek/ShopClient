package com.barteksokolowski.shopclient.model

import android.database.Cursor
import com.barteksokolowski.shopclient.data.BookContract.BookEntry

data class Book(var _ID: Long,
                var title: String,
                var authors: String,
                var category: Int,
                var photoURL: String,
                var price: Double,
                var note: String) {

    companion object {
        fun fromCursor(cursor: Cursor): Book {
            return Book(
                    cursor.getLong(cursor.getColumnIndexOrThrow(BookEntry._ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_AUTHOR)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_CATEGORY)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_PHOTO_URL)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(BookEntry.COLUMN_NOTE))
            )
        }
    }
}