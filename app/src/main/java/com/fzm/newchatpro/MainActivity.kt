package com.fzm.newchatpro

import android.app.Activity
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.core.view.WindowCompat
import com.fzm.newchatpro.home.Home
import com.fzm.newchatpro.ui.theme.NewChatProTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            window?.attributes = window?.attributes?.apply {
                layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            }
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)

        window?.statusBarColor = Color.TRANSPARENT
        window?.navigationBarColor = Color.TRANSPARENT

        setContent {
            ChatProContent(setupCustomDensity(this, 360))
        }
    }

    @Composable
    private fun ChatProContent(density: Density = LocalDensity.current) {
        CompositionLocalProvider(
            LocalDensity provides density
        ) {
            NewChatProTheme {
                Home()
            }
        }
    }
}

@Composable
fun setupCustomDensity(activity: Activity, dp: Int): Density {
    if (dp == 0) return LocalDensity.current
    val applicationMetrics = Resources.getSystem().displayMetrics
    val targetDensity: Float = applicationMetrics.widthPixels.toFloat() / dp
    val targetDensityDpi = 160 * targetDensity

    applicationMetrics.density = targetDensity
    applicationMetrics.scaledDensity = targetDensity
    applicationMetrics.densityDpi = targetDensityDpi.toInt()

    val activityMetrics = activity.resources.displayMetrics
    activityMetrics.density = targetDensity
    activityMetrics.scaledDensity = targetDensity
    activityMetrics.densityDpi = targetDensityDpi.toInt()

    return Density(targetDensity)
}