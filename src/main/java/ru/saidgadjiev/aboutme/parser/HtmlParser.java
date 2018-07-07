package ru.saidgadjiev.aboutme.parser;

import java.util.List;
import java.util.ListIterator;

public class HtmlParser {

    private final List<HtmlLexem> lexems;

    public HtmlParser(List<HtmlLexem> lexems) {
        this.lexems = lexems;
    }

    public String parse() {
        StringBuilder parsed = new StringBuilder();

        for (int i = 0; i < lexems.size(); i++) {
            HtmlLexem lexem = lexems.get(i);

            switch (lexem.getToken()) {
                case WORD:
                    parsed.append(lexem.getValue());
                    break;
                case TAG_CLOSE:
                    if (lexem.getValue().equals("</code>")) {
                        HtmlLexem next = next(i);

                        parsed.append(lexem.getValue());
                        if (next == null || !next.getValue().equals("</pre>")) {
                            parsed.append("</pre>");
                        }
                    } else {
                        if (lexem.getValue().equals("</pre>")) {
                            HtmlLexem prev = prev(i);

                            if (prev != null && prev.getValue().equals("</code>")) {
                                parsed.append(lexem.getValue());
                                break;
                            }
                        }
                        parsed.append(lexem.getValue().replace("<", "&lt;").replace(">", "&gt;"));
                    }
                    break;
                case TAG_OPEN:
                    if (lexem.getValue().startsWith("<code")) {
                        HtmlLexem prev = prev(i);

                        if (prev == null || !prev.getValue().equals("<pre>")) {
                            parsed.append("<pre>");
                        }
                        parsed.append(lexem.getValue());
                    } else {
                        if (lexem.getValue().equals("<pre>")) {
                            HtmlLexem next = next(i);

                            if (next != null && next.getValue().startsWith("<code")) {
                                parsed.append(lexem.getValue());
                                break;
                            }
                        }
                        parsed.append(lexem.getValue().replace("<", "&lt;").replace(">", "&gt;"));
                    }
                    break;
            }
        }

        return parsed.toString();
    }

    private HtmlLexem next(int i) {
        return i < lexems.size() - 1 ? lexems.get(i + 1) : null;
    }

    private HtmlLexem prev(int i) {
        return i > 0 ? lexems.get(i - 1) : null;
    }

    public static void main(String[] args) {
        String str = "fw  efewfewwef <pre><code hljs lang=\"java\">fwe  f  wefwefwfe</code></pre> sd &lt;script&gt;alert(5)&lt;/script&gt;";
        HtmlLexer lexer = new HtmlLexer(str);
        HtmlParser parser = new HtmlParser(lexer.tokenize());

        System.out.println(parser.parse());
    }
}
