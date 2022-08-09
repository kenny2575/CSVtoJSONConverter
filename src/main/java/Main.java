import java.io.FileNotFoundException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        CSVtoJSON convert = new CSVtoJSON("data.csv");
        convert.setColumnMapping(columnMapping);
        List<Employee> list = convert.parseCSV();
        String json = convert.listToJson(list);
        convert.writeString(json);
    }
}
