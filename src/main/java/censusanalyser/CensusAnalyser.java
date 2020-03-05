package censusanalyser;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusAnalyser {
    List<CensusDTO> CensusDTOS = null;
    Map<String, CensusDTO> censusDTOMap = null;
    public enum Country{INDIA,US};
    public int loadCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = new CensusLoader().loadCensusData(country, csvFilePath);
        CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
        return censusDTOMap.size();
    }

    /*public int loadUSCensusData(Country country,String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = new CensusLoader().loadCensusData(country, csvFilePath);
        CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
        return censusDTOMap.size();
    }*/


    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (CensusDTOS.size() == 0 || CensusDTOS == null) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.CENSUS_CENSUS_DATA);
        }
        Comparator<CensusDTO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(CensusDTOS);
        return sortedStateCensus;
    }

    private void sort(Comparator<CensusDTO> censusCSVComparator) {
        for (int i = 0; i < CensusDTOS.size() - 1; i++) {
            for (int j = 0; j < CensusDTOS.size() - i - 1; j++) {
                CensusDTO census1 = CensusDTOS.get(j);
                CensusDTO census2 = CensusDTOS.get(j + 1);
                if (censusCSVComparator.compare(census1, census2) > 0) {
                    CensusDTOS.set(j, census2);
                    CensusDTOS.set(j + 1, census1);
                }
            }
        }
    }


}
