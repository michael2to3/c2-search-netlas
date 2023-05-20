package c2.search.netlas.query;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

public class ListBuilderTest {

  @Test
  public void testBuild() {
    List<String> list = new ArrayList<>(Arrays.asList("value1", "value2", "value3"));
    ListBuilder builder = new ListBuilder("field", list);
    builder.setSeparator(" AND ");

    String expected = "(field:\"value1\" AND field:\"value2\" AND field:\"value3\")";
    String actual = builder.build();

    assertEquals(expected, actual);
  }

  @Test
  public void testBuildWithNullList() {
    ListBuilder builder = new ListBuilder("field", null);
    builder.setSeparator(" OR ");

    String expected = "";
    String actual = builder.build();

    assertEquals(expected, actual);
  }

  @Test
  public void testSetSeparator() {
    ListBuilder builder = new ListBuilder("field", new ArrayList<>());
    builder.setSeparator(" OR ");

    String expected = " OR ";
    String actual = builder.getSeparator();

    assertEquals(expected, actual);
  }
}
