package com.micrantha.skouter.ui.scan.preview

import com.micrantha.bluebell.data.Log
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Effect
import com.micrantha.bluebell.domain.arch.Reducer
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options.Replace
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.skouter.data.account.model.CurrentSession
import com.micrantha.skouter.domain.repository.LocationRepository
import com.micrantha.skouter.ui.component.combine
import com.micrantha.skouter.ui.scan.edit.ScanEditScreen
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditSaved
import com.micrantha.skouter.ui.scan.preview.ScanAction.EditScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ImageSaved
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveError
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScanSavable
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedColor
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedDetection
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedImage
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedLabel
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedMatch
import com.micrantha.skouter.ui.scan.preview.ScanAction.ScannedSegment
import com.micrantha.skouter.ui.scan.usecase.AnalyzeCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.EditCaptureUseCase
import com.micrantha.skouter.ui.scan.usecase.SaveCaptureUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ScanEnvironment(
    private val scope: CoroutineScope,
    private val context: ScreenContext,
    private val editCaptureUseCase: EditCaptureUseCase,
    private val saveCaptureUseCase: SaveCaptureUseCase,
    private val analyzeCaptureUseCase: AnalyzeCaptureUseCase,
    private val currentSession: CurrentSession,
    private val locationRepository: LocationRepository,
    private val mapper: ScanStateMapper,
) : Reducer<ScanState>, Effect<ScanState>,
    Router by context.router,
    FileSystem by context.fileSystem,
    Dispatcher by context.dispatcher,
    LocalizedRepository by context.i18n {

    override suspend fun invoke(action: Action, state: ScanState) {
        when (action) {
            is EditScan -> editCaptureUseCase(
                state.image!!
            ).onSuccess { url ->
                Log.d("image url: $url")
                dispatch(EditSaved(url))
            }.onFailure {
                Log.d("unable to save scan", it)
                dispatch(SaveError)
            }

            is EditSaved -> navigate(
                ScanEditScreen(
                    context = context,
                    proof = mapper.prove(state)
                ), options = Replace
            )

            is ImageSaved -> saveCaptureUseCase(mapper.prove(state))
                .onFailure {
                    Log.e("unable to save scan", it)
                    dispatch(SaveError)
                }
                .onSuccess {
                    navigateBack()
                }

            is SaveScan -> editCaptureUseCase(
                state.image!!
            ).onSuccess { url ->
                dispatch(ImageSaved(url))
            }.onFailure {
                dispatch(SaveError)
            }

            is ScannedImage -> analyzeCaptureUseCase(action.image)
                .onEach { res -> res.onSuccess(::dispatch) }
                .launchIn(scope)

            is ScanAction.Back -> context.router.navigateBack()
        }
    }

    override fun reduce(state: ScanState, action: Action) = when (action) {
        is ScannedImage -> state.copy(
            image = action.image,
        )

        is ScannedLabel -> state.copy(
            labels = state.labels.combine(action.label)
        )

        is ScannedColor -> state.copy(
            colors = state.colors.combine(action.color)
        )

        is ScannedDetection -> state.copy(
            detection = action.detection,
            labels = state.labels.combine(action.detection.labels)
        )

        is ScannedSegment -> state.copy(
            segment = action.segment
        )

        is ScannedMatch -> state.copy(
            match = action.match.firstOrNull()
        )

        is ScanSavable -> state.copy(
            path = action.path,
        )

        is SaveScan, is EditScan -> state.copy(
            playerID = currentSession.requirePlayer().id,
            location = locationRepository.currentLocation()
                ?: currentSession.requirePlayer().location,
            enabled = false,
        )

        is SaveError -> state.copy(
            enabled = true
        )

        else -> state
    }
}
