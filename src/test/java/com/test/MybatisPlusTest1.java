package com.test;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.test.entity.User;
import com.test.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author dwb
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisPlusTest1 {

    @Autowired
    private UserMapper userMapper;

    /**
     *  用mybatis plus CURD 操作 熟悉下使用
     */
    @Test
    public void selectTest(){
        //查询所有
        List<User> users = userMapper.selectList(null);
        users.forEach(System.out::println);

    }

    /**
     * insert into  操作
     * 引出问题 ： 主键生成策略
     *  一般都是用数据库 自增主键
     *  没有自增主键 : 未设置主键，会使用雪花算法。
     *  entity中注解 @TableId(type = IdType.Auto)
     */
    @Test
    public void insertTest(){
        User user = new User(null, "丁文彬2", 28, "365319828@qq.com",null);
        //插入User
        int insert = userMapper.insert(user);

        System.out.println("insert = " + insert);
    }

    /**
     * update 操作
     * 未赋值的不会去修改
     */
    @Test
    public void updateTest(){

        User user = new User(1333973169598107649L, "修改1", 30, null,null);
        //通过id去修改 NULL值不会去修改
        int i = userMapper.updateById(user);

        System.out.println("i = " + i);
    }


    /**
     * 乐观锁 测试
     */
    @Test
    public void optLockTest(){

        // 1.查询用户
        User user = userMapper.selectById(1);

        //2.修改用户信息
        user.setName("乐观锁");

        //3.执行更新操作
        int update = userMapper.updateById(user);

        System.out.println("update = " + update);

    }


    /**
     * 乐观锁测试2
     * 模拟更新并发
     */
    @Test
    public void optLockTest2(){
        // 1.查询用户
        //User user = userMapper.selectById(1);
        //user.setName("乐观锁");
        //2.修改用户信息
        //3.执行更新操作
        //int update = userMapper.updateById(user);
        //System.out.println("乐观锁 = " + update);

        new Thread(()->{

            this.casUpdate(1L);

            System.out.println(Thread.currentThread().getName() + "\t" + "更新完成");
        },"AAA").start();

        new Thread(()->{
            //在更新前被别的线程更新了
            User user2 = userMapper.selectById(1);
            user2.setName("捷足先登");
            int i = userMapper.updateById(user2);
            System.out.println(Thread.currentThread().getName() + "\t" + "更新完成");
        },"BBB").start();


        try {
            TimeUnit.SECONDS.sleep(6);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用cas + 乐观锁 更新。
     * @param id
     */
    public void casUpdate(Long id){
        //查询用户获取 version
        User user = userMapper.selectById(id);
        user.setName("cas1111");

        //暂停3秒 被别人修改掉
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        while( ! (userMapper.updateById(user) > 0 ) ){
            //更新失败 循环 再去获取version最新值
            user.setVersion(userMapper.selectById(id).getVersion());
        }
    }


    /***
     * 查询操作
     */
    @Test
    public void selectByIdTest(){
        User user = userMapper.selectById(1L);
        System.out.println("user = " + user);
    }

    /**
     * 通过id批量查询
     */
    @Test
    public void selectBatchIdsTest(){
        List<User> users = userMapper.selectBatchIds(Arrays.asList(1, 2, 3));
        users.forEach(System.out :: println);
    }

    /**
     * 通过map  等于时拼接条件查询
     */
    @Test
    public void selectByMap(){
        HashMap<String, Object> queryMap = new HashMap<>();
        queryMap.put("name","修改1");
        List<User> users = userMapper.selectByMap(queryMap);
        users.forEach(System.out :: println);
    }


    /**
     * 使用条件构造器
     * wrapper
     */
    @Test
    public void queryWrapperTest(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.isNotNull("email")
                .isNotNull("age")
                .ge("age","12");

        List<User> users = userMapper.selectList(queryWrapper);

        users.forEach(System.out :: println);
    }

    /**
     * 查看sql执行
     */
    @Test
    public void queryWrapperTest1(){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("name", "丁文彬");

        User user = userMapper.selectOne(queryWrapper);

        System.out.println("user = " + user);
    }

    /**
     * 查看sql执行
     */
    @Test
    public void queryWrapperTest2(){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.between("age", 20,25);

        Integer count = userMapper.selectCount(queryWrapper);

        System.out.println("count = " + count);
    }


    /**
     * 查看sql执行
     */
    @Test
    public void queryWrapperTest3(){

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();

        queryWrapper.orderByDesc("id");

        List<User> users = userMapper.selectList(queryWrapper);

        users.forEach(System.out :: println);
    }
}
