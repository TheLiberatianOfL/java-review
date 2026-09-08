# 🐳 java-review · 复习专用仓（W1–W3 复习期）

> 16 周实习冲刺计划 · 复习期代码 + 复盘笔记专用仓。
> 跟 untitled(day1-15) / pokemon / practices 彻底分家，不窜味。
> 规则：**每成功完成一项复习 commit → 同步一份复盘笔记**（错题集 + 知识点自查）。

## 📁 结构

```
java-review/
├── src/          ← 复习代码（按 package 分主题）
│   └── Test1/    动物收养站：Animal / Dog / Cat / ZooMain
└── README.md     ← 复盘笔记（错题集，越攒越厚）
```

## 📋 复盘记录

### 2026-09-08 · W1 · Java 封装 + 继承（多态雏形）· commit 3793b07

**练习**：动物收养站（Animal / Dog / Cat / ZooMain）

#### 踩的坑（错题集，重点！）

1. **拼写错误**：`describe` 写成 `dicribe`（i 和 r 反了）
   → 不报错但扣印象分。教训：写完自查英文单词。
2. **漏写 setter**：只写了 getter 忘了 setter
   → 封装 = private 字段 + 按需 getter/setter，成对检查。
3. **命名不规范**：`makesound` 全小写
   → Java 驼峰：`makeSound()`，方法名首字母小写、后续单词大写。

#### 知识点自查

| 主题 | 要点 | 今天验证 |
|---|---|---|
| 封装 | private 字段 + getter/setter；子类走 getter 取父类私有字段 | ✔ Dog/Cat 用 `getName()` |
| 继承 | `extends`；`super(name,age)` 第一行调父类构造；is-a 关系 | ✔ |
| 重写 | 同名+同参+同返回；`@Override` 让编译器查错 | ✔ 三个方法都标了 |
| 多态(预埋) | `Animal a = new Cat()`；父类引用调子类重写版；子类特有方法要 `((Cat)a).purr()` 向下转型 | ✔ 输出验证 |

#### 运行输出（多态生效证据）

```
Dog barks
咪咪 meows          ← 父类引用调用到 Cat 的重写方法
旺财 is fetching the ball
咪咪 is purring     ← ((Cat)a).purr() 向下转型成功
```

#### 下一步
- 周三：多态 + 接口/抽象类（动物饲养员，接今天的地基）
