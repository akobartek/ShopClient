package com.barteksokolowski.shopclient.data

import android.content.ContentResolver
import android.net.Uri
import android.provider.BaseColumns

object BookContract {

    internal const val CONTENT_AUTHORITY = "com.barteksokolowski.shop"

    val BASE_CONTENT_URI = Uri.parse("content://$CONTENT_AUTHORITY")!!

    const val PATH_BOOKS = "books"
    const val PATH_CART = "cart"
    const val PATH_SHOPPING_HISTORY = "shopping_history"

    abstract class BookEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BOOKS).build()!!

            internal const val TABLE_NAME = "books"

            internal const val CONTENT_LIST_TYPE =
                    "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$CONTENT_AUTHORITY/$PATH_BOOKS"

            internal const val CONTENT_ITEM_TYPE =
                    "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/$CONTENT_AUTHORITY/$PATH_BOOKS"

            const val _ID = BaseColumns._ID
            const val COLUMN_TITLE = "title"
            const val COLUMN_AUTHOR = "author"
            const val COLUMN_CATEGORY = "category"
            const val COLUMN_PHOTO_URL = "photoURL"
            const val COLUMN_PRICE = "price"
            const val COLUMN_NOTE = "note"
        }
    }

    abstract class CartEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CART).build()!!

            internal const val TABLE_NAME = "cart"

            internal const val CONTENT_LIST_TYPE =
                    "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$CONTENT_AUTHORITY/$PATH_CART"

            internal const val CONTENT_ITEM_TYPE =
                    "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/$CONTENT_AUTHORITY/$PATH_CART"

            const val _ID = BaseColumns._ID
            const val COLUMN_BOOK_ID = "bookID"
        }
    }

    abstract class ShoppingHistoryEntry : BaseColumns {
        companion object {
            val CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SHOPPING_HISTORY).build()!!

            internal const val TABLE_NAME = "shopping_history"

            internal const val CONTENT_LIST_TYPE =
                    "${ContentResolver.CURSOR_DIR_BASE_TYPE}/$CONTENT_AUTHORITY/$PATH_SHOPPING_HISTORY"

            internal const val CONTENT_ITEM_TYPE =
                    "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/$CONTENT_AUTHORITY/$PATH_SHOPPING_HISTORY"

            const val _ID = BaseColumns._ID
            const val COLUMN_BOOK_ID = "bookID"
            const val COLUMN_BOOK_TITLE = "bookTitle"
            const val COLUMN_BOOK_AUTHOR = "bookAuthor"
            const val COLUMN_BOOK_CATEGORY = "bookCategory"
            const val COLUMN_ORDER_DATE = "orderDate"
        }
    }
}