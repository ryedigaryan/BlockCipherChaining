package helpers;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Range {
    private long begin;
    private long end;

    public Range(long begin, long end) {
        this.begin = begin;
        this.end = end;
    }

    /**
     * Note: the result may not fit in {@link Long}'s range
     */
    public long distance() {
        return end - begin;
    }

    public long fit(long value) {
        if(begin <= value && value < end)
            return value;
        // convert value to fit in acceptable range
        long inRange = value % end;
        // if value is negative, then convert it to positive
        if(inRange < 0) {
            return end + inRange;
        }
        return inRange;
    }
}
