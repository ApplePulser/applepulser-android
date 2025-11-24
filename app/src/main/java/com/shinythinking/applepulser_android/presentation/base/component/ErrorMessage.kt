package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.R
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

@Composable
fun ErrorMessage(
    modifier: Modifier = Modifier,
    message: String = "Something went wrong. \nPlease try again later.",
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(Modifier.weight(1f))

        Image(
            painterResource(R.drawable.ic_oops_apple),
            contentDescription = "Error",
            modifier = Modifier.size(200.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Oops!",
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = message,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.weight(1f))

        if (onRetry != null) {
            Spacer(modifier = Modifier.height(12.dp))

            APShortButton(
                onClick = onRetry,
                text = "Retry",
            )
        }
    }
}

@Preview
@Composable
fun ErrorMessagePreview() {

    ApplepulserTheme {
        APBackground {
            ErrorMessage(
                onRetry = {}
            )
        }
    }
}