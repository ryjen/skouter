package com.micrantha.skouter.ui.scan

import androidx.compose.ui.graphics.painter.Painter
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.skouter.domain.model.Clues
import okio.Path

data class ScanState(
    val status: UiResult<Unit> = UiResult.Default,
    val clues: Clues? = null,
    val image: Path? = null,
    val name: String = ""
)

data class ScanUiState(
    val status: UiResult<Data> = UiResult.Default
) {
    data class Data(
        val clues: Clues?,
        val image: () -> Painter,
        val name: String = "",
    )
}

sealed class ScanAction : Action {
    object Init : ScanAction()

    object SaveScan : ScanAction()

    data class NameChanged(val data: String) : ScanAction()

    data class ImageCaptured(val path: Path) : ScanAction()

    data class NoCamera(val err: Throwable) : ScanAction()

    data class ScanError(val err: Throwable) : ScanAction()

    data class ScannedClues(val data: Clues) : ScanAction()

    object TestSave : ScanAction()
}
