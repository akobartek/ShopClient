package com.barteksokolowski.shopclient.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

import com.barteksokolowski.shopclient.data.BookContract.BookEntry
import com.barteksokolowski.shopclient.data.BookContract.CartEntry
import com.barteksokolowski.shopclient.data.BookContract.ShoppingHistoryEntry
import com.barteksokolowski.shopclient.model.Book
import com.barteksokolowski.shopclient.model.Order

class BookDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private val LOG_TAG = BookDbHelper::class.java.simpleName

        const val DATABASE_NAME = "books.db"

        const val DATABASE_VERSION = 2
    }

    override fun onCreate(db: SQLiteDatabase) {
        val SQL_CREATE_BOOKS_TABLE =
                "CREATE TABLE ${BookEntry.TABLE_NAME} (" +
                        "${BookEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "${BookEntry.COLUMN_TITLE} TEXT NOT NULL, " +
                        "${BookEntry.COLUMN_AUTHOR} TEXT NOT NULL, " +
                        "${BookEntry.COLUMN_CATEGORY} INTEGER NOT NULL DEFAULT 0, " +
                        "${BookEntry.COLUMN_PHOTO_URL} TEXT NOT NULL, " +
                        "${BookEntry.COLUMN_PRICE} REAL NOT NULL, " +
                        "${BookEntry.COLUMN_NOTE} TEXT NOT NULL" +
                        ");"

        Log.i(LOG_TAG, SQL_CREATE_BOOKS_TABLE)

        val SQL_CREATE_CART_TABLE = "CREATE TABLE ${CartEntry.TABLE_NAME} (" +
                "${CartEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${CartEntry.COLUMN_BOOK_ID} INTEGER NOT NULL, " +
                "FOREIGN KEY (${CartEntry.COLUMN_BOOK_ID}) REFERENCES ${BookEntry.TABLE_NAME}(${BookEntry._ID})" +
                ");"

        Log.i(LOG_TAG, SQL_CREATE_CART_TABLE)

        val SQL_CREATE_HISTORY_TABLE = "CREATE TABLE ${ShoppingHistoryEntry.TABLE_NAME} (" +
                "${ShoppingHistoryEntry._ID} INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "${ShoppingHistoryEntry.COLUMN_BOOK_ID} INTEGER NOT NULL, " +
                "${ShoppingHistoryEntry.COLUMN_BOOK_TITLE} TEXT NOT NULL, " +
                "${ShoppingHistoryEntry.COLUMN_BOOK_AUTHOR} TEXT NOT NULL, " +
                "${ShoppingHistoryEntry.COLUMN_BOOK_CATEGORY} INTEGER NOT NULL DEFAULT 0, " +
                "${ShoppingHistoryEntry.COLUMN_ORDER_DATE} INTEGER NOT NULL, " +
                "FOREIGN KEY (${ShoppingHistoryEntry.COLUMN_BOOK_ID}) REFERENCES " +
                "${ShoppingHistoryEntry.TABLE_NAME}(${ShoppingHistoryEntry._ID})" +
                ");"

        Log.i(LOG_TAG, SQL_CREATE_HISTORY_TABLE)

        db.execSQL(SQL_CREATE_BOOKS_TABLE)
        db.execSQL(SQL_CREATE_CART_TABLE)
        db.execSQL(SQL_CREATE_HISTORY_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${BookEntry.TABLE_NAME};")
        db.execSQL("DROP TABLE IF EXISTS ${CartEntry.TABLE_NAME};")
        db.execSQL("DROP TABLE IF EXISTS ${ShoppingHistoryEntry.TABLE_NAME};")
        onCreate(db)
    }

    fun getBookById(id: Long): Book {
        val SQL_GET_BOOK_SELECT_QUERY = "SELECT * FROM ${BookEntry.TABLE_NAME} WHERE ${BookEntry._ID}='$id'"
        Log.d(LOG_TAG, SQL_GET_BOOK_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_BOOK_SELECT_QUERY, null)
        val book = if (cursor.moveToFirst()) {
            Book.fromCursor(cursor)
        } else {
            Book(-1, "", "", 0, "", 0.0, "")
        }

        Log.d(LOG_TAG, book.toString())
        cursor.close()

        return book
    }

    fun getAllBooks(): ArrayList<Book> {
        val listOfBooks = arrayListOf<Book>()

        val SQL_GET_BOOK_SELECT_QUERY = "SELECT * FROM ${BookEntry.TABLE_NAME}"
        Log.d(LOG_TAG, SQL_GET_BOOK_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_BOOK_SELECT_QUERY, null)
        while (cursor.moveToNext()) {
            listOfBooks.add(Book.fromCursor(cursor))
        }

        Log.d(LOG_TAG, "Liczba książek: ${listOfBooks.size}")
        cursor.close()

        return listOfBooks
    }

    fun getPriceOfBook(id: Int): Double {
        val SQL_GET_PERSON_SELECT_QUERY =
                "SELECT ${CartEntry.COLUMN_BOOK_ID} FROM ${CartEntry.TABLE_NAME} WHERE ${CartEntry._ID}='$id'"
        Log.d(LOG_TAG, SQL_GET_PERSON_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_PERSON_SELECT_QUERY, null)
        val price = if (cursor.moveToFirst()) {
            getBookById(cursor.getLong(cursor.getColumnIndexOrThrow(CartEntry.COLUMN_BOOK_ID))).price
        } else {
            0.0
        }

        Log.d(LOG_TAG, price.toString())
        cursor.close()

        return price
    }

    fun getNumberOfBooks(): Int {
        val SQL_GET_BOOKS_SELECT_QUERY = "SELECT * FROM ${BookEntry.TABLE_NAME};"
        Log.d(LOG_TAG, SQL_GET_BOOKS_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_BOOKS_SELECT_QUERY, null)
        val numberOfBooks = cursor.count
        cursor.close()

        return numberOfBooks
    }

    fun getCartValue(): Double {
        val SQL_GET_BOOK_SELECT_QUERY = "SELECT * FROM ${CartEntry.TABLE_NAME}"
        Log.d(LOG_TAG, SQL_GET_BOOK_SELECT_QUERY)

        var cartValue = 0.0

        val cursor = this.readableDatabase.rawQuery(SQL_GET_BOOK_SELECT_QUERY, null)
        while (cursor.moveToNext()) {
            cartValue += getBookById(cursor.getLong(cursor.getColumnIndexOrThrow(CartEntry.COLUMN_BOOK_ID))).price
        }

        Log.d(LOG_TAG, cartValue.toString())
        cursor.close()

        return cartValue
    }

    fun getCartList(): ArrayList<Book> {
        val resultList = arrayListOf<Book>()

        val SQL_GET_BOOK_SELECT_QUERY = "SELECT * FROM ${CartEntry.TABLE_NAME}"
        Log.d(LOG_TAG, SQL_GET_BOOK_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_BOOK_SELECT_QUERY, null)
        while (cursor.moveToNext()) {
            resultList.add(getBookById(cursor.getLong(cursor.getColumnIndexOrThrow(CartEntry.COLUMN_BOOK_ID))))
        }

        cursor.close()
        return resultList
    }

    fun getNumberOfOrderedBooks(): Int {
        val SQL_GET_NUMBER_OF_ORDERS_SELECT_QUERY = "SELECT * FROM ${ShoppingHistoryEntry.TABLE_NAME};"
        Log.d(LOG_TAG, SQL_GET_NUMBER_OF_ORDERS_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_NUMBER_OF_ORDERS_SELECT_QUERY, null)
        val numberOfOrders = cursor.count
        cursor.close()

        return numberOfOrders
    }

    fun getRecommendations(): ArrayList<Book> {
        val orders = arrayListOf<Order>()
        var candidatesToRecommend = getAllBooks()
        val resultList = arrayListOf<Book>()
        var lastOrderDate: Long = 0

        val SQL_GET_BOOK_SELECT_QUERY = "SELECT * FROM ${ShoppingHistoryEntry.TABLE_NAME}"
        Log.d(LOG_TAG, SQL_GET_BOOK_SELECT_QUERY)

        val cursor = this.readableDatabase.rawQuery(SQL_GET_BOOK_SELECT_QUERY, null)
        while (cursor.moveToNext()) {
            orders.add(Order.fromCursor(cursor))
        }

        orders.forEach {
            val title = it.bookTitle
            candidatesToRecommend = ArrayList(candidatesToRecommend.filter { it.title != title })
            Log.d(LOG_TAG, "Current: ${it.orderDate}, last: $lastOrderDate")
            if (it.orderDate > lastOrderDate)
                lastOrderDate = it.orderDate
        }

        candidatesToRecommend.forEach {
            var rating = 0
            val authors = it.authors
            val category = it.category
            orders.forEach {
                if (it.bookAuthor == authors)
                    rating += 5
                if (it.category == category)
                    rating += 3
                if (it.orderDate == lastOrderDate)
                    rating *= 2
            }
            Log.d(LOG_TAG, it.toString())
            Log.d(LOG_TAG, "Rating = $rating")
            if (rating > 20)
                resultList.add(it)
        }

        Log.d(LOG_TAG, "Liczba rekomendacji: ${resultList.size}")
        cursor.close()

        return resultList
    }
}