

package iad1tya.echo.music.ui.screens.settings

import iad1tya.echo.music.R
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.navigation.NavController
import iad1tya.echo.music.BuildConfig
import iad1tya.echo.music.LocalPlayerAwareWindowInsets
import iad1tya.echo.music.constants.UserNameKey
import iad1tya.echo.music.ui.component.DefaultDialog
import iad1tya.echo.music.ui.component.IconButton
import iad1tya.echo.music.ui.component.Material3SettingsGroup
import iad1tya.echo.music.ui.component.Material3SettingsItem
import iad1tya.echo.music.ui.screens.Screens
import iad1tya.echo.music.ui.utils.backToMain
import iad1tya.echo.music.echomusic.updater.getUpdateAvailableState
import iad1tya.echo.music.utils.rememberPreference


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    scrollBehavior: TopAppBarScrollBehavior,
) {
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current
    val isAndroid12OrLater = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
    val isUpdateAvailable = getUpdateAvailableState(context) && iad1tya.echo.music.echomusic.updater.getAutoUpdateCheckSetting(context)

    val accountText = stringResource(R.string.account)
    val appearanceText = stringResource(R.string.appearance)
    val playerText = stringResource(R.string.player_and_audio)
    val listenTogetherText = stringResource(R.string.listen_together)
    val contentText = stringResource(R.string.content)
    val aiLyricsText = stringResource(R.string.ai_lyrics_translation)
    val privacyText = stringResource(R.string.privacy)
    val storageText = stringResource(R.string.storage)
    val backupText = stringResource(R.string.backup_restore)
    val aboutText = stringResource(R.string.about)

    val scrollState = rememberScrollState()

    var userName by rememberPreference(UserNameKey, "")
    var showNameDialog by remember { mutableStateOf(false) }

    if (showNameDialog) {
        var tempName by remember { mutableStateOf(userName) }
        DefaultDialog(
            onDismiss = { showNameDialog = false },
            icon = { Icon(painterResource(R.drawable.person), contentDescription = null) },
            title = { Text("Edit Name") },
            buttons = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text(stringResource(android.R.string.cancel))
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { 
                    userName = tempName.trim()
                    showNameDialog = false 
                }) {
                    Text(stringResource(android.R.string.ok))
                }
            }
        ) {
            OutlinedTextField(
                value = tempName,
                onValueChange = { tempName = it },
                label = { Text("Display Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    Column(
        Modifier
            .windowInsetsPadding(LocalPlayerAwareWindowInsets.current.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom))
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(
            Modifier.windowInsetsPadding(
                LocalPlayerAwareWindowInsets.current.only(
                    WindowInsetsSides.Top
                )
            )
        )
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 16.dp)
        )

        val itemsList = listOf(
            Material3SettingsItem(
                icon = painterResource(R.drawable.person),
                title = { Text("Name") },
                description = { Text(if (userName.isBlank()) "Not set" else userName) },
                onClick = { showNameDialog = true }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.account),
                title = { Text(accountText) },
                onClick = { navController.navigate("settings/account") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.palette),
                title = { Text(appearanceText) },
                onClick = { navController.navigate("settings/appearance") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.play),
                title = { Text(playerText) },
                onClick = { navController.navigate("settings/player") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.group),
                title = { Text(listenTogetherText) },
                onClick = { navController.navigate(Screens.ListenTogether.route) }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.language),
                title = { Text(contentText) },
                onClick = { navController.navigate("settings/content") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.translate),
                title = { Text(aiLyricsText) },
                onClick = { navController.navigate("settings/ai") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.security),
                title = { Text(privacyText) },
                onClick = { navController.navigate("settings/privacy") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.storage),
                title = { Text(storageText) },
                onClick = { navController.navigate("settings/storage") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.restore),
                title = { Text(backupText) },
                onClick = { navController.navigate("settings/backup_restore") }
            ),
            Material3SettingsItem(
                icon = painterResource(R.drawable.info),
                title = { Text(aboutText) },
                onClick = { navController.navigate("settings/about") }
            )
        )

        Material3SettingsGroup(items = itemsList)
        
        Spacer(modifier = Modifier.height(50.dp))
    }

    TopAppBar(
        title = {
            androidx.compose.animation.AnimatedVisibility(
                visible = scrollState.value > 100,
                enter = androidx.compose.animation.fadeIn(),
                exit = androidx.compose.animation.fadeOut()
            ) {
                Text(
                    text = stringResource(R.string.settings),
                    style = MaterialTheme.typography.titleLarge
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = navController::navigateUp,
                onLongClick = navController::backToMain
            ) {
                Icon(
                    painterResource(R.drawable.arrow_back),
                    contentDescription = null
                )
            }
        }
    )
}
