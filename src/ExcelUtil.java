import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExcelUtil {
    public static ArrayList<ArrayList<Object>> readExcel(File file){
        if (file == null) return null;
        if (file.getName().endsWith("xlsx")){
            return readXlsx(file);
        } else return readXls(file);

    }
    public static ArrayList<ArrayList<Object>> readXlsx(File file){
        ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> colList;
        XSSFWorkbook wb = null;
        try {
            wb = new XSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        XSSFSheet sheet = wb.getSheetAt(0);
        XSSFRow row;
        XSSFCell cell;
        Object value;
        for (int i=sheet.getFirstRowNum(),rowCount=0;rowCount<sheet.getPhysicalNumberOfRows();i++){
            row = sheet.getRow(i);
            colList = new ArrayList<Object>();
            if (row==null){
                if (i<sheet.getPhysicalNumberOfRows()){
                    rowList.add(colList);
                }
                continue;
            }
            else {
                rowCount++;
            }
            for (int j=row.getFirstCellNum();j<=row.getLastCellNum();j++){
                cell = row.getCell(j);
                if(cell == null){
                    if(j != row.getLastCellNum()){
                        colList.add("");
                    }
                    continue;
                }
                switch (cell.getCellTypeEnum()){
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    case BOOLEAN:
                        value =cell.getBooleanCellValue();
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        if ((Math.round((Double) value))-(Double)value==0) value = Math.round((Double) value);
                        break;
                    case BLANK:
                        value = "";
                        break;
                    default:value = cell.getRawValue();
                }
                colList.add(value);
            }
            rowList.add(colList);
        }
        return rowList;
    }
    public static ArrayList<ArrayList<Object>> readXls(File file){
        ArrayList<ArrayList<Object>> rowList = new ArrayList<ArrayList<Object>>();
        ArrayList<Object> colList;
        HSSFWorkbook wb = null;
        try {
            wb = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HSSFSheet sheet = wb.getSheetAt(0);
        HSSFRow row;
        HSSFCell cell;
        Object value;
        for (int i=sheet.getFirstRowNum(),rowCount=0;rowCount<sheet.getPhysicalNumberOfRows();i++){
            row = sheet.getRow(i);
            colList = new ArrayList<Object>();
            if (row==null){
                if (i<sheet.getPhysicalNumberOfRows()){
                    rowList.add(colList);
                }
                continue;
            }
            else {
                rowCount++;
            }
            for (int j=row.getFirstCellNum();j<=row.getLastCellNum();j++){
                cell = row.getCell(j);
                if(cell == null){
                    if(j != row.getLastCellNum()){
                        colList.add("");
                    }
                    continue;
                }
                switch (cell.getCellTypeEnum()){
                    case STRING:
                        value = cell.getStringCellValue();
                        break;
                    case BOOLEAN:
                        value =cell.getBooleanCellValue();
                        break;
                    case NUMERIC:
                        value = cell.getNumericCellValue();
                        break;
                    case BLANK:
                        value = "";
                        break;
                        default:value = cell.getStringCellValue();
                }
                colList.add(value);
            }
            rowList.add(colList);
        }
        return rowList;
    }
}
