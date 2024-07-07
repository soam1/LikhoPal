package com.example.notesapp.repository

import com.example.notesapp.db.NoteDatabase
import com.example.notesapp.model.Note

class NoteRepository(private val db: NoteDatabase) {
    fun getNote() = db.getNoteDao().getAllNotes()

    fun searchNote(query: String) = db.getNoteDao().searchNotes(query)

    suspend fun addNote(note: Note) = db.getNoteDao().insertNote(note)
    suspend fun deleteNote(note: Note) = db.getNoteDao().deleteNote(note)
    suspend fun updateNote(note: Note) = db.getNoteDao().updateNote(note)
}