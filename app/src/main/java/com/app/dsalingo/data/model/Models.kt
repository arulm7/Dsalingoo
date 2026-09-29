package com.app.dsalingo.data.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val level: Int = 1,
    val xp: Int = 0,
    val streak: Int = 0,
    val hearts: Int = 8,        // max 10, lose 1 on wrong answer
    val crowns: Int = 0,
    val isPro: Boolean = false,
    val achievements: List<Achievement> = emptyList(),
    val dailyGoal: Int = 50,     // XP target per day (default 50)
    val joinDate: String = ""
)

data class Achievement(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val icon: String = "",       // emoji string
    val unlockedAt: String = "",
    val xpReward: Int = 0
)

data class Lesson(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val type: LessonType = LessonType.CONCEPT,           // CONCEPT, PRACTICE, CHALLENGE, QUIZ
    val difficulty: Difficulty = Difficulty.BEGINNER,      // BEGINNER, INTERMEDIATE, ADVANCED
    val xpReward: Int = 50,              // 50 / 75 / 100
    val unit: String = "",               // category ID (e.g. "array", "stack")
    val content: LessonContent = LessonContent("", null, emptyList()),
    val isCompleted: Boolean = false,
    val isLocked: Boolean = false,
    val crownLevel: Int = 0            // 0-3
)

enum class LessonType { CONCEPT, PRACTICE, CHALLENGE, QUIZ }
enum class Difficulty { BEGINNER, INTERMEDIATE, ADVANCED }

data class LessonContent(
    val explanation: String = "",
    val codeExample: String? = null,
    val questions: List<Question> = emptyList()
)

data class Question(
    val id: String = "",
    val type: QuestionType = QuestionType.MULTIPLE_CHOICE,
    val question: String = "",
    val options: List<String>? = null,          // for multiple-choice
    val correctAnswer: Any = "",              // Int index, String, or List<Int>
    val explanation: String = "",
    val code: String? = null,                   // code snippet shown with question
    val blanks: List<String>? = null,           // for code-completion
    val items: List<String>? = null,            // for drag-drop / array interaction
    val correctOrder: List<Int>? = null,         // for drag-drop
    val arrayData: List<String>? = null,         // initial array state
    val imageUrl: String? = null
)

enum class QuestionType { 
    THEORY, 
    MULTIPLE_CHOICE, 
    CODE_COMPLETION, 
    DRAG_DROP, 
    FILL_BLANK, 
    ARRAY_INTERACTION 
}

data class Challenge(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val difficulty: ChallengeDifficulty = ChallengeDifficulty.EASY,
    val category: String = "",
    val xpReward: Int = 0,
    val timeLimit: Int? = null,
    val isCompleted: Boolean = false,
    val link: String = ""                    // LeetCode URL
)

enum class ChallengeDifficulty { EASY, MEDIUM, HARD }

data class LeaderboardUser(
    val id: String = "",
    val username: String = "",
    val xp: Int = 0,
    val streak: Int = 0,
    val rank: Int = 0,
    val avatar: String = ""
)

data class DataStructureCategory(
    val id: String = "",
    val title: String = "",
    val icon: String = "",       // emoji
    val color: Long = 0,        // hex color
    val totalQuestions: Int = 0,
    val completedQuestions: Int = 0,
    val lessons: List<Lesson> = emptyList(),
    val isComingSoon: Boolean = false
)
