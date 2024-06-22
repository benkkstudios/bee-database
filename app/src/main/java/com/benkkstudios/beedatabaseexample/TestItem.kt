package com.benkkstudios.beedatabaseexample

import com.benkkstudios.database.BeeModel

data class TestItem(
    override var id: String,
    var url: String
) : BeeModel
