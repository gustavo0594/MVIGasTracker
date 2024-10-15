package com.example.mvigastracker.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mvigastracker.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

class GasRecordCallback(
    private val context: Context
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        fetchEntriesFromJson(db)
    }

    private fun fetchEntriesFromJson(db: SupportSQLiteDatabase) {
        runCatching {
            val recordList: JSONArray =
                context.resources.assets.open("gas_record.json").bufferedReader().use {
                    JSONArray(it.readText())
                }
            recordList.takeIf { it.length() > 0 }?.let { list ->
                for (index in 0 until list.length()) {
                    list.getJSONObject(index).also {
                        insertEntry(
                            db = db,
                            entry = ContentValues().apply {
                                put("recordDate", it.getString("date"))
                                put("kilometers", it.getString("kilometers"))
                                put("totalPayment", it.getString("totalPayment"))
                            }
                        )
                    }

                }

            }
        }.onFailure {
            it.printStackTrace()
        }
    }

    private fun insertEntry(db: SupportSQLiteDatabase, entry: ContentValues) {
        CoroutineScope(Dispatchers.IO).launch {
            db.insert(
                table = "gas_record",
                conflictAlgorithm = SQLiteDatabase.CONFLICT_REPLACE,
                values = entry
            )
        }
    }
}