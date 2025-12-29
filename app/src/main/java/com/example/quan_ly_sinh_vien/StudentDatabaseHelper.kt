package com.example.quan_ly_sinh_vien

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class StudentDatabaseHelper(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

  companion object {
    private const val DATABASE_NAME = "student_manager.db"
    private const val DATABASE_VERSION = 1
    private const val TABLE_STUDENTS = "students"
    private const val COLUMN_ID = "id"
    private const val COLUMN_NAME = "name"
    private const val COLUMN_PHONE = "phone"
    private const val COLUMN_ADDRESS = "address"
  }

  override fun onCreate(db: SQLiteDatabase) {
    val createTableQuery =
            "CREATE TABLE $TABLE_STUDENTS (" +
                    "$COLUMN_ID TEXT PRIMARY KEY," +
                    "$COLUMN_NAME TEXT," +
                    "$COLUMN_PHONE TEXT," +
                    "$COLUMN_ADDRESS TEXT)"
    db.execSQL(createTableQuery)
  }

  override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
    onCreate(db)
  }

  fun getAllStudents(): MutableList<Student> {
    val studentList = mutableListOf<Student>()
    val db = this.readableDatabase
    val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENTS", null)

    if (cursor.moveToFirst()) {
      do {
        val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
        val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
        val phone = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PHONE))
        val address = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADDRESS))
        studentList.add(Student(id, name, phone, address))
      } while (cursor.moveToNext())
    }
    cursor.close()
    db.close()
    return studentList
  }

  fun addStudent(student: Student): Long {
    val db = this.writableDatabase
    val values =
            ContentValues().apply {
              put(COLUMN_ID, student.id)
              put(COLUMN_NAME, student.name)
              put(COLUMN_PHONE, student.phone)
              put(COLUMN_ADDRESS, student.address)
            }
    val result = db.insert(TABLE_STUDENTS, null, values)
    db.close()
    return result
  }

  fun updateStudent(student: Student): Int {
    val db = this.writableDatabase
    val values =
            ContentValues().apply {
              put(COLUMN_NAME, student.name)
              put(COLUMN_PHONE, student.phone)
              put(COLUMN_ADDRESS, student.address)
            }
    val result = db.update(TABLE_STUDENTS, values, "$COLUMN_ID = ?", arrayOf(student.id))
    db.close()
    return result
  }

  fun deleteStudent(studentId: String): Int {
    val db = this.writableDatabase
    val result = db.delete(TABLE_STUDENTS, "$COLUMN_ID = ?", arrayOf(studentId))
    db.close()
    return result
  }
}
