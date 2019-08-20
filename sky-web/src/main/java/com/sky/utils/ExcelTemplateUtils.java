package com.sky.utils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExcelTemplateUtils {

    /**
     * 根据模板生成 excel 文件
     *  @param templateStream 模板文件流
     * @param data         数据
     * @param os           生成 excel 输出流，可保存成文件或返回到前端等
     */
    public static void process(InputStream templateStream, Object data, OutputStream os) {
        try {
            OPCPackage pkg = OPCPackage.open(templateStream);

            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            Iterator<Sheet> iterable = wb.sheetIterator();
            while (iterable.hasNext()) {
                processSheet(data, iterable.next());
            }
            wb.write(os);
            pkg.close();
            templateStream.close();
            wb.close();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }
    /**
     * 根据模板生成 excel 文件
     *  @param templatePath 模板文件路径
     * @param data         数据
     * @param os           生成 excel 输出流，可保存成文件或返回到前端等
     */
    public static void process(String templatePath, Object data, OutputStream os) {
        try {
            OPCPackage pkg = OPCPackage.open(templatePath);

            XSSFWorkbook wb = new XSSFWorkbook(pkg);
            Iterator<Sheet> iterable = wb.sheetIterator();
            while (iterable.hasNext()) {
                processSheet(data, iterable.next());
            }
            wb.write(os);
            pkg.close();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }


    private static void processSheet(Object data, Sheet sheet) {
        Map<Integer, Map<Integer, Cell>> listRecord = new LinkedHashMap<>();
        int lastRowNum = sheet.getLastRowNum();
        for (int i = lastRowNum; i >= 0; i--) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            int lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                if (cell == null) {
                    continue;
                }
                try {
                    String cellValue = cell.getStringCellValue();
                    if (cellValue.matches(".*\\$\\{[\\w.()]+}.*")) {
                        fillCell(cell, cellValue, data);
                    } else if (cellValue.matches(".*\\$\\{[\\w.]+\\[#][\\w.]+}.*")) {
                        Map<Integer, Cell> rowRecord = listRecord.computeIfAbsent(i, k -> new HashMap<>());
                        rowRecord.put(j, cell);
                    }
                } catch (Exception ignored) {

                }
            }
        }

        Map<String, List> listInData = new HashMap<>();
        Map<String, CellStyle> listCellStyle = new HashMap<>();
        Map<Cell, String> listCellPath = new HashMap<>();
        listRecord.forEach((rowNum, colMap) -> {
            Pattern p = Pattern.compile("\\$\\{[\\w.\\[#\\]]+}");
            Set<String> listPath = new HashSet<>();
            colMap.forEach((colNum, cell) -> {
                String cellValue = cell.getStringCellValue();
                Matcher m = p.matcher(cellValue);
                if (m.find()) {
                    String reg = m.group();
                    String regPre = reg.substring(2, reg.indexOf("["));
                    String regSuf = reg.substring(reg.lastIndexOf("].") + 2, reg.length() - 1);
                    listPath.add(regPre);
                    listCellStyle.put(String.format("%s.%s", regPre, regSuf), cell.getCellStyle());
                    listCellPath.put(cell, String.format("%s#%s", regPre, regSuf));
                }
            });
            int maxRow = 0;
            for (String s : listPath) {
                Object list = getAttributeByPath(data, s);
                if (list == null) {
                    list = new ArrayList<>();
                }
                if (list instanceof List) {
                    int len = ((List) list).size();
                    maxRow = Math.max(maxRow, len);
                    listInData.put(s, ((List) list));
                } else {
                    throw new IllegalArgumentException(String.format("%s is not a list but a %s", s, list.getClass().getSimpleName()));
                }
            }
            if (maxRow > 1) {
                int endRow = sheet.getLastRowNum();
                sheet.shiftRows(rowNum + 1, endRow + 1, maxRow - 1);
            }
        });

        listRecord.forEach((rowNum, colMap) -> colMap.forEach((colNum, cell) -> {
            String path = listCellPath.get(cell);
            String[] pathData = path.split("#");
            List list = listInData.get(pathData[0]);
            int baseRowIndex = cell.getRowIndex();
            int colIndex = cell.getColumnIndex();
            CellStyle style = listCellStyle.get(String.format("%s.%s", pathData[0], pathData[1]));
            for (int i = 0; i < list.size(); i++) {
                int rowIndex = baseRowIndex + i;
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                }
                Cell cellToFill = row.getCell(colIndex);
                if (cellToFill == null) {
                    cellToFill = row.createCell(colIndex);
                }
                cellToFill.setCellStyle(style);
                setCellValue(cellToFill, getAttribute(list.get(i), pathData[1]));
            }
        }));
    }


    /**
     * @param cell       要置换的单元格
     * @param expression 单元格内的置换标记
     * @param data       数据源
     */
    private static void fillCell(Cell cell, String expression, Object data) {
        Pattern p = Pattern.compile("\\$\\{[\\w.\\[\\]()]+}");
        Matcher m = p.matcher(expression);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String exp = m.group();
            String path = exp.substring(2, exp.length() - 1);
            Object value = getAttributeByPath(data, path);
            m.appendReplacement(sb, value == null ? "" : value.toString());
        }
        setCellValue(cell, sb.toString());
    }


    /**
     * @param cell  单元格
     * @param value 值
     */
    private static void setCellValue(Cell cell, Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        } else if (value instanceof Character) {
            cell.setCellValue((Character) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else {
            cell.setCellValue(value.toString());
        }
    }

    /**
     * @param obj  访问对象
     * @param path 属性路径，形如(cls.type, cls.students.size())
     */
    private static Object getAttributeByPath(Object obj, String path) {
        String[] paths = path.split("\\.");
        Object o = obj;
        for (String s : paths) {
            o = getAttribute(o, s);
        }
        return o;
    }

    private static Object getAttribute(Object obj, String member) {
        if (obj == null) {
            return null;
        }
        boolean isMethod = member.endsWith("()");
        if (!isMethod && obj instanceof Map) {
            return ((Map) obj).get(member);
        }
        try {
            Class<?> cls = obj.getClass();
            if (isMethod) {
                Method method = cls.getDeclaredMethod(member.substring(0, member.length() - 2));
                return method.invoke(obj);
            } else {
                Field field = cls.getDeclaredField(member);
                field.setAccessible(true);
                return field.get(obj);
            }
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ExcelTemplateUtils() {
    }
}
