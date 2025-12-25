package com.proyect.ocean_words.view.screens

import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
class FishShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {

        val w = size.width
        val h = size.height

        val path = Path().apply {

            // 🟦 Cuerpo MUY ancho
            moveTo(w * 0.10f, h * 0.15f)

            cubicTo(
                w * 1.20f, h * 0.10f,   // frente largo
                w * 1.20f, h * 0.90f,
                w * 0.10f, h * 0.85f
            )

            cubicTo(
                w * 0.02f, h * 0.65f,
                w * 0.02f, h * 0.35f,
                w * 0.10f, h * 0.15f
            )

            // 🟨 Cola grande pero compacta
            moveTo(w * 0.10f, h * 0.35f)
            lineTo(0f, h * 0.50f)
            lineTo(w * 0.10f, h * 0.65f)
            close()
        }

        return Outline.Generic(path)
    }
}
