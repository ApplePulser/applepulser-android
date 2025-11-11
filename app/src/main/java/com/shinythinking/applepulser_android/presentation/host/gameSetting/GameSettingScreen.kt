package com.shinythinking.applepulser_android.presentation.host.gameSetting

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.presentation.base.component.APBackground
import com.shinythinking.applepulser_android.presentation.base.component.APBigButton
import com.shinythinking.applepulser_android.presentation.base.component.TextWithSideBar
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun GameSettingScreen(
    modifier: Modifier = Modifier
) {
    APBackground(
        modifier = modifier,
        backExist = true,
        buttonExist = true,
        buttonText = "Next",
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.height(32.dp))
            Text(
                text = "Mode",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(32.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                APBigButton(
                    onClick = { /*TODO*/ },
                    text = "Steady\nBeat",
                    modifier = Modifier.weight(1f),
                    isClicked = false
                )
                APBigButton(
                    onClick = { /*TODO*/ },
                    text = "Beat\nRush",
                    modifier = Modifier.weight(1f),
                    isClicked = false
                )
            }
            Spacer(Modifier.height(28.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TextWithSideBar(
                    title = "Consistency is key",
                    description = "Hold your pulse in range\nto climb the leaderboard",
                    modifier = Modifier.weight(1f)
                )
                TextWithSideBar(
                    title = "Push your limits",
                    description = "Compete to hit \nthe highest heart rate",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun GameSettingScreenPreview() {
    ApplepulserTheme {
        GameSettingScreen()
    }
}