package tag;

public class Tag {
    public int backing;
    public int len;
    public char nextChar;

    public Tag(int backing, int len, char nextChar) {
        this.backing = backing;
        this.len = len;
        this.nextChar = nextChar;
    }
}

