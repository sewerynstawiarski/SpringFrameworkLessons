package com.seweryn.RestMvcProject.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.seweryn.RestMvcProject.model.BeerCSVRecord;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertCSV(InputStream csvFile) {
        List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new InputStreamReader(csvFile, StandardCharsets.UTF_8))
                .withType(BeerCSVRecord.class)
                .build()
                .parse();
        return beerCSVRecords;
    }
}
