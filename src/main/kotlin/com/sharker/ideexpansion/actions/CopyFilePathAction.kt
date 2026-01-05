package com.sharker.ideexpansion.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.IconLoader
import utils.PathResolver
import java.awt.datatransfer.StringSelection

internal class CopyFilePathAction : AnAction(
    "Copy Current Editing File Path",
    "Copy current editing file path to clipboard",
    IconLoader.getIcon("/icons/copy_action.svg", CopyFilePathAction::class.java)
), DumbAware {
    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val editor = event.getData(CommonDataKeys.EDITOR)
        val virtualFile = event.getData(CommonDataKeys.VIRTUAL_FILE)
        if (project == null || editor == null) {
            return
        }

        val fileRelativePath = PathResolver.resolve(
            project = project,
            editor = editor,
            file = virtualFile
        )

        if (fileRelativePath == null) {
            return
        }

        val fileRelativePathText = PathResolver.formatInsertText(fileRelativePath)
        CopyPasteManager.getInstance().setContents(StringSelection(fileRelativePathText))
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.EDT



}
