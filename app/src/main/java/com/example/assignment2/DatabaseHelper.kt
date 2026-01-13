package com.example.assignment2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_STUDENTS = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_AGE = "age"
        private const val COLUMN_GRADE = "grade"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_STUDENTS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT NOT NULL,
                $COLUMN_AGE INTEGER NOT NULL,
                $COLUMN_GRADE TEXT NOT NULL
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_STUDENTS")
        onCreate(db)
    }

    // Add a new student
    fun addStudent(student: Student): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_AGE, student.age)
            put(COLUMN_GRADE, student.grade)
        }
        val result = db.insert(TABLE_STUDENTS, null, values)
        db.close()
        return result
    }

    // Get all students
    fun getAllStudents(): List<Student> {
        val students = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_STUDENTS", null)

        if (cursor.moveToFirst()) {
            do {
                val student = Student(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                    age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                    grade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE))
                )
                students.add(student)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return students
    }

    // Get a single student by ID
    fun getStudent(id: Int): Student? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_STUDENTS,
            null,
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null, null, null
        )

        var student: Student? = null
        if (cursor.moveToFirst()) {
            student = Student(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                age = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AGE)),
                grade = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GRADE))
            )
        }
        cursor.close()
        db.close()
        return student
    }

    // Update a student
    fun updateStudent(student: Student): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, student.name)
            put(COLUMN_AGE, student.age)
            put(COLUMN_GRADE, student.grade)
        }
        val result = db.update(TABLE_STUDENTS, values, "$COLUMN_ID = ?", arrayOf(student.id.toString()))
        db.close()
        return result
    }

    // Delete a student
    fun deleteStudent(id: Int): Int {
        val db = writableDatabase
        val result = db.delete(TABLE_STUDENTS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }
}
