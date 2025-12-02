package com.lebaillyapp.flowribbonagsl

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.lebaillyapp.flowribbonagsl.flowRibbon.DemoFlowRibbonText

import com.lebaillyapp.flowribbonagsl.ui.theme.FlowRibbonAGSLTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlowRibbonAGSLTheme {
                //debug
               // FlowRibbonDebug()

                // real one !
                /**

                FlowRibbonShader(
                modifier = Modifier.fillMaxSize()
                .scale(1.0f)
                .alpha(1.0f),
                color = Color.hsv(71.31f, 1f, 1f),
                distortionStrength = 3.18f,
                highlightIntensity = 0.33f,
                gamma = 1.86f,
                horizontalMix = 0.72f,
                microStrength = 0f,
                autoAnimate = true,
                scaleOverride = 1.85f,
                rotationOverride = 0f,
                timeSpeedOverride = 1.24f
                )
                 */

                //demo on text
                DemoFlowRibbonText()

            }
        }
    }
}
