package com.magic.theme.resource.proxy

import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Created by mistletoe
 * on 2021/9/1
 **/
class MultiThemePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val module = project.extensions.getByType(BaseExtension::class.java)
        module.registerTransform(ReplaceResourceTransform())
    }
}