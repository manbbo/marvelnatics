package br.com.digitalhouse.marvelnaticos.marvelnatics.ui.comic

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import br.com.digitalhouse.marvelnaticos.marvelnatics.R
import br.com.digitalhouse.marvelnaticos.marvelnatics.adapters.CharacterAdapter
import br.com.digitalhouse.marvelnaticos.marvelnatics.models.Character

class ComicFragment : DialogFragment() {

    private lateinit var ctx: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ctx = context
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

    private var imgExpandido: ImageView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_comic, container, false)

        val rc: RecyclerView = root.findViewById(R.id.rc_comic_characters)
        val backBtn: ImageButton = root.findViewById(R.id.ib_comic_backbtn)

        backBtn.setOnClickListener {
            dismiss()
        }

//        rc.adapter = CharacterAdapter(
//            root.context, mutableListOf(
//                Character(),
//                Character(),
//                Character(),
//                Character(),
//            )
//        )

        // ANIMAÇÃO DE EXPANDIR A IMAGEM

        val animShort = resources.getInteger(android.R.integer.config_shortAnimTime)
        imgExpandido = root.findViewById(R.id.img_comic_zoom);
        root.findViewById<ImageView>(R.id.iv_item_comic).also { miniImg ->
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
        fun newInstance() = ComicFragment().apply { }
    }
}