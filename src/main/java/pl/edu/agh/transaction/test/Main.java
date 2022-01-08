package pl.edu.agh.transaction.test;


import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {

        try {
            String path = new File(".").getCanonicalPath();
            System.out.println(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("asddsf");


        DateTime date = new DateTime();
        DateTime dateDiff = new DateTime();
        date = date.withTime(23,59,59,999);

        System.out.println(date);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.ZONE_OFFSET, 1);
        System.out.println(calendar.getTime());
    }
}
