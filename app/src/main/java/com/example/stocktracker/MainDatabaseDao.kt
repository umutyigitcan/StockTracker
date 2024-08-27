package com.example.stocktracker

class MainDatabaseDao {

    fun PersonalAdd(vt:MainDatabase,mail:String,username:String,password:String){

        var db=vt.writableDatabase
        db.execSQL("INSERT INTO Users(mail,username,password) VALUES('$mail','$username','$password')")
        db.close()

    }

    fun List(vt:MainDatabase):ArrayList<PersonalInformation>{

        var db=vt.writableDatabase
        var list =ArrayList<PersonalInformation>()
        var cursor=db.rawQuery("SELECT * FROM Users",null)
        while (cursor.moveToNext()){
            var user=PersonalInformation(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")))
            list.add(user)
        }
        return list
    }

    fun LastData(vt:MainDatabase):ArrayList<PersonalInformation>{
        var db=vt.writableDatabase
        var list=ArrayList<PersonalInformation>()
        var cursor=db.rawQuery("SELECT * FROM Users ORDER BY id DESC LIMIT 1",null)
        while (cursor.moveToNext()){
            var user=PersonalInformation(cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                cursor.getString(cursor.getColumnIndexOrThrow("mail")),
                cursor.getString(cursor.getColumnIndexOrThrow("username")),
                cursor.getString(cursor.getColumnIndexOrThrow("password")))
            list.add(user)
        }
        return list
    }
}