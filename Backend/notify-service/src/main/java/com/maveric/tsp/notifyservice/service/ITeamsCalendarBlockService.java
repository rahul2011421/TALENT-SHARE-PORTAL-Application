package com.maveric.tsp.notifyservice.service;

import com.maveric.tsp.notifyservice.dto.CalendarBlockingInfoDto;

public interface ITeamsCalendarBlockService {
    public String blockCalendar(CalendarBlockingInfoDto calendarBlockingInfo);
}
