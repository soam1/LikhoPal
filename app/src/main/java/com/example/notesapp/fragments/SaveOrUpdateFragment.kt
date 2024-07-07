package com.example.notesapp.fragments

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.notesapp.R
import com.example.notesapp.activities.MainActivity
import com.example.notesapp.databinding.FragmentSaveOrUpdateBinding
import com.example.notesapp.model.Note
import com.example.notesapp.utils.hideKeyboard
import com.example.notesapp.viewModel.NoteActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.text.SimpleDateFormat
import java.util.Date


class SaveOrUpdateFragment : Fragment(R.layout.fragment_save_or_update) {
    private lateinit var navController: NavController
    private lateinit var contentBinding: FragmentSaveOrUpdateBinding
    private var note: Note? = null
    private var color = -1
    private lateinit var result: String
    private val noteActivityViewModel: NoteActivityViewModel by activityViewModels()
    private val currentDate = SimpleDateFormat.getInstance().format(Date())
    private val job = CoroutineScope(Dispatchers.Main)
    private val args: SaveOrUpdateFragmentArgs by navArgs()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val animation = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragment
            scrimColor = Color.TRANSPARENT
            duration = 300L

        }
        sharedElementEnterTransition = animation
        sharedElementReturnTransition = animation
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentBinding = FragmentSaveOrUpdateBinding.bind(view)

        navController = Navigation.findNavController(view)
        val activity = activity as MainActivity

        contentBinding.backBtn.setOnClickListener {
            requireView().hideKeyboard()
            navController.popBackStack()
        }
        contentBinding.lastEdited.text = "EditedOn: $currentDate"

        contentBinding.saveNote.setOnClickListener {
            saveNoteMethod()
        }
        try {
            contentBinding.etNoteContent.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    contentBinding.bottomBar.visibility = View.VISIBLE
                    contentBinding.etNoteContent.setStylesBar(contentBinding.styleBar)
                } else contentBinding.bottomBar.visibility = View.GONE
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("SaveOrUpdateFragment", "onViewCreated: ${e.message}")
        }
        contentBinding.fabColorPick.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(
                requireContext(), R.style.BottomSheetDialogTheme
            )
            val bottomSheetView = layoutInflater.inflate(
                R.layout.bottom_sheet_layout, null
            )
            with(bottomSheetDialog) {
                setContentView(bottomSheetView)
                show()
            }
            val bottomSheetBinding =
                com.example.notesapp.databinding.BottomSheetLayoutBinding.bind(bottomSheetView)
            bottomSheetBinding.apply {
                colorPicker.apply {
                    setSelectedColor(color)
                    setOnColorSelectedListener { value ->
                        color = value
                        contentBinding.apply {
                            noteContentFragmentParent.setBackgroundColor(color)
                            toolbarFragmentNoteContent.setBackgroundColor(color)
                            bottomBar.setBackgroundColor(color)
                            activity.window.statusBarColor = color
                        }
                        bottomSheetBinding.bottomSheetParent.setCardBackgroundColor(color)
                    }
                }
                bottomSheetParent.setCardBackgroundColor(color)
            }
            bottomSheetView.post {
                bottomSheetDialog.behavior.state =
                    com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
            }
        }


    }

    private fun saveNoteMethod() {
        if (contentBinding.etNoteContent.text.toString()
                .isEmpty() || contentBinding.etTitle.text.toString().isEmpty()
        ) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }
        note = args.note
        when (note) {
            null -> {
                //save new note
                note = Note(
                    title = contentBinding.etTitle.text.toString(),
                    content = contentBinding.etNoteContent.text.toString(),
                    date = currentDate,
                    color = color
                )
                noteActivityViewModel.saveNote(note!!)

                result = "Note saved"
                setFragmentResult("key", bundleOf("bundleKey" to result))
                navController.navigate(SaveOrUpdateFragmentDirections.actionSaveOrUpdateFragmentToNoteFragment())
            }

            else -> {

                //update already existing note
//                note!!.title = contentBinding.etTitle.text.toString()
//                note!!.content = contentBinding.etNoteContent.text.toString()
//                note!!.date = currentDate
//                note!!.color = color
//                noteActivityViewModel.saveNote(note!!)
//                navController.navigate(SaveOrUpdateFragmentDirections.actionSaveOrUpdateFragmentToNoteFragment())
            }
        }
    }


}