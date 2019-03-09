package com.demo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
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
     * 导出excel
     *
     * @param list         数据
     * @param fields       数据字段名称
     * @param excelFields  excel字段名称,与fields下标对应
     * @param outputStream
     * @throws IOException
     */
    public static void toXlsx(List list, String[] fields, String[] excelFields, OutputStream outputStream) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook(); OutputStream out = outputStream) {
            XSSFSheet sheet = workbook.createSheet();
            Row headRow = sheet.createRow(0);
            for (int i = 0; i < excelFields.length; i++) {

                Cell headCell = headRow.createCell(i);
                headCell.setCellValue(excelFields[i]);
                headCell.setCellStyle(getHeadStyle(workbook));
            }

            int rowIndex = 1;
            for (Object object : list) {
                Row dataRow = sheet.createRow(rowIndex);
                Map map = JsonUtil.change(object, Map.class);
                for (int i = 0; i < fields.length; i++) {
                    Cell cell = dataRow.createCell(i);
                    Object value = map.get(fields[i]);
                    setValue(cell, value);
                    cell.setCellStyle(getCellStyle(workbook));
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
        if (null == value) {
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
            cell.setCellValue(value.toString());
        }
    }

    // 设置下拉框
    public static void setDropDownBox(XSSFSheet sheet, String[] values, int colIndex) {
        DataValidationHelper dvHelper = new XSSFDataValidationHelper(sheet);
        XSSFDataValidationConstraint constraint = (XSSFDataValidationConstraint) dvHelper.createExplicitListConstraint(values);
        CellRangeAddressList addressList = new CellRangeAddressList(1, 100, colIndex, colIndex);
        DataValidation validation = dvHelper.createValidation(constraint, addressList);
        sheet.addValidationData(validation);
    }

    // 设置标题行样式
    public static XSSFCellStyle getHeadStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeight(12);
        font.setColor(HSSFColor.WHITE.index);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.index);// 背景色
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setVerticalAlignment(VerticalAlignment.CENTER);             // 竖向居中
        style.setAlignment(HorizontalAlignment.CENTER);                   // 横向居中
        return style;
    }

    // 设置内容样式
    public static XSSFCellStyle getCellStyle(XSSFWorkbook workbook) {
        XSSFFont font = workbook.createFont();
        font.setFontName("Arial");
        font.setFontHeight(10);

        XSSFCellStyle style = workbook.createCellStyle();
        style.setFont(font);
        style.setVerticalAlignment(VerticalAlignment.CENTER);             // 竖向居中
        style.setAlignment(HorizontalAlignment.LEFT);                     // 横向居中
        return style;
    }
}