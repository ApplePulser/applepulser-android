package com.shinythinking.applepulser_android.presentation.client.clientRoom

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APLabel
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun ClientRoomScreen(modifier: Modifier = Modifier) {
    val dummyList = listOf("홍사인", "한예준", "신바다", "김나경")
    APBackground(
        modifier = modifier,
        appleExist = true,
        backExist = false,
        buttonExist = true,
        buttonText = stringResource(R.string.ready),
        isClicked = true,
        onButtonClick = { /*TODO*/ },
    ) {
        Column(modifier = Modifier.padding(horizontal = 36.dp, vertical = 48.dp)) {
            Text(
                text = stringResource(R.string.participation_status),
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.member),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground

            )
            Spacer(Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier,
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(dummyList) {
                    APLabel(
                        text = it,
                        isReady = false,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            }

        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun ClientRoomScreenPreview() {
    ApplepulserTheme {
        ClientRoomScreen()
    }
}