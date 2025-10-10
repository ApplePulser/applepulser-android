package com.shinythinking.applepulser_android.presentation.client.enterByCode

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.presentation.base.component.APTextField
import com.shinythinking.applepulser_android.presentation.base.component.Background
import com.shinythinking.applepulser_android.ui.theme.Applepulser_androidTheme

@Composable
fun CodeInputScreen(
    modifier: Modifier = Modifier,
) {
    Background(
        modifier = modifier,
        appleExist = true,
        backExist = true,
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Enter Code",
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(28.dp))
            APTextField(
                text = "",
                onTextChange = { "" },
                modifier = Modifier.fillMaxWidth(0.85f)
            )
        }
    }
}

@Preview
@Composable
fun CodeInputScreenPreview() {
    Applepulser_androidTheme {
        CodeInputScreen()
    }
}
