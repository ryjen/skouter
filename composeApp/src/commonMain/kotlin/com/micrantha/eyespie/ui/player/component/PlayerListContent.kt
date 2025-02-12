package com.micrantha.eyespie.ui.player.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.i18n.stringResource
import com.micrantha.bluebell.ui.components.status.EmptyContent
import com.micrantha.eyespie.domain.model.PlayerList
import com.micrantha.eyespie.ui.component.S

@Composable
fun PlayerListContent(players: PlayerList, dispatch: Dispatch) {
    when {
        players.isEmpty() -> EmptyContent(stringResource(S.NoPlayersFound))
        else -> LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(players, key = { it.id }) {
                PlayerListCard(player = it)
            }
        }
    }
}
