package com.taotao.cloud.office.easypoi.test.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * import java.time.Instant;
 * import java.time.LocalDate;
 * import java.time.LocalDateTime;
 * import java.time.ZoneId;
 *
 * @author by jueyue on 19-6-23.
 */
public class NewDateEntity {

    @Excel(name = "instant", format = "yyyy-MM-dd")
    private Instant       instant;
    @Excel(name = "localdate", format = "yyyy-MM-dd")
    private LocalDate     localDate;
    @Excel(name = "localdatetime", format = "yyyy-MM-dd")
    private LocalDateTime localDateTime;
    @Excel(name = "ZoneId", format = "yyyy-MM-dd")
    private ZoneId        zoneId;

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public void setZoneId(ZoneId zoneId) {
        this.zoneId = zoneId;
    }
}
