package com.fzm.newchatpro.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.BottomNavigationDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.ui.BottomNavigationSurface

/**
 * @author zhengjy
 * @since 2022/10/21
 * Description:
 */
private val BottomNavigationHeight = 56.dp

/**
 * 自定义BottomNavigation的高度时，如果想要支持[WindowInsets.Companion.navigationBars]，那么高度一定要在设置完[contentPadding]之
 * 后设置，否则BottomNavigation的整体高度会被影响（如果就是要这样的效果，那么直接通过[modifier]设置高度来覆盖[Row]的高度）
 *
 */
@Composable
fun PaddingAndHeightBottomNavigation(
    modifier: Modifier = Modifier,
    height: Dp = BottomNavigationHeight,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    backgroundColor: Color = MaterialTheme.colors.primarySurface,
    contentColor: Color = contentColorFor(backgroundColor),
    elevation: Dp = BottomNavigationDefaults.Elevation,
    content: @Composable RowScope.() -> Unit,
) {
    BottomNavigationSurface(modifier, backgroundColor, contentColor, elevation) {
        Row(
            modifier = modifier
                .padding(contentPadding)
                .fillMaxWidth()
                .height(height)
                .selectableGroup(),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = content,
        )
    }
}