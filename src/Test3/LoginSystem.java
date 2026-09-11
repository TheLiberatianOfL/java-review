package Test3;

import java.util.Scanner;

/**
 * 🐳 复习重写：登录系统 · 异常体系串联（2026-09-10 · 晚上 Java 时段任务）
 * 参照 untitled/src/day10/ 的登录异常素材，把整个流程重写一遍：
 *
 * 要求（写一个完整可运行的登录模拟）：
 *  1. 模拟用户表：预设 1~2 个账号密码（可放数组 / 简单类）
 *  2. 登录方法 login(String username, String password)：
 *     - 用户名查不到      → 抛 UsernameNotFoundException("用户名不存在")
 *     - 密码对不上        → 抛 BadCredentialsException("密码错误")
 *     - 都对              → 提示登录成功
 *  3. main 里调用 login，用 try-catch 分别捕获两个自定义异常并提示
 *  4. 验证异常体系：为什么继承 Exception（受检异常）而不是 RuntimeException？
 *
 * 自测期望：
 *  输入错误用户名 → 捕获 UsernameNotFoundException
 *  输入正确用户名 + 错误密码 → 捕获 BadCredentialsException
 *  全对 → 登录成功
 *
 * ⚠️ 核心考点：自定义异常的抛出时机（throw）与声明（throws）、
 *    受检异常必须处理（try-catch 或继续 throws）、多 catch 分支顺序。
 */

// TODO 2026-09-10：主人重写区 —— 从空类开始写，写完编译运行验证

public class LoginSystem {
    int[] password=new int[]{123456,456789};
    String[] username={"admin","26437"};
    public void login(String user,int pass){
        System.out.println("Enter your username: ");

        try {
            if (!user.equals(username[0]) &&! user.equals(username[1])) {
                throw new UsernameNotFoundException("Username or password doesn't match");

            }
            int i=-1;
                if(user.equals(username[0])){
                i=0;
                }
             else {
                 i=1;
             }
                System.out.println("Enter your password: ");
                if(pass==password[i]){
                 System.out.println("Login successful");

            }
            else {
                 throw new BadCredentialsException("Bad credentials");
             }
        }
           catch(UsernameNotFoundException e){
                   System.out.println("请输入正确的账号");
            }
            catch(BadCredentialsException e){
                System.out.println("密码输入错误");
         }
     }
    public static void main(String[] args) {
        LoginSystem l=new LoginSystem();
        Scanner sc=new Scanner(System.in);
        String user=sc.nextLine();
        int pass=sc.nextInt();
        l.login(user,pass);
    }
}
