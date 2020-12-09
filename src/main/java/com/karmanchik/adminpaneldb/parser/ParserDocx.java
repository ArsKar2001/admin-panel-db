package com.karmanchik.adminpaneldb.parser;

import lombok.Getter;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.*;

@Log4j
public class ParserDocx {
    private static final ArrayList<String> NUMBERS =
            new ArrayList<>(Arrays.asList("I", "I-II", "I-III", "I-IV", "I-V", "I-VI", "II", "II-III", "II-IV", "II-V", "II-VI", "III", "III-IV", "III-V", "III-VI", "IV", "IV-V", "IV-VI", "V", "V-VI", "VI"));
    private static final HashMap<String, Integer> DAYS = new HashMap<String, Integer>() {{
        put("Понедельник", 0);
        put("Вторник", 1);
        put("Среда", 2);
        put("Четверг", 3);
        put("Пятница", 4);
        put("Суббота", 5);
    }};


    private final ArrayList<ArrayList<String>> pages = new ArrayList<>();
    @Getter
    private final HashMap<String, HashMap<Integer, ArrayList<String>>> timeTablesGroups = new HashMap<>();
    private final File inputFile;


    public ParserDocx(File inputFile) {
        this.inputFile = inputFile;
    }

    public void parse() {
        parserDocxInText_ApachePOI(inputFile);
    }

    private void parserDocxInText_ApachePOI(File f) {
        log.info("[STARTED] " + ParserDocx.class);
        try (FileInputStream stream = new FileInputStream(f)) {
            XWPFDocument docxFile = new XWPFDocument(OPCPackage.open(stream));
            XWPFWordExtractor extractor = new XWPFWordExtractor(docxFile);
            log.debug("Считали данные из " + f.getName());
            onPages(extractor.getText());
            onDaysTimeTable();
            log.debug("Собрали новый объект: " + timeTablesGroups.toString());
        } catch (IOException | InvalidFormatException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Рабивает строки в текстовом файле на страницы.
     *
     * @param text Текстовый файл
     */
    private void onPages(String text) {
        String[] txtLines = text.split("\n");
        ArrayList<String> page = new ArrayList<>();
        ArrayList<ArrayList<String>> pages = new ArrayList<>();
        for (var line : txtLines) {
            if ("".equals(line)) {
                pages.add(page);
                page = new ArrayList<>();
            } else {
                page.add(line);
            }
        }
        pages.add(page);
        pages.removeIf(pg -> pg.size() < 10);
        correctPages(pages);
    }

    private void correctPages(ArrayList<ArrayList<String>> pages1) {
        for (var page : pages1) {
            page.remove(1);
            splitPage(page);
        }
    }

    private void splitPage(List<String> page) {
        // Объявляем новые страницы
        ArrayList<String> leftListLesson = new ArrayList<>();
        ArrayList<String> rightListLesson = new ArrayList<>();
        StringBuilder leftStringBuilder;
        StringBuilder rightStringBuilder;

        for (var line : page) {
            leftStringBuilder = new StringBuilder();
            rightStringBuilder = new StringBuilder();
            String[] splitLine = line.split("\t");
            try {
                if (page.get(0).equals(line)) {
                    leftListLesson.add(splitLine[0].split("\\s")[1]);
                    rightListLesson.add(splitLine[1].split("\\s")[1]);
                    continue;
                }
                leftStringBuilder.append(splitLine[1].trim()).append(";");
                for (int i = 2; i < splitLine.length; i++) {
                    if (DAYS.containsKey(splitLine[i].trim())) {
                        leftListLesson.add(splitLine[0].trim());
                        rightListLesson.add(splitLine[i].trim());
                        continue;
                    }

                    if (NUMBERS.contains(splitLine[i].trim())) {
                        for (int j = i; j < splitLine.length; j++)
                            rightStringBuilder.append(splitLine[j].trim()).append(";");
                        break;
                    }
                    leftStringBuilder.append(splitLine[i].trim()).append(";");
                }
            } catch (Exception e) {
                log.warn(e.getMessage() + " В строке \"" + line + "\"");
            }

            leftListLesson.add(leftStringBuilder.toString().trim());
            rightListLesson.add(rightStringBuilder.toString().trim());

        }
        pages.add(leftListLesson);
        pages.add(rightListLesson);
    }

    private void onDaysTimeTable() {
        HashMap<Integer, ArrayList<String>> timeTableToDay = new HashMap<>();
        ArrayList<String> lessons = new ArrayList<>();
        try {
            int day;
            String groupName;
            for (var group : pages) {
                groupName = group.get(0);
                if(groupName.equals("")) continue;
                day = DAYS.get(group.get(1));
                for (var line : group) {
                    if (line.equals("")) continue;
                    if (!line.equals(groupName)) {
                        if (DAYS.containsKey(line)) {
                            int newDay = DAYS.get(line);
                            if (day != newDay) {
                                timeTableToDay.put(day, lessons);
                                lessons = new ArrayList<>();
                                day = newDay;
                            }
                            continue;
                        }
                        lessons.add(line);
                    }
                }
                timeTableToDay.put(day, lessons);
                lessons = new ArrayList<>();
                timeTablesGroups.put(groupName, timeTableToDay);
                timeTableToDay = new HashMap<>();
            }
            timeTablesGroups.remove("");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
