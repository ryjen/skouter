package com.micrantha.skouter.ui.scan.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.kodein.rememberScreenModel
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.ui.theme.Dimensions
import com.micrantha.skouter.domain.model.Clue
import com.micrantha.skouter.platform.CameraScanner
import com.micrantha.skouter.ui.component.LocationEnabledEffect
import com.micrantha.skouter.ui.scan.preview.ScanAction.SaveScan
import dev.icerock.moko.permissions.Permission.CAMERA
import dev.icerock.moko.permissions.PermissionsController
import org.kodein.di.compose.rememberInstance

class ScanScreen : Screen {
    @Composable
    override fun Content() {
        val permissions by rememberInstance<PermissionsController>()

        LaunchedEffect(Unit) {
            permissions.providePermission(CAMERA)
        }

        LocationEnabledEffect(Unit)

        val viewModel: ScanScreenModel = rememberScreenModel()

        val state by viewModel.state.collectAsState()

        Render(state, viewModel::dispatch)
    }

    @Composable
    private fun Render(state: ScanUiState, dispatch: Dispatch) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CameraScanner(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxSize(),
                state.enabled,
                dispatch
            )

            if (state.clues.isEmpty().not()) {
                ScannedClues(
                    clues = state.clues,
                    modifier = Modifier.align(Alignment.TopEnd)
                )
            }
            Button(
                enabled = state.enabled,
                modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
                    .padding(Dimensions.content),
                onClick = { dispatch(SaveScan) }
            ) {
                Text("Save")
            }
        }
    }

    @Composable
    private fun ScannedClues(modifier: Modifier, clues: List<Clue<*>>) {
        Surface(
            modifier = modifier.padding(Dimensions.screen)
        ) {
            Column(
                modifier = modifier
                    .background(MaterialTheme.colorScheme.surface)
            ) {
                clues.forEach {
                    Text(
                        text = it.display(),
                        maxLines = 1,
                        modifier = Modifier.padding(Dimensions.content)
                    )
                }
            }
        }
    }
}
