package com.micrantha.skouter.domain.repository

import com.micrantha.skouter.domain.model.Location
import com.micrantha.skouter.domain.model.Proof
import com.micrantha.skouter.domain.model.Thing
import com.micrantha.skouter.domain.model.ThingList

interface ThingsRepository {

    suspend fun things(playerID: String): Result<ThingList>

    suspend fun create(name: String, url: String, proof: Proof, playerID: String): Result<Thing>

    suspend fun nearby(
        location: Location.Point,
        distance: Double = 10.0
    ): Result<ThingList>
}
