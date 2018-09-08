/*
 * Copyright 2010-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.maven

import com.intellij.openapi.externalSystem.service.project.IdeModifiableModelsProvider
import com.intellij.openapi.module.Module
import org.jetbrains.idea.maven.importing.MavenImporter
import org.jetbrains.idea.maven.importing.MavenRootModelAdapter
import org.jetbrains.idea.maven.project.MavenProject
import org.jetbrains.idea.maven.project.MavenProjectChanges
import org.jetbrains.idea.maven.project.MavenProjectsProcessorTask
import org.jetbrains.idea.maven.project.MavenProjectsTree
import org.jetbrains.kotlin.idea.formatter.KotlinObsoleteCodeStyle
import org.jetbrains.kotlin.idea.formatter.KotlinStyleGuideCodeStyle
import org.jetbrains.kotlin.idea.formatter.ProjectCodeStyleImporter
import org.jetbrains.kotlin.idea.maven.KotlinMavenImporter.Companion.KOTLIN_PLUGIN_ARTIFACT_ID
import org.jetbrains.kotlin.idea.maven.KotlinMavenImporter.Companion.KOTLIN_PLUGIN_GROUP_ID

class KotlinCodeStyleMavenImporter : MavenImporter(KOTLIN_PLUGIN_GROUP_ID, KOTLIN_PLUGIN_ARTIFACT_ID) {
    override fun isApplicable(mavenProject: MavenProject): Boolean {
        return mavenProject.properties.getProperty("kotlin.code.style") != null
    }

    override fun preProcess(
        module: Module?,
        mavenProject: MavenProject?,
        changes: MavenProjectChanges?,
        modifiableModelsProvider: IdeModifiableModelsProvider?
    ) {
        // Do nothing
    }

    override fun process(
        modifiableModelsProvider: IdeModifiableModelsProvider?,
        module: Module,
        rootModel: MavenRootModelAdapter?,
        mavenModel: MavenProjectsTree,
        mavenProject: MavenProject,
        changes: MavenProjectChanges?,
        mavenProjectToModuleName: MutableMap<MavenProject, String>?,
        postTasks: MutableList<MavenProjectsProcessorTask>
    ) {
        if (mavenProject !in mavenModel.rootProjects) return

        val codeStyleStr = mavenProject.properties.getProperty("kotlin.code.style")
        val selectedCodeStyle = when (codeStyleStr) {
            KotlinObsoleteCodeStyle.CODE_STYLE_SETTING -> KotlinObsoleteCodeStyle.INSTANCE
            KotlinStyleGuideCodeStyle.CODE_STYLE_SETTING -> KotlinStyleGuideCodeStyle.INSTANCE
            else -> null
        }

        if (selectedCodeStyle != null) {
            ProjectCodeStyleImporter.apply(module.project, selectedCodeStyle)
        }
    }
}