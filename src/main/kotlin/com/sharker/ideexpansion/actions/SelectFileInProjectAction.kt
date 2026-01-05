package com.sharker.ideexpansion.actions

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware


internal class SelectFileInProjectAction: AnAction(
    "Select Opened File in Project View",
    "Reveal current file in Project tool window",
    null
), DumbAware {

    override fun actionPerformed(e: AnActionEvent) {
        println("xxxx");
        ActionManager.getInstance()
            .getAction("SelectOpenedFileInProjectView")
            ?.actionPerformed(e)
    }

    override fun update(e: AnActionEvent) {
        ActionManager.getInstance()
            .getAction("SelectOpenedFileInProjectView")
            ?.update(e)
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
}