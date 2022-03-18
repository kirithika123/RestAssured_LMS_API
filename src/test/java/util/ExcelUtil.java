package util;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelUtil {
    public static FileInputStream fi;
    public static FileOutputStream fo;



    public static int getRowCount(String xlFile, String xlSheet) throws IOException {
        Workbook workbook;
        Sheet sheet;
        fi = new FileInputStream(xlFile);
        workbook = WorkbookFactory.create(fi);
        sheet = workbook.getSheet(xlSheet);
        int rowCount = sheet.getLastRowNum();
        workbook.close();
        fi.close();
        return rowCount;
    }
    public static int getCellCount(String xlFile, String xlSheet, int rowNum) throws IOException{
        Workbook workbook;
        Sheet sheet;
        Row row;
        fi = new FileInputStream(xlFile);
        workbook = WorkbookFactory.create(fi);
        sheet = workbook.getSheet(xlSheet);
        row = sheet.getRow(rowNum);
        int columnCount = row.getLastCellNum();
        workbook.close();
        fi.close();
        return columnCount;
    }
    public static String getCellData(String xlFile, String xlSheet, int rowNum, int columnNum) throws IOException{
        Workbook workbook;
        Sheet sheet;
        Row row;
        Cell cell;
        fi = new FileInputStream(xlFile);
        workbook = WorkbookFactory.create(fi);
        sheet = workbook.getSheet(xlSheet);
        row = sheet.getRow(rowNum);
        cell = row.getCell(columnNum);
        String data;
        try {

            DataFormatter formatter = new DataFormatter();
            data = formatter.formatCellValue(cell);

        } catch (Exception e) {
            data = "";
        }

        workbook.close();
        fi.close();
        return data;
    }
    public static void setCellData(String xlFile, String xlSheet, int rowNum, String columnName, String data) throws IOException {
        Workbook workbook;
        Sheet sheet;
        Row row;
        Cell cell;
        fi = new FileInputStream(xlFile);
        workbook = WorkbookFactory.create(fi);
        sheet = workbook.getSheet(xlSheet);
        row = sheet.getRow(rowNum);
        int colCount=row.getPhysicalNumberOfCells();
        String colName;
        int columnNum = 0;
        for(int j=0;j<colCount;j++){
            colName = String.valueOf(sheet.getRow(0).getCell(j)).trim();
            if (colName.equals(columnName)){
                columnNum=j;
                break;
            }
        }

        cell = row.getCell(columnNum);
        fi = new FileInputStream(xlFile);
        cell.setCellValue(data);
        fo = new FileOutputStream(xlFile);
        workbook.write(fo);
        workbook.close();
        fo.close();
        fi.close();
    }

}

