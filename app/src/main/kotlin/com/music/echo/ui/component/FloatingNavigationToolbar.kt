
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package iad1tya.echo.music.ui.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import iad1tya.echo.music.R
import iad1tya.echo.music.ui.screens.Screens

@Composable
fun FloatingNavigationToolbar(
    items: List<Screens>,
    pureBlack: Boolean,
    modifier: Modifier = Modifier,
    onDownloadedClick: () -> Unit,
    isSelected: (Screens) -> Boolean,
    onItemClick: (Screens, Boolean) -> Unit,
) {
    // Material 3 Dark Glass Colors
    val containerColor = if (pureBlack) {
        Color.Black.copy(alpha = 0.9f)
    } else {
        Color(0xFF1C1B1F).copy(alpha = 0.85f)
    }
    
    val borderColor = Color.White.copy(alpha = 0.1f)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .widthIn(max = 340.dp)
                .height(64.dp)
                .border(0.5.dp, borderColor, CircleShape),
            shape = CircleShape,
            color = containerColor,
            tonalElevation = 0.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Home and Search
                items.take(2).forEach { screen ->
                    val selected = isSelected(screen)
                    NavIconItem(
                        screen = screen,
                        selected = selected,
                        onClick = { onItemClick(screen, selected) }
                    )
                }

                // Downloaded Icon (Replaces Overflow)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .clickable(
                            onClick = onDownloadedClick
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.download),
                        contentDescription = "Downloaded",
                        tint = Color(0xFF938F99),
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Library and Account
                items.drop(2).forEach { screen ->
                    val selected = isSelected(screen)
                    NavIconItem(
                        screen = screen,
                        selected = selected,
                        onClick = { onItemClick(screen, selected) }
                    )
                }
            }
        }
    }
}

@Composable
private fun NavIconItem(
    screen: Screens,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "iconScale"
    )

    // Match the screenshot design: circular indicator for selected tab
    val containerColor = if (selected) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)
    } else {
        Color.Transparent
    }

    val iconColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        Color(0xFF938F99)
    }

    Box(
        modifier = modifier
            .size(54.dp) // Covers most of the pill's height
            .scale(scale)
            .clip(CircleShape)
            .background(containerColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(if (selected) screen.iconIdActive else screen.iconIdInactive),
            contentDescription = stringResource(screen.titleId),
            tint = iconColor,
            modifier = Modifier.size(28.dp) // Slightly larger icons like in the screenshot
        )
    }
}
