package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import kotlinx.android.synthetic.main.item_charbubble.view.*

class CharacterAdapter(val context: Context, val listCharacter: MutableList<Character>) :
    RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    class CharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val charImg: ImageView = itemView.findViewById(R.id.iv_char_bubble)
        val charPlace: TextView = itemView.findViewById(R.id.tv_char_place)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val root =
            LayoutInflater.from(parent.context).inflate(R.layout.item_charbubble, parent, false)
        return CharacterViewHolder(root)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
    }

    override fun getItemCount(): Int = listCharacter.size
}