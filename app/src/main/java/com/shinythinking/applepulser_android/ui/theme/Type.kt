package com.shinythinking.applepulser_android.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.shinythinking.applepulser_android.R

val inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_thin, FontWeight.Thin)
)

val Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 40.sp,
        lineHeight = 48.sp

    ),
    titleMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp,
        lineHeight = 44.sp,

    ),
    titleSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Thin,
        fontSize = 32.sp,
        lineHeight = 40.sp

    ),
    labelLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 32.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Thin,
        fontSize = 24.sp,
        lineHeight = 32.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Thin,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 24.sp,

        ),
    bodySmall = TextStyle(
        fontFamily = inter,
        fontWeight = FontWeight.Light,
        fontSize = 12.sp,
        lineHeight = 20.sp,
    )
)