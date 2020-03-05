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
    List<IndiaCensusDTO> indiaCensusDTOS = null;
    Map<String, IndiaCensusDTO> censusDTOMap = null;

    public CensusAnalyser() {
        censusDTOMap = new TreeMap();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath))) {
            ICSVBuilder csvBuilder = CsvToBuilderFactory.creatCSVBuilder();
            Iterator<IndiaCensusCSV> censusCSVIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            while (censusCSVIterator.hasNext()) {
                IndiaCensusCSV indiaCensusCSV = censusCSVIterator.next();
                censusDTOMap.put(indiaCensusCSV.state, new IndiaCensusDTO(indiaCensusCSV));
            }
            indiaCensusDTOS = censusDTOMap.values().stream().collect(Collectors.toList());
            return indiaCensusDTOS.size();
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

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (indiaCensusDTOS.size() == 0 || indiaCensusDTOS == null) {
            throw new CensusAnalyserException("No Census Data", CensusAnalyserException.ExceptionType.CENSUS_CENSUS_DATA);
        }
        Comparator<IndiaCensusDTO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator);
        String sortedStateCensus = new Gson().toJson(indiaCensusDTOS);
        return sortedStateCensus;
    }

    private void sort(Comparator<IndiaCensusDTO> censusCSVComparator) {
        for (int i = 0; i < indiaCensusDTOS.size() - 1; i++) {
            for (int j = 0; j < indiaCensusDTOS.size() - i - 1; j++) {
                IndiaCensusDTO census1 = indiaCensusDTOS.get(j);
                IndiaCensusDTO census2 = indiaCensusDTOS.get(j + 1);
                if (censusCSVComparator.compare(census1, census2) > 0) {
                    indiaCensusDTOS.set(j, census2);
                    indiaCensusDTOS.set(j + 1, census1);
                }
            }
        }
    }
}
