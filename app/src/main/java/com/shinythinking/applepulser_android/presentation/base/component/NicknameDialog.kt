package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun NicknameDialog(
    nickname: String,
    onNicknameChanged: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        NicknameDialogContent(
            nickname = nickname,
            onNicknameChange = onNicknameChanged,
            onConfirm = onConfirm,
            modifier = modifier
        )
    }
}

@Composable
fun NicknameDialogContent(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFF2F2F7))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Set your nickname",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(16.dp))

        APTextField(
            text = nickname,
            onTextChange = onNicknameChange,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onConfirm,
            shape = RoundedCornerShape(90),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4A3434)
            ),
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(
                text = "Enter Room",
                color = Color.White,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}

@Preview
@Composable
fun NicknameDialogPreview() {
    ApplepulserTheme {
        APBackground(
            modifier = Modifier.fillMaxSize(),
            appleExist = true,
            backExist = true,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                NicknameDialogContent(
                    nickname = "Sample",
                    onNicknameChange = {},
                    onConfirm = {},
                    modifier = Modifier.fillMaxWidth(0.9f)
                )
            }
        }
    }
}