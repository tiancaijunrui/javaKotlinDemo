package main.test;

/**
 * @Since2017/8/2 ZhaCongJie@HF
 */

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class autoCreateSqlTest {

    public static void main(String[] args) throws IOException {
        Map<String,String> execFiledMap = propertiesToMap();
        String path = "C:\\Users\\junrui\\Desktop\\20170731(1)\\20170731";
        File filePth = new File(path);
        if (filePth.exists()) {
            File[] files = filePth.listFiles();
            assert files != null;
            for (File file2 : files) {
                System.out.println(file2.getName());
                String file_dir = file2.getAbsolutePath();
                String fileName = file2.getName().substring(0, file2.getName().indexOf("xlsx") - 1);
                List<String> buildSql = readExcel(file_dir, fileName,execFiledMap);
                createSqlFile(fileName, buildSql);
            }
        }
    }

    private static void createSqlFile(String fileName, List<String> buildSql) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter("C:\\Users\\junrui\\Desktop\\新建文件夹 (2)\\" + fileName + ".sql", "utf-8");
        for (String sql : buildSql) {
            writer.println(sql);
        }
        writer.close();
    }

    private static List<String> readExcel(String file_dir, String fileName,Map<String,String> dataMap) throws IOException {
        Workbook book = null;
        book = getExcelWorkbook(file_dir);
        Sheet sheet = getSheetByNum(book, 0);
        int lastRowNum = sheet.getLastRowNum();
        List<String> list = new ArrayList<>();
        StringBuilder builder = new StringBuilder();
        StringBuilder builderHead = new StringBuilder();
        List<String> buildSqlList = new ArrayList<>();
        buildSqlList.add("delete from " + fileName + ";");
        int hasEmptyHead = 0;
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = null;
            row = sheet.getRow(i);
            if (row != null) {
                int lastCellNum = row.getLastCellNum();
                Cell cell = null;
                builder.delete(0, builder.length());
                if (i == 0) {
                    builderHead.append("INSERT INTO ").append(fileName).append(" (");
//                    int sign = 0;
//                    if (MapUtils.isNotEmpty(dataMap) && StringUtils.isNotBlank(dataMap.get(fileName))){
//                        sign = dataMap.get(fileName).split(",").length;
//                    }
                    for (int j = 0; j < lastCellNum; j++) {
                        cell = row.getCell(j);
                        if (cell != null) {
                            String cellValue = cell.getStringCellValue();
                            if (StringUtils.isBlank(cellValue)) {
                                hasEmptyHead = 1;
                                continue;
                            }
                            list.add(cellValue);
                            if (j == lastCellNum - 1) {
                                builderHead.append("`").append(cellValue).append("`)");
                            } else {
                                builderHead.append("`").append(cellValue).append("`,");
                            }
                        }
                    }
                    continue;
                }
                builder.append(builderHead.toString()).append(" VALUES(");
                for (int j = hasEmptyHead, size = list.size() + hasEmptyHead; j < size; j++) {
                    cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = null;
                        cellValue = parseExcel(cell);
                        if (j == size - 1) {
                            builder.append("'").append(cellValue).append("')");
                        } else {
                            builder.append("'").append(cellValue).append("',");
                        }
                    } else {
                        if (j == size - 1) {
                            builder.append("NULL").append(")");
                        } else {
                            builder.append("NULL").append(",");
                        }
                    }
                }
                builder.append(";");
                buildSqlList.add(builder.toString());
                System.out.println(builder.toString());
            }
        }
        buildSqlList.add("select count(*) from " + fileName + ";");
        return buildSqlList;
    }

    private static Sheet getSheetByNum(Workbook book, int number) {
        Sheet sheet = null;
        try {
            sheet = book.getSheetAt(number);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        return sheet;
    }

    private static Workbook getExcelWorkbook(String filePath) throws IOException {
        Workbook book = null;
        File file = null;
        FileInputStream fis = null;

        try {
            file = new File(filePath);
            if (!file.exists()) {
                throw new RuntimeException("文件不存在");
            } else {
                fis = new FileInputStream(file);
                book = WorkbookFactory.create(fis);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            if (fis != null) {
                fis.close();
            }
        }
        return book;
    }

    private static String parseExcel(Cell cell) {
        String result;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                            .getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil
                            .getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value).replaceAll(",", "");
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:// String类型
                result = TransactSQLInjection(cell.getRichStringCellValue().toString());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                result = "";
            default:
                result = "";
                break;
        }
        return result;
    }

    public static String TransactSQLInjection(String str) {
//        return str.replaceAll(".*([';]+|(--)+).*","");
        return str.replaceAll("'", "‘");
    }
    public static  Map<String, String> propertiesToMap(){
        Properties properties=new Properties();
        try {
            properties.load(new FileInputStream("D:\\IdeaProjects\\javaKotlinDemo\\src\\test\\java\\main\\test\\config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<Object> keys=properties.keySet();
        Map<String, String> map=new HashMap<String, String>();
        for (Object k : keys) {
            map.put((String)k, (String)properties.get(k));
        }
        return  map;
    }
}
