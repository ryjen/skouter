package com.micrantha.bluebell.domain.ext

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.arch.Store
import com.micrantha.bluebell.domain.flux.Flux
import kotlinx.coroutines.Dispatchers
import org.kodein.di.compose.rememberInstance
import kotlin.coroutines.CoroutineContext

fun Dispatcher.dispatch(vararg actions: Action) = actions.forEach { dispatch(it) }

@Composable
fun <State> rememberStore(
    state: State,
    coroutineContext: CoroutineContext = Dispatchers.Default
): Store<State> {
    val flux: Flux by rememberInstance()
    val scope = rememberCoroutineScope { coroutineContext }
    return remember { flux.createStore(state, scope) }
}

