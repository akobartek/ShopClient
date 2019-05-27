package com.barteksokolowski.shopclient.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import android.util.Log

import com.barteksokolowski.shopclient.data.BookContract.BookEntry
import com.barteksokolowski.shopclient.data.BookContract.CartEntry
import com.barteksokolowski.shopclient.data.BookContract.ShoppingHistoryEntry

class BookProvider : ContentProvider() {

    companion object {
        private val LOG_TAG = BookProvider::class.java.simpleName

        const val CODE_BOOKS = 100
        const val CODE_BOOK_ID = 101

        const val CODE_CART_MULTIPLE = 200
        const val CODE_CART_SINGLE = 201

        const val CODE_HISTORY_MULTIPLE = 300
        const val CODE_HISTORY_SINGLE = 301

        private val sUriMatcher = buildUriMatcher()

        private fun buildUriMatcher(): UriMatcher {
            val matcher = UriMatcher(UriMatcher.NO_MATCH)
            val authority = BookContract.CONTENT_AUTHORITY

            matcher.addURI(authority, BookContract.PATH_BOOKS, CODE_BOOKS)
            matcher.addURI(authority, "${BookContract.PATH_BOOKS}/#", CODE_BOOK_ID)
            matcher.addURI(authority, BookContract.PATH_CART, CODE_CART_MULTIPLE)
            matcher.addURI(authority, "${BookContract.PATH_CART}/#", CODE_CART_SINGLE)
            matcher.addURI(authority, BookContract.PATH_SHOPPING_HISTORY, CODE_HISTORY_MULTIPLE)
            matcher.addURI(authority, "${BookContract.PATH_SHOPPING_HISTORY}/#", CODE_HISTORY_SINGLE)

            return matcher
        }
    }

    private lateinit var mDbHelper: BookDbHelper


    override fun onCreate(): Boolean {
        mDbHelper = BookDbHelper(context)
        return true
    }

    override fun getType(uri: Uri?): String {
        val match = sUriMatcher.match(uri)
        return when (match) {
            CODE_BOOKS -> BookEntry.CONTENT_LIST_TYPE
            CODE_BOOK_ID -> BookEntry.CONTENT_ITEM_TYPE
            CODE_CART_MULTIPLE -> CartEntry.CONTENT_LIST_TYPE
            CODE_CART_SINGLE -> CartEntry.CONTENT_ITEM_TYPE
            CODE_HISTORY_MULTIPLE -> ShoppingHistoryEntry.CONTENT_LIST_TYPE
            CODE_HISTORY_SINGLE -> ShoppingHistoryEntry.CONTENT_ITEM_TYPE
            else -> throw IllegalStateException("Unknown URI $uri with match $match")
        }
    }

    override fun query(uri: Uri?, projection: Array<out String>?, selection: String?,
                       selectionArgs: Array<out String>?, sortOrder: String?): Cursor {
        val database = mDbHelper.readableDatabase

        val cursor: Cursor

        val match = sUriMatcher.match(uri)
        cursor = when (match) {
            CODE_BOOKS -> database.query(
                    BookEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder
            )
            CODE_BOOK_ID -> {
                val select = "${BookEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())

                database.query(
                        BookEntry.TABLE_NAME, projection, select, selectArgs, null, null, sortOrder
                )
            }
            CODE_CART_MULTIPLE -> database.query(
                    CartEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder
            )
            CODE_CART_SINGLE -> {
                val select = "${CartEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())

                database.query(
                        CartEntry.TABLE_NAME, projection, select, selectArgs, null, null, sortOrder
                )
            }
            CODE_HISTORY_MULTIPLE -> database.query(
                    ShoppingHistoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder
            )
            CODE_HISTORY_SINGLE -> {
                val select = "${ShoppingHistoryEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())

                database.query(
                        ShoppingHistoryEntry.TABLE_NAME, projection, select, selectArgs, null, null, sortOrder
                )
            }
            else -> throw IllegalArgumentException("Cannot query URI $uri with unknown match $match")
        }

        cursor.setNotificationUri(context!!.contentResolver, uri)
        return cursor
    }

    override fun insert(uri: Uri?, values: ContentValues?): Uri? {
        val match = sUriMatcher.match(uri)
        val tableName = when (match) {
            CODE_BOOKS -> BookEntry.TABLE_NAME
            CODE_CART_MULTIPLE -> CartEntry.TABLE_NAME
            CODE_HISTORY_MULTIPLE -> ShoppingHistoryEntry.TABLE_NAME

            else -> throw IllegalArgumentException("Insertion is not supported for URI with match $match")
        }

        val rowID = mDbHelper.writableDatabase.insert(tableName, null, values)

        if (rowID == -1L) {
            Log.e(LOG_TAG, "Failed to insert row for $uri")
            return null
        }
        context!!.contentResolver.notifyChange(uri, null)
        return ContentUris.withAppendedId(uri, rowID)
    }

    override fun update(uri: Uri, values: ContentValues, selection: String?, selectionArgs: Array<out String>?): Int {
        val match = sUriMatcher.match(uri)
        return when (match) {
            CODE_BOOKS -> updateBooks(uri, values, selection, selectionArgs)
            CODE_BOOK_ID -> {
                val select = "${BookEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())
                updateBooks(uri, values, select, selectArgs)
            }
            else -> throw IllegalArgumentException("Update is not supported for URI with match $match")
        }
    }

    private fun updateBooks(uri: Uri?, values: ContentValues, selection: String?, selectionArgs: Array<out String>?): Int {
        if (values.size() == 0)
            return 0

        val rowsUpdated = mDbHelper.writableDatabase.update(BookEntry.TABLE_NAME, values, selection, selectionArgs)

        if (rowsUpdated != 0)
            context!!.contentResolver.notifyChange(uri, null)

        return rowsUpdated
    }

    override fun delete(uri: Uri?, selection: String?, selectionArgs: Array<out String>?): Int {
        val database = mDbHelper.writableDatabase
        val rowsDeleted: Int

        val match = sUriMatcher.match(uri)
        rowsDeleted = when (match) {
            CODE_BOOKS -> database.delete(
                    BookEntry.TABLE_NAME, selection, selectionArgs
            )
            CODE_BOOK_ID -> {
                val select = "${BookEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())
                database.delete(
                        BookEntry.TABLE_NAME, select, selectArgs
                )
            }
            CODE_CART_MULTIPLE -> database.delete(
                    CartEntry.TABLE_NAME, selection, selectionArgs
            )
            CODE_CART_SINGLE -> {
                val select = "${CartEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())
                database.delete(
                        CartEntry.TABLE_NAME, select, selectArgs
                )
            }
            CODE_HISTORY_MULTIPLE -> database.delete(
                    ShoppingHistoryEntry.TABLE_NAME, selection, selectionArgs
            )
            CODE_HISTORY_SINGLE -> {
                val select = "${ShoppingHistoryEntry._ID}=?"
                val selectArgs = arrayOf(ContentUris.parseId(uri).toString())
                database.delete(
                        ShoppingHistoryEntry.TABLE_NAME, select, selectArgs
                )
            }
            else -> throw IllegalArgumentException("Deletion is not supported for URI with match $match")
        }

        if (rowsDeleted != 0)
            context!!.contentResolver.notifyChange(uri, null)

        return rowsDeleted
    }
}