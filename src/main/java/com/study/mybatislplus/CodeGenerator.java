package com.study.mybatislplus;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.fill.Column;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CodeGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:dm://124.70.17.102:30236/activiti", "activiti", "activiti2024")
                .globalConfig(builder -> {
                    builder.disableOpenDir()  //禁止打开文件
                            .author("tuhao") // 设置作者
                            .outputDir("src/main/java"); // 指定输出目录

                })
                .packageConfig(builder ->
                        builder.parent("com.study") // 设置父包名
                                .moduleName("demo") // 设置父包模块名
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "src/main/resources/mybatis/mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude("edu_leave") // 设置需要生成的表名
                                .addTablePrefix("edu_") // 设置过滤表前缀
                                .entityBuilder()
                                .enableLombok()
                                .serviceBuilder()
                                .formatServiceFileName("%sService")
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
