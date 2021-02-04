package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.FavoritesActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.User
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.login.LoginActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import kotlinx.android.synthetic.main.fragment_profile.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfileFragment : Fragment() {

    lateinit var currentUser : FirebaseUser
    val viewModel: OfflineViewModel by viewModels<OfflineViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return OfflineViewModel(repo, context!!) as T
            }
        }
    }

    private lateinit var ctx: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) ctx = context

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)

        currentUser = FirebaseAuth.getInstance().currentUser!!

        val favoritos: AppCompatButton = root.findViewById(R.id.bt_favorites_profile)

        Log.i("CURRENT USER", "onAttach: $currentUser")

        favoritos.setOnClickListener {
            ctx.goToActivity(FavoritesActivity::class.java, R.anim.slide_in_right, R.anim.static_animation)
        }

        //Implementar regras p/ preencher os seguintes dados:

        val nameUser = if (currentUser.isAnonymous || currentUser == null) {
            "Fulano da Silva"
        } else currentUser.displayName
        val imageUser =  if (!currentUser.isAnonymous && currentUser.photoUrl.toString().isNullOrEmpty()) currentUser.photoUrl else R.drawable.profilemock
        val emailUser = if (currentUser.isAnonymous || currentUser == null) "e@mail" else currentUser.email
        val readComics = "10% HQs lidas"
        val readSeries = "20% Séries lidas"
        val readCharacters = "7% Personagens vistos"

        val tv_nameUser: TextView = root.findViewById(R.id.tv_name_user)
        val tv_emailUser: TextView = root.findViewById(R.id.tv_email_user)
        val tv_photoUser: ImageView = root.findViewById(R.id.iv_profile)
        val tv_readComics: TextView = root.findViewById(R.id.tv_read_comics)
        val tv_readSeries: TextView = root.findViewById(R.id.tv_read_series)
        val tv_readCharacters: TextView = root.findViewById(R.id.tv_read_characters)
        val btn_logout: TextView = root.findViewById(R.id.btn_logout)


        tv_nameUser.text = nameUser
        //tv_photoUser.7
        Glide.with(root).load(imageUser)
                .placeholder(R.drawable.profilemock)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(tv_photoUser)
        tv_emailUser.text = emailUser
        tv_readComics.text = readComics
        tv_readSeries.text = readSeries
        tv_readCharacters.text = readCharacters

        val infoShared: String = "$nameUser's Statistics on Marvelnaltics: \n\n $readComics \n $readSeries \n $readCharacters \n\nYou can also keep track of your Marvel Comics with Marvelnatics!"

        val compartilhar: AppCompatImageButton = root.findViewById(R.id.bt_share_profile)

        compartilhar.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, infoShared)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            viewModel.clearDatabase()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return root
    }

    companion object {

        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int) = ProfileFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, sectionNumber)
            }
        }
    }
}