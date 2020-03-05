package censusanalyser;

public class IndiaCensusDTO {
    public String state;
    public int population;
    public int areaCode;
    public int dencityPerSqKm;
    public String stateCode;

    public IndiaCensusDTO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        areaCode = indiaCensusCSV.areaInSqKm;
        dencityPerSqKm = indiaCensusCSV.densityPerSqKm;
    }
}
