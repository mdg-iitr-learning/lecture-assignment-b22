package com.amishgarg.wartube.Activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.amishgarg.wartube.R
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.BarGraphSeries
import com.jjoe64.graphview.series.DataPoint

import androidx.lifecycle.Observer
import com.amishgarg.wartube.PicassoUtil
import com.amishgarg.wartube.ViewModels.StatsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.card.MaterialCardView
import com.squareup.picasso.Picasso


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [StatsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [StatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

class StatsFragment : Fragment() {


    private lateinit var viewModel : StatsViewModel

    lateinit var subsPdp:TextView
    lateinit var  subsTS:TextView
    lateinit var  logoPDP : ImageView
    lateinit var  logoTS : ImageView
    lateinit var  diffTextView:TextView
    //lateinit var  graphView:GraphView
    lateinit var card1  : MaterialCardView
    lateinit var card2 : MaterialCardView
    lateinit var chart: BarChart
    lateinit var set1 : BarDataSet
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)

        return inflater.inflate(R.layout.fragment_stats, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subsPdp= view!!.findViewById(R.id.subsPdp)
        subsTS = view!!.findViewById(R.id.subsTS)
        logoPDP  = view!!.findViewById<ImageView>(R.id.img_pdp)
        logoTS = view!!.findViewById<ImageView>(R.id.img_ts)
        diffTextView = view!!.findViewById(R.id.diff_text)
      //  graphView = view!!.findViewById(R.id.graph)
        card1 = view!!.findViewById(R.id.card1)
        card2 = view!!.findViewById(R.id.card2)
        chart = view!!.findViewById(R.id.chart1)
        PicassoUtil.loadImagePicasso("https://yt3.ggpht.com/3Ss-aMQD695qaWBSWMy1mt6aNrIs5kIlL78Ccf_YGO4OHV1txzdWGy5J5bCUu7-T5MXJT3_W=w1280-fcrop64=1,32b75a57cd48a5a8-nd-c0xffffffff-rj-k-no", view!!.findViewById(R.id.bannerP))
        PicassoUtil.loadImagePicasso("https://yt3.ggpht.com/pZI010B_jIyHwAM7qDEpxZ5Zc4zc7StRe5USIB_QKc17k9NW_oSy0afhxd_jgc7pM3Mqn6FP=w1280-fcrop64=1,32b75a57cd48a5a8-nd-c0xffffffff-rj-k-no", view!!.findViewById(R.id.bannerT))

        card1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UC-lHJZR3Gqxm24_Vd_AJ5Yw"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)

        }

        card2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCq-Fj5jknLsUf-MWSy4_brA"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.setPackage("com.google.android.youtube")
            startActivity(intent)

        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
      /*  val series = BarGraphSeries(arrayOf(DataPoint(0.2, 2.0), DataPoint(1.0, 5.0), DataPoint(2.0, 3.0)))
        graphView.addSeries(series)
        series.setDrawValuesOnTop(true)
        series.setSpacing(25);
        graphViewSettings(this!!.graphView!!)*/



        Picasso.get().load("https://yt3.ggpht.com/a-/AN66SAztY6oYWZnS1Cae9o4_msEE1H83Tld5cFtl3Q=s240-mo-c-c0xffffffff-rj-k-no").into(logoPDP);
        Picasso.get().load("https://yt3.ggpht.com/a-/AN66SAxPfKnfHAnAs0rOqaSwINOxDYJsyj-gPBP0OQ=s240-mo-c-c0xffffffff-rj-k-no").into(logoTS)

        viewModel = ViewModelProviders.of(this).get(StatsViewModel::class.java)



        val subsObserver: Observer<List<Int>> = Observer {
            subsPdp.text = it[0].toString()
            subsTS.text = it[1].toString()
            diffTextView.text = it[2].toString()
           /* var values = arrayOf(DataPoint(1.0, it[0].toDouble()), DataPoint(2.0, it[1].toDouble()), DataPoint(3.0, it[2].toDouble()))
            series.resetData(values)*/




            val values : ArrayList<BarEntry> = ArrayList()
            values.add(BarEntry(1.0f,it[0].toFloat()))
            values.add(BarEntry(2.0f,it[1].toFloat()))
            values.add(BarEntry(3.0f,it[2].toFloat()))
            if (chart.data != null && chart.data.dataSetCount > 0) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set1.setValues(values)
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
                chart.invalidate()

            } else {
                set1 = BarDataSet(values, "Realtime Chart")

                set1.setDrawIcons(false)


                //*int startColor = ContextCompat.getColor(this, android.R.color.holo_blue_dark);
                val endColor = ContextCompat.getColor(context!!, android.R.color.holo_blue_bright);
                //set1.setGradientColor(startColor1, endColor1)
                val startColor1 = ContextCompat.getColor(context!!, android.R.color.holo_orange_light)
                val startColor2 = ContextCompat.getColor(context!!, android.R.color.holo_blue_light)
                val startColor3 = ContextCompat.getColor(context!!, android.R.color.holo_orange_light)
                val startColor4 = ContextCompat.getColor(context!!, android.R.color.holo_green_light)
                val startColor5 = ContextCompat.getColor(context!!, android.R.color.holo_red_light)
                val endColor1 = ContextCompat.getColor(context!!, android.R.color.holo_blue_dark)
                val endColor2 = ContextCompat.getColor(context!!, android.R.color.holo_purple)
                val endColor3 = ContextCompat.getColor(context!!, android.R.color.holo_green_dark)
                val endColor4 = ContextCompat.getColor(context!!, android.R.color.holo_red_dark)
                val endColor5 = ContextCompat.getColor(context!!, android.R.color.holo_orange_dark)

                set1.colors =  listOf(startColor4,startColor5,endColor1)

/*

                val gradientColors = java.util.ArrayList<GradientColor>()
                gradientColors.add(GradientColor(startColor1, endColor1))
                gradientColors.add(GradientColor(startColor2, endColor2))
                gradientColors.add(GradientColor(startColor3, endColor3))
                gradientColors.add(GradientColor(startColor4, endColor4))
                gradientColors.add(GradientColor(startColor5, endColor5))
*/

                //set1.setGradientColors(gradientColors)

                val dataSets = java.util.ArrayList<IBarDataSet>()
                dataSets.add(set1)

                val data = BarData(dataSets)
                data.setValueTextSize(10f)
                data.barWidth = 0.9f

                chart.data = data
            }
           // chart.notifyDataSetChanged()

//            series.setValuesOnTopColor(R.color.colorPrimaryDark);
        }

        viewModel.subsData.observe(this, subsObserver)

    }

    /*fun graphViewSettings(graphView: GraphView)
    {
        // set manual X bounds
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setMinY(0.0);
        graphView.getViewport().setMaxY(100000000.0);


        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setMinX(0.0);
        graphView.getViewport().setMaxX(4.0);
        graphView.getViewport().setScrollable(true); // enables horizontal scrolling
        graphView.getViewport().setScrollableY(true); // enables vertical scrolling
    }*/
}









