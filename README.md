# code-generator-spring-boot-starter

mybatis数据层代码生成工具，并且对mapper查询进行了封装，更加方便使用。

### 1. 直接实例化AutoGenerator实例
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
        properties.setDatasource(new Jdbc("admin", "123456", "jdbc:oracle:thin:@****:1521/orcl", "oracle.jdbc.driver.OracleDriver"));
        AutoGenerator autoGenerator = new AutoGenerator(properties);
        // 生成代码， moduleName 选填
        autoGenerator.generator("project-oms", autoGenerator.mapToTables(map));
    }
```

### 2. 自动注入AutoGenerator，可在properties文件中配置输出路径、数据源信息，生成代码时只需要指定table即可
```Java
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
        Map<String, String> map = new HashMap<>();
        map.put("CT_ENQ_InquiryBill", "InquiryBill");
        map.put("CT_ENQ_InquiryBillEntry", "InquiryBillEntry");
        autoGenerator.generator("project-oms", autoGenerator.mapToTables(map));
        return Ret.ok();
    }
```

执行完成后将会在 G:\test\project\project-oms 目录下 自动生成 project-oms-dao、project-oms-service文件夹
