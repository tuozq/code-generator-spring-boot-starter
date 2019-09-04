# code-generator-spring-boot-starter

mybatis数据层代码生成工具，并且对mapper查询进行了封装，更加方便使用。

#### BaseRepository源码，提供了常用的增删改查方法、批量数据处理、以及特色的FilterInfo对象查询工具

```Java
public interface BaseRepository<M extends BaseEntity<ID>, ID> {
    
    int insert(M model);

    int insertSelective(M model);

    int deleteByPrimaryKey(ID primaryKey);

    int deleteByPrimaryKeys(List<ID> primaryKeys);

    int deleteByFilter(@Param("filter") FilterInfo filter);

    int updateByPrimaryKey(M model);

    int updateByPrimaryKeySelective(M model);

    int updateByFilterSelective(@Param("model") M model, @Param("filter") FilterInfo filter);

    M selectByPrimaryKey(ID primaryKey);

    List<M> selectByPrimaryKeys(List<ID> var1);

    List<M> selectAll();

    List<M> selectByEntity(M model);

    List<M> selectByFilter(@Param("filter") FilterInfo filter);

    int insertBatch(List<M> list);

    int updateBatch(List<M> list);

    Long count();

    Map<String, Object> selectByFunction(@Param("function") String function, @Param("column") String column);

}
```


### 1. 直接实例化AutoGenerator实例（建议使用）
```Java
    public static void main(String args[]){
        // 需要生成代码的table, key = 数据库表名 -> value = java类名
        Map<String, String> map = new HashMap<>();
        map.put("CT_ENQ_InquiryBill", "InquiryBill");
        map.put("CT_ENQ_InquiryBillEntry", "InquiryBillEntry");
        // 配置信息
        CodeGeneratorProperties properties = new CodeGeneratorProperties();
        // 代码输出路径
        properties.setOutputDirectory("G:\\test\\project\\project-oms");
        // 数据库联系信息
        properties.setDatasource(new Jdbc("user", "****", "jdbc:oracle:thin:@******:1521/orcl", "oracle.jdbc.driver.OracleDriver"));
        AutoGenerator autoGenerator = new AutoGenerator(properties);
        Configuration configuration = new Configuration();
        // 设置为不拆分目录生成
        //configuration.setSplitCatalogue(false);
        // 设置自带基类的扩展
        //configuration.setModelExtendType(BaseModelExt.class);
        // 生成代码， moduleName 模块名称
        autoGenerator.generator(configuration,"oms", autoGenerator.mapToTables(map));
    }
```

### 2. 自动注入AutoGenerator，可在properties文件中配置输出路径、数据源信息，生成代码时只需要指定table即可
```Java
application.properties 配置文件

spring:
  code:
    generator:
      outputDirectory: G:\\test\\project\\project-oms
      datasource:
        user: admin
        password: 123456
        url: jdbc:oracle:thin:@****:1521/orcl
        driver: oracle.jdbc.driver.OracleDriver
```

```Java
    @Autowired
    AutoGenerator autoGenerator;

    @RequestMapping(value = "/generator", method = RequestMethod.POST)
    public Ret generator() {
        // 可以做成可视化界面，由前端提交需要生成代码的表以及模块信息
        Map<String, String> map = new HashMap<>();
        map.put("CT_ENQ_InquiryBill", "InquiryBill");
        map.put("CT_ENQ_InquiryBillEntry", "InquiryBillEntry");
        autoGenerator.generator("project-oms", autoGenerator.mapToTables(map));
        return Ret.ok();
    }
```

示例中的代码执行完成后将会在 G:\test\project\project-oms 目录下 自动生成 project-oms-dao、project-oms-service文件夹
