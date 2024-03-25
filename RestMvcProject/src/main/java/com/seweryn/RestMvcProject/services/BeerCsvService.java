package com.seweryn.RestMvcProject.services;

import com.seweryn.RestMvcProject.model.BeerCSVRecord;

import java.io.File;
import java.io.InputStream;
import java.util.List;

public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(InputStream csvFile);
}
