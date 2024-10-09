//package com.example.kakaomobilitytest.ui
//
//import androidx.compose.material3.AlertDialog
//import androidx.compose.material3.Text
//import androidx.compose.material3.Button
//import androidx.compose.runtime.*
//import androidx.compose.ui.window.DialogProperties
//
//@Composable
//fun ShowErrorDialog(errorCode: Int, errorMessage: String, origin: String, destination: String, onDismiss: () -> Unit) {
//    var openDialog by remember { mutableStateOf(true) }
//
//    if (openDialog) {
//        AlertDialog(
//            onDismissRequest = {
//                openDialog = false
//                onDismiss()
//            },
//            confirmButton = {
//                Button(
//                    onClick = {
//                        openDialog = false
//                        onDismiss()
//                    }
//                ) {
//                    Text("확인")
//                }
//            },
//            title = {
//                Text(text = "경로조회 실패")
//            },
//            text = {
//                Text(text = "경로: $origin ~ $destination\ncode: $errorCode\nmessage: $errorMessage")
//            },
//            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
//        )
//    }
//}
