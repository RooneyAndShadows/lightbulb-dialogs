package com.github.rooneyandshadows.lightbulb.dialogsdemo.activity

import android.annotation.SuppressLint
import android.view.View
import com.github.rooneyandshadows.lightbulb.application.activity.BaseActivity
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.SliderMenu
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.config.SliderMenuConfiguration
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.items.PrimaryMenuItem
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R

object MenuConfigurations {
    @SuppressLint("InflateParams")
    fun getConfiguration(activity: BaseActivity): SliderMenuConfiguration {
        val headingView: View = activity.layoutInflater.inflate(R.layout.demo_drawer_header_view, null)
        val configuration = SliderMenuConfiguration()
        configuration.withHeaderView(headingView)
        configuration.addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_alert_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoAlert().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_loading_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoLoading().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_adapter_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoAdapter().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_color_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoColorPicker().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_icon_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoIconPicker().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_date_range_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoDateRange().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_date_time_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoDateTime().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_month_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoMonth().replace()
            }
        ).addMenuItem(
            PrimaryMenuItem(
                -1,
                ResourceUtils.getPhrase(activity, R.string.demo_time_title),
                null,
                null,
                1
            ) { slider: SliderMenu ->
                slider.closeSlider()
                MainActivityNavigator.route().toDemoTime().replace()
            }
        )
        return configuration
    }
}