package com.app.dsalingo.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * State container for the interactive Array visualizer
 */
class ArrayVisualizerState(
    initialElements: List<String> = listOf("10", "20", "30", "40")
) {
    var elements by mutableStateOf(initialElements)
    var initialSnapshot by mutableStateOf(initialElements)
    var highlightedIndex by mutableStateOf<Int?>(null)
    var secondaryHighlightedIndex by mutableStateOf<Int?>(null)
    var activeSearchIndex by mutableStateOf<Int?>(null)
    var statusMessage by mutableStateOf<String?>(null)
    var isOperating by mutableStateOf(false)

    fun reset() {
        elements = initialSnapshot
        highlightedIndex = null
        secondaryHighlightedIndex = null
        activeSearchIndex = null
        statusMessage = "Array reset to initial state"
    }

    suspend fun access(index: Int): String? {
        if (index !in elements.indices) {
            statusMessage = "IndexOutOfBounds: $index is out of range [0..${elements.size - 1}]"
            return null
        }
        isOperating = true
        activeSearchIndex = null
        highlightedIndex = index
        val value = elements[index]
        statusMessage = "Accessing arr[$index] ➜ Value: $value in O(1) time"
        delay(600)
        isOperating = false
        return value
    }

    suspend fun update(index: Int, newValue: String) {
        if (index !in elements.indices) {
            statusMessage = "IndexOutOfBounds: $index"
            return
        }
        isOperating = true
        highlightedIndex = index
        statusMessage = "Updating arr[$index] from '${elements[index]}' to '$newValue'"
        delay(400)
        val list = elements.toMutableList()
        list[index] = newValue
        elements = list
        delay(400)
        isOperating = false
    }

    suspend fun insert(index: Int, value: String) {
        val targetIdx = index.coerceIn(0, elements.size)
        isOperating = true
        highlightedIndex = targetIdx
        statusMessage = "Inserting '$value' at index $targetIdx. Shifting subsequent elements right..."
        delay(400)
        val list = elements.toMutableList()
        list.add(targetIdx, value)
        elements = list
        delay(400)
        statusMessage = "Inserted '$value' at index $targetIdx in O(N) time"
        isOperating = false
    }

    suspend fun delete(index: Int) {
        if (index !in elements.indices) {
            statusMessage = "Index $index is out of bounds for deletion"
            return
        }
        isOperating = true
        highlightedIndex = index
        val deletedVal = elements[index]
        statusMessage = "Deleting '$deletedVal' at index $index. Elements shifting left..."
        delay(400)
        val list = elements.toMutableList()
        list.removeAt(index)
        elements = list
        highlightedIndex = null
        delay(300)
        statusMessage = "Deleted '$deletedVal' in O(N) time"
        isOperating = false
    }

    suspend fun append(value: String) {
        isOperating = true
        val targetIndex = elements.size
        highlightedIndex = targetIndex
        statusMessage = "Appending '$value' to the end of the array..."
        delay(300)
        elements = elements + value
        delay(300)
        statusMessage = "Appended '$value' at index $targetIndex in O(1) amortized time"
        isOperating = false
    }

    suspend fun search(target: String): Int {
        isOperating = true
        statusMessage = "Linear searching for target '$target' starting from index 0..."
        highlightedIndex = null
        for (i in elements.indices) {
            activeSearchIndex = i
            delay(400)
            if (elements[i].equals(target, ignoreCase = true)) {
                highlightedIndex = i
                activeSearchIndex = null
                statusMessage = "Target '$target' FOUND at index $i! (Scanned ${i + 1} steps)"
                isOperating = false
                return i
            }
        }
        activeSearchIndex = null
        statusMessage = "Target '$target' NOT FOUND in array after scanning ${elements.size} elements."
        isOperating = false
        return -1
    }

    suspend fun swap(idx1: Int, idx2: Int) {
        if (idx1 !in elements.indices || idx2 !in elements.indices) return
        isOperating = true
        highlightedIndex = idx1
        secondaryHighlightedIndex = idx2
        statusMessage = "Swapping arr[$idx1] ('${elements[idx1]}') with arr[$idx2] ('${elements[idx2]}')"
        delay(500)
        val list = elements.toMutableList()
        val temp = list[idx1]
        list[idx1] = list[idx2]
        list[idx2] = temp
        elements = list
        delay(400)
        highlightedIndex = null
        secondaryHighlightedIndex = null
        isOperating = false
    }
}

@Composable
fun rememberArrayVisualizerState(initialElements: List<String>): ArrayVisualizerState {
    return remember(initialElements) { ArrayVisualizerState(initialElements) }
}

/**
 * Reusable Jetpack Compose ArrayVisualizer component
 */
@Composable
fun ArrayVisualizer(
    state: ArrayVisualizerState,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    showControls: Boolean = true,
    teacherComment: String? = null,
    teacherEmotion: CharacterEmotion = CharacterEmotion.THINKING,
    onElementClick: ((index: Int, value: String) -> Unit)? = null,
    onArrayStateChanged: ((List<String>) -> Unit)? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var showOperationDialog by remember { mutableStateOf<String?>(null) }
    var inputIndex by remember { mutableStateOf("") }
    var inputValue by remember { mutableStateOf("") }

    LaunchedEffect(state.elements) {
        onArrayStateChanged?.invoke(state.elements)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        // Teacher mascot guidance above visualizer if provided
        if (!teacherComment.isNullOrBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                DuoCharacter(
                    emotion = teacherEmotion,
                    size = 56.dp,
                    showAura = false
                )
                Spacer(modifier = Modifier.width(10.dp))
                CharacterSpeechBubble(
                    message = teacherComment,
                    pointerDirection = BubblePointerDirection.LEFT,
                    backgroundColor = DuoCardBg,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp)),
            color = Color(0xFF131F24),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder),
            shadowElevation = 4.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
            // Header Bar: Memory & Capacity Metadata
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(DuoGreen)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "CONTIGUOUS MEMORY",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = DuoGreen,
                        letterSpacing = 0.5.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "SIZE: ${state.elements.size}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuoSubtext,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Visualizer Scrollable Track
            val scrollState = rememberScrollState()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.elements.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[ Empty Array ]",
                            color = DuoSubtext,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    state.elements.forEachIndexed { index, value ->
                        val isHighlighted = state.highlightedIndex == index || state.secondaryHighlightedIndex == index
                        val isSearching = state.activeSearchIndex == index

                        ArrayCell(
                            index = index,
                            value = value,
                            isHighlighted = isHighlighted,
                            isSearching = isSearching,
                            onClick = {
                                if (isInteractive && !state.isOperating) {
                                    onElementClick?.invoke(index, value)
                                }
                            }
                        )
                    }
                }
            }

            // Status Message or Operation Feedback Banner
            state.statusMessage?.let { msg ->
                Spacer(modifier = Modifier.height(12.dp))
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = DuoCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("💡", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            // Interactive Operation Buttons (Optional toolbar)
            if (showControls) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    VisualizerMiniButton("Access", DuoBlue, Modifier.weight(1f)) {
                        showOperationDialog = "ACCESS"
                        inputIndex = "0"
                    }
                    VisualizerMiniButton("Update", AmberGold, Modifier.weight(1f)) {
                        showOperationDialog = "UPDATE"
                        inputIndex = "0"
                        inputValue = "99"
                    }
                    VisualizerMiniButton("Insert", DuoGreen, Modifier.weight(1f)) {
                        showOperationDialog = "INSERT"
                        inputIndex = "1"
                        inputValue = "25"
                    }
                    VisualizerMiniButton("Delete", DuoRed, Modifier.weight(1f)) {
                        showOperationDialog = "DELETE"
                        inputIndex = "0"
                    }
                    VisualizerMiniButton("Search", DuoPurple, Modifier.weight(1f)) {
                        showOperationDialog = "SEARCH"
                        inputValue = state.elements.firstOrNull() ?: ""
                    }
                    VisualizerMiniButton("Reset", DuoSubtext, Modifier.weight(1f)) {
                        state.reset()
                    }
                }
            }
        }
    }
}

    // Operation Input Dialog for direct manipulation
    if (showOperationDialog != null) {
        val op = showOperationDialog!!
        AlertDialog(
            containerColor = DuoCardBg,
            titleContentColor = Color.White,
            textContentColor = DuoSubtext,
            onDismissRequest = { showOperationDialog = null },
            title = { Text("$op Operation", fontWeight = FontWeight.Black) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (op in listOf("ACCESS", "UPDATE", "INSERT", "DELETE")) {
                        DuoTextField(
                            value = inputIndex,
                            onValueChange = { inputIndex = it },
                            placeholder = "Index (0 to ${state.elements.size})"
                        )
                    }
                    if (op in listOf("UPDATE", "INSERT", "SEARCH")) {
                        DuoTextField(
                            value = inputValue,
                            onValueChange = { inputValue = it },
                            placeholder = "Value (e.g. 99)"
                        )
                    }
                }
            },
            confirmButton = {
                DuoButton(
                    text = "EXECUTE",
                    onClick = {
                        val idx = inputIndex.toIntOrNull() ?: 0
                        showOperationDialog = null
                        coroutineScope.launch {
                            when (op) {
                                "ACCESS" -> state.access(idx)
                                "UPDATE" -> state.update(idx, inputValue)
                                "INSERT" -> state.insert(idx, inputValue)
                                "DELETE" -> state.delete(idx)
                                "SEARCH" -> state.search(inputValue)
                            }
                        }
                    },
                    height = 42.dp,
                    fontSize = 13
                )
            },
            dismissButton = {
                TextButton(onClick = { showOperationDialog = null }) {
                    Text("CANCEL", color = DuoSubtext, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/**
 * Single Animated Array Cell
 */
@Composable
fun ArrayCell(
    index: Int,
    value: String,
    isHighlighted: Boolean,
    isSearching: Boolean,
    onClick: () -> Unit
) {
    val scaleAnim by animateFloatAsState(
        targetValue = if (isHighlighted || isSearching) 1.08f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "cellScale"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isSearching -> DuoOrange
            isHighlighted -> DuoGreen
            else -> DuoInputBorder
        },
        animationSpec = tween(300),
        label = "cellBorder"
    )

    val bgColor by animateColorAsState(
        targetValue = when {
            isSearching -> DuoOrange.copy(alpha = 0.25f)
            isHighlighted -> DuoGreen.copy(alpha = 0.25f)
            else -> Color(0xFF1E2B32)
        },
        animationSpec = tween(300),
        label = "cellBg"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .scale(scaleAnim)
            .clickable { onClick() }
    ) {
        // Pointer / Scanner Arrow Indicator
        AnimatedVisibility(
            visible = isSearching || isHighlighted,
            enter = fadeIn() + slideInVertically { it },
            exit = fadeOut() + slideOutVertically { it }
        ) {
            Text(
                text = if (isSearching) "🔍" else "👇",
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }

        if (!isSearching && !isHighlighted) {
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Cell Box: ┌────┐ │ 10 │ └────┘
        Surface(
            modifier = Modifier
                .size(64.dp, 64.dp)
                .shadow(if (isHighlighted) 8.dp else 2.dp, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            color = bgColor,
            border = androidx.compose.foundation.BorderStroke(2.dp, borderColor)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                AnimatedContent(
                    targetState = value,
                    transitionSpec = {
                        (slideInVertically { height -> height } + fadeIn()).togetherWith(
                            slideOutVertically { height -> -height } + fadeOut()
                        )
                    },
                    label = "valAnim"
                ) { targetVal ->
                    Text(
                        text = targetVal,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Index Label underneath: 0, 1, 2, 3...
        Surface(
            shape = RoundedCornerShape(6.dp),
            color = if (isHighlighted) DuoGreen.copy(alpha = 0.2f) else Color.Transparent
        ) {
            Text(
                text = "idx $index",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHighlighted) DuoGreen else DuoSubtext,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun VisualizerMiniButton(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() },
        color = color.copy(alpha = 0.15f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Black,
            color = color,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 6.dp)
        )
    }
}
