package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character
import br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic.ComicFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import java.util.*

class ComicFragment : DialogFragment() {

    private lateinit var ctx: Context
    private lateinit var spinner: CircularProgressDrawable

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context

        spinner = CircularProgressDrawable(ctx).apply {
            strokeCap = android.graphics.Paint.Cap.ROUND
            centerRadius = 40f
            strokeWidth = 15f
            start()
        }
    }

    override fun onStart() {
        super.onStart()

        val parameter = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.setLayout(parameter, parameter)
        dialog?.window?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    ctx,
                    R.color.backgroupDialog
                )
            )
        )
        dialog?.window?.setWindowAnimations(R.style.dialog_animation_from_top)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comic, container, false)

        val titulo: TextView = root.findViewById(R.id.tv_comic_title)
        val descricao: TextView = root.findViewById(R.id.tv_comic_descricao)
        val dataPub: TextView = root.findViewById(R.id.tv_comic_data_pub)
        val ivCapa : ImageView = root.findViewById(R.id.iv_comic_capa)
        val criadores: TextView = root.findViewById(R.id.tv_comic_creator)
        val desenhistas: TextView = root.findViewById(R.id.tv_comic_ilustrator)
        val artistasCapa: TextView = root.findViewById(R.id.tv_comic_cover)

        val rc: RecyclerView = root.findViewById(R.id.rc_comic_characters)
        val backBtn: ImageButton = root.findViewById(R.id.ib_comic_backbtn)

        val btFavorito : ImageView = root.findViewById(R.id.bt_favorito_comic)
        val btQueroler : ImageView = root.findViewById(R.id.bt_queroler_comic)
        val btJali : ImageView = root.findViewById(R.id.bt_jali_comic)
        val btTenho : ImageView = root.findViewById(R.id.bt_tenho_comic)

        val btStars : List<ImageView> = listOf(root.findViewById(R.id.s0), root.findViewById(R.id.s1),
                root.findViewById(R.id.s2), root.findViewById(R.id.s3), root.findViewById(R.id.s4))


        // Botoes de ação
        var countFav = false
        btFavorito.setOnClickListener {
            if (!countFav) {
                btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.favoritebt), PorterDuff.Mode.SRC_IN)
                countFav = true
            }
            else {
                btFavorito?.setColorFilter(ContextCompat.getColor(ctx, R.color.white), PorterDuff.Mode.SRC_IN)
                countFav = false
            }

            Toast.makeText(ctx, "Você clicou em 'FAVORITOS'", Toast.LENGTH_SHORT).show()
        }

        var countQler = false
        btQueroler.setOnClickListener {
            if (!countQler) {
                btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.querolerbt), PorterDuff.Mode.SRC_IN)
                countQler =true
            }
            else {
                btQueroler?.setColorFilter(ContextCompat.getColor(ctx, R.color.white), PorterDuff.Mode.SRC_IN)
                countQler = false
            }
            Toast.makeText(ctx, "Você clicou em 'QUERO LER'", Toast.LENGTH_SHORT).show()
        }

        var countJali = false
        btJali.setOnClickListener {
            if (!countJali) {
                btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.jalibt), PorterDuff.Mode.SRC_IN)
                countJali = true
            }
            else {
                btJali?.setColorFilter(ContextCompat.getColor(ctx, R.color.white), PorterDuff.Mode.SRC_IN)
                countJali = false
            }

            Toast.makeText(ctx, "Você clicou em 'Ja li'", Toast.LENGTH_SHORT).show()
        }

        var countTenho = false
        btTenho.setOnClickListener {
            if (!countTenho) {
                btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.tenhobt), PorterDuff.Mode.SRC_IN)
                countTenho = true
            }
            else {
                btTenho?.setColorFilter(ContextCompat.getColor(ctx, R.color.white), PorterDuff.Mode.SRC_IN)
                countTenho = false
            }

            Toast.makeText(ctx, "Você clicou em 'TENHO'", Toast.LENGTH_SHORT).show()
        }
        //////

        var countStars = false
        // Botões de estrela - implementar para ficar com cor
        for (i in 0..4) {
            btStars[i].setOnClickListener {
                if (!countStars) {
                    for (j in 0..4) {
                        btStars[j]?.setColorFilter(ContextCompat.getColor(ctx, R.color.white), PorterDuff.Mode.SRC_IN)
                    }

                    for (j in 0..i) {
                        btStars[j]?.setColorFilter(ContextCompat.getColor(ctx, R.color.favoritebt), PorterDuff.Mode.SRC_IN)
                    }

                }
                else {
                    for (j in 0..4) {
                        btStars[j]?.setColorFilter(ContextCompat.getColor(ctx, R.color.white), PorterDuff.Mode.SRC_IN)
                    }

                }
                countStars = !countStars
                Toast.makeText(ctx, "Você clicou em '$i ESTRELAS'", Toast.LENGTH_SHORT).show()
            }
        }
        ////

        backBtn.setOnClickListener {
            dismiss()
        }

        rc.adapter = CharacterAdapter(
            root.context, mutableListOf(
                //Character(),
                //Character(),
               // Character(),
                //Character(),
            )
        )
        ////////

        // ANIMAÇÃO DE EXPANDIR A IMAGEM

        val animShort = resources.getInteger(android.R.integer.config_shortAnimTime)
        var imgExpandido = root.findViewById<ImageView>(R.id.img_comic_zoom)
        root.findViewById<ImageView>(R.id.iv_comic_capa).also { miniImg ->
            miniImg.setOnClickListener {
                // CHAMA A ANIMAÇÃO
                zoomImage(
                        root,
                        miniImg,
                        imgExpandido!!,
                        animShort
                )
            }
        }

        var title = arguments?.getString("title");
        titulo.text = title

        var finalText = ""
        val source = TranslateLanguage.ENGLISH
        val target = Locale.getDefault().getLanguage().toString();
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(source)
            .setTargetLanguage(target)
            .build()

        val translator = Translation.getClient(options)

        if (arguments?.getString("desc").toString().trim() != "" &&
            source.toString() != target.toString()){


            getLifecycle().addObserver(translator)

            var conditions = DownloadConditions.Builder()
                .requireWifi()
                .build()
            translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener {
                    Log.i("TRANSLATOR", "DOWNLOAD OF MODELS CONCLUDED")
                }
                .addOnFailureListener { exception ->
                    Log.i("TRANSLATOR", "DOWNLOAD OF MODELS FAILED: $exception")
                }

            translator.translate(arguments?.getString("desc")!!)
                .addOnSuccessListener { translatedText ->
                    descricao.text = translatedText
                    Log.i("TRANSLATOR", "translate: successful")
                }
                .addOnFailureListener { exception ->
                    finalText = descricao.text.toString()
                    Log.i("TRANSLATOR", "translate: failed, exception: $exception")
                }

        }

        descricao.text = finalText

        var dataP = arguments?.getString("date");
        dataPub.text = dataP

        var creators = arguments?.getString("creators");
        criadores.text = creators

        var drawers = arguments?.getString("drawers");
        desenhistas.text = drawers

        var cover = arguments?.getString("cover");
        artistasCapa.text = cover

        var urlImg = arguments?.getString("urlImage");
        Glide
            .with(ctx)
            .load(urlImg)
            .placeholder(spinner!!)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(ivCapa)

        Glide
            .with(ctx)
            .load(urlImg)
            .placeholder(spinner!!)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(imgExpandido)
        return root
    }

    // REFERENCIA https://developer.android.com/training/animation/zoom.html
    private var animator: Animator? = null
    fun zoomImage(view: View, miniImg: ImageView, zoomImg: ImageView, animDuration: Int) {
        animator?.cancel() // CANCELA A ANIMAÇÃO ATUAL

        // IMAGEM EXPANDIDA
        // zoomImg.setImageResource(R.drawable.img_comic)

        // POSIÇÔES PARA A ANIMAÇÕES
        val startBoundsInt = Rect()
        val finalBoundsInt = Rect()
        val globalOffset = Point()

        // CALCULO DAS POSIÇÕES
        miniImg.getGlobalVisibleRect(startBoundsInt)
        view.getGlobalVisibleRect(finalBoundsInt, globalOffset)
        startBoundsInt.offset(-globalOffset.x, -globalOffset.y)
        finalBoundsInt.offset(-globalOffset.x, -globalOffset.y)

        // RESULTADO DOS CALCULOS
        val startBounds = RectF(startBoundsInt)
        val finalBounds = RectF(finalBoundsInt)


        // AJUSTES PARA MANTER AS PROPORÇÕES DA IMAGEM
        val startScale: Float
        if ((finalBounds.width() / finalBounds.height() > startBounds.width() / startBounds.height())) {
            // EXTENDER HORIZONTALMENTE
            startScale = startBounds.height() / finalBounds.height()
            val startWidth: Float = startScale * finalBounds.width()
            val deltaWidth: Float = (startWidth - startBounds.width()) / 2
            startBounds.left -= deltaWidth.toInt()
            startBounds.right += deltaWidth.toInt()
        } else {
            // EXTENDER VERTICALMENTE
            startScale = startBounds.width() / finalBounds.width()
            val startHeight: Float = startScale * finalBounds.height()
            val deltaHeight: Float = (startHeight - startBounds.height()) / 2f
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()
        }

        // ESCONDE O IMAGEVIEW PEQUENO
        miniImg.alpha = 0f
        // MOSTRA O IMAGEVIEW EXPANDIDO
        zoomImg.visibility = View.VISIBLE

        // MOVE O PIVOT DAS TRANSFORMAÇÕES PARA O CANTO SUPERIOR ESQUERDO
        zoomImg.pivotX = 0f
        zoomImg.pivotY = 0f

        // ANIMAÇÃO - COPIEI E AJUSTEI OS IDs E PARAMETROS
        animator = AnimatorSet().apply {
            play(
                ObjectAnimator.ofFloat(
                    zoomImg,
                    View.X,
                    startBounds.left,
                    finalBounds.left
                )
            ).apply {
                with(ObjectAnimator.ofFloat(zoomImg, View.Y, startBounds.top, finalBounds.top))
                with(ObjectAnimator.ofFloat(zoomImg, View.SCALE_X, startScale, 1f))
                with(ObjectAnimator.ofFloat(zoomImg, View.SCALE_Y, startScale, 1f))
            }
            duration = animDuration.toLong()
            interpolator = DecelerateInterpolator()
            addListener(object : AnimatorListenerAdapter() {

                override fun onAnimationEnd(animation: Animator) {
                    animator = null
                }

                override fun onAnimationCancel(animation: Animator) {
                    animator = null
                }
            })
            start()
        }

        // EVENTO DE CLIQUE PARA RETORNAR AO NORMAL
        zoomImg.setOnClickListener {
            animator?.cancel()

            // ANIMAÇÃO REVERSA - COPIEI DE NOVO
            animator = AnimatorSet().apply {
                play(ObjectAnimator.ofFloat(zoomImg, View.X, startBounds.left)).apply {
                    with(ObjectAnimator.ofFloat(zoomImg, View.Y, startBounds.top))
                    with(ObjectAnimator.ofFloat(zoomImg, View.SCALE_X, startScale))
                    with(ObjectAnimator.ofFloat(zoomImg, View.SCALE_Y, startScale))
                }
                duration = animDuration.toLong()
                interpolator = DecelerateInterpolator()
                addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        miniImg.alpha = 1f
                        zoomImg.visibility = View.GONE
                        animator = null
                    }

                    override fun onAnimationCancel(animation: Animator) {
                        miniImg.alpha = 1f
                        zoomImg.visibility = View.GONE
                        animator = null
                    }
                })
                start()
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ComicFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}