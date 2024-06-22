package com.benkkstudios.beedatabaseexample

import android.content.Context
import com.benkkstudios.database.BeeTable

class TestRepository(context: Context) : BeeTable<TestItem>(context) {
    companion object {
        private var repo: TestRepository? = null
        fun getInstance(context: Context): TestRepository {
            if (repo == null) repo = TestRepository(context)
            return repo!!
        }
    }
}