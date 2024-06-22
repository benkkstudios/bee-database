        // to get all database as arraylist
        database.get()

        // to get all database by id
        database.get("id")

        // to get all database by id list
        val idList = listOf("1", "2", "3")
        database.get(idList)

        // insert item
        val insertItem = TestItem("1", "test")
        database.insert(insertItem, ExistMode.REPLACE)

        // insert item by defined id
        database.insert("100", insertItem, ExistMode.NOTHING)

        // insert item from list
        val listInsertItem = listOf(TestItem("1", "test"), TestItem("2", "test"))
        database.insert(listInsertItem, ExistMode.NOTHING)

        // update item
        val updateItem = TestItem("1", "test")
        database.update(updateItem)

        // update item by id
        database.update("item id", updateItem)

        // update with condition
        database.updateWhere({ it.url == "asd" }, { updateItem })
