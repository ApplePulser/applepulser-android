package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.ui.theme.Applepulser_androidTheme

@Composable
fun APLongButton(
    onClick: () -> Unit,
    text: String,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.80f),
        contentPadding = PaddingValues(vertical = 28.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun APShortButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isClicked: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(0.55f),
        contentPadding = PaddingValues(vertical = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isClicked) {
                MaterialTheme.colorScheme.tertiary
            } else {
                MaterialTheme.colorScheme.primary
            },
            contentColor = if (isClicked) {
                MaterialTheme.colorScheme.onTertiary
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
        ),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview
@Composable
fun ButtonsPreview() {
    Applepulser_androidTheme {
        Background {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                APLongButton(
                    onClick = {},
                    text = "APLong Button"
                )
                Spacer(modifier = Modifier.height(16.dp))
                APShortButton(
                    onClick = {},
                    text = "Clicked",
                    isClicked = true
                )
                Spacer(modifier = Modifier.height(16.dp))
                APShortButton(
                    onClick = {},
                    text = "not clicked",
                    isClicked = false
                )
            }
        }
    }
}