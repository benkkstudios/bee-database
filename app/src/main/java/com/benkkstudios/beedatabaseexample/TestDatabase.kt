package com.benkkstudios.beedatabaseexample

import android.content.Context
import com.benkkstudios.database.BeeTable

class TestDatabase(context: Context) : BeeTable<TestItem>(context) {
    companion object {
        private var repo: TestDatabase? = null
        fun getInstance(context: Context): TestDatabase {
            if (repo == null) repo = TestDatabase(context)
            return repo!!
        }
    }
}