package com.tsp.notifyservice.service;

import com.tsp.notifyservice.dto.CalendarBlockingInfoDto;

public interface ITeamsCalendarBlockService {
    public String blockCalendar(CalendarBlockingInfoDto calendarBlockingInfo);
}
