package main;

public class Token {
    private String value;
    private String type;
    private int lineIndex;

    public Token(String value, String type, int lineIndex) {
        this.value = value;
        this.type = type;
        this.lineIndex = lineIndex;
    }

    public int getLineIndex() {
        return lineIndex;
    }

    public void setLineIndex(int lineIndex) {
        this.lineIndex = lineIndex;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "< " + value + ", '" + type + "' >";
    }
}