package com.micrantha.skouter

import android.content.Context
import com.micrantha.bluebell.get
import com.micrantha.bluebell.platform.ConnectivityStatus
import com.micrantha.bluebell.platform.Platform
import com.micrantha.skouter.platform.scan.analyzer.ColorCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.DetectCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.LabelCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.MatchCaptureAnalyzer
import com.micrantha.skouter.platform.scan.analyzer.SegmentCaptureAnalyzer
import org.kodein.di.DI
import org.kodein.di.bindInstance
import org.kodein.di.bindProvider
import org.kodein.di.bindProviderOf
import org.kodein.di.bindSingletonOf

fun androidDependencies(
    context: Context,
) = DI {
    bindInstance { context }

    bindProviderOf(::ConnectivityStatus)

    bindSingletonOf(::Platform)

    bindProvider {
        LabelCaptureAnalyzer(get())
    }

    bindProvider {
        ColorCaptureAnalyzer(get())
    }
    bindProviderOf(::DetectCaptureAnalyzer)

    bindProviderOf(::SegmentCaptureAnalyzer)

    bindProviderOf(::MatchCaptureAnalyzer)
}
