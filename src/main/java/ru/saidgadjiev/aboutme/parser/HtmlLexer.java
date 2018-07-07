package ru.saidgadjiev.aboutme.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

public class HtmlLexer {

    private HtmlTokenizeState tokenizeState = HtmlTokenizeState.DEFAULT;

    private final String expression;

    private String value = "";

    private int position = -1;

    public HtmlLexer(String expression) {
        this.expression = expression;
    }

    public List<HtmlLexem> tokenize() {
        List<HtmlLexem> lexems = new LinkedList<>();
        int ch;

        while ((ch = get()) != -1) {
            HtmlLexem lexem = nextLexem(ch);

            if (lexem != null) {
                lexems.add(lexem);
            }
        }
        if (this.tokenizeState != HtmlTokenizeState.DEFAULT) {
            lexems.add(createToken(HtmlToken.WORD, false));
        }

        return lexems;
    }

    private HtmlLexem nextLexem(int b) {
        char ch = (char) b;

        switch (tokenizeState) {
            case DEFAULT:
                if (ch == '<') {
                    tokenizeState = HtmlTokenizeState.TAG_START;
                    position -= 1;
                } else {
                    tokenizeState = HtmlTokenizeState.WORD_START;
                    position -= 1;
                }
                break;
            case TAG_START:
                value += ch;
                if (ch == '/') {
                    tokenizeState = HtmlTokenizeState.TAG_CLOSE;
                } else if (ch == '>') {
                    tokenizeState = HtmlTokenizeState.TAG_END;
                    position -= 1;
                }
                break;
            case TAG_CLOSE:
                value += ch;

                if (ch == '>') {
                    return createToken(HtmlToken.TAG_CLOSE, false);
                }
                break;
            case TAG_END:
                return createToken(HtmlToken.TAG_OPEN, false);
            case WORD_START:
                if (ch == '<') {
                    tokenizeState = HtmlTokenizeState.WORD_END;
                    position -= 1;
                } else {
                    value += ch;
                }
                break;
            case WORD_END:
                return createToken(HtmlToken.WORD, true);
        }

        return null;
    }

    private int get() {
        position += 1;
        return position < expression.length() ?
                expression.charAt(position) : -1;
    }

    private HtmlLexem createToken(HtmlToken token, boolean back) {
        HtmlLexem lexem = new HtmlLexem(token, value);
        value = "";

        tokenizeState = HtmlTokenizeState.DEFAULT;
        if (back) {
            position -= 1;
        }

        return lexem;
    }

    public static void main(String[] args) {
        HtmlLexer lexer = new HtmlLexer("fw  efewfewwef <pre><code hljs lang=\"java\">fwe  f  wefwefwfe</code><script></script>");

        System.out.println(lexer.tokenize().toString());
    }
}
