import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poepart2.R

class BudgetGoalAdapter(private val goals: List<BudgetGoal>) :
    RecyclerView.Adapter<BudgetGoalAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvBudgetTitle: TextView = view.findViewById(R.id.tvBudgetTitle)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val tvLeftToSpend: TextView = view.findViewById(R.id.tvLeftToSpend)
        val tvMonthlyBudget: TextView = view.findViewById(R.id.tvMonthlyBudget)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_budget_goal, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val goal = goals[position]
        holder.tvBudgetTitle.text = goal.budgetName

        val budget = goal.amount
        val spent = goal.spentAmount
        val leftToSpend = budget - spent

        holder.tvLeftToSpend.text = "Left to spend\nZAR %.2f".format(leftToSpend.coerceAtLeast(0.0))
        holder.tvMonthlyBudget.text = "${goal.recurrence} budget\nZAR %.2f".format(budget)

        val progress = if (budget > 0) {
            ((spent / budget) * 100).toInt().coerceAtMost(100)
        } else {
            0
        }
        holder.progressBar.progress = progress
    }


    override fun getItemCount() = goals.size
}
