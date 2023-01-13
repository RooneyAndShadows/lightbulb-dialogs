package com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.icons

import com.github.rooneyandshadows.lightbulb.dialogsdemo.utils.icon.IDemoIcon
import com.mikepenz.iconics.typeface.IIcon
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome
import java.util.*

enum class DemoIcons(val iconName: String, val iconValue: IIcon) : IDemoIcon {
    DEFAULT_ICON_INCOME("DEFAULT_ICON_INCOME", FontAwesome.Icon.faw_angle_double_up),
    DEFAULT_ICON_EXPENSE("DEFAULT_ICON_EXPENSE", FontAwesome.Icon.faw_angle_double_down),
    DEFAULT_ICON_SALARY("DEFAULT_ICON_SALARY", FontAwesome.Icon.faw_briefcase),
    DEFAULT_ICON_UTILITY_BILLS("DEFAULT_ICON_UTILITY_BILLS", FontAwesome.Icon.faw_file_invoice_dollar),
    DEFAULT_ICON_FOOD("DEFAULT_ICON_FOOD", FontAwesome.Icon.faw_utensils),
    DEFAULT_ICON_FOOD_OUTSIDE("DEFAULT_ICON_FOOD_OUTSIDE", FontAwesome.Icon.faw_hamburger),
    DEFAULT_ICON_SMOKING("DEFAULT_ICON_SMOKING", FontAwesome.Icon.faw_smoking),
    DEFAULT_ICON_ALCOHOL("DEFAULT_ICON_ALCOHOL", FontAwesome.Icon.faw_cocktail),
    DEFAULT_ICON_TRANSPORT("DEFAULT_ICON_TRANSPORT", FontAwesome.Icon.faw_bus_alt),
    DEFAULT_ICON_CAR("DEFAULT_ICON_CAR", FontAwesome.Icon.faw_car),
    DEFAULT_ICON_CLOTHES("DEFAULT_ICON_CLOTHES", FontAwesome.Icon.faw_tshirt),
    DEFAULT_ICON_CHILDREN("DEFAULT_ICON_CHILDREN", FontAwesome.Icon.faw_child),
    DEFAULT_ICON_EDUCATION("DEFAULT_ICON_EDUCATION", FontAwesome.Icon.faw_user_graduate),
    DEFAULT_ICON_ENTERTAINMENT("DEFAULT_ICON_ENTERTAINMENT", FontAwesome.Icon.faw_theater_masks),
    DEFAULT_ICON_GIFTS("DEFAULT_ICON_GIFTS", FontAwesome.Icon.faw_gifts),
    DEFAULT_ICON_SPORT("DEFAULT_ICON_SPORT", FontAwesome.Icon.faw_snowboarding),
    DEFAULT_ICON_HOSPITAL("DEFAULT_ICON_HOSPITAL", FontAwesome.Icon.faw_hospital),
    DEFAULT_ICON_PET("DEFAULT_ICON_PET", FontAwesome.Icon.faw_dog),
    DEFAULT_ICON_OTHER("DEFAULT_ICON_OTHER", FontAwesome.Icon.faw_th_large),
    DEFAULT_ICON_SAVING("DEFAULT_ICON_SAVING", FontAwesome.Icon.faw_piggy_bank),
    DEFAULT_ICON_HOUSE("DEFAULT_ICON_HOUSE", FontAwesome.Icon.faw_home),
    DEFAULT_ICON_TRIP("DEFAULT_ICON_TRIP", FontAwesome.Icon.faw_plane),
    ICON_INDUSTRY("ICON_INDUSTRY", FontAwesome.Icon.faw_industry),
    ICON_LAPTOP("ICON_LAPTOP", FontAwesome.Icon.faw_laptop),
    ICON_LEMON("ICON_LEMON", FontAwesome.Icon.faw_lemon),
    ICON_LIFE_RING("ICON_LIFE_RING", FontAwesome.Icon.faw_life_ring),
    ICON_LUGGAGE("ICON_LUGGAGE", FontAwesome.Icon.faw_luggage_cart),
    ICON_MOTORCYCLE("ICON_MOTORCYCLE", FontAwesome.Icon.faw_motorcycle),
    ICON_BIKING("ICON_BIKING", FontAwesome.Icon.faw_biking),
    ICON_PAINTING("ICON_PAINTING", FontAwesome.Icon.faw_paint_roller),
    ICON_PHARMACY("ICON_PHARMACY", FontAwesome.Icon.faw_prescription_bottle),
    ICON_PHARMACY_ALT("ICON_PHARMACY_ALT", FontAwesome.Icon.faw_capsules),
    ICON_PUZZLE_GAME("ICON_PUZZLE_GAME", FontAwesome.Icon.faw_puzzle_piece),
    ICON_ROBOT("ICON_ROBOT", FontAwesome.Icon.faw_robot),
    ICON_TRAVELING("ICON_TRAVELING", FontAwesome.Icon.faw_route),
    ICON_TRAVELING_ALT("ICON_TRAVELING_ALT", FontAwesome.Icon.faw_fly),
    ICON_TRAVELING_ALT1("ICON_TRAVELING_ALT1", FontAwesome.Icon.faw_ship),
    ICON_HOUSE_WORK("ICON_HOUSE_WORK", FontAwesome.Icon.faw_screwdriver),
    ICON_HOUSE_WORK_ALT("ICON_HOUSE_WORK_ALT", FontAwesome.Icon.faw_hammer),
    ICON_SHIPPING("ICON_SHIPPING", FontAwesome.Icon.faw_shipping_fast),
    ICON_SKIING("ICON_SKIING", FontAwesome.Icon.faw_skiing),
    ICON_SMS("ICON_SMS", FontAwesome.Icon.faw_sms),
    ICON_SOCKS("ICON_SOCKS", FontAwesome.Icon.faw_socks),
    ICON_SPA("ICON_SPA", FontAwesome.Icon.faw_spa),
    ICON_SHOPPING("ICON_SHOPPING", FontAwesome.Icon.faw_store),
    ICON_TABLET("ICON_TABLET", FontAwesome.Icon.faw_tablet_alt),
    ICON_TV("ICON_TV", FontAwesome.Icon.faw_tv),
    ICON_TAXI("ICON_TAXI", FontAwesome.Icon.faw_taxi),
    ICON_TREE("ICON_TREE", FontAwesome.Icon.faw_tree),
    ICON_DRUM("ICON_DRUM", FontAwesome.Icon.faw_drum),
    ICON_GUITAR("ICON_GUITAR", FontAwesome.Icon.faw_guitar),
    ICON_DUMPSTER("ICON_DUMPSTER", FontAwesome.Icon.faw_dumpster),
    ICON_FITNESS("ICON_FITNESS", FontAwesome.Icon.faw_dumbbell),
    ICON_MAIL("ICON_MAIL", FontAwesome.Icon.faw_envelope),
    ICON_GAMES("ICON_GAMES", FontAwesome.Icon.faw_gamepad),
    ICON_JEWELRY("ICON_JEWELRY", FontAwesome.Icon.faw_gem),
    ICON_WINE("ICON_WINE", FontAwesome.Icon.faw_glass_cheers),
    ICON_BEER("ICON_BEER", FontAwesome.Icon.faw_beer),
    ICON_GLASSES("ICON_GLASSES", FontAwesome.Icon.faw_glasses),
    ICON_LOVE("ICON_LOVE", FontAwesome.Icon.faw_heart),
    ICON_COFFEE("ICON_COFFEE", FontAwesome.Icon.faw_coffee),
    ICON_COINS("ICON_COINS", FontAwesome.Icon.faw_coins),
    ICON_FURNITURE("ICON_FURNITURE", FontAwesome.Icon.faw_couch),
    ICON_CANDY("ICON_CANDY", FontAwesome.Icon.faw_candy_cane),
    ICON_CANDY_ALT("ICON_CANDY_ALT", FontAwesome.Icon.faw_cookie),
    ICON_GAMBLING("ICON_GAMBLING", FontAwesome.Icon.faw_dice),
    ICON_TRADING("ICON_TRADING", FontAwesome.Icon.faw_balance_scale),
    ICON_CRYPTO("ICON_CRYPTO", FontAwesome.Icon.faw_bitcoin),
    ICON_BOOKS("ICON_BOOKS", FontAwesome.Icon.faw_book),
    ICON_BOWLING("ICON_BOWLING", FontAwesome.Icon.faw_bowling_ball),
    ICON_FOOD("ICON_FOOD", FontAwesome.Icon.faw_bread_slice),
    ICON_RESIDENCE("ICON_RESIDENCE", FontAwesome.Icon.faw_building),
    ICON_PHOTOS("ICON_PHOTOS", FontAwesome.Icon.faw_camera),
    ICON_PET("ICON_PET", FontAwesome.Icon.faw_cat);

    override val icon: IIcon
        get() = iconValue

    companion object {

        @JvmStatic
        fun getByName(iconName: String): DemoIcons {
            return DemoIcons.values().first { it.iconName == iconName }
        }

        @JvmStatic
        val random: DemoIcons
            get() = values()[Random().nextInt(values().size)]
    }
}