package Test3;

/**
 * 🐳 复习重写：day10 登录异常体系（2026-09-10 · 晚上 Java 时段任务）
 * 自定义异常①：用户名不存在
 * 参照 untitled/src/day10/ 同名类重写 —— 写完后对照检查
 */
public class UsernameNotFoundException extends Exception {

    // TODO 主人重写区（参照 day10）：无参构造 + 带 message 构造（super(message)）
    public UsernameNotFoundException() {}
    public UsernameNotFoundException(String message) {
        super(message);
    }

}
