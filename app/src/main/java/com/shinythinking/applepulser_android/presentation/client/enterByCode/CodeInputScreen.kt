package com.shinythinking.applepulser_android.presentation.client.enterByCode

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APTextField
import com.shinythinking.applepulser_android.presentation.base.component.NicknameDialog
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun CodeInputScreen(
    onNavigateToRoom: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CodeInputViewModel = hiltViewModel(),
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CodeInputContract.SideEffect.NavigateToRoom -> onNavigateToRoom(sideEffect.roomId)
            is CodeInputContract.SideEffect.ShowToast -> Toast.makeText(
                context,
                sideEffect.message,
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    CodeInputContent(
        modifier = Modifier.fillMaxSize(),
        state = state,
        onIntent = viewModel::onIntent
    )
}

@Composable
fun CodeInputContent(
    modifier: Modifier = Modifier,
    state: CodeInputContract.State,
    onIntent: (CodeInputContract.Intent) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    APBackground(
        modifier = modifier,
        appleExist = true,
        backExist = true,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Enter Code",
                    style = MaterialTheme.typography.displaySmall,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(28.dp))

                APTextField(
                    text = state.roomCode,
                    onTextChange = { onIntent(CodeInputContract.Intent.CodeChanged(it)) },
                    modifier = Modifier.fillMaxWidth(0.85f),
                )

                if (state.isCodeError) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Invalid Code",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .align(Alignment.Start)
                            .padding(start = 40.dp)
                            .clickable { onIntent(CodeInputContract.Intent.CodeChanged("")) }
                    )
                }
            }

            if (state.showNicknameDialog) {
                NicknameDialog(
                    nickname = state.nickname,
                    onNicknameChanged = { onIntent(CodeInputContract.Intent.NicknameChanged(it)) },
                    onConfirm = {
                        keyboardController?.hide()
                        onIntent(CodeInputContract.Intent.SubmitCode)
                    },
                    onDismiss = { onIntent(CodeInputContract.Intent.DismissDialog) },
                )
            }
        }
    }
}

@Preview
@Composable
fun CodeInputScreenPreview() {
    ApplepulserTheme {
        CodeInputContent(
            modifier = Modifier,
            state = CodeInputContract.State(),
            onIntent = {}
        )
    }
}