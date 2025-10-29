package com.proyect.ocean_words.view.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.proyect.ocean_words.R
val BricolageGrotesque = FontFamily(
    Font(R.font.bricolagegrotesque_variablefont_opszwdthwght, FontWeight.Normal)
)
val Gabriela = FontFamily(
    Font(R.font.gabriela_regular, FontWeight.Normal),
)
val Lato_regular = FontFamily(
    Font(R.font.lato_regular, FontWeight.Normal),
)
// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    titleLarge = TextStyle(
        fontFamily = Gabriela,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        lineHeight = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = Gabriela,
        fontWeight = FontWeight.Black,
        fontSize = 24.sp,
        lineHeight = 28.sp
    ),
    labelMedium = TextStyle(
        fontFamily = BricolageGrotesque,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 24.sp

    ),
    labelSmall = TextStyle(
        fontFamily = Lato_regular,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp

    )


)