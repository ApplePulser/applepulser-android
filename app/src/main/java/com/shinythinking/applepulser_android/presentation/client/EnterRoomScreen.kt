package com.shinythinking.applepulser_android.presentation.client

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.presentation.base.component.APLongButton
import com.shinythinking.applepulser_android.presentation.base.component.Background
import com.shinythinking.applepulser_android.ui.theme.Applepulser_androidTheme

@Composable
fun EnterRoomScreen(
    modifier: Modifier = Modifier,
) {
    Background(
        appleExist = true,
        backExist = true,
    ) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(2f))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(48.dp)
            ) {
                Text(
                    text = stringResource(R.string.join_by),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            APLongButton(
                onClick = {},
                text = stringResource(R.string.qr),
            )
            Spacer(modifier = Modifier.height(32.dp))
            APLongButton(
                onClick = {},
                text = stringResource(R.string.code),
            )
            Spacer(modifier = Modifier.weight(5f))
        }
    }
}

@Preview
@Composable
fun EnterRoomScreenPreview() {
    Applepulser_androidTheme {
        EnterRoomScreen()
    }
}
