package censusanalyser;

import java.util.Map;

public class CensusAdaptorFactory {
    public static Map<String, CensusDTO> getCountryData(CensusAnalyser.Country country, String[] csvFilePath) throws CensusAnalyserException {
        if (country.equals(CensusAnalyser.Country.INDIA))
            return new IndiaCesusAdaptor().loadCensusData(csvFilePath);
        else if (country.equals(CensusAnalyser.Country.US))
            return new UsCensusAdabter().loadCensusData(csvFilePath);
        throw new CensusAnalyserException("Incorrect Country", CensusAnalyserException.ExceptionType.INVALID_COUNTRY);

    }
}
