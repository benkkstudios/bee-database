package com.benkkstudios.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.google.gson.Gson

abstract class TableImpl {
    private lateinit var table: String
    private lateinit var db: SQLiteDatabase
    internal fun create(context: Context, table: String) {
        this.table = table
        db = context.openOrCreateDatabase("benkkstudios-db.db", Context.MODE_PRIVATE, null)
        db.execSQL("CREATE TABLE IF NOT EXISTS $table(id VARCHAR PRIMARY KEY,value VARCHAR);")
    }

    fun insert(id: String, obj: Any, existMode: ExistMode) {
        when (existMode) {
            ExistMode.REPLACE -> runCatching {
                insert(id, obj)
            }.onFailure {
                update(id, obj)
            }

            ExistMode.NOTHING -> runCatching {
                insert(id, obj)
            }

            ExistMode.ERROR -> runCatching {
                insert(id, obj)
            }.onFailure {
                throw it
            }
        }
    }

    private fun insert(id: String, obj: Any) {
        db.execSQL("INSERT INTO $table VALUES('$id','${Gson().toJson(obj)}');")
    }

    fun update(id: String, obj: Any) {
        runCatching {
            val contentValues = ContentValues().apply {
                put("value", Gson().toJson(obj))
            }
            db.update(table, contentValues, "id = ? ", arrayOf(id))
        }.onFailure {
            throw it
        }
    }

    @SuppressLint("Recycle")
    protected fun getById(id: String): String? {
        val cursor = db.rawQuery("Select value from $table where id='$id';", null)!!
        cursor.moveToFirst()
        if (cursor.isAfterLast) return null
        return cursor.getString(0)
    }

    @SuppressLint("Recycle")
    protected fun getById(ids: List<String>): List<String> {
        val idsMap = ids.associateBy { it }
        val result = ArrayList<String>()

        val cursor = db.rawQuery("Select * from $table;", null)!!
        cursor.moveToFirst()
        while (!cursor.isAfterLast)
            if (idsMap.containsKey(cursor.getString(0)))
                result.add(cursor.getString(1))

        return result
    }

    fun delete(id: String) = db.delete(table, "id = ? ", arrayOf(id))

    protected fun deleteMany(ids: Array<String>) = ids.forEach { delete(it) }

    fun drop() = db.execSQL("DROP TABLE IF EXISTS $table")


    fun clear() {
        runCatching {
            db.execSQL("delete from $table")
        }.onFailure {
            it.printStackTrace()
        }
    }

    @SuppressLint("Recycle")
    protected fun innerIterator(): Iterator<String> {
        val resultSet = db.rawQuery("Select value from $table;", null)!!
        resultSet.moveToFirst()
        return object : Iterator<String> {
            override fun hasNext(): Boolean {
                return !resultSet.isAfterLast
            }

            override fun next(): String {
                val str = resultSet.getString(0)
                resultSet.moveToNext()
                return str
            }
        }
    }

}