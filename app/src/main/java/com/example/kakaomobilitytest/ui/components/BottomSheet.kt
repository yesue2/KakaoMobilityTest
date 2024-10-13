package com.example.kakaomobilitytest.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.kakaomobilitytest.ui.theme.DarkColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(
    errorCode: Int,
    errorMessage: String,
    origin: String,
    destination: String,
    onDismiss: () -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        modifier = Modifier.fillMaxWidth(),
        sheetState = bottomSheetState,
        onDismissRequest = {
            scope.launch { bottomSheetState.hide() }
            onDismiss()
        },
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "경로조회 실패",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = DarkColor
            )
            Spacer(modifier = Modifier.height(13.dp))

            Text(
                text = "경로 : $origin ~ $destination",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = DarkColor
            )
            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "code: $errorCode",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = DarkColor
            )
            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = "message: $errorMessage",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = DarkColor
            )
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    scope.launch { bottomSheetState.hide() }
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = DarkColor)
            ) {
                Text(text = "확인", color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
    }
}
