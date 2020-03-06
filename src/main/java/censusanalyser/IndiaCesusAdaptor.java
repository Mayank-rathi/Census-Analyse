package censusanalyser;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class IndiaCesusAdaptor extends CensusAdaptor {
    public Map<String, CensusDTO> censusDTOMap = null;

    @Override
    public Map<String, CensusDTO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
        censusDTOMap = super.loadCensusData(IndiaCensusCSV.class, csvFilePath[0]);
        this.IndianStateCodeData(csvFilePath[1]);
        return censusDTOMap;
    }

    private int IndianStateCodeData(String csvFilePath) throws CensusAnalyserException {
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
