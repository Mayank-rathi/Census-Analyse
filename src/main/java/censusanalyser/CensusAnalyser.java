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

    public CensusAnalyser() {
        censusDTOMap = new TreeMap();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        return this.loadCensusData(csvFilePath, IndiaCensusCSV.class);

    }

    private <E> int loadCensusData(String csvFilePath, Class<E> censusCSVclass) throws CensusAnalyserException {
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
            return censusDTOMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PASS);
        }
    }

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csvIterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numOfEnteries;
    }

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

    public int loadIndianStateCode(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CsvToBuilderFactory.creatCSVBuilder();
            Iterator<IndiaStateCodeCsv> codeCsvIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCsv.class);
            Iterable<IndiaStateCodeCsv> codeCsvIterable = () -> codeCsvIterator;
            StreamSupport.stream(codeCsvIterable.spliterator(), false)
                    .filter(csvState -> censusDTOMap.get(csvState.state) != null)
                    .forEach(csvState -> censusDTOMap.get(csvState.state).stateCode = csvState.stateCode);
            return censusDTOMap.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PASS);
        }
    }


    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CsvToBuilderFactory.creatCSVBuilder();
            Iterator<USCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, USCensusCSV.class);
            while (censusCSVIterator.hasNext()) {
                USCensusCSV usCensusCSV = censusCSVIterator.next();
                censusDTOMap.put(usCensusCSV.state, new CensusDTO(usCensusCSV));
            }
            CensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
            return CensusDTOS.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(),
                    CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (IllegalStateException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.UNABLE_TO_PASS);
        }
    }

}
