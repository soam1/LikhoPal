package com.example.notesapp.activities

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.databinding.ActivityMainBinding
import com.example.notesapp.db.NoteDatabase
import com.example.notesapp.repository.NoteRepository
import com.example.notesapp.viewModel.NoteActivityViewModel
import com.example.notesapp.viewModel.NoteActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var noteActivityViewModel: NoteActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        supportActionBar?.hide()
        binding = ActivityMainBinding.inflate(layoutInflater)
        try {
            setContentView(binding.root)
            val noteRepository = NoteRepository(NoteDatabase(this))
            val noteActivityViewModelFactory = NoteActivityViewModelFactory(noteRepository)
            noteActivityViewModel = ViewModelProvider(
                this,
                noteActivityViewModelFactory
            )[NoteActivityViewModel::class.java]

        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}")
        }
    }
}