package censusanalyser;

public class CsvToBuilderFactory {
    public static ICSVBuilder creatCSVBuilder() {
        return new OpenCSVBuilder();
    }
}
