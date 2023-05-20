package ethereum.util.misc;

import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.BigInteger;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Util {

    public static JsonObject readJson(String text) {
        Reader reader = new StringReader(text);
        JsonReader jsonReader = Json.createReader(reader);
        return jsonReader.readObject();
    }

    public static BigDecimal bigIntegerToInt(BigInteger value) {
        BigDecimal quotient = BigDecimal.valueOf(Math.pow(10, 18));
        BigDecimal scaledBal = new BigDecimal(value);
        return scaledBal.divide(quotient);
    }
}
