package com.micrantha.eyespie.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import com.micrantha.bluebell.domain.arch.Action
import com.micrantha.bluebell.domain.arch.Dispatch
import com.micrantha.bluebell.domain.arch.Dispatcher
import com.micrantha.bluebell.domain.i18n.LocalizedRepository
import com.micrantha.bluebell.domain.i18n.LocalizedString
import com.micrantha.bluebell.platform.FileSystem
import com.micrantha.bluebell.ui.components.Router
import com.micrantha.bluebell.ui.components.Router.Options
import com.micrantha.bluebell.ui.components.StateRenderer
import com.micrantha.bluebell.ui.screen.LocalDispatcher
import com.micrantha.bluebell.ui.screen.LocalScreenContext
import com.micrantha.bluebell.ui.screen.ScreenContext
import com.micrantha.eyespie.androidDependencies
import okio.Path
import org.kodein.di.DI

class PreviewContext(
    context: Context
) : ScreenContext, Dispatch {
    override val i18n: LocalizedRepository = object : LocalizedRepository {
        override fun resource(str: LocalizedString, vararg args: Any?): String = str.key

        override fun format(
            epochSeconds: Long,
            format: String,
            timeZone: String,
            locale: String
        ): String = format

    }
    override val router: Router = object : Router {
        override fun navigateBack(): Boolean = false
        override val canGoBack: Boolean = false

        override fun <T : Screen> navigate(screen: T, options: Options) = Unit

        override val screen: Screen = object : Screen {
            @Composable
            override fun Content() = Unit
        }

    }
    override val dispatcher: Dispatcher = object : Dispatcher {
        override fun dispatch(action: Action) = Unit
        override suspend fun send(action: Action) = Unit
    }

    override suspend fun send(action: Action) = dispatcher.send(action)
    override fun dispatch(action: Action) = dispatcher.dispatch(action)

    override val fileSystem: FileSystem = object : FileSystem {
        override fun write(path: Path, data: ByteArray) = Unit
        override fun read(path: Path): ByteArray = byteArrayOf()
    }

    override val di: DI = androidDependencies(context)
}

@Composable
fun <State> PreviewContext(state: State, renderer: (ScreenContext) -> StateRenderer<State>) {

    val screenContext = PreviewContext(LocalContext.current)

    CompositionLocalProvider(
        LocalScreenContext provides screenContext,
        LocalDispatcher provides screenContext
    ) {
        renderer(screenContext).Render(
            state = state,
            dispatch = screenContext
        )
    }
}
