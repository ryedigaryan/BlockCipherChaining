package helpers;

public class Range {
    private int begin;
    private int end;

    public Range(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public int getBegin() {
        return begin;
    }

    public int getEnd() {
        return end;
    }

    public void setBegin(int begin) {
        this.begin = begin;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public long distance() {
        return (long)end - (long)begin;
    }
}
