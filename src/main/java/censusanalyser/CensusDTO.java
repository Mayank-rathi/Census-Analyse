package censusanalyser;

public class CensusDTO {
    public String state;
    public double population;
    public double areaInSqKm;
    public double densityPerSqKm;
    public String stateCode;


    public CensusDTO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        areaInSqKm = indiaCensusCSV.areaInSqKm;
        densityPerSqKm = indiaCensusCSV.densityPerSqKm;


    }

    public CensusDTO(USCensusCSV usCensusCSV) {
        this.state = usCensusCSV.state;
        this.population = usCensusCSV.population;
        this.areaInSqKm = usCensusCSV.areaInSqKm;
        this.densityPerSqKm = usCensusCSV.densityPerSqKm;
        this.stateCode = usCensusCSV.stateCode;
    }
}
