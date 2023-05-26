package ethereum.util.misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

    // public static void writeToDML(String sqlQuery, Object... params) {
    //     for (Object object : params) {
    //         sqlQuery.replaceFirst("?", object.toString());
    //     }

    //     // Append the executed queries to DML.sql file
    //     try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("DML.sql", true)))) {
    //         out.println("INSERT INTO employees (id, name, age) VALUES (1, 'John Doe', 25);");
    //         out.println("UPDATE employees SET age = 26 WHERE id = 1;");
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //     }

    // }
}
