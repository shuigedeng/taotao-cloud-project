package com.taotao.cloud.java.javaee.s1.c3_mybatis.hellomybatis.test;

public class TestMybatis2 {
    /*

    public static void test3(){
        PassengerDAO mapper = MyBatisUtil.getMapper(PassengerDAO.class);
        Passenger passenger = mapper.queryPassengerById(1);
        System.out.println("============");
        System.out.println(passenger);
        System.out.println(passenger.getPassport());

        PassportDAO mapper = MyBatisUtil.getMapper(PassportDAO.class);
        Passport passport = mapper.queryPassportById(1);
        System.out.println(passport);
        System.out.println(passport.getPassenger());

        DepartmentDAO mapper = MyBatisUtil.getMapper(DepartmentDAO.class);
        Department department = mapper.queryDepartmentById(2);
        System.out.println(department);
        List<Employee> employees = department.getEmployees();
        for (Employee employee : employees) {
            System.out.println(employee);
        }

    EmployeeDAO mapper = MyBatisUtil.getMapper(EmployeeDAO.class);
    Employee employee = mapper.queryEmployeeById(2);
        System.out.println(employee);
        System.out.println(employee.getDepartment());
    }
    public static void test2(){

        *//*StudentDAO studentMapper = MyBatisUtil.getMapper(StudentDAO.class);

        Student student = new Student(null,"test_util",true);
        studentMapper.insertStudent(student);

        MyBatisUtil.commit();*//*

        UserDAO userMapper = MyBatisUtil.getMapper(UserDAO.class);
        User user = userMapper.queryUserById(10006);
        System.out.println(user);
    }
    public static void test1() throws IOException {
        // MyBatis API
        // 1. 加载配置文件
        InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");

        // 2. 构建 SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 3. 通过SqlSessionFactory 创建 SqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();

        //4. 通过SqlSession 获得 DAO实现类的对象
        UserDAO mapper = sqlSession.getMapper(UserDAO.class);//获取UserDAO对应的实现类的对象

        StudentDAO studentMapper = sqlSession.getMapper(StudentDAO.class);
        新增用户
        User new_user = new User(null, "shine_222", "00000", true, new Date());

        mapper.insertUser(new_user);

        System.out.println(new_user);*//*

        //学生增加
        Student student = new Student(null, "shine_001", true);
        studentMapper.insertStudent(student);
        System.out.println(student);
        //5.提交事务
        sqlSession.commit();
        //sqlSession.rollback();

        //6. 资源释放
        sqlSession.close();
    }*/
}
