package com.lebaillyapp.flowribbonagsl.flowRibbon

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RuntimeShader
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameMillis
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import com.lebaillyapp.flowribbonagsl.R
import kotlinx.coroutines.isActive
import kotlin.math.PI

@Composable
fun FlowRibbonTextWithOffset(
    text: String,
    modifier: Modifier = Modifier,
    fontSize: Float = 180f,
    fontResId: Int = R.font.sarina,
    waveColor: Color = Color(0xFFFC6380),
    backgroundColor: Color = Color(0xFF00E676),
    overlayColor: Color = Color.White,
    overlayOffset: Offset = Offset(4f, 4f),
    distortionStrength: Float = 0.18f,
    highlightIntensity: Float = 0.43f,
    gamma: Float = 0.6f,
    horizontalMix: Float = 3.72f,
    microStrength: Float = 0.0f,
    autoAnimate: Boolean = true,
    scaleOverride: Float = 0.5f,
    rotationOverride: Float = 0f,
    timeSpeedOverride: Float = 1.24f
) {
    val context = LocalContext.current
    val shaderSource = remember {
        context.resources.openRawResource(R.raw.text_flow_ribbon_shader)
            .bufferedReader().use { it.readText() }
    }
    val shader = remember(shaderSource) { RuntimeShader(shaderSource) }

    var time by remember { mutableStateOf(0f) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }

    val customTypeface = remember {
        ResourcesCompat.getFont(context, fontResId) ?: Typeface.DEFAULT
    }

    LaunchedEffect(autoAnimate) {
        val start = System.nanoTime()
        while (isActive) {
            withFrameMillis {
                if (autoAnimate) {
                    time = ((System.nanoTime() - start) / 1_000_000_000f) % 1000f
                }
            }
        }
    }

    Canvas(
        modifier
            .fillMaxSize()
            .onSizeChanged { canvasSize = Size(it.width.toFloat(), it.height.toFloat()) }
    ) {
        val w = canvasSize.width
        val h = canvasSize.height
        if (w == 0f || h == 0f) return@Canvas

        // 1. DESSINER LE TEXTE SHADERISÉ (en dessous)
        val textBitmap = createTextBitmap(
            text = text,
            width = w.toInt(),
            height = h.toInt(),
            typeface = customTypeface,
            fontSize = fontSize
        )

        val textShader = android.graphics.BitmapShader(
            textBitmap,
            android.graphics.Shader.TileMode.CLAMP,
            android.graphics.Shader.TileMode.CLAMP
        )

        val animatedRotation = if (autoAnimate) (time * 0.3f) % (2 * PI.toFloat()) else rotationOverride
        val animatedScale = scaleOverride

        shader.apply {
            setInputShader("u_TextMask", textShader)
            setColorUniform("u_WaveColor", waveColor.toArgb())
            setColorUniform("u_BackgroundColor", backgroundColor.toArgb())
            setFloatUniform("u_Time", time)
            setFloatUniform("u_DistortionStrength", distortionStrength)
            setFloatUniform("u_TimeSpeed", timeSpeedOverride)
            setFloatUniform("u_HighlightIntensity", highlightIntensity)
            setFloatUniform("u_Gamma", gamma)
            setFloatUniform("u_HorizontalMix", horizontalMix)
            setFloatUniform("u_MicroStrength", microStrength)
            setFloatUniform("u_Scale", animatedScale)
            setFloatUniform("u_Rotation", animatedRotation)
            setFloatUniform("u_Resolution", floatArrayOf(w, h))
        }

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                this.shader = shader
            }
            canvas.nativeCanvas.drawRect(0f, 0f, w, h, paint)
        }

        // 2. DESSINER LE TEXTE OVERLAY PAR-DESSUS (avec offset)
        drawIntoCanvas { canvas ->
            val overlayPaint = android.graphics.Paint().apply {
                typeface = customTypeface
                textSize = fontSize // ← MÊME taille que le shader
                color = overlayColor.toArgb()
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.CENTER
            }

            val textHeight = overlayPaint.descent() - overlayPaint.ascent()
            val textOffset = (textHeight / 2) - overlayPaint.descent()

            // Position centrale + offset
            canvas.nativeCanvas.drawText(
                text,
                w / 2f + overlayOffset.x, // ← Décalage X
                h / 2f + textOffset + overlayOffset.y, // ← Décalage Y
                overlayPaint
            )
        }
    }
}

// Fonction helper pour créer le bitmap du texte
private fun createTextBitmap(
    text: String,
    width: Int,
    height: Int,
    typeface: Typeface,
    fontSize: Float
): Bitmap {
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        this.typeface = typeface
        textSize = fontSize
        color = android.graphics.Color.WHITE
        isAntiAlias = true
        textAlign = Paint.Align.CENTER
    }

    val textHeight = paint.descent() - paint.ascent()
    val textOffset = (textHeight / 2) - paint.descent()

    canvas.drawText(
        text,
        width / 2f,
        height / 2f + textOffset,
        paint
    )

    return bitmap
}

@Composable
fun DemoFlowRibbonText() {
    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        FlowRibbonTextWithOffset(
            modifier = Modifier.align(Alignment.Center),
            text = "Candy",
            fontSize = 190f,
            fontResId = R.font.sarina,
            waveColor = Color(0xFFFF498A),
            backgroundColor = Color(0xFF00E5FF),
            overlayColor = Color(0xFFFFFFFF),
            overlayOffset = Offset(-5f, -14f),
            autoAnimate = true,
            distortionStrength = 3.18f,
            horizontalMix = 2.72f,
            scaleOverride = 0.7f

        )
    }
}