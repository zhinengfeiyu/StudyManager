package pl.surecase.eu;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class MyDaoGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(2, "com.caiyu.entity");
        // 1: 数据库版本号
        // com.xxx.bean:自动生成的Bean对象会放到/java-gen/com/xxx/bean中

        schema.setDefaultJavaPackageDao("com.caiyu.dao");
        // DaoMaster.java、DaoSession.java、BeanDao.java会放到/java-gen/com/xxx/dao中

        initClassTable(schema);

        new DaoGenerator().generateAll(schema, args[0]);
    }

    private static void initClassTable(Schema schema) {
        Entity classTableEntity = schema.addEntity("ClassTableEntity");// 表名
        classTableEntity.setTableName("class_table"); // 可以对表重命名
        classTableEntity.addIntProperty("id").primaryKey();// 主键，索引
        classTableEntity.addIntProperty("day_of_week");//星期几
        classTableEntity.addIntProperty("order_of_day");//第几节
        classTableEntity.addStringProperty("class_name");//课程名
        classTableEntity.addStringProperty("class_room");//教室
        classTableEntity.addStringProperty("teacher");//教师
        classTableEntity.addIntProperty("start_week");//起始周
        classTableEntity.addIntProperty("end_week");//结束周
    }
}