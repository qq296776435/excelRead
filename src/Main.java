import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Input your Excel absolute path: ");

        File file = new File(input.nextLine());
        ArrayList<ArrayList<Object>> result = ExcelUtil.readExcel(file);
        MySQLOperator.createDatabase("stu");
        MySQLOperator.createTable("stu","stu",result);
        MySQLOperator.excel2Tables("stu","stu",result);
//        for(int i = 0 ;i < result.size() ;i++){
//            for(int j = 0;j<result.get(i).size(); j++){
//                System.out.println(i+"行 "+j+"列  "+ result.get(i).get(j).toString());
//            }
//        }

    }
}
