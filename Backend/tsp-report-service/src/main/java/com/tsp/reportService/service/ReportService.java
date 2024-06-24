package com.tsp.reportService.service;

import com.tsp.reportService.dtos.ReportRequestDto;
import com.tsp.reportService.dtos.User;

import java.io.IOException;
import java.util.ArrayList;

public interface ReportService {
    /**
     *
     * @param reportRequestDto
     * @return
     * @throws IOException
     */
    ArrayList<User> getDataDownloaded(ReportRequestDto reportRequestDto) throws IOException;
}
