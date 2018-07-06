package ru.saidgadjiev.aboutme.parser;

import java.util.List;

public class HtmlParser {

    public String parse(List<HtmlLexem> lexems) {
        StringBuilder parsed = new StringBuilder();

        for (HtmlLexem lexem: lexems) {
            switch (lexem.getToken()) {
                case WORD:
                    parsed.append(lexem.getValue());
                    break;
                case TAG:
                    if (lexem.getValue().startsWith("<code")) {
                        parsed.append("<pre>");
                        parsed.append(lexem.getValue().substring(0, 5)).append(" hljs").append(lexem.getValue().substring(5));
                    } else if (lexem.getValue().equals("</code>")) {
                        parsed.append(lexem.getValue());
                        parsed.append("</pre>");
                    } else {
                        parsed.append(lexem.getValue().replace("<", "&lt;").replace(">", "&gt;"));
                    }
                    break;
            }
        }

        return parsed.toString();
    }

    public static void main(String[] args) {
        HtmlLexer lexer = new HtmlLexer("fw  efewfewwef <code lang=\"java\">fwe  f  wefwefwfe</code> sd <script>alert(5)</script>");
        HtmlParser parser = new HtmlParser();

        System.out.println(parser.parse(lexer.tokenize()));
    }
}
