package com.daojie.game;

import com.daojie.domain.*;
import com.daojie.inter.*;
import org.daojie.game.DrawUtils;
import org.daojie.game.Window;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameWindow extends Window {

    List<Element> list = new CopyOnWriteArrayList<>();// 定义一个集合用来存储所有的砖墙、铁墙、草墙、水墙；
    // 我方坦克对象
    MyTank myTank;
    

    // 创建2个敌方坦克
    EnemyTank et1;// 敌方坦克
    EnemyTank et2;// 敌方坦克

    public GameWindow(String title, int width, int height, int fps) {
        super(title, width, height, fps);
    }

    @Override
    protected void onCreate() {
        // 循环创建转墙，添加到list集合中，减1表示预留最后一个空间，过坦克
        for(int i=0;i< Config.WIDTH/64-1;i++){
            if(i == 3){
                continue;
            }
            Wall wall = new Wall(i*64,64);
            list.add(wall);
        }

        // 循环创建水墙，添加到list集合中，减1表示预留最后一个空间，过坦克
        for(int i=1;i<Config.WIDTH/64;i++){
            Water water = new Water(i*64,3*64);
            list.add(water);
        }

        // 循环创建铁墙，添加到list集合中，减1表示预留最后一个空间，过坦克
        for(int i=0;i<Config.WIDTH/64-1;i++){
            Steel steel = new Steel(i*64,5*64);
            list.add(steel);
        }

        // 循环创建草墙，添加到list集合中，减1表示预留最后一个空间，过坦克
        for(int i=1;i<Config.WIDTH/64;i++){
            Grass grass = new Grass(i*64,7*64);
            list.add(grass);
        }

        // 创建我方坦克
        myTank = new MyTank(Config.WIDTH/2 - 64/2,Config.HEIGHT - 64);
        list.add(myTank);

        // 创建敌方坦克
        et1 = new EnemyTank(0,0);
        et2 = new EnemyTank(Config.WIDTH-64,0);
        list.add(et1);
        list.add(et2);

        // 在List集合中，越后添加到集合中，就越晚绘制，也就是说集合中越靠后的元素，就越晚绘制
        // 根据渲染级别对集合进行排序，渲染级别越高的，就越排在集合的越靠后的位置
        Collections.sort(list, new Comparator<Element>() {
            @Override
            public int compare(Element o1, Element o2) {
                // 前减后，升序
                // 后减前，降序
                // 表示升序，getLevel()返回值大的元素（即草墙）放置到最后，从而实现草墙隐藏坦克
                return o1.getLevel() - o2.getLevel();
            }
        });

    }

    @Override
    protected void onMouseEvent(int key, int x, int y) {

    }

    @Override
    protected void onKeyEvent(int key) {
        // 按键，坦克要移动
        if(key == Keyboard.KEY_UP){ // 向上键
            // 坦克向上移动
            myTank.move(Direction.UP);
        }
        else if(key == Keyboard.KEY_DOWN){ // 向下键
            // 坦克向下移动
            myTank.move(Direction.DOWN);
        }
        else if(key == Keyboard.KEY_LEFT){ // 向左键
            // 坦克向左移动
            myTank.move(Direction.LEFT);
        }
        else if(key == Keyboard.KEY_RIGHT){ // 向右键
            // 坦克向右移动
            myTank.move(Direction.RIGHT);
        }
        else if(key == Keyboard.KEY_SPACE){ // 空格键
            // 发射子弹就会创建一颗子弹，添加到list集合中，通过list遍历绘制出来
            Bullet bullet = myTank.shoot();
            // 将bullet存放到list中，遍历list可以将子弹绘制到窗口中
            // 子弹的位置不对，子弹不能移动，子弹的方向不对
            // 子弹出来边界应该销毁，子弹发射应该有间隔，并发修改异常
            // 如果这次按键和上次按键的时间间隔小于300ms，就不创建子弹，如果大于或者等于300ms就创建子弹
            // 判断bullet对象是否为null，如果不为null，才添加到集合中
            if(bullet != null){
                list.add(bullet);
            }
        }
    }

    @Override
    protected void onDisplayUpdate() {
        // 判断，如果我军坦克和敌军坦克的血量为0，就将屏幕的对象清空（即list集合清空）
        if(myTank.getBlood() == 0 || (et1.getBlood() == 0 && et2.getBlood() == 0)){
            list.clear();
            try {
                DrawUtils.draw("TankWar2/res/img/tankover.png",0,0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (Element element : list) {
            element.draw();
            // 控制敌方坦克的移动
            if(element instanceof  EnemyTank){
                // 实现敌方坦克自动移动
                EnemyTank et = (EnemyTank)element;
                // 这里不要传递方向，敌方坦克是根据随机数自动移动的
                et.move();
                // 敌方坦克发射子弹要有间隔
                int num = new Random().nextInt(40); // 产生一个随机数
                if(num == 30){ // 只有随机数为30的时候，才发射子弹，保证敌方坦克不会一直发射子弹
                    Bullet bullet = et.shoot();// 敌方坦克发射子弹
                    if(bullet != null){
                        list.add(bullet);
                    }
                }
            }

            if(element instanceof Destroyable){
                Destroyable destroyable = (Destroyable)element;
                boolean res = destroyable.isDestroy();
                if(res){
                    list.remove(element);
                }
            }
            // 判断element是否是子弹，如果是子弹，那么就调用子弹的move()方法
            if(element instanceof Bullet){
                // 向下转型，父类强制转换成子类，因为需要调用子弹特有的move方法
                Bullet bullet = (Bullet)element;
                bullet.move();
            }

            // 判断element是否是子弹，如果是子弹，那么就调用子弹的move()方法
//            if(element instanceof Bullet){
//                // 向下转型，父类强制转换成子类，因为需要调用子弹特有的move方法
//                Bullet bullet = (Bullet)element;
//                bullet.move();
//                // 子弹移动过程中，如果出了边界，应该立马删除
//                boolean destroy = bullet.isDestroy();
//                if(destroy){
//                    list.remove(element);
//                }
//            }
//            // 判断element是否是爆炸物，如果确定爆炸物消失，需要移除爆炸物
//            if(element instanceof Blast){
//                Blast blast = (Blast)element;
//                boolean res = blast.isDestroy();
//                if(res){
//                    list.remove(element);
//                }
//            }
        }
        System.out.println(list.size());
        // 添加实施刷新方法里面调用坦克的校验碰撞的方法，校验坦克和铁墙是否碰撞上
        // 移动功能事物和阻挡功能事物碰撞校验是否碰撞
        for (Element e1 : list) {
            if(e1 instanceof Moveable){
                for (Element e2 : list) {
                    if(e2 instanceof Blockable){
                        // e1 和 e2是同一个坦克
                        // 判断e1和e2是否是同一个坦克，如果是同一个坦克，那么就不校验碰撞
                        if(e1 == e2){
                            continue;
                        }
                        // 调用坦克的校验碰撞的方法，来校验是否和铁墙碰撞上
                        Moveable moveable = (Moveable)e1;
                        Blockable blockable = (Blockable)e2;
                        boolean flag = moveable.checkHit(blockable);
                        if(flag){
                            // 如果校验到坦克和铁墙碰撞上了，那么应该使得再按当前移动方法键的时候，就不能移动了
                            // 需要在坦克移动的move方法完成
                            System.out.println("说明碰撞上了铁墙");
                            break;
                        }
                    }
                }
            }
        }

        // 添加校验子弹和铁墙的碰撞
        // 攻击功能事物和挨打功能事物碰撞校验
        for (Element e1 : list) {
            if(e1 instanceof Attackable){
                for (Element e2 : list) {
                    if(e2 instanceof Hitable){
                        // 调用子弹的校验碰撞的方法，来校验是否和铁墙碰撞上
                        Attackable attackable = (Attackable)e1;
                        Hitable hitable = (Hitable)e2;

                        // 判断e1和e2是否是同一个对象，如果e1和e2是同一颗子弹，那么就不校验碰撞
                        // 解决子弹对象即实现Attackable接口，也实现Hitable接口，即子弹刚出来就会被抵消的问题
                        if(e1 == e2){
                            continue;
                        }
                        // 判断e1子弹所属的坦克和e2子弹所属的坦克是同一个类，那么就不校验碰撞
                        // 解决友方子弹不能相互碰撞抵消问题
                        if(attackable instanceof Bullet){
                            Bullet bullet1 = (Bullet)attackable;
                            if(hitable instanceof Bullet){
                                Bullet bullet2 = (Bullet)hitable;
                                if(bullet1.tank.getClass() == bullet2.tank.getClass()){
                                    continue;
                                }
                            }
                        }

                        // 如果攻击对象是子弹，就判断是否可以攻击友方坦克
                        if(attackable instanceof Bullet){
                            Bullet bullet = (Bullet)attackable;
                            // 如果子弹所属的坦克和被攻击的坦克是属于同一个类，那么就不攻击
                            if(bullet.tank.getClass() == hitable.getClass()){
                                continue;
                            }
                        }

                        boolean flag = attackable.checkAttack(hitable);
                        if(flag){
                            // 如果校验到子弹和铁墙碰撞上了，就应该移除子弹
                            System.out.println("说明子弹攻击到了铁墙");
                            list.remove(e1);
                            // 如果攻击上了，应该显示爆炸物
                            // 爆炸物遗留的问题：爆炸物的坐标，爆炸物的个数，爆炸物的消失，爆炸物的声音
                            Blast blast = hitable.showBlast();// 在墙上创建爆炸物
                            list.add(blast);// 把爆炸物添加到list集合中，也就是添加到了窗体上
                            break;
                        }
                    }
                }
            }
        }
    }
}
