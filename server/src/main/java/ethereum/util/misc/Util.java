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

    public static BigInteger convertToBaseUnit(long value, int decimals) {
        BigInteger decimalsBN = BigInteger.valueOf(10).pow(decimals);
        return BigInteger.valueOf(value).multiply(decimalsBN);
    }

    public static long revertFromBaseUnit(BigInteger value, int decimals) {
        BigInteger decimalsBN = BigInteger.valueOf(10).pow(decimals);
        return value.divide(decimalsBN).longValue();
    }
}
