package practicum.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Interval {
    private Long longStartTime;
    private Long longEndTime;
    private static final Long MINUTES_15_TO_MILLI = 900000L;
    private static final Long WEEKS_15_TO_MILLI = 1000 * 60 * 60 * 24 * 7L;

    public Interval(LocalDateTime startTime, LocalDateTime endTime) {
        this.longStartTime = convertStartTimeToGrid(startTime);
        this.longEndTime = convertEndTimeToGrid(endTime);
    }

    public Interval(Long longStartTime, Long longEndTime) {
        this.longStartTime = longStartTime;
        this.longEndTime = longEndTime;
    }

    //левая граница, приведенная к 15-и минутной сетке
    public Long convertStartTimeToGrid(LocalDateTime startTime) {
        longStartTime = Long.valueOf(startTime.toInstant(ZoneOffset.UTC).toEpochMilli()) -
                Long.valueOf(startTime.toInstant(ZoneOffset.UTC).toEpochMilli()) % MINUTES_15_TO_MILLI;

        return longStartTime;
    }

    //правая граница, приведенная к 15-и минутной сетке
    public Long convertEndTimeToGrid(LocalDateTime endTime) {
        longEndTime = Long.valueOf(endTime.toInstant(ZoneOffset.UTC).toEpochMilli()) +
                (MINUTES_15_TO_MILLI - (Long.valueOf(endTime.toInstant(ZoneOffset.UTC).toEpochMilli()) % MINUTES_15_TO_MILLI));

        return longEndTime;
    }

    //Планирую на +/- неделю!
    //возвращаю 15-минутные интервалы для новой задачи +/- неделя
    public Set<Interval> getIntervals() {
        Set<Interval> intervals = new HashSet<>();
        Long longEndTimePlusWeek = longEndTime + WEEKS_15_TO_MILLI;
        Long longEndTimeMinusWeek = longEndTime - WEEKS_15_TO_MILLI;
        //зафиксировать левую границу oт startTime до endTime не включая
        //рассчитать интервалы при зафиксированной левой границе до endTime.plusWeeks(1)
        for (long i = longStartTime; i < longEndTime; i += MINUTES_15_TO_MILLI) {
            for (long j = i + MINUTES_15_TO_MILLI; j <= longEndTimePlusWeek; j += MINUTES_15_TO_MILLI) {
                Interval interval = new Interval(i, j);
                intervals.add(interval);
            }
        }

        //зафиксировать правую границу oт startTime до endTime не включая startTime
        //рассчитать интервалы при зафиксированной правой границе до endTime.minusWeeks(1)
        for (long i = longStartTime + MINUTES_15_TO_MILLI; i <= longEndTime; i += MINUTES_15_TO_MILLI) {
            for (long j = longEndTimeMinusWeek; j < i; j += MINUTES_15_TO_MILLI) {
                Interval interval = new Interval(j, i);
                intervals.add(interval);
            }
        }
        return intervals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Interval)) return false;
        Interval interval = (Interval) o;
        return Objects.equals(longStartTime, interval.longStartTime) && Objects.equals(longEndTime, interval.longEndTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(longStartTime, longEndTime);
    }

}
