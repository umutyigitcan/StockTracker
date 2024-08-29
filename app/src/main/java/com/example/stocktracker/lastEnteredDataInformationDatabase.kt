package com.example.stocktracker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class lastEnteredDataInformationDatabase(mContext:Context):SQLiteOpenHelper(mContext,"lastEnteredData",null,1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE lastEnteredData(qrcode TEXT,itemName TEXT,itemQuantity TEXT);")
        db.execSQL("INSERT INTO lastEnteredData(qrcode,itemName,itemQuantity) VALUES('null','null','null')")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS lastEnteredData",null)
        onCreate(db)
    }
}