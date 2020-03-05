package censusanalyser;

public class CensusDTO {
    public String state;
    public double dencityPerSqKm;
    public double population;
    public double totalArea;
    public double populationDensity;
    public String stateCode;


    public CensusDTO(IndiaCensusCSV indiaCensusCSV) {
        state = indiaCensusCSV.state;
        population = indiaCensusCSV.population;
        totalArea = indiaCensusCSV.areaInSqKm;
        dencityPerSqKm = indiaCensusCSV.densityPerSqKm;

    }

    public CensusDTO(USCensusCSV usCensusCSV) {
        this.state = usCensusCSV.state;
        this.dencityPerSqKm = usCensusCSV.totalArea;
        this.population = usCensusCSV.population;
        this.totalArea = usCensusCSV.totalArea;
        this.populationDensity = usCensusCSV.populationDensity;
    }
}
