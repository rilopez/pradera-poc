package com.pradera.poc.domain.enumeration;

/**
 * The BlockType enumeration.
 */
public enum BlockType {
    TITLE,
    PARAGRAPH,
    CHAPTER;

    public static BlockType fromString(String editorType) {
        switch (editorType) {
            case "heading":
                return TITLE;
            case "paragraph":
                return PARAGRAPH;
            case "chapter":
                return CHAPTER;
        }
        throw new RuntimeException("unknown editor block type:" + editorType);
    }

    public String toEditorType() {
        switch (this) {
            case TITLE:
                return "heading";
            case PARAGRAPH:
                return "paragraph";
            case CHAPTER:
                return "chapter";
        }
        return "";
    }
}
