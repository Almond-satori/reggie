# Day1

实体类到controller

```java
@Data
public class Employee implements Serializable {...}
```

```java
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
```

```
public interface EmployeeService extends IService<Employee> {
}
```

```java
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
```

```java
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
}
```



登录逻辑:在backend,request.js中更改前端接收响应的时间,方便debug

```
    // 超时,运行为10000,debug时为1000000
    timeout: 1000000
```



