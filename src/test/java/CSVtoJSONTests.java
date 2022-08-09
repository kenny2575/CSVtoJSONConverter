import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
        System.out.println("test #" + ++testNumber + " started");
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
    public void countOfRowsJunitStyle() throws FileNotFoundException {
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

    @Test
    public void countOfRowsHamcrestStyle() throws FileNotFoundException {
        List<Employee> lt = sut.parseCSV();
        assertThat(lt, hasSize(2));
    }
    @Test
    public void check2ClassesExtends(){
        assertThat(sut.getClass(), typeCompatibleWith(Converter.class));
    }

    @Test
    public void check2ClassesNotExtends(){
        assertThat(sut.getClass(), is(not(typeCompatibleWith(Employee.class))));
    }

    @Test
    public void checkHasToString() throws FileNotFoundException {
        String str= "File name is data.csv";
        assertThat(sut,hasToString(str));
    }
}
