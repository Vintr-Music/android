package pw.vintr.music.domain.server.useCase.accessControl

import android.graphics.Bitmap
import android.graphics.Color
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.Dimension
import com.google.zxing.qrcode.QRCodeWriter
import pw.vintr.music.data.server.dto.ConnectNewServerRequestDto
import pw.vintr.music.domain.server.model.ServerInviteModel
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetServerInviteQRUseCase {

    private val qrWriter = QRCodeWriter()

    suspend operator fun invoke(
        inviteModel: ServerInviteModel,
        dimension: Dimension = Dimension(1000, 1000)
    ): Bitmap = suspendCoroutine {
        val inviteJson = Gson().toJson(
            ConnectNewServerRequestDto(
                serverName = inviteModel.server.name,
                code = inviteModel.code.toString(),
            )
        )
        val bitMatrix = qrWriter.encode(
            inviteJson,
            BarcodeFormat.QR_CODE,
            dimension.width,
            dimension.height
        )

        val w = bitMatrix.width
        val h = bitMatrix.height
        val pixels = IntArray(size = w * h)

        for (y in 0 until h) {
            for (x in 0 until w) {
                pixels[y * w + x] = if (bitMatrix.get(x, y)) {
                    Color.BLACK
                } else {
                    Color.WHITE
                }
            }
        }

        it.resume(
            Bitmap
                .createBitmap(w, h, Bitmap.Config.ARGB_8888)
                .apply { setPixels(pixels, 0, w, 0, 0, w, h) }
        )
    }
}
