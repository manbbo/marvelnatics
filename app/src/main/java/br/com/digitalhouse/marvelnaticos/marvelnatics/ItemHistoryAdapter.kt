package br.com.digitalhouse.marvelnaticos.marvelnatics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ItemHistoryAdapter(val lists: List<ItemHistory>) : RecyclerView.Adapter<ItemHistoryAdapter.ItemHistoryViewHolder>() {


    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ItemHistoryAdapter.ItemHistoryViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_histories, parent, false)
        return ItemHistoryViewHolder(itemView)

    }

    inner class ItemHistoryViewHolder(listView: View) : RecyclerView.ViewHolder(listView) {
        val iv_item_comic: ImageView = itemView.findViewById(R.id.iv_item_comic)
        val iv_item_star1: ImageView = itemView.findViewById(R.id.iv_item_star1)
        val iv_item_star2: ImageView = itemView.findViewById(R.id.iv_item_star2)
        val iv_item_star3: ImageView = itemView.findViewById(R.id.iv_item_star3)
        val iv_item_star4: ImageView = itemView.findViewById(R.id.iv_item_star4)
        val iv_item_star5: ImageView = itemView.findViewById(R.id.iv_item_star5)
    }

    override fun onBindViewHolder(holder: ItemHistoryViewHolder, position: Int) {
        val currentItem = lists[position]
        holder.iv_item_comic.setImageResource(R.drawable.img_comic)
        if(currentItem.rate >= 1) { holder.iv_item_star1.setImageResource(R.drawable.ic_baseline_star_24_checked)}
        if(currentItem.rate >= 2) { holder.iv_item_star2.setImageResource(R.drawable.ic_baseline_star_24_checked)}
        if(currentItem.rate >= 3) { holder.iv_item_star3.setImageResource(R.drawable.ic_baseline_star_24_checked)}
        if(currentItem.rate >= 4) { holder.iv_item_star4.setImageResource(R.drawable.ic_baseline_star_24_checked)}
        if(currentItem.rate == 5) { holder.iv_item_star5.setImageResource(R.drawable.ic_baseline_star_24_checked)}

    }

    override fun getItemCount(): Int = lists.size
}






