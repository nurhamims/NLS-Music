
@file:OptIn(ExperimentalMaterial3ExpressiveApi::class)

package iad1tya.echo.music.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
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
    onShuffleClick: (() -> Unit)? = null,
    shuffleIconRes: Int? = null,
    onMusicRecognitionClick: (() -> Unit)? = null,
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

                // 3 Dot (Overflow)
                OverflowMenuButton(
                    pureBlack = pureBlack,
                    onShuffleClick = onShuffleClick,
                    shuffleIconRes = shuffleIconRes,
                    onMusicRecognitionClick = onMusicRecognitionClick
                )

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
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "iconScale"
    )

    val iconColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        Color(0xFF938F99)
    }

    Box(
        modifier = Modifier
            .size(56.dp)
            .scale(scale)
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            // Soft glow underneath
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f), CircleShape)
                    .blur(14.dp)
            )
            // Frosted glass orb
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.45f),
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                            )
                        ),
                        shape = CircleShape
                    )
                    .border(0.5.dp, Color.White.copy(alpha = 0.2f), CircleShape)
            )
        }

        Icon(
            painter = painterResource(if (selected) screen.iconIdActive else screen.iconIdInactive),
            contentDescription = stringResource(screen.titleId),
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun OverflowMenuButton(
    pureBlack: Boolean,
    onShuffleClick: (() -> Unit)?,
    shuffleIconRes: Int?,
    onMusicRecognitionClick: (() -> Unit)?,
) {
    var menuExpanded by rememberSaveable { mutableStateOf(false) }
    
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "overflowScale"
    )

    Box(contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .scale(scale)
                .clip(CircleShape)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = { menuExpanded = true }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(R.drawable.more_horiz),
                contentDescription = "More",
                tint = Color(0xFF938F99),
                modifier = Modifier.size(26.dp)
            )
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false },
            shape = RoundedCornerShape(24.dp),
            containerColor = if (pureBlack) Color.Black.copy(alpha = 0.9f) else Color.White.copy(alpha = 0.95f),
            tonalElevation = 8.dp,
        ) {
            if (onShuffleClick != null && shuffleIconRes != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.shuffle)) },
                    onClick = {
                        menuExpanded = false
                        onShuffleClick()
                    },
                    leadingIcon = {
                        Icon(painterResource(shuffleIconRes), contentDescription = null)
                    }
                )
            }

            if (onMusicRecognitionClick != null) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.recognition)) },
                    onClick = {
                        menuExpanded = false
                        onMusicRecognitionClick()
                    },
                    leadingIcon = {
                        Icon(painterResource(R.drawable.mic), contentDescription = null)
                    }
                )
            }
        }
    }
}
