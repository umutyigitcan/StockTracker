package com.example.stocktracker

class lastEnteredDataInformationDatabaseDao {

    fun changeData(vt:lastEnteredDataInformationDatabase,qrcode:String,itemName:String,itemQuantity:String){

        var db=vt.writableDatabase
        db.execSQL("UPDATE lastEnteredData SET qrcode='$qrcode'")
        db.execSQL("UPDATE lastEnteredData SET itemName='$itemName'")
        db.execSQL("UPDATE lastEnteredData SET itemQuantity='$itemQuantity'")
        db.close()
    }

    fun changeQuantityIncreaseData(vt:lastEnteredDataInformationDatabase,qrcode: String,itemQuantity: String){

        var db=vt.writableDatabase
        var guncel=itemQuantity.toInt()
        var guncel2=guncel+1
        var guncel3=guncel2.toString()
        db.execSQL("UPDATE lastEnteredData SET itemQuantity='$guncel3' WHERE qrcode='$qrcode'")
        db.close()
    }

    fun changeQuantityDecreaseData(vt:lastEnteredDataInformationDatabase,qrcode: String,itemQuantity: String){

        var db=vt.writableDatabase
        var guncel=itemQuantity.toInt()
        var guncel2=guncel-1
        var guncel3=guncel2.toString()
        db.execSQL("UPDATE lastEnteredData SET itemQuantity='$guncel3' WHERE qrcode='$qrcode'")
        db.close()
    }

    fun getData(vt:lastEnteredDataInformationDatabase):ArrayList<lastEnteredDataInformation>{

        var db=vt.writableDatabase
        var allData=ArrayList<lastEnteredDataInformation>()
        var cursor=db.rawQuery("SELECT * FROM lastEnteredData",null)
        while (cursor.moveToNext()){
            var data=lastEnteredDataInformation(cursor.getString(cursor.getColumnIndexOrThrow("qrcode")),
                cursor.getString(cursor.getColumnIndexOrThrow("itemName")),
                cursor.getString(cursor.getColumnIndexOrThrow("itemQuantity")))
            allData.add(data)
        }
        return allData
    }





}