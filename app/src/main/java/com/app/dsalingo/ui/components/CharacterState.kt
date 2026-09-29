package com.app.dsalingo.ui.components

/**
 * All supported emotional and situational states for the Dsalingoo mascot character.
 */
enum class CharacterEmotion {
    IDLE,          // Gentle breathing, subtle bobbing on dashboard/map
    HAPPY,         // Joyful / cheerful stance
    EXCITED,       // High energy bounce / on-fire motivation
    WELCOME,       // Friendly greeting, wave/bounce
    LESSON_START,  // Energetic pointing/ready pose
    THINKING,      // Curious tilt, analyzing
    CORRECT,       // High jump, victory celebration with sparkles
    WRONG,         // Concerned / thoughtful reaction (supportive)
    ENCOURAGING,   // Cheering, "almost there! try again"
    HEART_LOST,    // Shocked / empathetic droop
    LESSON_COMPLETE,// Major celebration, victory dance + confetti
    PERFECT,       // Golden glow, crown celebration
    BOSS,          // Determined hero pose, fiery energy aura
    BOSS_COMPLETE, // Ultimate victory, champion celebration
    LEVEL_UP       // Ascending star burst celebration
}

/**
 * Contextual message paired with a character emotion.
 */
data class CharacterMessage(
    val text: String,
    val emotion: CharacterEmotion = CharacterEmotion.IDLE,
    val title: String? = null
)
