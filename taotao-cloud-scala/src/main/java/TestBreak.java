public class TestBreak {
    public static void main(String[] args) {
        try{
            for (int i = 0; i < 5; i++){
                if (i == 3)
//                    break;
                    throw new RuntimeException();
                System.out.println(i);
            }
        } catch (Exception e){
            // 什么都不做，只是退出循环
        }
        System.out.println("这是循环外的代码");
    }
}
