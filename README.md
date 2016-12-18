#如何防止SQL注入

>在我们平时的开发中，作为新手写JDBC很有可能忽略了一点，那就是根本没有考虑SQL注入的问题，
那么，什么是SQL注入，以及如何防止SQL注入的问题。

# 一什么是SQL注入

>所谓SQL注入，就是通过把SQL命令插入到Web表单提交或输入域名或页面请求的查询字符串，最终达到欺骗服务器执行恶意的SQL命令。具体来说，它是利用现有应用程序，将（恶意）的SQL命令注入到后台数据库引擎执行的能力，它可以通过在Web表单中输入（恶意）SQL语句得到一个存在安全漏洞的网站上的数据库，而不是按照设计者意图去执行SQL语句。[1]  比如先前的很多影视网站泄露VIP会员密码大多就是通过WEB表单递交查询字符暴出的，这类表单特别容易受到SQL注入式攻击．

# 二.先看一下数据表中的数据

###### 1. 查询代码：
 
 ```java
    /**数据库表名**/
    String tableName = "emp_test";
 
   /**先查看一下数据库当中的表数据**/
     DBUtil.query(tableName, null);
 ```
 
###### 2. 查询结果：
  
  ```java
 SELECT  * FROM emp_test
 成功查询到了14行数据
 第1行：{DEPT_TEST_ID=10, EMP_ID=1001, SALARY=10000, HIRE_DATE=2010-01-12, PASSWORD=123456, BONUS=2000, MANAGER=1005, JOB=Manager, NAME=张无忌}
 第2行：{DEPT_TEST_ID=10, EMP_ID=1002, SALARY=8000, HIRE_DATE=2011-01-12, PASSWORD=123456, BONUS=1000, MANAGER=1001, JOB=Analyst, NAME=刘苍松}
 第3行：{DEPT_TEST_ID=10, EMP_ID=1003, SALARY=9000, HIRE_DATE=2010-02-11, PASSWORD=123456, BONUS=1000, MANAGER=1001, JOB=Analyst, NAME=李翊}
 第4行：{DEPT_TEST_ID=10, EMP_ID=1004, SALARY=5000, HIRE_DATE=2010-02-11, PASSWORD=123456, BONUS=null, MANAGER=1001, JOB=Programmer, NAME=郭芙蓉}
 第5行：{DEPT_TEST_ID=20, EMP_ID=1005, SALARY=15000, HIRE_DATE=2008-02-15, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=President, NAME=张三丰}
 第6行：{DEPT_TEST_ID=20, EMP_ID=1006, SALARY=5000, HIRE_DATE=2009-02-01, PASSWORD=123456, BONUS=400, MANAGER=1005, JOB=Manager, NAME=燕小六}
 第7行：{DEPT_TEST_ID=20, EMP_ID=1007, SALARY=3000, HIRE_DATE=2009-02-01, PASSWORD=123456, BONUS=500, MANAGER=1006, JOB=clerk, NAME=陆无双}
 第8行：{DEPT_TEST_ID=30, EMP_ID=1008, SALARY=5000, HIRE_DATE=2009-05-01, PASSWORD=123456, BONUS=500, MANAGER=1005, JOB=Manager, NAME=黄蓉}
 第9行：{DEPT_TEST_ID=30, EMP_ID=1009, SALARY=4000, HIRE_DATE=2009-02-20, PASSWORD=123456, BONUS=null, MANAGER=1008, JOB=salesman, NAME=韦小宝}
 第10行：{DEPT_TEST_ID=30, EMP_ID=1010, SALARY=4500, HIRE_DATE=2009-05-10, PASSWORD=123456, BONUS=500, MANAGER=1008, JOB=salesman, NAME=郭靖}
 第11行：{DEPT_TEST_ID=null, EMP_ID=1011, SALARY=null, HIRE_DATE=null, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=于泽成}
 第12行：{DEPT_TEST_ID=null, EMP_ID=1012, SALARY=null, HIRE_DATE=2011-08-10, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=amy}
 第13行：{DEPT_TEST_ID=null, EMP_ID=1014, SALARY=8000, HIRE_DATE=null, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=张无忌}
 第14行：{DEPT_TEST_ID=20, EMP_ID=1015, SALARY=null, HIRE_DATE=null, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=刘苍松}
 ```
  
# 三.模拟登录，演示SQL注入

###### 1. 模拟登录伪代码
 
 ```java
            /**用户输入的用户名**/
             String name = "'1' OR '1'='1'";
             /**用户输入的密码**/
             String password = "'1' OR '1'='1'";
             
             /**我们拼SQL语句去查询**/
             String sql = "SELECT * FROM emp_test WHERE name = " + name + " and password = " + password;
             DBUtil.query(sql);
 ```
 
###### 2. 查询结果
  
  ```java
  SELECT * FROM emp_test WHERE name = '1' OR '1'='1' and password = '1' OR '1'='1'
  成功查询到了14行数据
  第1行：{DEPT_TEST_ID=10, EMP_ID=1001, SALARY=10000, HIRE_DATE=2010-01-12, PASSWORD=123456, BONUS=2000, MANAGER=1005, JOB=Manager, NAME=张无忌}
  第2行：{DEPT_TEST_ID=10, EMP_ID=1002, SALARY=8000, HIRE_DATE=2011-01-12, PASSWORD=123456, BONUS=1000, MANAGER=1001, JOB=Analyst, NAME=刘苍松}
  第3行：{DEPT_TEST_ID=10, EMP_ID=1003, SALARY=9000, HIRE_DATE=2010-02-11, PASSWORD=123456, BONUS=1000, MANAGER=1001, JOB=Analyst, NAME=李翊}
  第4行：{DEPT_TEST_ID=10, EMP_ID=1004, SALARY=5000, HIRE_DATE=2010-02-11, PASSWORD=123456, BONUS=null, MANAGER=1001, JOB=Programmer, NAME=郭芙蓉}
  第5行：{DEPT_TEST_ID=20, EMP_ID=1005, SALARY=15000, HIRE_DATE=2008-02-15, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=President, NAME=张三丰}
  第6行：{DEPT_TEST_ID=20, EMP_ID=1006, SALARY=5000, HIRE_DATE=2009-02-01, PASSWORD=123456, BONUS=400, MANAGER=1005, JOB=Manager, NAME=燕小六}
  第7行：{DEPT_TEST_ID=20, EMP_ID=1007, SALARY=3000, HIRE_DATE=2009-02-01, PASSWORD=123456, BONUS=500, MANAGER=1006, JOB=clerk, NAME=陆无双}
  第8行：{DEPT_TEST_ID=30, EMP_ID=1008, SALARY=5000, HIRE_DATE=2009-05-01, PASSWORD=123456, BONUS=500, MANAGER=1005, JOB=Manager, NAME=黄蓉}
  第9行：{DEPT_TEST_ID=30, EMP_ID=1009, SALARY=4000, HIRE_DATE=2009-02-20, PASSWORD=123456, BONUS=null, MANAGER=1008, JOB=salesman, NAME=韦小宝}
  第10行：{DEPT_TEST_ID=30, EMP_ID=1010, SALARY=4500, HIRE_DATE=2009-05-10, PASSWORD=123456, BONUS=500, MANAGER=1008, JOB=salesman, NAME=郭靖}
  第11行：{DEPT_TEST_ID=null, EMP_ID=1011, SALARY=null, HIRE_DATE=null, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=于泽成}
  第12行：{DEPT_TEST_ID=null, EMP_ID=1012, SALARY=null, HIRE_DATE=2011-08-10, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=amy}
  第13行：{DEPT_TEST_ID=null, EMP_ID=1014, SALARY=8000, HIRE_DATE=null, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=张无忌}
  第14行：{DEPT_TEST_ID=20, EMP_ID=1015, SALARY=null, HIRE_DATE=null, PASSWORD=123456, BONUS=null, MANAGER=null, JOB=null, NAME=刘苍松}
  ```
 
 >通过上面的操作我们发现，这根本就不是我们想要的结果，这样的话，随便一个人都能够登录成功了，那么如何去避免这种事情呢？
 
# 四.模拟登录，防止SQL注入
 
###### 1. 模拟登录伪代码
  
  ```java
               /**用户输入的用户名**/
               String name = "'1' OR '1'='1'";
               /**用户输入的密码**/
               String password = "'1' OR '1'='1'";
               
              /**我们用参数绑定去查询**/
               String where = "name = ?  AND password = ? ";
               String[] whereArgs = new String[]{name, password};
               DBUtil.query("emp_test", where, whereArgs);
  ```
  
###### 2. 查询结果
   
  ```java
    /**这条语句只是为了方便调试自己代印的语句，并不是PreparedStatement真正执行的SQL语句**/
    SELECT  * FROM emp_test WHERE name = '1' OR '1'='1'  AND password = '1' OR '1'='1'
    成功查询到了0行数据
  ```
   >通过参数绑定预编译的方案我们就可以有效的避免这种情况的发生。
   
# 五.[GitHub](https://github.com/linglongxin24/SQLInjection)