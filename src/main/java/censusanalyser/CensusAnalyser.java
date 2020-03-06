package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    List<CensusDTO> CensusDTOS = null;
    Map<String, CensusDTO> censusDTOMap = null;

    public enum Country {INDIA, US}

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = CensusAdaptorFactory.getCountryData(country, csvFilePath);
        return censusDTOMap.size();
    }

    public String getStateWiseSortedCensusData(String csvFilePath) throws CensusAnalyserException {
        if (censusDTOMap == null || censusDTOMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data",
                    CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }

        Comparator<CensusDTO> censusComparator = Comparator.comparing(census -> census.state);
        CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
        this.sort(censusComparator);
        //Collections.reverse(CensusDTOS);
        String sortedStateCensusJson = new Gson().toJson(CensusDTOS);
        return sortedStateCensusJson;
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
