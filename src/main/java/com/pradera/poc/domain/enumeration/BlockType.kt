package com.pradera.poc.domain.enumeration

import java.lang.RuntimeException

/**
 * The BlockType enumeration.
 */
enum class BlockType {
    TITLE, PARAGRAPH, CHAPTER;

    fun toEditorType(): String {
        return when (this) {
            TITLE -> "heading"
            PARAGRAPH -> "paragraph"
            CHAPTER -> "chapter"
        }
    }

    companion object {
        @JvmStatic
        fun fromString(editorType: String): BlockType {
            return when (editorType) {
                "heading" -> TITLE
                "paragraph" -> PARAGRAPH
                "chapter" -> CHAPTER
                else -> null
            } ?: throw RuntimeException("unknown editor block type:$editorType")
        }
    }
}
