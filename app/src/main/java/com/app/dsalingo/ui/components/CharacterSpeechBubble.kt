package com.app.dsalingo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*

enum class BubblePointerDirection {
    DOWN, LEFT, RIGHT, UP
}

val PointerDownShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width / 2f, size.height)
    close()
}

val PointerLeftShape = GenericShape { size, _ ->
    moveTo(size.width, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height / 2f)
    close()
}

val PointerRightShape = GenericShape { size, _ ->
    moveTo(0f, 0f)
    lineTo(size.width, size.height / 2f)
    lineTo(0f, size.height)
    close()
}

val PointerUpShape = GenericShape { size, _ ->
    moveTo(size.width / 2f, 0f)
    lineTo(size.width, size.height)
    lineTo(0f, size.height)
    close()
}

/**
 * Reusable Duolingo-style speech bubble with pointer arrow,
 * optional title, optional action button, and customizable pointer orientation.
 */
@Composable
fun CharacterSpeechBubble(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    actionText: String? = null,
    onActionClick: (() -> Unit)? = null,
    pointerDirection: BubblePointerDirection = BubblePointerDirection.DOWN,
    backgroundColor: Color = DuoCardBg,
    borderColor: Color = DuoInputBorder,
    textColor: Color = Color.White,
    elevation: Dp = 6.dp,
    content: (@Composable () -> Unit)? = null
) {
    when (pointerDirection) {
        BubblePointerDirection.DOWN -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BubbleCard(
                    message = message,
                    title = title,
                    actionText = actionText,
                    onActionClick = onActionClick,
                    backgroundColor = backgroundColor,
                    borderColor = borderColor,
                    textColor = textColor,
                    elevation = elevation,
                    content = content
                )
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 8.dp)
                        .background(backgroundColor, PointerDownShape)
                )
            }
        }
        BubblePointerDirection.UP -> {
            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 8.dp)
                        .background(backgroundColor, PointerUpShape)
                )
                BubbleCard(
                    message = message,
                    title = title,
                    actionText = actionText,
                    onActionClick = onActionClick,
                    backgroundColor = backgroundColor,
                    borderColor = borderColor,
                    textColor = textColor,
                    elevation = elevation,
                    content = content
                )
            }
        }
        BubblePointerDirection.LEFT -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(width = 8.dp, height = 16.dp)
                        .background(backgroundColor, PointerLeftShape)
                )
                BubbleCard(
                    message = message,
                    title = title,
                    actionText = actionText,
                    onActionClick = onActionClick,
                    backgroundColor = backgroundColor,
                    borderColor = borderColor,
                    textColor = textColor,
                    elevation = elevation,
                    content = content
                )
            }
        }
        BubblePointerDirection.RIGHT -> {
            Row(
                modifier = modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BubbleCard(
                    message = message,
                    title = title,
                    actionText = actionText,
                    onActionClick = onActionClick,
                    backgroundColor = backgroundColor,
                    borderColor = borderColor,
                    textColor = textColor,
                    elevation = elevation,
                    content = content
                )
                Box(
                    modifier = Modifier
                        .size(width = 8.dp, height = 16.dp)
                        .background(backgroundColor, PointerRightShape)
                )
            }
        }
    }
}

@Composable
private fun BubbleCard(
    message: String,
    title: String?,
    actionText: String?,
    onActionClick: (() -> Unit)?,
    backgroundColor: Color,
    borderColor: Color,
    textColor: Color,
    elevation: Dp,
    content: (@Composable () -> Unit)?
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        shadowElevation = elevation,
        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!title.isNullOrBlank()) {
                Text(
                    text = title.uppercase(),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = DuoGreen,
                    letterSpacing = 0.5.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (content != null) {
                content()
            } else {
                Text(
                    text = message,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = textColor,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
            }

            if (!actionText.isNullOrBlank() && onActionClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onActionClick() },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    shadowElevation = 3.dp
                ) {
                    Text(
                        text = actionText.uppercase(),
                        color = DuoDarkBg,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 14.dp)
                    )
                }
            }
        }
    }
}
