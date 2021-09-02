package com.mistletoe.multi.theme.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import com.android.build.gradle.BaseExtension

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