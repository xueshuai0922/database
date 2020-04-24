package com.xs.database;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.swing.BrowserView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.util.ClassUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

@EnableCaching
@SpringBootApplication
public class DatabaseApplication {

    public static void main(String[] args) {

//        SpringApplication.run(DatabaseApplication.class, args);
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DatabaseApplication.class);
        builder.headless(false).run(args);

        String url = "http://localhost:8888/";

        JFrame frame = new JFrame();

        // 谷歌内核浏览器
        Browser browser = new Browser();
        browser.canGoBack();
        browser.canGoForward();
        BrowserView view = new BrowserView(browser);
        //禁用close功能
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //隐藏任务栏图标
//        frame.setType(JFrame.Type.UTILITY);
//        //不显示标题栏,最大化,最小化,退出按钮
        frame.setUndecorated(false);
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        Image image = new ImageIcon(path+"/static/assetss/images/favicon.ico").getImage();
        frame.setIconImage(image);
        //尺寸
//        frame.setSize(500, 500);
        //坐标
        frame.setLocation(0, 0);
        frame.add(view);
        //全屏显示
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        // 是否显示
        frame.setVisible(true);
        //是否在屏幕最上层显示
        frame.setAlwaysOnTop(true);
        //加载地址
        browser.loadURL(url);
        frame.addWindowListener(new WindowAdapter() {
            // 窗口关闭时间监听
            @Override
            public void windowClosing(WindowEvent e){
                System.out.println("窗口关闭...");
            }
        });


    }

}
