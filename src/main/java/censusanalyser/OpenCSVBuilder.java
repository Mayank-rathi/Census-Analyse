package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;

public class OpenCSVBuilder implements ICSVBuilder {

    public Iterator<ICSVBuilder> getCSVFileIterator(Reader reader, Class csvClass) {
        CsvToBeanBuilder<ICSVBuilder> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
        csvToBeanBuilder.withType(csvClass);
        csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
        CsvToBean<ICSVBuilder> csvToBean = csvToBeanBuilder.build();
        return csvToBean.iterator();
    }
}
