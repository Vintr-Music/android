package pw.vintr.music.tools.extension

import android.content.res.Resources

fun Int.pxToDpFloat(): Float = this / Resources.getSystem().displayMetrics.density
