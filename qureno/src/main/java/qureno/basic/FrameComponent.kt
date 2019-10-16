package qureno.basic

import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import qureno.components.Component

fun <T> frameComponent(vararg children: Component<T>): Component<T> =
    orderedViewGroupComponent(children, ::FrameLayout)

fun <T> verticalComponent(vararg children: Component<T>): Component<T> =
    orderedViewGroupComponent(children) { LinearLayout(it).apply { orientation = VERTICAL } }
