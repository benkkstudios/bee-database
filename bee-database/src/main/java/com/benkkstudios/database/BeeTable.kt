package com.benkkstudios.database

import android.content.Context
import com.google.gson.Gson
import java.lang.reflect.ParameterizedType

@Suppress("UNCHECKED_CAST")
abstract class BeeTable<T : BeeModel>(context: Context) : TableImpl(), Iterable<T> {
    private val type: Class<T> = ((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<T>)

    init {
        create(context, type.simpleName)
    }

    open fun insert(row: T, existMode: ExistMode = ExistMode.ERROR) = super.insert(row.id, row, existMode)

    open fun insert(rows: Iterable<T>, existMode: ExistMode = ExistMode.ERROR) = rows.forEach { insert(it, existMode) }

    open fun update(row: T) = super.update(row.id, row)

    open fun delete(row: T) = super.delete(row.id)

    private fun updateMany(rows: Iterable<T>) = rows.forEach { update(it.id, it) }

    open fun updateWhere(condition: (obj: T) -> Boolean, change: (row: T) -> T) = updateMany(filter(condition).map(change))

    open fun deleteWhere(condition: (obj: T) -> Boolean) = deleteMany(filter(condition).map { it.id }.toTypedArray())

    open fun get(): ArrayList<T> {
        val list = ArrayList<T>()
        val itr = iterator()
        while (itr.hasNext())
            list.add(itr.next())
        return list
    }

    open fun get(id: String) = Gson().fromJson(super.getById(id), type)

    open fun get(id: List<String>) = super.getById(id).map { Gson().fromJson(it, type) }

    override fun iterator(): Iterator<T> {
        val itr = super.innerIterator()
        return object : Iterator<T> {
            override fun hasNext(): Boolean {
                return itr.hasNext()
            }

            override fun next(): T {
                return Gson().fromJson(
                    itr.next(),
                    type
                ) as T
            }
        }
    }
}