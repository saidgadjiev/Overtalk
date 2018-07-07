package ru.saidgadjiev.aboutme.parser;

public enum HtmlTokenizeState {

    DEFAULT,
    TAG_START,
    TAG_CLOSE,
    TAG_END,
    WORD_START,
    WORD_END
}
