package main.models;

public class Token {
    public TokenType tokentype;
    public String value;
    public int line;
    public int pos;

    public Token(TokenType token, String value, int line, int pos) {
        this.tokentype = token;
        this.value = value;
        this.line = line;
        this.pos = pos;
    }

    @Override
    public String toString() {
        String result = String.format("%5d  %5d %-15s", this.line, this.pos, this.tokentype);

        switch (this.tokentype) {
            case Integer, Keyword_bool:
                result += String.format("  %4s", value);
                break;
            case Identifier:
                result += String.format(" %s", value);
                break;
        }
        return result;
    }
}


