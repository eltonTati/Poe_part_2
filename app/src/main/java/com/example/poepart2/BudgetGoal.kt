data class BudgetGoal(
    val budgetName: String = "",
    val amount: Double = 0.0,
    val recurrence: String = "",
    var spentAmount: Double = 0.0 // New field (not stored in Firestore, only used in adapter)
)
