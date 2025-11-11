package com.shinythinking.applepulser_android.presentation.base.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.shinythinking.applepulser_android.ui.theme.ApplepulserTheme

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
            style = MaterialTheme.typography.titleSmall
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

@Composable
fun APBigButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    isClicked: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxHeight(0.65f)
            .fillMaxWidth(0.4f)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp),
            ),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(vertical = 20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isClicked) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.secondary
            },
            contentColor = if (isClicked) {
                MaterialTheme.colorScheme.onTertiary
            } else {
                MaterialTheme.colorScheme.onPrimary
            },
        ),
    ) {
        Box(
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = text,
                maxLines = 2,
                modifier = Modifier.padding(horizontal = 24.dp),
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun ButtonsPreview() {
    ApplepulserTheme {
        Background {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth(0.85f)
                ) {
                    APBigButton(
                        onClick = {},
                        text = "Steady\nBeat",
                        isClicked = true,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(Modifier.width(16.dp))
                    APBigButton(
                        onClick = {},
                        text = "Pulse\nRush",
                        isClicked = false,
                        modifier = Modifier.weight(1f),
                    )
                }
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