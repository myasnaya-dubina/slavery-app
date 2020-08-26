package ru.tzkt.slavery.domain;

import java.util.Calendar;

public class DateFormatter {

    private String[] months = {
            "январе",
            "феврале",
            "марте",
            "апреле",
            "мае",
            "июне",
            "июле",
            "августе",
            "сентябре",
            "октябре",
            "ноябре",
            "декабре"
    };
    private Calendar calendar = Calendar.getInstance();

    public String formatDate(long date) {
        if (date == 0) {
            return "... никогда =(";
        } else {
            if (date == 1) {
                return "слишком большой срок ипотеки =(";
            }
        }
        calendar.setTimeInMillis(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return getDate(year, month);
    }

    private String getDate(int year, int month) {
        String monthRu = months[month];
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(monthRu)
                .append(" ")
                .append(year)
                .append(" года");
        return stringBuilder.toString();
    }
}