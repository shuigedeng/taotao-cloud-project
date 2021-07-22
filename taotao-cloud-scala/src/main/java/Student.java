import java.lang.reflect.Array;
import java.util.Arrays;

public class Student {
    private String name;
    private Integer age;
    private static String school = "atguigu";

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void printInfo(){
        System.out.println(this.name + " " + this.age + " " + Student.school);
    }

    public static void main(String[] args) {
        Student alice = new Student("alice", 20);
        Student bob = new Student("bob", 23);
        alice.printInfo();
        bob.printInfo();
    }
}
