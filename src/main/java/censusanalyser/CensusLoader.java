package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class CensusLoader {
    List<CensusDTO> CensusDTOS = null;
    public Map<String, CensusDTO> censusDTOMap = null;


    public <E> Map<String, CensusDTO> loadCensusData(Class<E> censusCSVclass, String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]))) {
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
            if (csvFilePath.length == 1)
                return censusDTOMap;
            this.loadIndianStateCode(censusDTOMap, csvFilePath[1]);
            return censusDTOMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PASS);
        }
    }

    private int loadIndianStateCode(Map<String, CensusDTO> censusDTOMap, String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CsvToBuilderFactory.creatCSVBuilder();
            Iterator<IndiaStateCodeCsv> codeCsvIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCsv.class);
            Iterable<IndiaStateCodeCsv> codeCsvIterable = () -> codeCsvIterator;
            StreamSupport.stream(codeCsvIterable.spliterator(), false)
                    .filter(csvState -> this.censusDTOMap.get(csvState.state) != null)
                    .forEach(csvState -> this.censusDTOMap.get(csvState.state).stateCode = csvState.stateCode);
            return this.censusDTOMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PASS);
        }
    }
}
