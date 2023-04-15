package c2.search.netlas.target.cobaltstrike;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import c2.search.netlas.annotation.Test;
import c2.search.netlas.scheme.Host;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class UtilsTest {

  @Test
  void testCompareList() {
    List<List<String>> lhs =
        Arrays.asList(
            Collections.singletonList("US"),
            Collections.singletonList("Washington"),
            Collections.singletonList("Redmond"));

    String[] rhs = {"US", "Washington", "Redmond"};

    assertTrue(Utils.compareList(lhs, rhs));

    String[] rhs2 = {"US", "Washington", "Seattle"};

    assertFalse(Utils.compareList(lhs, rhs2));
  }

  @Test
  void testGetDataLength() throws IOException {
    InputStream inputStream = mock(InputStream.class);

    when(inputStream.read(any(byte[].class))).thenReturn(1024, 1024, 500, -1);

    int result = Utils.getDataLength(inputStream);

    assertEquals(2548, result);
  }

  @Test
  void testSendHttpRequest() throws IOException {
    Host host = new Host("localhost", 80);

    HttpURLConnection connection = mock(HttpURLConnection.class);
    when(connection.getResponseCode()).thenReturn(200);

    int statusCode = Utils.sendHttpRequest(host, "", "/");

    assertEquals(200, statusCode);
  }
}
