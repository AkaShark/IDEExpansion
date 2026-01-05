package utils

import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import java.nio.file.Path
import kotlin.io.path.Path


object PathResolver {
    fun resolve(
        project: Project,
        editor: Editor? = null,
        file: VirtualFile? = null
    ): FileRelativePath? {
        val editorManager = FileEditorManager.getInstance(project)
        val targetFile = file ?: editorManager.selectedFiles.firstOrNull() ?: return null
        val relativePath = resolveRelativePath(project, targetFile) ?: return null

        val targetEditor = editor ?: editorManager.selectedTextEditor
        val lineRange = targetEditor?.let { resolveSelectionLineRange(it) }

        return FileRelativePath(relativePath, lineRange)
    }

    fun formatInsertText(payload: FileRelativePath): String {
        return buildString {
            append(payload.relativePath)
            payload.lineRange?.let { range ->
                append(':')
                append(range.start)
                range.end?.takeIf { it != range.start }?.let { end ->
                    append('-')
                    append(end)
                }
            }
            append(' ')
        }
    }

    private fun resolveRelativePath(project: Project, file: VirtualFile): String? {
        val rawPath = file.canonicalPath ?: file.presentableUrl ?: file.path
        val basePath  = project.basePath ?: rawPath
        return runCatching {
            val base = Path.of(basePath).normalize()
            val target = Path.of(rawPath).normalize()
            if (target.startsWith(base)) base.relativize(target).toString() else rawPath
        }.getOrElse {
            rawPath
        }
    }

    private fun resolveSelectionLineRange(editor: Editor): LineRange? {
        val selectionModel = editor.selectionModel
        if (!selectionModel.hasSelection() || selectionModel.selectedText.isNullOrEmpty()) {
            return null
        }

        return toLineRange(editor.document, selectionModel.selectionStart, selectionModel.selectionEnd)
    }

    private fun toLineRange(document: Document, startOffset: Int, endOffset: Int): LineRange? {
        if (startOffset < 0 || endOffset < startOffset) {
            return null
        }

        val startLine = runCatching { document.getLineNumber(startOffset) }.getOrElse {
            return null
        }

        val adjustedEnd = when {
            endOffset <= startOffset -> startOffset
            endOffset == document.textLength -> endOffset
            else -> endOffset - 1
        }

        val endLine = runCatching { document.getLineNumber(adjustedEnd.coerceAtLeast(startOffset)) }.getOrElse {
            startLine
        }

        val start = startLine + 1
        val end = (endLine + 1).takeIf { it > start }
        return LineRange(start, end)
    }
}


data class FileRelativePath(val relativePath: String, val lineRange: LineRange?)

data class LineRange(val start: Int, val end: Int?)