package com.micrantha.eyespie.ui.dashboard

import com.micrantha.bluebell.domain.model.UiResult
import com.micrantha.eyespie.domain.model.Location
import com.micrantha.eyespie.domain.model.PlayerList
import com.micrantha.eyespie.domain.model.Thing
import com.micrantha.eyespie.domain.model.ThingList

data class DashboardState(
    val playerID: String? = null,
    val location: Location? = null,
    val friends: PlayerList? = null,
    val things: ThingList? = null,
    val players: PlayerList? = null,
    val status: UiResult<Unit> = UiResult.Default
)

data class DashboardUiState(
    val status: UiResult<Data>,
) {
    data class Data(
        val nearby: Nearby,
        val friends: TabContent<PlayerList>
    ) {
        data class TabContent<T>(
            val data: T,
            val hasMore: Boolean
        )

        data class Nearby(
            val players: TabContent<PlayerList>,
            val things: TabContent<ThingList>
        )
    }
}

sealed interface DashboardAction {

    data object ScanNewThing : DashboardAction

    data object HasMoreThings : DashboardAction

    data object HasMorePlayers : DashboardAction

    data object Load : DashboardAction

    data object LoadError : DashboardAction

    data class Loaded(val things: ThingList, val friends: PlayerList, val players: PlayerList) :
        DashboardAction

    data object AddFriendClicked : DashboardAction

    data class GuessThing(val thing: Thing.Listing) : DashboardAction
}
