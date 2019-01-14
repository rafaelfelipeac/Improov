package com.rafaelfelipeac.domore.ui.fragments.search

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hookedonplay.decoviewlib.charts.SeriesItem
import com.rafaelfelipeac.domore.R
import com.rafaelfelipeac.domore.ui.activities.MainActivity
import com.rafaelfelipeac.domore.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_metrics.*
import com.hookedonplay.decoviewlib.events.DecoEvent

class SearchFragment : BaseFragment() {

    var cont: Float = 0F

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (activity as MainActivity).supportActionBar?.title = "Metricas"

        return inflater.inflate(R.layout.fragment_metrics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arcView.configureAngles(300, 0) // formato e orientacão

        arcView.addSeries(
            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
                .setRange(0f, 100f, 100f)
                .setInitialVisibility(true)
                .setLineWidth(32f)
                .build())
        // linha cinza
        // caminho total

        val seriesItem1 =
            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
                .setRange(0f, 100f, 0f)
                .setLineWidth(32f)
                .build()
        // linha verde
        // progresso feito

        val series1Index = arcView.addSeries(seriesItem1)

        //arcView.addEvent(DecoEvent.Builder(0F).setIndex(series1Index).setDelay(500).build())

        btnP.setOnClickListener {
            cont += 10

            arcView.addEvent(DecoEvent.Builder(cont).setIndex(series1Index).setDelay(0).build())
        }

        btnN.setOnClickListener {
            cont -= 10

            arcView.addEvent(DecoEvent.Builder(cont).setIndex(series1Index).setDelay(0).build())
        }


//        arcView.addSeries(
//            SeriesItem.Builder(Color.argb(255, 218, 218, 218))
//                .setRange(0f, 100f, 100f)
//                .setInitialVisibility(false)
//                .setLineWidth(32f)
//                .build())
        // linha cinza
        // caminho total

//        val seriesItem1 =
//            SeriesItem.Builder(Color.argb(255, 64, 196, 0))
//            .setRange(0f, 100f, 50f)
//            .setLineWidth(32f)
//               // .setChartStyle(SeriesItem.ChartStyle.STYLE_DONUT)
//            .build()
//
//        // linha verde
//        // progresso feito
//
//        val series1Index = arcView.addSeries(seriesItem1)
//
//        arcView.addEvent(DecoEvent.Builder(DecoEvent.EventType.EVENT_SHOW, true)
//                .setDelay(500)
//                .setDuration(1000)
//                .build()
//        )

//        arcView.addEvent(DecoEvent.Builder(25f).setIndex(series1Index).setDelay(2000).build())
//        arcView.addEvent(DecoEvent.Builder(90f).setIndex(series1Index).setDelay(6000).build())
//        arcView.addEvent(DecoEvent.Builder(10f).setIndex(series1Index).setDelay(10000).build())
//        arcView.addEvent(DecoEvent.Builder(75f).setIndex(series1Index).setDelay(14000).build())
    }

//    private var open: Boolean = false
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        (activity as MainActivity).supportActionBar?.title = "Pesquisar"
//
//        return inflater.inflate(R.layout.fragment_search, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        search_edittext.onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
//            open = !open
//
//            if (!hasFocus) {
//                hideSoftKeyboard(activity!!)
//            }
//
//            verifyOpenOrClose()
//        }
//
//        search_imageview.setOnClickListener {
//            if (search_edittext.text?.isEmpty()!! && !open) {
//                open = !open
//
//                if (open) {
//                    search_edittext.requestFocus()
//                    open = !open
//                }
//
//                verifyOpenOrClose()
//            } else {
//                hideSoftKeyboard(activity!!)
//                Snackbar.make(view, "Pesquisa.", Snackbar.LENGTH_SHORT).show()
//            }
//        }
//
//        frame_layout_search.setOnClickListener {
//            if (open) {
//                open = !open
//
//                verifyOpenOrClose()
//            }
//        }
//    }
//
//    private fun verifyOpenOrClose() = if (open) openSearch() else closeSearch()
//
//    private fun openSearch() {
//        setVerticalBias(search_edittext, 0f)
//        setVerticalBias(search_imageview, 0f)
//
//        showSoftKeyboard(activity!!)
//    }
//
//    private fun closeSearch() {
//        setVerticalBias(search_edittext, 0.5f)
//        setVerticalBias(search_imageview, 0.5f)
//
//        hideSoftKeyboard(activity!!)
//    }
//
//    private fun setVerticalBias(view: View, value: Float) {
//        val params = view.layoutParams as ConstraintLayout.LayoutParams
//        params.verticalBias = value
//        view.layoutParams = params
//    }
}
