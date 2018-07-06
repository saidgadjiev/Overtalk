package ru.saidgadjiev.aboutme.parser;

public class HtmlLexem {

    private final HtmlToken token;

    private final String value;

    public HtmlLexem(HtmlToken token, String value) {
        this.token = token;
        this.value = value;
    }

    public HtmlToken getToken() {
        return token;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "HtmlLexem{" +
                "token=" + token +
                ", value='" + value + '\'' +
                '}';
    }
}
