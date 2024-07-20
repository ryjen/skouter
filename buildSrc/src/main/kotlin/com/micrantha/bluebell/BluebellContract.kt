package com.micrantha.bluebell

open class BluebellConfig {
    var packageName: String = "com.micrantha.bluebell"
    var className: String = "AppConfig"
    var envVarName: String = "BLUEBELL"

    override fun toString() = "($packageName.$className)"
}

open class BluebellModels {
    var outputPaths: Array<String> = arrayOf(
        "composeApp/src/androidMain/assets/models",
        "iosApp/iosApp/Models"
    )
    var downloadPath: String = "models"

    //var ids: List<String> = emptyList()
    var files: Map<String, String> = emptyMap()
}
