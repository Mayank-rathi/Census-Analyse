package censusanalyser;

import java.util.Map;

public class UsCensusAdabter extends CensusAdaptor{
    public Map<String, CensusDTO> censusDTOMap = null;
    @Override
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = super.loadCensusData(USCensusCSV.class, csvFilePath[0]);
        return censusDTOMap;
    }
}
