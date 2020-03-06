package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

abstract class CensusAdaptor {
    public abstract Map<String, CensusDTO> loadCensusData(String... csvFilePath) throws CensusAnalyserException;

    public <E> Map<String, CensusDTO> loadCensusData(Class<E> censusCSVclass, String csvFilePath) throws CensusAnalyserException {
        List<CensusDTO> CensusDTOS;
        Map<String, CensusDTO> censusDTOMap;
        censusDTOMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CsvToBuilderFactory.creatCSVBuilder();
            Iterator<E> csvIterator = csvBuilder.getCSVFileIterator(reader, censusCSVclass);
            Iterable<E> csvIterable = () -> csvIterator;
            if (censusCSVclass.getName().equals("censusanalyser.IndiaCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusDTOMap.put(censusCSV.state, new CensusDTO(censusCSV)));
                CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
            } else if (censusCSVclass.getName().equals("censusanalyser.USCensusCSV")) {
                StreamSupport.stream(csvIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusDTOMap.put(censusCSV.state, new CensusDTO(censusCSV)));
                CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
            }
            return censusDTOMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PASS);
        }
    }
}