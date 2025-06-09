import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.poepart2.R
import com.example.poepart2.Transaction

class TransactionAdapter(private val transactions: List<Transaction>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_EMPTY = 0
    private val VIEW_TYPE_TRANSACTION = 1

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textLabel: TextView = itemView.findViewById(R.id.textLabel)
        val textDate: TextView = itemView.findViewById(R.id.textDate)
        val textAmount: TextView = itemView.findViewById(R.id.textAmount)
    }

    class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val emptyText: TextView = itemView.findViewById(R.id.emptyText)
    }

    override fun getItemViewType(position: Int): Int {
        return if (transactions.isEmpty()) VIEW_TYPE_EMPTY else VIEW_TYPE_TRANSACTION
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_TRANSACTION) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_transaction, parent, false)
            TransactionViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empty_transaction, parent, false)
            EmptyViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TransactionViewHolder) {
            val transaction = transactions[position]
            holder.textLabel.text = transaction.label
            holder.textDate.text = transaction.date
            holder.textAmount.text = "R${"%.2f".format(transaction.amount)}"
        } else if (holder is EmptyViewHolder) {
            holder.emptyText.text = "No transactions yet"
        }
    }

    override fun getItemCount(): Int {
        return if (transactions.isEmpty()) 1 else transactions.size
    }
}