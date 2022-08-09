import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

public class CSVtoJSON extends Converter{
    private String[] columnMapping;
    private String destinationFileName;
    public void setColumnMapping(String[] columnMapping) {
        this.columnMapping = columnMapping;
    }

    public void setFileName(String fileName){
        this.fileName = fileName;
    }
    public CSVtoJSON(String fileName){
        super(fileName);
        destinationFileName = "new_data.json";
    }
    public List<Employee> parseCSV() throws FileNotFoundException {
        List<Employee> returningList;
        try (CSVReader csvReader = new CSVReader(new FileReader(this.fileName))){
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(this.columnMapping);

            CsvToBean<Employee> csv = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();

            returningList = csv.parse();
        }catch (FileNotFoundException e){
            throw new FileNotFoundException();
        }catch (IOException e){
            throw new RuntimeException(e);
        }

        return returningList;
    }

    public String listToJson(List<Employee> list){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    public void writeString(String json){
        try (FileWriter file = new
                FileWriter(destinationFileName)) {
            file.write(json);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
