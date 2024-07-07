package com.example.notesapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.notesapp.model.Note

@Dao
interface Dao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNote(note: Note)

    @Update
    suspend fun updateNote(note: Note)

    @Query("select* from notes order by id desc")
    fun getAllNotes(): LiveData<List<Note>>

    @Query(
        "select* from notes where title like :query or content like :query or date like :query order by id ASC"
    )
    fun searchNotes(query: String): LiveData<List<Note>>

    @Delete
    suspend fun deleteNote(note: Note)

}