package com.topic2.android.notes.ui.screens

import androidx.compose.runtime.Composable
import com.topic2.android.notes.viewmodel.MainViewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import com.topic2.android.notes.domain.model.NoteModel
import com.topic2.android.notes.ui.components.Note
import android.annotation.SuppressLint
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.rememberCoroutineScope
import com.topic2.android.notes.routing.Screen
import com.topic2.android.notes.ui.ui.components.components.AppDrawer
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun NotesScreen(
    viewModel: MainViewModel
) {
    val notes: List<NoteModel> by viewModel
        .notesNotInTrash
        .observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold (topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "Notes",
                    color = MaterialTheme.colors.onPrimary
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            scaffoldState.drawerState.open()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.List,
                        contentDescription = "Drawer Button"
                    )
                }
            }
        )
    },
        scaffoldState = scaffoldState,
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.Notes, closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewNoteClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Note Button"
                    )
                }
            )
        },
        content = {
            if (notes.isNotEmpty()) {
                NotesList(
                    notes = notes, onNoteCheckedChange = {
                        viewModel.onNoteCheckedChange(it)
                    },
                    onNoteClick = { viewModel.onNoteClick(it)}
                )
            }
        }
    )
}

@Composable
private fun NotesList(
    notes: List<NoteModel>,
    onNoteCheckedChange: (NoteModel) -> Unit,
    onNoteClick:(NoteModel) -> Unit
){
    LazyColumn{
        items(count = notes.size){noteIndex ->
            val note = notes[noteIndex]
            Note(
                note = note,
                onNoteClick = onNoteClick,
                onNoteCheckedChange = onNoteCheckedChange
            )
        }
    }
}

@Preview
@Composable
private fun  NotesListPreview(){
    NotesList(
        notes = listOf(
            NoteModel(1,"Note 1", "Content 1", null),
            NoteModel(2,"Note 2", "Content 2", false) ,
            NoteModel(3,"Note 1", "Content 3", true)
        ),
        onNoteCheckedChange = {},
        onNoteClick = {}
    )
}