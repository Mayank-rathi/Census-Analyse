package censusanalyser;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCSVBuilder implements ICSVBuilder {

    public Iterator<ICSVBuilder> getCSVFileIterator(Reader reader, Class csvClass) {
        return this.getCSVToBean(reader, csvClass).iterator();

    }

    @Override
    public List getCSVFileList(Reader reader, Class csvClass) {
        return this.getCSVToBean(reader, csvClass).parse();
    }


    private CsvToBean getCSVToBean(Reader reader, Class csvclass) {
        try {
            CsvToBeanBuilder<ICSVBuilder> csvToBeanBuilder = new CsvToBeanBuilder<>(reader);
            csvToBeanBuilder.withType(csvclass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<ICSVBuilder> csvToBean = csvToBeanBuilder.build();
            return csvToBean;
        } catch (IllegalStateException e) {
            throw new CSVBuilderException(e.getMessage(), CSVBuilderException.ExceptionType.UNABLE_TO_PASS);
        }
    }
}
