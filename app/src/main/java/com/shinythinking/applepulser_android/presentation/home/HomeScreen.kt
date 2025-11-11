package com.shinythinking.applepulser_android.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.presentation.base.component.APLongButton
import com.shinythinking.applepulser_android.presentation.base.component.Background
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Background(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(bottom = 100.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_home),
                contentDescription = "App Logo",
                modifier = Modifier
                    .padding(top = 100.dp)
                    .fillMaxWidth(0.9f),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.height(36.dp))

            APLongButton(
                onClick = {},
                "Launch Room"
            )

            Spacer(modifier = Modifier.height(16.dp))

            APLongButton(
                onClick = {},
                "Enter Room"
            )
        }
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    ApplepulserTheme {
        HomeScreen()
    }
}