package com.app.dsalingo.ui.screens.lesson

import androidx.activity.compose.BackHandler
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.app.dsalingo.R
import com.app.dsalingo.data.model.Question
import com.app.dsalingo.data.model.QuestionType
import com.app.dsalingo.ui.components.*
import com.app.dsalingo.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonDetailScreen(
    categoryId: String,
    lessonId: String,
    onNavigateBack: () -> Unit,
    onLessonComplete: () -> Unit,
    viewModel: LessonViewModel = hiltViewModel()
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    val hearts by viewModel.hearts.collectAsState()
    val secondsRemaining by viewModel.secondsRemaining.collectAsState()
    val isFull by viewModel.isFull.collectAsState()
    var showHeartDialog by remember { mutableStateOf(false) }

    var streakCount by remember { mutableIntStateOf(0) }
    var showLeaveDialog by remember { mutableStateOf(false) }
    var isLessonFinished by remember { mutableStateOf(false) }
    var isGameOver by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }

    val questions by viewModel.questions.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(categoryId, lessonId) {
        viewModel.loadQuestions(categoryId, lessonId)
        if (viewModel.hearts.value <= 0) {
            isGameOver = true
        }
    }

    val currentQuestion = questions.getOrNull(currentQuestionIndex)
    val progress = if (questions.isNotEmpty()) (currentQuestionIndex).toFloat() / questions.size.toFloat() else 0f

    var totalLessonXpReward by remember { mutableIntStateOf(50) }

    if (showLeaveDialog) {
        AlertDialog(
            containerColor = DuoCardBg,
            titleContentColor = Color.White,
            textContentColor = DuoSubtext,
            onDismissRequest = { showLeaveDialog = false },
            title = { Text("Wait, don't go!", fontWeight = FontWeight.Black) },
            text = { Text("You'll lose your progress and heart lives if you quit now.") },
            confirmButton = {
                DuoButton(
                    text = "KEEP LEARNING",
                    onClick = { showLeaveDialog = false },
                    height = 42.dp,
                    fontSize = 13,
                    color = DuoGreen,
                    shadowColor = DuoGreenDark
                )
            },
            dismissButton = {
                TextButton(onClick = { showLeaveDialog = false; onNavigateBack() }) {
                    Text("END SESSION", color = DuoRed, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showHeartDialog) {
        HeartRechargeDialog(
            hearts = hearts,
            formattedTimeRemaining = viewModel.getFormattedTime(),
            rechargeProgress = viewModel.getRechargeProgress(),
            isFull = isFull,
            onRefillOneHeart = { viewModel.gainHeart(1) },
            onRefillAllHearts = { viewModel.refillHearts() },
            onDismiss = { showHeartDialog = false }
        )
    }

    if (isGameOver || hearts <= 0) {
        GameOverScreen(
            formattedTimeRemaining = viewModel.getFormattedTime(),
            rechargeProgress = viewModel.getRechargeProgress(),
            onPracticeRefill = {
                viewModel.gainHeart(1)
                isGameOver = false
            },
            onQuit = onNavigateBack
        )
    } else if (isLessonFinished) {
        LessonCompleteScreen(
            xpReward = totalLessonXpReward,
            accuracyPercent = if (questions.isNotEmpty()) ((correctAnswersCount.toFloat() / questions.size) * 100).toInt() else 100,
            onContinue = onLessonComplete
        )
    } else {
        Box(modifier = Modifier.fillMaxSize().background(DuoDarkBg)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = DuoGreen)
                }
            } else if (currentQuestion != null) {
                Scaffold(
                    containerColor = DuoDarkBg,
                    topBar = {
                        DuolingoQuizTopBar(
                            progress = progress,
                            hearts = hearts,
                            onHeartClick = { showHeartDialog = true },
                            onCloseClick = { showLeaveDialog = true }
                        )
                    }
                ) { padding ->
                    Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                        AnimatedContent(
                            targetState = currentQuestionIndex,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                        slideOutHorizontally { width -> -width } + fadeOut())
                                } else {
                                    (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                        slideOutHorizontally { width -> width } + fadeOut())
                                }.using(SizeTransform(clip = false))
                            },
                            label = "questionTransition"
                        ) { index ->
                            val question = questions[index]
                            DuolingoQuestionBody(
                                question = question,
                                hearts = hearts,
                                streakCount = streakCount,
                                onSubmitAnswer = { answer, interactionState, callback ->
                                    viewModel.submitAnswer(question.id, answer, interactionState, callback)
                                },
                                onCorrectAnswer = { earnedXp ->
                                    correctAnswersCount++
                                    if (question.type != QuestionType.THEORY) {
                                        streakCount++
                                    }
                                    totalLessonXpReward = if (earnedXp > 0) earnedXp else 20
                                    if (currentQuestionIndex < questions.size - 1) {
                                        currentQuestionIndex++
                                    } else {
                                        isLessonFinished = true
                                    }
                                },
                                onWrongAnswer = {
                                    streakCount = 0
                                    if (viewModel.hearts.value <= 0) {
                                        isGameOver = true
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No questions found for this lesson.", color = DuoSubtext)
                }
            }
        }
    }
}

/**
 * Quiz Top Bar with Progress Capsule and Animated Hearts
 */
@Composable
fun DuolingoQuizTopBar(
    progress: Float,
    hearts: Int,
    onHeartClick: () -> Unit = {},
    onCloseClick: () -> Unit
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "progressBar"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .statusBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onCloseClick) {
            Icon(Icons.Default.Close, contentDescription = "Close", tint = DuoSubtext, modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.width(8.dp))

        // Progress Capsule
        Box(
            modifier = Modifier
                .weight(1f)
                .height(14.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(DuoCardBg)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress)
                    .clip(RoundedCornerShape(8.dp))
                    .background(DuoGreen)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Hearts Count Pill (Interactive)
        Surface(
            modifier = Modifier.clickable { onHeartClick() },
            shape = RoundedCornerShape(12.dp),
            color = DuoCardBg,
            border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("❤️", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(4.dp))
                AnimatedContent(
                    targetState = hearts,
                    transitionSpec = { (scaleIn() + fadeIn()).togetherWith(scaleOut() + fadeOut()) },
                    label = "heartsAnim"
                ) { targetHearts ->
                    Text(
                        targetHearts.toString(),
                        fontWeight = FontWeight.Black,
                        color = DuoRed,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }
}

/**
 * Question Body matching Duolingo AssessmentView
 */
/**
 * Question Body matching Duolingo AssessmentView with Array Visualizer Integration
 */
@Composable
fun DuolingoQuestionBody(
    question: Question,
    hearts: Int,
    streakCount: Int,
    onSubmitAnswer: (answer: Any?, interactionState: Map<String, Any>?, (com.app.dsalingo.data.network.QuestionSubmitResponse) -> Unit) -> Unit,
    onCorrectAnswer: (earnedXp: Int) -> Unit,
    onWrongAnswer: () -> Unit
) {
    var selectedOptionIndex by remember(question.id) { mutableStateOf<Int?>(null) }
    var textInput by remember(question.id) { mutableStateOf("") }
    
    val initialArray = remember(question.id) {
        question.arrayData ?: listOf("10", "20", "30", "40")
    }
    val visualizerState = rememberArrayVisualizerState(initialArray)

    var showResult by remember(question.id) { mutableStateOf(false) }
    var isCorrect by remember(question.id) { mutableStateOf(false) }
    var serverExplanation by remember(question.id) { mutableStateOf(question.explanation) }
    var serverXpEarned by remember(question.id) { mutableIntStateOf(0) }
    var isSubmitting by remember(question.id) { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DuoDarkBg)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Question Type Title
            val typeTitle = when (question.type) {
                QuestionType.THEORY -> "New Concept"
                QuestionType.MULTIPLE_CHOICE -> "Select the correct answer"
                QuestionType.FILL_BLANK -> "Complete the code"
                QuestionType.CODE_COMPLETION -> "Fill in the blank"
                QuestionType.ARRAY_INTERACTION -> "Interactive Array Challenge"
                else -> "Coding Exercise"
            }

            Text(
                text = typeTitle,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Speaker Chat Bubble Card
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Speaker Avatar
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = CircleShape,
                    color = DuoCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = R.drawable.duo_pencil),
                            contentDescription = "Duo",
                            modifier = Modifier.size(44.dp).clip(CircleShape),
                            contentScale = androidx.compose.ui.layout.ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Prompt Speech Bubble
                Surface(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(18.dp),
                    color = DuoCardBg,
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text("🔊", fontSize = 16.sp, modifier = Modifier.padding(top = 2.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = question.question,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

            if (!question.imageUrl.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder),
                    color = DuoCardBg
                ) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(question.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Question Image",
                        modifier = Modifier.fillMaxSize().padding(8.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    color = DuoGreen,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("🖼️", fontSize = 32.sp)
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Question Interactive Body
            when (question.type) {
                QuestionType.THEORY -> {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Surface(
                            shape = RoundedCornerShape(18.dp),
                            color = DuoCardBg,
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder)
                        ) {
                            Text(
                                text = question.explanation,
                                modifier = Modifier.padding(18.dp),
                                fontSize = 15.sp,
                                lineHeight = 22.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Array Visualizer Demonstration for Theory
                        Text(
                            text = "INTERACTIVE DEMONSTRATION",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = DuoSubtext,
                            letterSpacing = 0.5.sp
                        )

                        ArrayVisualizer(
                            state = visualizerState,
                            isInteractive = true,
                            showControls = true
                        )

                        if (question.code != null) {
                            DuolingoCodeSnippet(question.code)
                        }
                    }
                }
                QuestionType.MULTIPLE_CHOICE -> {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        val correctVal = when (val res = question.correctAnswer) {
                            is Number -> res.toInt()
                            is String -> res.toDoubleOrNull()?.toInt() ?: -1
                            else -> -1
                        }

                        // Display Visualizer preview if question has arrayData
                        if (question.arrayData != null) {
                            ArrayVisualizer(
                                state = visualizerState,
                                isInteractive = false,
                                showControls = false
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        question.options?.forEachIndexed { index, optionText ->
                            val isSelected = selectedOptionIndex == index
                            val isThisCorrect = index == correctVal

                            val bgColor = when {
                                showResult && isSelected && isCorrect -> DuoGreen.copy(alpha = 0.2f)
                                showResult && isSelected && !isCorrect -> DuoRed.copy(alpha = 0.2f)
                                showResult && isThisCorrect -> DuoGreen.copy(alpha = 0.25f)
                                isSelected -> DuoBlue.copy(alpha = 0.18f)
                                else -> DuoInputBg
                            }

                            val borderColor = when {
                                showResult && isSelected && isCorrect -> DuoGreen
                                showResult && isSelected && !isCorrect -> DuoRed
                                showResult && isThisCorrect -> DuoGreen
                                isSelected -> DuoBlue
                                else -> DuoInputBorder
                            }

                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable(enabled = !showResult && !isSubmitting) { selectedOptionIndex = index },
                                shape = RoundedCornerShape(18.dp),
                                color = bgColor,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected || (showResult && isThisCorrect)) 2.dp else 1.dp,
                                    borderColor
                                )
                            ) {
                                Row(
                                    modifier = Modifier.padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = DuoCardBg,
                                        border = androidx.compose.foundation.BorderStroke(
                                            1.5.dp,
                                            if (isSelected) DuoBlue else DuoInputBorder
                                        ),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${index + 1}",
                                                fontWeight = FontWeight.Black,
                                                fontSize = 13.sp,
                                                color = if (isSelected) DuoBlue else DuoSubtext
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.width(14.dp))

                                    Text(
                                        text = optionText,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (showResult) {
                                        if (isSelected) {
                                            Text(
                                                text = if (isCorrect) "✅" else "❌",
                                                fontSize = 18.sp
                                            )
                                        } else if (isThisCorrect) {
                                            Text(
                                                text = "✅",
                                                fontSize = 18.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                QuestionType.FILL_BLANK, QuestionType.CODE_COMPLETION -> {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        if (question.code != null) {
                            DuolingoCodeSnippet(question.code)
                        }
                        DuoTextField(
                            value = textInput,
                            onValueChange = { if (!showResult) textInput = it },
                            placeholder = "Type your answer in here...",
                            enabled = !showResult && !isSubmitting
                        )
                    }
                }
                QuestionType.ARRAY_INTERACTION -> {
                    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Text(
                            text = "YOUR TURN: MANIPULATE THE ARRAY",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = AmberGold,
                            letterSpacing = 0.5.sp
                        )

                        ArrayVisualizer(
                            state = visualizerState,
                            isInteractive = !showResult && !isSubmitting,
                            showControls = true,
                            onElementClick = { index, value ->
                                coroutineScope.launch {
                                    visualizerState.access(index)
                                }
                            }
                        )

                        if (question.code != null) {
                            DuolingoCodeSnippet(question.code)
                        }
                    }
                }
                else -> {
                    Text(
                        text = question.question,
                        color = Color.White,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }

        // Bottom Action & Feedback Bar
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(DuoDarkBg)
        ) {
            // Result Feedback Banner
            AnimatedVisibility(
                visible = showResult,
                enter = slideInVertically { it } + fadeIn(),
                exit = slideOutVertically { it } + fadeOut()
            ) {
                val bannerBg = if (isCorrect) DuoGreen.copy(alpha = 0.18f) else DuoRed.copy(alpha = 0.18f)
                val iconTint = if (isCorrect) DuoGreen else DuoRed
                val titleText = if (isCorrect) "Excellent! (+${if (serverXpEarned > 0) serverXpEarned else 10} XP)" else "Incorrect"

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(bannerBg)
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isCorrect) "✅" else "❌",
                        fontSize = 28.sp
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = titleText,
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp,
                            color = iconTint
                        )
                        if (serverExplanation.isNotBlank()) {
                            Text(
                                text = serverExplanation,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(DuoInputBorder)
            )

            // Button Action Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (showResult) {
                    DuoButton(
                        text = "CONTINUE",
                        onClick = {
                            if (isCorrect) {
                                showResult = false
                                onCorrectAnswer(serverXpEarned)
                            } else {
                                showResult = false
                                selectedOptionIndex = null
                                textInput = ""
                                visualizerState.reset()
                            }
                        },
                        color = if (isCorrect) DuoGreen else DuoRed,
                        shadowColor = if (isCorrect) DuoGreenDark else DuoRedDark,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    val isTheory = question.type == QuestionType.THEORY
                    val canCheck = when (question.type) {
                        QuestionType.THEORY -> true
                        QuestionType.MULTIPLE_CHOICE -> selectedOptionIndex != null
                        QuestionType.FILL_BLANK, QuestionType.CODE_COMPLETION -> textInput.isNotBlank()
                        QuestionType.ARRAY_INTERACTION -> visualizerState.elements.isNotEmpty()
                        else -> true
                    }

                    DuoButton(
                        text = if (isSubmitting) "VALIDATING..." else if (isTheory) "CONTINUE" else "CHECK",
                        onClick = {
                            isSubmitting = true
                            val answerPayload: Any? = when (question.type) {
                                QuestionType.THEORY -> ""
                                QuestionType.MULTIPLE_CHOICE -> selectedOptionIndex
                                QuestionType.FILL_BLANK, QuestionType.CODE_COMPLETION -> textInput.trim()
                                QuestionType.ARRAY_INTERACTION -> visualizerState.elements
                                else -> textInput.trim()
                            }
                            val interactionPayload: Map<String, Any>? = when (question.type) {
                                QuestionType.ARRAY_INTERACTION -> mapOf("array" to visualizerState.elements)
                                else -> null
                            }

                            onSubmitAnswer(answerPayload, interactionPayload) { response ->
                                isSubmitting = false
                                isCorrect = response.correct
                                serverExplanation = if (response.explanation.isNotBlank()) response.explanation else question.explanation
                                serverXpEarned = response.xpAwarded

                                if (isTheory) {
                                    onCorrectAnswer(if (response.xpAwarded > 0) response.xpAwarded else 10)
                                } else {
                                    showResult = true
                                    if (!response.correct) {
                                        onWrongAnswer()
                                    }
                                }
                            }
                        },
                        enabled = canCheck && !isSubmitting,
                        color = DuoGreen,
                        shadowColor = DuoGreenDark,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DuolingoCodeSnippet(code: String) {
    Surface(
        color = DuoInputBg,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, DuoInputBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = code,
            modifier = Modifier.padding(16.dp),
            fontFamily = FontFamily.Monospace,
            fontSize = 13.sp,
            color = Color(0xFF58CC02),
            lineHeight = 20.sp
        )
    }
}

@Composable
fun getQuestionImageSource(name: String): String? {
    if (name.contains("|")) {
        val url = name.substringAfter("|").trim()
        if (url.isNotEmpty()) return url
    }
    val cleanName = name.substringBefore("|").trim()
    return when (cleanName) {
        "Tom Cruise", "Tom" -> "https://image.tmdb.org/t/p/w342/maf8PhSvDCdEwjEMbYfGpojR5RP.jpg"
        "Zendaya" -> "https://image.tmdb.org/t/p/w342/1qup8tSt95HLbcy2c2xrx4iJNxv.jpg"
        "The Rock" -> "https://image.tmdb.org/t/p/w342/5QApZVV8FUFlVxQpIK3Ew6cqotq.jpg"
        "Spider-Man" -> "https://image.tmdb.org/t/p/w342/1g0dhYtq4irTY1GPXvft6k4YLjm.jpg"
        "Iron Man" -> "https://image.tmdb.org/t/p/w342/78lPtwv72eTNqFW9COBYI0dWDJa.jpg"
        "Thor" -> "https://image.tmdb.org/t/p/w342/prSfAi1xGrhLQNxVSUFh61xQ4Qy.jpg"
        "Avengers" -> "https://image.tmdb.org/t/p/w342/RYMX2wcKCBAr24UyPD7xwmjaTn.jpg"
        "Batman" -> "https://image.tmdb.org/t/p/w342/74xTEgt7R36Fpooo50r9T25onhq.jpg"
        "Cars" -> "https://image.tmdb.org/t/p/w342/2Touk3m5gzsqr1VsvxypdyHY5ci.jpg"
        "Robert" -> "https://image.tmdb.org/t/p/w342/5qHNjhtjMD4YWH3UP0rm4tKwxCL.jpg"
        "Cillian" -> "https://image.tmdb.org/t/p/w342/2lKs67r7FI4bPu0AXxMUJZxmUXn.jpg"
        else -> if (cleanName.startsWith("http://") || cleanName.startsWith("https://")) cleanName else null
    }
}

@Composable
fun DuolingoArrayInteractionBody(
    currentItems: List<String>,
    availableBank: List<String>,
    showResult: Boolean,
    onItemClick: (String, Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("🎬", fontSize = 20.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text("FILM STRIP (ARRAY)", fontWeight = FontWeight.Black, color = DuoSubtext, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.height(12.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(2.dp, DuoInputBorder),
            color = Color(0xFF0F181D)
        ) {
            LazyRow(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                itemsIndexed(currentItems) { index, item ->
                    val isEmpty = item == "(empty slot)"
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier
                                .size(85.dp, 110.dp)
                                .clickable(enabled = !isEmpty && !showResult) { onItemClick(item, false) },
                            shape = RoundedCornerShape(14.dp),
                            color = if (isEmpty) DuoCardBg else DuoInputBg,
                            border = androidx.compose.foundation.BorderStroke(
                                2.dp,
                                if (isEmpty) DuoInputBorder else DuoBlue
                            )
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                if (isEmpty) {
                                    Text("?", color = DuoSubtext, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                                } else {
                                    val imageSource = getQuestionImageSource(item)
                                    if (imageSource != null) {
                                        SubcomposeAsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(imageSource)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = item.substringBefore('|').trim(),
                                            modifier = Modifier.fillMaxSize(),
                                            contentScale = androidx.compose.ui.layout.ContentScale.Crop,
                                            loading = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(
                                                        color = DuoBlue,
                                                        modifier = Modifier.size(16.dp),
                                                        strokeWidth = 2.dp
                                                    )
                                                }
                                            },
                                            error = {
                                                Box(
                                                    modifier = Modifier.fillMaxSize(),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        text = item.substringBefore('|').trim(),
                                                        fontSize = 11.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color.White,
                                                        textAlign = TextAlign.Center,
                                                        modifier = Modifier.padding(4.dp)
                                                    )
                                                }
                                            }
                                        )
                                    } else {
                                        Text(
                                            text = item.substringBefore('|').trim(),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier.padding(6.dp)
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "[$index]",
                            color = DuoSubtext,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        Text("ELEMENT BANK", fontWeight = FontWeight.Black, color = DuoSubtext, fontSize = 12.sp)
        Spacer(modifier = Modifier.height(12.dp))
        
        @OptIn(ExperimentalLayoutApi::class)
        androidx.compose.foundation.layout.FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            availableBank.forEach { item ->
                Surface(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .clickable(enabled = !showResult) { onItemClick(item, true) },
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, DuoInputBorder),
                    color = DuoCardBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("🎬", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item.substringBefore('|').trim(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}
