package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import br.com.digitalhouse.marvelnaticos.marvelnatics.FavoritesActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.ComicsAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.HePAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.api.Credentials
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.ComicClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.interfaces.HePClickListener
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Comic
import br.com.digitalhouse.marvelnaticos.marvelnatics.services.repo
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.colecao.ColecaoActivity
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import com.google.gson.Gson
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.security.MessageDigest
import kotlin.concurrent.thread


class HomeFragment : Fragment(), ComicClickListener, HePClickListener {

    private lateinit var ctx: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) ctx = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        // Toolbar
        val toolbar: Toolbar = root.findViewById(R.id.include_toolbar)
        val colecao: TextView = toolbar.findViewById(R.id.btn_toolbar_colecao)
        val favoritos: TextView = toolbar.findViewById(R.id.btn_toolbar_favoritos)


        colecao.setOnClickListener {
            ctx.goToActivity(
                ColecaoActivity::class.java,
                R.anim.slide_in_right,
                R.anim.static_animation
            )
        }

        favoritos.setOnClickListener {
            ctx.goToActivity(
                FavoritesActivity::class.java,
                R.anim.slide_in_right,
                R.anim.static_animation
            )
        }

        // Personagens mais populares
        val includePmP: View = root.findViewById(R.id.include_pmp)
        val rvPmP: RecyclerView = includePmP.findViewById(R.id.rv_list_charsList)
        rvPmP.adapter = CharacterAdapter(
            root.context, mutableListOf(
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                //Character(),
                // Character()
            )
        )

        // Historias em destaques
        val includeHeP: View = root.findViewById(R.id.include_hep)
        val vpHeP: ViewPager = includeHeP.findViewById(R.id.vp_hed)

        vpHeP.adapter = HePAdapter(
            root.context, this
        ).also { hep ->
            // TODO / APENAS PARA TESTES / APENAS PARA TESTES / APENAS PARA TESTES / APENAS PARA TESTES
            thread(start = true) {
                Credentials().apply {
                    repo.getComics(publicKey, "1$privateKey$publicKey".let { txt ->
                        MessageDigest.getInstance("MD5").digest(txt.toByteArray(Charsets.UTF_8))
                            .joinToString(separator = "") { b ->
                                "%02x".format(b)
                            }
                    }, "1")
                        .enqueue(object : Callback<JsonObject> {
                            override fun onResponse(
                                call: Call<JsonObject>,
                                response: Response<JsonObject>
                            ) {
                                val results = response.body()?.let { resp ->
                                    resp.getAsJsonObject("data").getAsJsonArray("results")
                                }
                                if (results != null) {
                                    val comics = Gson().fromJson(
                                        results,
                                        Array<Comic>::class.java
                                    ) as Array<Comic>

                                    activity?.runOnUiThread {
                                        hep.listHeP = mutableListOf<Comic>().also { list ->
                                            list.addAll(comics)
                                        }
                                        hep.notifyDataSetChanged()
                                    }

                                }
                            }

                            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                            }
                        })
                }
            }
            // TODO / APENAS PARA TESTES / APENAS PARA TESTES / APENAS PARA TESTES / APENAS PARA TESTES
        }

        vpHeP.setOnTouchListener { v, event ->
            v.parent?.requestDisallowInterceptTouchEvent(true)

            v?.onTouchEvent(event) ?: true
        }
        vpHeP.pageMargin = 10

        // Historias mais lidas
        val includeHML: View = root.findViewById(R.id.include_hml)
        val includeHMA: View = root.findViewById(R.id.include_hma)
        val rvHistoriasMaisLidas: RecyclerView = includeHML.findViewById(R.id.rv_list_listImages)
        val rvHistoriasMaisAvaliadas: RecyclerView =
            includeHMA.findViewById(R.id.rv_list_listImages)

        val titleHML: TextView = includeHML.findViewById(R.id.tv_list_listName)
        val titleHMA: TextView = includeHMA.findViewById(R.id.tv_list_listName)

        titleHML.text = "Histórias mais lidas"
        titleHMA.text = "Histórias melhor avaliadas"

        rvHistoriasMaisLidas.adapter = ComicsAdapter(
            root.context, mutableListOf(
                //Comic(),
                //Comic(),
                //Comic(),
                //Comic(),
                //Comic()
            ), this
        )

        rvHistoriasMaisAvaliadas.adapter = rvHistoriasMaisLidas.adapter
        return root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int) = HomeFragment().apply {
            arguments = Bundle().apply {
                putInt(ARG_SECTION_NUMBER, sectionNumber)
            }
        }
    }

    override fun onComicClickListener(position: Int): View.OnClickListener = View.OnClickListener {
        val t = ctx.supportFragmentManager.beginTransaction()
        val frag: DialogFragment = ComicFragment.newInstance()
        frag.show(t, "teste")
    }

    override fun onHePClickListener(position: Int): View.OnClickListener = View.OnClickListener {
        Log.v("HeP", "Clock")
        val t = ctx.supportFragmentManager.beginTransaction()
        val frag: DialogFragment = ComicFragment.newInstance()
        frag.show(t, "HeP")
    }
}