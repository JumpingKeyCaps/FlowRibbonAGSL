package com.lebaillyapp.flowribbonagsl.flowRibbon

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlin.math.PI


@Composable
fun FlowRibbonDebug() {
    var showControls by remember { mutableStateOf(false) }
    var isAnimating by remember { mutableStateOf(true) }

    // Paramètres
    var colorHue by remember { mutableStateOf(335.46f) }
    var distortionStrength by remember { mutableStateOf(3.18f) }
    var timeSpeed by remember { mutableStateOf(1.24f) }
    var highlightIntensity by remember { mutableStateOf(0.51f) }
    var gamma by remember { mutableStateOf(1.82f) }
    var horizontalMix by remember { mutableStateOf(1.52f) }
    var microStrength by remember { mutableStateOf(0.0f) }
    var scale by remember { mutableStateOf(2.28f) }
    var rotation by remember { mutableStateOf(0f) }
    var animSpeed by remember { mutableStateOf(1f) }

    val color = Color.hsv(colorHue, 1f, 1f)




    Box(Modifier.fillMaxSize()) {

        FlowRibbonShader(
            modifier = Modifier.fillMaxSize(),
            color = color,
            distortionStrength = distortionStrength,
            highlightIntensity = highlightIntensity,
            gamma = gamma,
            horizontalMix = horizontalMix,
            microStrength = microStrength,
            autoAnimate = isAnimating,
            scaleOverride = scale,
            rotationOverride = rotation,
            timeSpeedOverride = timeSpeed,
            animSpeedMultiplicator = animSpeed
        )

        if (showControls) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.3f)),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(0.dp)
            ) {
                Column(Modifier.padding(8.dp)) {
                    ParameterSlider(
                        "Hue",
                        colorHue,
                        0f,
                        360f
                    ) { colorHue = it }
                   ParameterSlider(
                        "Distortion",
                        distortionStrength,
                        0f,
                        10f
                    ) { distortionStrength = it }
                    ParameterSlider(
                        "Anim Speed",
                        animSpeed,
                        0.0f,
                        5f
                    ) { animSpeed = it }
                   ParameterSlider(
                        "Highlight",
                        highlightIntensity,
                        0f,
                        1f
                    ) { highlightIntensity = it }
                    ParameterSlider(
                        "Gamma",
                        gamma,
                        0.1f,
                        2f
                    ) { gamma = it }
                    ParameterSlider(
                        "Horizontal Mix",
                        horizontalMix,
                        0f,
                        10f
                    ) { horizontalMix = it }
                    ParameterSlider(
                        "Micro Strength",
                        microStrength,
                        0f,
                        1f
                    ) { microStrength = it }
                    ParameterSlider(
                        "Scale",
                        scale,
                        0.1f,
                        10f
                    ) { scale = it }
                    ParameterSlider(
                        "Rotation",
                        rotation,
                        0.00f,
                        10.0f
                    ) { rotation = it }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { isAnimating = !isAnimating }) {
                        Text(if (isAnimating) "Stop Animation" else "Resume Animation")
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { showControls = !showControls },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(if (showControls) "▲" else "▼")
        }
    }
}





@Composable
fun ParameterSlider(label: String, value: Float, min: Float, max: Float, onValueChange: (Float) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text("$label: ${"%.2f".format(value)}", color = Color.White)
        Slider(value = value, onValueChange = onValueChange, valueRange = min..max)
    }
}