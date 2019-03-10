package com.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);


    /**
     * @param inputStream
     * @param sheetNo     工作簿编号
     * @param start       数据起始行
     * @param fields      字段数组
     * @param clazz
     * @param <T>
     * @return
     * @throws IOException
     */
    public static <T> List<T> toList(InputStream inputStream, int sheetNo, int start, String[] fields, Class<T> clazz) throws IOException {
        try (InputStream in = inputStream; Workbook work = new XSSFWorkbook(in)) {
            Sheet sheet = work.getSheetAt(sheetNo);
            List<T> list = new LinkedList();
            for (int rowIndex = start; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    JSONObject json = new JSONObject();
                    for (int colIndex = 0; colIndex < fields.length; colIndex++) {
                        Cell cell = row.getCell(colIndex);
                        json.put(fields[colIndex], getValue(cell));
                    }
                    list.add(json.toJavaObject(clazz));
                }
            }
            return list;
        }
    }

    /**
     * 导入excel
     *
     * @param in     模板文件流
     * @param out    生成excel文件流
     * @param fields 字段数组
     * @param list   数据集
     * @throws IOException
     */
    public static void toXlsxByTemplate(InputStream in, OutputStream out, List list, String[] fields) throws IOException {
        toXlsx(new XSSFWorkbook(in), out, list, fields);
    }


    public static void toXlsxByTemplate(InputStream in, OutputStream out, List list) throws IOException {
        String[] fields =  JsonUtil.getFieldNames(list.get(0));
        toXlsx(new XSSFWorkbook(in), out, list, fields);
    }

    public static void toXlsx(OutputStream out, List list, String[] fields) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet();
        Row headRow = sheet.createRow(0);
//            Set<String> fields = JsonUtil.change(list.get(0), LinkedHashMap.class).keySet();
        int colIndex = 0;
        for (String field : fields) {
            Cell headCell = headRow.createCell(colIndex);
            headCell.setCellValue(field);
            colIndex++;
        }
        toXlsx(workbook, out, list, fields);
    }

    private static void toXlsx(XSSFWorkbook workbook, OutputStream out, List list, String[] fields) throws IOException {
        try (XSSFWorkbook wk = workbook; OutputStream outputStream = out) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowIndex = 1;
            for (Object object : list) {
                Row dataRow = sheet.createRow(rowIndex);
                Map map = JsonUtil.change(object, Map.class);
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = dataRow.createCell(i);
                    Object value = map.get(fields[i]);
                    setValue(cell, value);
                }
                rowIndex++;
            }
            workbook.write(out);
        }
    }

    private static Object getValue(Cell cell) {
        if (null == cell) {
            return null;
        }
        Object value;
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_NUMERIC: // 数字
                value = DateUtil.isCellDateFormatted(cell) ?
                        DateUtil.getJavaDate(cell.getNumericCellValue()) : new DataFormatter().formatCellValue(cell);
                break;
            case Cell.CELL_TYPE_BOOLEAN: // Boolean
                value = cell.getBooleanCellValue();
                break;
            default:
                value = cell.toString();
                break;
        }
        return value;
    }

    private static void setValue(Cell cell, Object value) {
        if (null == value || null == value) {
            cell.setCellValue("");
        }
        if (value instanceof Date) {
            cell.setCellValue(DateUtil.getExcelDate((Date) value));
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue("");
//            cell.setCellValue(value.toString());
        }
    }

}