package com.fzm.newchatpro.home

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import com.fzm.newchatpro.R
import com.fzm.newchatpro.ui.theme.ChatColor
import com.google.accompanist.insets.ui.BottomNavigation
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator

/**
 * @author zhengjy
 * @since 2022/10/21
 * Description:
 */
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun Home() {
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val navController = rememberAnimatedNavController(bottomSheetNavigator)

    Scaffold(
        bottomBar = {
            val currentSelectedItem by navController.currentScreenAsState()
            HomeBottomNavigation(
                selectedNavigation = currentSelectedItem,
                onNavigationSelected = { selected ->
                    navController.navigate(selected.route) {
                        launchSingleTop = true
                        restoreState = true

                        popUpTo(currentSelectedItem.route) {
                            inclusive = true
                            saveState = true
                        }
                    }
                },
            )
        }
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ModalBottomSheetLayout(bottomSheetNavigator) {
                AppNavigation(
                    navController = navController,
                    onOpenSettings = { },
                    modifier = Modifier
                )
            }
            val context = LocalContext.current
            BackHandler(onBack = {
                context.startActivity(
                    Intent()
                        .setAction(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_HOME)
                )
            })
        }
    }
}

/**
 * Adds an [NavController.OnDestinationChangedListener] to this [NavController] and updates the
 * returned [State] which is updated as the destination changes.
 */
@Stable
@Composable
private fun NavController.currentScreenAsState(): State<Screen> {
    val selectedItem = remember { mutableStateOf<Screen>(Screen.Session) }

    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                destination.hierarchy.any { it.route == Screen.Session.route } -> {
                    selectedItem.value = Screen.Session
                }
                destination.hierarchy.any { it.route == Screen.AddressBook.route } -> {
                    selectedItem.value = Screen.AddressBook
                }
                destination.hierarchy.any { it.route == Screen.Wallet.route } -> {
                    selectedItem.value = Screen.Wallet
                }
                destination.hierarchy.any { it.route == Screen.Mine.route } -> {
                    selectedItem.value = Screen.Mine
                }
            }
        }
        addOnDestinationChangedListener(listener)

        onDispose {
            removeOnDestinationChangedListener(listener)
        }
    }

    return selectedItem
}

@Composable
internal fun HomeBottomNavigation(
    selectedNavigation: Screen,
    onNavigationSelected: (Screen) -> Unit,
    modifier: Modifier = Modifier
) {
    PaddingAndHeightBottomNavigation(
        height = 50.dp,
        backgroundColor = MaterialTheme.colors.surface/*.copy(alpha = AppBarAlphas.translucentBarAlpha())*/,
        contentPadding = WindowInsets.navigationBars.asPaddingValues(),
        modifier = modifier.fillMaxWidth(),
    ) {
        HomeNavigationItems.forEach { item ->
            val selected = selectedNavigation == item.screen
            BottomNavigationItem(
                icon = {
                    HomeNavigationItemIcon(
                        item = item,
                        selected = selected
                    )
                },
                label = {
                    Text(
                        text = item.displayLabel(),
                        style = TextStyle(fontSize = 12.sp),
                        color = if (selected) ChatColor.BlueAccent else ChatColor.TextGrey
                    )
                },
                selected = selected,
                onClick = { onNavigationSelected(item.screen) }
            )
        }
    }
}

@Composable
private fun HomeNavigationItemIcon(item: HomeNavigationItem, selected: Boolean) {
    val painter = when (item) {
        is HomeNavigationItem.ResourceIcon -> painterResource(item.iconResId)
        is HomeNavigationItem.ImageVectorIcon -> rememberVectorPainter(item.iconImageVector)
    }
    val selectedPainter = when (item) {
        is HomeNavigationItem.ResourceIcon -> item.selectedIconResId?.let { painterResource(it) }
        is HomeNavigationItem.ImageVectorIcon -> item.selectedImageVector?.let { rememberVectorPainter(it) }
    }

    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                modifier = Modifier.size(26.dp),
                painter = if (it) selectedPainter else painter,
                contentDescription = item.displayDescription(),
                tint = if (it) ChatColor.BlueAccent else ChatColor.TextGrey
            )
        }
    } else {
        Icon(
            modifier = Modifier.size(26.dp),
            painter = painter,
            contentDescription = item.displayDescription()
        )
    }
}

private sealed class HomeNavigationItem(
    val screen: Screen,
    private val label: String,
    @StringRes private val labelResId: Int,
    private val contentDescription: String,
    @StringRes private val contentDescriptionResId: Int
) {
    class ResourceIcon(
        screen: Screen,
        label: String,
        @StringRes labelResId: Int = 0,
        contentDescription: String = "",
        @StringRes contentDescriptionResId: Int = 0,
        @DrawableRes val iconResId: Int,
        @DrawableRes val selectedIconResId: Int? = null
    ) : HomeNavigationItem(screen, label, labelResId, contentDescription, contentDescriptionResId)

    class ImageVectorIcon(
        screen: Screen,
        label: String,
        @StringRes labelResId: Int = 0,
        contentDescription: String = "",
        @StringRes contentDescriptionResId: Int = 0,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null
    ) : HomeNavigationItem(screen, label, labelResId, contentDescription, contentDescriptionResId)

    @Composable
    fun displayLabel(): String {
        return if (labelResId != 0) {
            stringResource(id = labelResId)
        } else {
            label
        }
    }

    @Composable
    fun displayDescription(): String {
        return if (contentDescriptionResId != 0) {
            stringResource(id = contentDescriptionResId)
        } else {
            contentDescription
        }
    }
}

private val HomeNavigationItems = listOf(
    HomeNavigationItem.ResourceIcon(
        screen = Screen.Session,
        label = "消息",
        contentDescription = "",
        iconResId = R.drawable.ic_main_session_unselected,
        selectedIconResId = R.drawable.ic_main_session
    ),
    HomeNavigationItem.ResourceIcon(
        screen = Screen.AddressBook,
        label = "通讯录",
        contentDescription = "",
        iconResId = R.drawable.ic_main_contact_unselected,
        selectedIconResId = R.drawable.ic_main_contact
    ),
    HomeNavigationItem.ResourceIcon(
        screen = Screen.Wallet,
        label = "票券",
        contentDescription = "",
        iconResId = R.drawable.ic_main_wallet_unselected,
        selectedIconResId = R.drawable.ic_main_wallet
    ),
    HomeNavigationItem.ResourceIcon(
        screen = Screen.Mine,
        label = "我的",
        contentDescription = "",
        iconResId = R.drawable.ic_main_account_unselected,
        selectedIconResId = R.drawable.ic_main_account
    )
)