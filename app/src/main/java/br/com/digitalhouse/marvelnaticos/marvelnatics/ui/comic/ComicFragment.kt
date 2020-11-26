package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character

class ComicFragment : DialogFragment(){

    private lateinit var ctx: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
    }

    override fun onStart() {
        super.onStart()

        val parameter = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(parameter, parameter)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(ctx, R.color.backgroupDialog)))
        dialog?.window?.setWindowAnimations(R.style.dialog_animation_from_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comic, container, false)

        val rc: RecyclerView = root.findViewById(R.id.rc_comic_characters)
        val backBtn: ImageButton = root.findViewById(R.id.ib_comic_backbtn)

        backBtn.setOnClickListener {
            dismiss()
            Log.v("t", "teste")
        }

        rc.adapter = CharacterAdapter(root.context, mutableListOf(
            Character(),
            Character(),
            Character(),
            Character(),
        ))
        return root
    }

    companion object {
        fun newInstance() = ComicFragment().apply {  }
    }
}