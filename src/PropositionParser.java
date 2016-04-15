public class PropositionParser {
    public String str;
    public int length, pos;

    private boolean isAlpha(char c) {
        return c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z';
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void skipSpace() {
        if (pos < length) {
            char c = str.charAt(pos);
            while (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                c = str.charAt(++pos);
            }
        }
    }

    private boolean match(String pattern) {
        return match(pattern, true);
    }

    private boolean match(String pattern, boolean trim) {
        int l = pattern.length();
        for (int i = 0; i<l; i++) {
            if (pos >= length || str.charAt(pos + i) != pattern.charAt(i))
                return false;
        }
        if (isAlpha(pattern.charAt(l-1)) && pos+l < length && isAlpha(str.charAt(pos+l)))
            return false;
        pos += l;
        if (trim) skipSpace();
        return true;
    }

    private boolean match(char c) {
        return match(c, true);
    }

    private boolean match(char c, boolean trim) {
        if (pos < length && str.charAt(pos) == c) {
            pos++;
            if (trim) skipSpace();
            return true;
        } else {
            return false;
        }
    }

    private String parseIdentifier() {
        StringBuilder builder = new StringBuilder();
        while (pos < length) {
            char c = str.charAt(pos);
            if (!isAlpha(c))
                break;
            builder.append(c);
            ++pos;
        }
        skipSpace();
        return builder.toString();
    }

    private int parseInt() {
        int result = 0;
        while (pos < length) {
            char c = str.charAt(pos);
            if (!isDigit(c))
                break;
            result = result * 10 + (c - '0');
            ++pos;
        }
        skipSpace();
        return result;
    }

    public Ast parse(String str) {
        this.str = str;
        length = str.length();
        pos = 0;
        return parseProposition();
    }

    public Ast parseProposition() {
        if (pos >= length) {
            return null;
        } else if (isAlpha(str.charAt(pos))) {
            return parsePropositionalLetter();
        } else if (match('(')) {
            Ast ast;
            if (match("\\not")) {
                ast = parseProposition();
                ast = new Not(ast);
            } else {
                Ast first = parseProposition();
                if (first == null)
                    return null;
                CompoundProposition.ConnectiveType type;
                if (match("\\and")) {
                    type = CompoundProposition.ConnectiveType.AND;
                } else if (match("\\or")) {
                    type = CompoundProposition.ConnectiveType.OR;
                } else if (match("\\imply")) {
                    type = CompoundProposition.ConnectiveType.IMPLY;
                } else if (match("\\eq")) {
                    type = CompoundProposition.ConnectiveType.EQ;
                } else {
                    return null;
                }
                Ast second = parseProposition();
                ast = new CompoundProposition(type, first, second);
            }
            if (!match(')'))
                return null;
            return ast;
        } else {
            return null;
        }
    }

    public Ast parsePropositionalLetter() {
        String id = parseIdentifier();
        if (id.isEmpty())
            return null;
        if (match('_'))  {
            if (!match('{'))
                return null;
            int sub = parseInt();
            if (!match('}')) {
                return null;
            }
            return new PropositionalLetter(id, sub);
        }
        return new PropositionalLetter(id);
    }
}
