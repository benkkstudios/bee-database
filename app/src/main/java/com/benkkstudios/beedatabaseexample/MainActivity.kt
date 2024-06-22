package com.benkkstudios.beedatabaseexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.benkkstudios.database.ExistMode

class MainActivity : ComponentActivity() {
    private val repository: TestRepository by lazy { TestRepository.getInstance(this) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // to get all database as arraylist
        repository.get()

        // to get all database by id
        repository.get("id")

        // to get all database by id list
        val idList = listOf("1", "2", "3")
        repository.get(idList)

        // insert item
        val insertItem = TestItem("1", "test")
        repository.insert(insertItem, ExistMode.REPLACE)

        // insert item by defined id
        repository.insert("100", insertItem, ExistMode.NOTHING)

        // insert item from list
        val listInsertItem = listOf(TestItem("1", "test"), TestItem("2", "test"))
        repository.insert(listInsertItem, ExistMode.NOTHING)

        // update item
        val updateItem = TestItem("1", "test")
        repository.update(updateItem)

        // update item by id
        repository.update("item id", updateItem)

        // update with condition
        repository.updateWhere({ it.url == "asd" }, { updateItem })
    }
}
