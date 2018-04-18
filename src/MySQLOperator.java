import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MySQLOperator {
    static Connection conn = null;
    static Statement stmt = null;
    static MyDBConnection myDBConnection=new MyDBConnection();
    private MySQLOperator(){

    }
    public static void createDatabase(String DBName){

        try {
            myDBConnection.initWithoutDB();
            conn = myDBConnection.getMyConnection();
            stmt = conn.createStatement();
            String sql="CREATE DATABASE "+DBName+";";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            if (conn!=null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static void createTable(String DBName, String tableName, ArrayList<ArrayList<Object>> excelData){
        try {
            myDBConnection.initWithDB(DBName);
            conn = myDBConnection.getMyConnection();
            stmt = conn.createStatement();
            ArrayList<String> colName = new ArrayList<String>();
            for (Object o:excelData.get(0)) {
                String name = (String) o;

                colName.add(name.replaceAll("[\\pP\\p{Punct}]",""));
            }
            ArrayList<String> dataType = new ArrayList<String>();
            for (Object o:excelData.get(1)) {
                if (o instanceof Integer) dataType.add("INTEGER");
                else if (o instanceof String) dataType.add("VARCHAR(255)");
                else if (o instanceof Double) dataType.add("DOUBLE");
                else dataType.add("VARCHAR(255)");

            }
            StringBuffer parameter=new StringBuffer("");
            for (int i=0;i<colName.size()&&i<dataType.size();i++)  parameter.append(colName.get(i)+" "+dataType.get(i)+",");
            System.out.println(parameter.toString());
            String sql = "CREATE TABLE "+tableName +
                    "(" +
                    parameter.toString()+
                    " PRIMARY KEY ( id ))";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (conn!=null)
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void excel2Tables(String DBName, String tableName, ArrayList<ArrayList<Object>> excelData){

        try {
            myDBConnection.initWithDB(DBName);
            conn = myDBConnection.getMyConnection();
            stmt = conn.createStatement();

            String checkTable="SHOW TABLES like \""+tableName+"\";";
            ResultSet resultSet= stmt.executeQuery(checkTable);
//            System.out.println(resultSet.getString(0));
            if (!resultSet.next()) createTable(DBName,DBName,excelData);

            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO "+tableName+" VALUES(?,?,?,?,?,?,?,?,?,?);");
            Boolean isParameterRow=true;
            for (ArrayList<Object> rowData:excelData){
                if (isParameterRow){
                    isParameterRow=false;
                    continue;
                }
//                ArrayList<Object> cellValues = new ArrayList<Object>();
                int count=1;
                for (Object o:rowData) {
//                    cellValues.add(o);
                    //不同列类型不同怎么批量处理？
                    if (o instanceof Double) pstmt.setDouble(count, (double) o);
                    else if (o instanceof Long)pstmt.setLong(count,(long) o);
                    else if (o instanceof Integer)pstmt.setInt(count,(int)o);
                    else pstmt.setString(count,(String)o);
                    count++;
                }
//                String values=Arrays.toString(cellValues.toArray());
//                Array values = conn.createArrayOf("NUMERIC",cellValues.toArray());
//                pstmt.setArray(1,values);


                pstmt.addBatch();
            }
            pstmt.executeBatch();




        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                if (conn!=null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
