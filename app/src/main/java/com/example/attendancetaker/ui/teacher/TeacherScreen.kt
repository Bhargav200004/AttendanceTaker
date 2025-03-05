package com.example.attendancetaker.ui.teacher

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Class
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.attendancetaker.ui.teacher.components.DropDownSection

@Composable
fun TeacherScreen(modifier: Modifier = Modifier) {

    val viewModel : TeacherViewModel = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()


    CreateClassScreen(
        modifier = modifier,
        onCreateButtonClick = {
            viewModel.onEvent(TeacherEvent.OnShowDialogChange(uiState.showDialog))
        }
    )

    if (uiState.showDialog){
        DialogWithClassAndSectionInput(
            uiState = uiState,
            onEvent =  viewModel::onEvent
        )
    }
}

@Composable
private fun CreateClassScreen(
    modifier: Modifier,
    onCreateButtonClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            modifier = Modifier.size(200.dp),
            imageVector = Icons.Outlined.Class,
            contentDescription = "Class image"
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "You Don't have any class",
            style = TextStyle(
                fontSize = 18.sp
            )
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Create a class",
            style = TextStyle(
                fontSize = 18.sp
            )
        )
        Spacer(modifier = Modifier.height(10.dp))
        OutlinedButton(
            onClick = {
                onCreateButtonClick()
            },
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = "Create a Class",
                style = TextStyle(
                    fontSize = 18.sp
                )
            )
        }
    }
}

@Composable
fun DialogWithClassAndSectionInput(
    uiState : TeacherData,
    onEvent : (TeacherEvent) -> Unit,
) {
    Dialog(
        onDismissRequest = {
            onEvent(TeacherEvent.OnShowDialogChange(uiState.showDialog))
        }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(14.dp),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Enter Class Details",
                    modifier = Modifier.padding(16.dp),
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = uiState.classRoom,
                        onValueChange = {
                            onEvent(TeacherEvent.OnClassRoomChange(it))
                        },
                        label = {
                            Text(
                                text = "Class Name"
                            )
                        },
                        keyboardOptions  = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(10.dp))


                    DropDownSection(
                        options = uiState.section,
                        selectedOptionText = uiState.selectedSection,
                        selectedOptionTextSelected = {
                            onEvent(TeacherEvent.OnSectionChange(it))
                        }
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = {
                                onEvent(TeacherEvent.OnShowDialogChange(uiState.showDialog))
                                 },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Cancel"
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                onEvent(TeacherEvent.OnSubmitChange)
                                onEvent(TeacherEvent.OnShowDialogChange(uiState.showDialog))
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "Submit"
                            )
                        }
                    }
                }
            }
        }
    }
}
