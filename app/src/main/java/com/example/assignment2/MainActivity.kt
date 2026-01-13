package com.example.assignment2

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Add Student
        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val ageText = binding.etAge.text.toString().trim()
            val grade = binding.etGrade.text.toString().trim()

            if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val age = ageText.toIntOrNull()
            if (age == null) {
                Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(name = name, age = age, grade = grade)
            val result = dbHelper.addStudent(student)

            if (result > 0) {
                Toast.makeText(this, "Student added successfully", Toast.LENGTH_SHORT).show()
                clearFields()
                viewAllStudents()
            } else {
                Toast.makeText(this, "Failed to add student", Toast.LENGTH_SHORT).show()
            }
        }

        // View All Students
        binding.btnView.setOnClickListener {
            viewAllStudents()
        }

        // Update Student
        binding.btnUpdate.setOnClickListener {
            val idText = binding.etStudentId.text.toString().trim()
            val name = binding.etName.text.toString().trim()
            val ageText = binding.etAge.text.toString().trim()
            val grade = binding.etGrade.text.toString().trim()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Please enter Student ID to update", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (name.isEmpty() || ageText.isEmpty() || grade.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = idText.toIntOrNull()
            val age = ageText.toIntOrNull()

            if (id == null || age == null) {
                Toast.makeText(this, "Please enter valid ID and age", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val student = Student(id = id, name = name, age = age, grade = grade)
            val result = dbHelper.updateStudent(student)

            if (result > 0) {
                Toast.makeText(this, "Student updated successfully", Toast.LENGTH_SHORT).show()
                clearFields()
                viewAllStudents()
            } else {
                Toast.makeText(this, "Failed to update student. Check if ID exists.", Toast.LENGTH_SHORT).show()
            }
        }

        // Delete Student
        binding.btnDelete.setOnClickListener {
            val idText = binding.etStudentId.text.toString().trim()

            if (idText.isEmpty()) {
                Toast.makeText(this, "Please enter Student ID to delete", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val id = idText.toIntOrNull()
            if (id == null) {
                Toast.makeText(this, "Please enter a valid ID", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val result = dbHelper.deleteStudent(id)

            if (result > 0) {
                Toast.makeText(this, "Student deleted successfully", Toast.LENGTH_SHORT).show()
                clearFields()
                viewAllStudents()
            } else {
                Toast.makeText(this, "Failed to delete student. Check if ID exists.", Toast.LENGTH_SHORT).show()
            }
        }

        // Clear Fields
        binding.btnClear.setOnClickListener {
            clearFields()
        }
    }

    private fun viewAllStudents() {
        val students = dbHelper.getAllStudents()
        if (students.isEmpty()) {
            binding.tvStudentList.text = "No students found."
        } else {
            val sb = StringBuilder()
            students.forEach { student ->
                sb.append("ID: ${student.id}\n")
                sb.append("Name: ${student.name}\n")
                sb.append("Age: ${student.age}\n")
                sb.append("Grade: ${student.grade}\n")
                sb.append("------------------------\n")
            }
            binding.tvStudentList.text = sb.toString()
        }
    }

    private fun clearFields() {
        binding.etStudentId.text.clear()
        binding.etName.text.clear()
        binding.etAge.text.clear()
        binding.etGrade.text.clear()
    }
}
