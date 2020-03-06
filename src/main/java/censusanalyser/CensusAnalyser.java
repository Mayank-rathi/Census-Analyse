package censusanalyser;

import com.google.gson.Gson;

import java.util.*;
import java.util.stream.Collectors;

public class CensusAnalyser {
    List<CensusDTO> CensusDTOS = null;
    Map<String, CensusDTO> censusDTOMap = null;
    Map<SortField, Comparator<CensusDTO>> sortMap = null;

    public CensusAnalyser() {
        this.sortMap = new HashMap<>();
        this.sortMap.put(SortField.STATE, Comparator.comparing(census -> census.state));
        this.sortMap.put(SortField.POPULATION, Comparator.comparing(census -> census.population));
        this.sortMap.put(SortField.POPULATIONS_DENSITY, Comparator.comparing(census -> census.densityPerSqKm));
        this.sortMap.put(SortField.TOTAL_AREA, Comparator.comparing(census -> census.areaInSqKm));
        this.sortMap.put(SortField.STATE_ID, Comparator.comparing(census -> census.stateCode));
    }

    public enum Country {INDIA, US}

    public int loadCensusData(Country country, String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = CensusAdaptorFactory.getCountryData(country, csvFilePath);
        return censusDTOMap.size();
    }

    public String getStateWiseSortedCensusData(SortField sortField) throws CensusAnalyserException {
        if (censusDTOMap == null || censusDTOMap.size() == 0) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
        this.sort(CensusDTOS, this.sortMap.get(sortField).reversed());
        String sortedStateCensusJson = new Gson().toJson(CensusDTOS);
        return sortedStateCensusJson;

    }

    private void sort(List<CensusDTO> censusDTOList, Comparator<CensusDTO> censusCSVComparator) {
        for (int i = 0; i < this.CensusDTOS.size() - 1; i++) {
            for (int j = 0; j < this.CensusDTOS.size() - i - 1; j++) {
                CensusDTO census1 = this.CensusDTOS.get(j);
                CensusDTO census2 = this.CensusDTOS.get(j + 1);
                if (censusCSVComparator.compare(census1, census2) > 0) {
                    this.CensusDTOS.set(j, census2);
                    this.CensusDTOS.set(j + 1, census1);
                }
            }
        }
    }

}
