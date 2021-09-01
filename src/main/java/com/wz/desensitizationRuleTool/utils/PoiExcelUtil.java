package com.wz.desensitizationRuleTool.utils;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.security.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * poi-excel 工具类
 * @author Angelo
 * @date 2021/9/1 10:26
 **/
public class PoiExcelUtil {

    /**
     * 获取excel的表头（去重后）
     * @param file
     * @return
     * @throws Exception
     */
    public static Set<String> getExcelHeaders(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println("该excel除标题共"+sheet.getLastRowNum()+"条数据");
        //获取 excel 第一行数据（表头）
        Row row = sheet.getRow(0);
        //存放表头信息
        Set<String> set = new HashSet<>();
        //算下有多少列
        int colCount = sheet.getRow(0).getLastCellNum();
        System.out.println("该excel共"+colCount+"列");
        for (int j = 0; j < colCount; j++) {
            Cell cell = row.getCell(j);
            String cellValue = cell.getStringCellValue().trim();
            set.add(cellValue);
        }
        return set;
    }


    /**
     * 获取 Excel 文件信息(除去表头)
     *
     * @param file
     * @return
     * @throws Exception
     */
    public static List<List<String>> getExcelData(File file) throws Exception {
        InputStream is = new FileInputStream(file);
        Workbook workbook = new XSSFWorkbook(is);
        Sheet sheet = workbook.getSheetAt(0);
        //获取 Excel 中 sheet 的行数
        int rowNum = sheet.getLastRowNum();
        List<List<String>> resList = new ArrayList<>();
        //负责标记检测到空行时,跳过
        boolean flag = false;
        for (int i = 1; i <= rowNum; i++) {
            //默认认为此行为空行
            flag = true;
            Row row = sheet.getRow(i);
            //过滤空行
            if (row == null) {
                continue;
            }
            //创建列表，负责装纳一行数据
            List<String> list = new ArrayList<>();
            //获取列数
            int colCount = sheet.getRow(i).getLastCellNum();
            for (int j = 0; j < colCount; j++) {
                //获得制定空格
                Cell cell = row.getCell(j);
                String cellValue = "";
                //如果存在空格内有内容,就将标志位设置为 false，表示这一行不是空行
                if(!(cell == null)){
                    cellValue = getStringCellValue(cell);
                    if(!"".equals(cellValue)){
                        flag = false;
                    }
                }
                list.add(cellValue);
            }
            if(!flag){
                resList.add(list);
            }else{
                continue;
            }
        }
        return resList;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    public static String getStringCellValue(Cell cell) {
        String strCell = "";
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue().trim();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue()).trim();
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case Cell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 将excel数据组装成map
     * @param heads
     * @param list
     * @return
     */
    public static ObservableList<Map<String, Object>> generateMapData(Set<String> heads, List<List<String>> list){
        List<String> headList = new ArrayList<>(heads);
        ObservableList<Map> allData = FXCollections.observableArrayList();
        list.forEach(rowData->{
            Map<String,String> dataRow = new HashMap<>();
            for (int i = 0; i < headList.size(); i++) {
                String headColumn = headList.get(i);
                dataRow.put(headColumn,rowData.get(i));
            }
            allData.add(dataRow);
        });
        ObservableList<Map<String, Object>> analysisResults = FXCollections.observableArrayList();
        allData.forEach(data->{
            analysisResults.add(data);
        });
        return analysisResults;
    }

    /**
     * 导出数据
     *
     * @param headMap
     * @param dataList
     */
    public static void exportXlsx(String sheetName, Map<String, String> headMap, List<Map<String, Object>> dataList,File file) throws IOException {

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet = workbook.createSheet(sheetName);


        int rowIndex = 0, columnIndex = 0;
        Set<String> keys = headMap.keySet();

        //表头
        Row row = sheet.createRow(rowIndex++);
        for (String key : keys) {
            Cell cell = row.createCell(columnIndex++);
            cell.setCellValue(headMap.get(key));
        }

        //内容
        if (dataList != null && !dataList.isEmpty()) {
            for (Map<String, Object> map : dataList) {
                row = sheet.createRow(rowIndex++);
                columnIndex = 0;
                for (String key : keys) {
                    Cell cell = row.createCell(columnIndex++);
                    setCellValue(cell, map.get(key));
                }
            }
        }
        //
        if(workbook!=null && file!=null){
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            //写入在输出流
            workbook.write(fileOutputStream);
            //关闭输出流
            fileOutputStream.close();
        }

    }

    private static void setCellValue(Cell cell, Object obj) {
        if (obj == null) {
            return;
        }
        if (obj instanceof String) {
            cell.setCellValue((String) obj);
        } else if (obj instanceof Date) {
            Date date = (Date) obj;
            if (date != null) {
                cell.setCellValue(DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss"));
            }
         } else if (obj instanceof Double) {
            cell.setCellValue((Double) obj);
        } else {
            cell.setCellValue(obj.toString());
        }
    }


}
