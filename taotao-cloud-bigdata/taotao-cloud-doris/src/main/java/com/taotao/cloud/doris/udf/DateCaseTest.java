package com.taotao.cloud.doris.udf;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hive.ql.exec.UDF;

public class DateCaseTest extends UDF {

    public String evaluate(LocalDate startDate, Integer start, Integer end)  {
        if (startDate == null || start == null || end == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<String> result = new ArrayList<>();
        for (int i = start; i <= end - 1; i++) {
            LocalDate oneDate = startDate.plusDays(i);
            if (oneDate.isAfter(yesterday)) break;
            String dateString = formatter.format(oneDate);
            result.add(dateString);
        }
        return String.join(",", result);
    }

}
