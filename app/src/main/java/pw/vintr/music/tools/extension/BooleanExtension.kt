package pw.vintr.music.tools.extension

import pw.vintr.music.R

fun Boolean.toStringRes() = if (this) R.string.common_yes else R.string.common_no
