package br.com.digitalhouse.marvelnaticos.marvelnatics.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.BuscaFragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.HomeFragment
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main.ProfileFragment

class MainPagerAdapter(fm: FragmentActivity, private val qtd: Int): FragmentStateAdapter(fm) {
    override fun getItemCount(): Int = qtd

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment.newInstance(position+1)
            1 -> BuscaFragment.newInstance(position+1)
            2 -> ProfileFragment.newInstance(position+1)
            else -> HomeFragment.newInstance(position+1)
        }
    }

/*    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        if (recyclerView.findViewHolderForLayoutPosition(2)?.itemView!! is ProfileFragment) {
            recyclerView.findViewHolderForLayoutPosition(2).itemView.viewModel.getStatistics()
        }

    }*/

}