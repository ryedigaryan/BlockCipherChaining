package helpers;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Range {
    private int begin;
    private int end;

    public Range(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    public long distance() {
        return (long)end - (long)begin;
    }
}
