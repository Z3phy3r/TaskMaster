package com.example.taksmasterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import nl.builders.taskmaster.R

class Task : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.task_screen)
    }
}