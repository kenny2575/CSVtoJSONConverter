import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CSVtoJSONTests {

    static int testNumber;
    CSVtoJSON sut;
    @BeforeAll
    public static void start(){
        System.out.println("test started");
    }

    @BeforeEach
    public void init(){
        sut = new CSVtoJSON("data.csv");
        String column = "id,firstName,lastName,country,age";
        sut.setColumnMapping(column.split(","));
        System.out.println("test # " + ++testNumber + " started");
    }

    @AfterEach
    public void finish(){
        System.out.println("Test #" + testNumber + " completed");
    }

    @AfterAll
    public static void end(){
        System.out.println("All tests completed");
    }

    @Test
    public void countOfRows() throws FileNotFoundException {
        int expected = 2;
        List<Employee> lt;
        lt = sut.parseCSV();
        assertEquals(expected, lt.stream().count());
    }

    @ParameterizedTest
    @MethodSource("source")
    public void testParsedException(String fileName){
        sut.setFileName(fileName);
        var expected = FileNotFoundException.class;

        assertThrows(expected,
                () -> sut.parseCSV());
    }

    private static Stream<Arguments> source(){
        return Stream.of(Arguments.of("file.csv", "test.csv", "input_file.csv"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"{}", "{\"json\":1}", "{\"json\":[{\"arr\":\"val1\"}, {\"arr\":\"val2\"}]}"})
    public void testWriteString(String json){

        assertDoesNotThrow(
                //act
                () -> sut.writeString(json)
        );
    }
}
