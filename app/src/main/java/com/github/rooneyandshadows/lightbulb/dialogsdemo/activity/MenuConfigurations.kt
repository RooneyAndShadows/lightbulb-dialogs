package com.github.rooneyandshadows.lightbulb.dialogsdemo.activity

import android.annotation.SuppressLint
import android.view.View
import com.github.rooneyandshadows.lightbulb.application.activity.BaseActivity
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.SliderMenu
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.config.SliderMenuConfiguration
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.config.SliderMenuConfiguration.*
import com.github.rooneyandshadows.lightbulb.application.activity.slidermenu.items.PrimaryMenuItem
import com.github.rooneyandshadows.lightbulb.commons.utils.ResourceUtils
import com.github.rooneyandshadows.lightbulb.dialogsdemo.R
import com.github.rooneyandshadows.lightbulb.dialogsdemo.activity.MainActivityNavigator.*

object MenuConfigurations {
    @SuppressLint("InflateParams")
    fun getConfiguration(activity: BaseActivity): SliderMenuConfiguration {
        val configuration = SliderMenuConfiguration(HeaderConfiguration(R.layout.demo_drawer_header_view)).apply {
                itemsList.apply {
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_alert_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoAlert().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_loading_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoLoading().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_custom_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoCustom().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_custom_extended_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoCustomExtended().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_adapter_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoAdapter().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_color_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoColorPicker().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_icon_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoIconPicker().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_date_range_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoDateRange().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_date_time_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoDateTime().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_month_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoMonth().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_time_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoTime().replace()
                        }
                    )
                    add(
                        PrimaryMenuItem(
                            -1,
                            ResourceUtils.getPhrase(activity, R.string.demo_chips_title),
                            null,
                            null,
                            1
                        ) { slider: SliderMenu ->
                            slider.closeSlider()
                            route().toDemoChipsPicker().replace()
                        }
                    )
                }
            }
        return configuration
    }
}