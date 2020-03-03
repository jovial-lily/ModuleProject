package com.jovial.compiler;

import com.google.auto.service.AutoService;
import com.jovial.annotation.Parameter;
import com.jovial.compiler.factory.ParameterFactory;
import com.jovial.compiler.utils.Constants;
import com.jovial.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 参数注解处理器]
 */
@AutoService(Processor.class)
//代替下面的getSupportedAnnotationTypes方法：该注解处理器要处理那种类型的注解
@SupportedAnnotationTypes({Constants.PARAMETER__ANNOTATION_TYPES})
//代替下面的getSupportedSourceVersion方法：需要是用版本的jdk来编译
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class ParameterProsessor extends AbstractProcessor {

    //用来处理Element的工具类
    private Elements elementUtils;
    //用来处理类信息的工具类
    private Types typeUtils;
    //用来输出错误、警告等日志信息
    private Messager messager;
    //文件生成器
    private Filer filer;

    //历史map存储，用来存放被@Parameter注解的属性集合，生成类文件时遍历
    //key：类节点，value：属性集合
    private Map<TypeElement, List<Element>> tempParameterMap = new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //一旦属性之上有@Parameter注解
        if(!EmptyUtils.isEmpty(annotations)){
            //获取所有被@Parameter注解元素集合
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Parameter.class);
            if(!EmptyUtils.isEmpty(elements)){
                //用临时map存储，用来遍历生成代码
                valueOfParameterMap(elements);
                //生成类文件
                try {
                    createParameterFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private void createParameterFile() throws IOException {
        //如果临时map集合为空，说明没有使用注解
        if(EmptyUtils.isEmpty(tempParameterMap)) return;
        //生成类文件
        //通过Element工具类获取Parameter类型
        TypeElement parameterType = elementUtils.getTypeElement(Constants.PARAMETER_LOAD);
        //javapoet语法：参数体配置
        ParameterSpec parameterSpec = ParameterSpec.builder(TypeName.OBJECT,Constants.PARAMETER_NAME).build();
        //循环没一个类
        for(Map.Entry<TypeElement,List<Element>> entry:tempParameterMap.entrySet()){
            //Map集合中的key是类名，如：MainActivity
            TypeElement typeElement = entry.getKey();
            //获取类名
            ClassName className = ClassName.get(typeElement);//typeElement.getSimpleName();也可以

            //方法体内容构建
            ParameterFactory factory = new ParameterFactory.Builder(parameterSpec)
                    .setMessager(messager)
                    .setElementUtils(elementUtils)
                    .setTypeUtils(typeUtils)
                    .setClassName(className)
                    .build();
            //添加方法内容的第一行：MainActivity t = (MainActivity)target;
            factory.addFirstStatement();
            //循环每一个类属性：遍历类里面所有属性： t.name = t.getIntent().getStringExtra("name");
            for (Element fieldElement : entry.getValue()){
                factory.buildStatement(fieldElement);
            }
            //最终生产成本的类文件名（类名$$Parameter）
            String finalClassName = typeElement.getSimpleName() + Constants.PARAMETER_FILE_NAME;
            messager.printMessage(Diagnostic.Kind.NOTE,"APT生成获取参数类文件："
                    +className.packageName()+"."+finalClassName);
            //MainActvity$$Parameter
            JavaFile.builder(className.packageName(), //包名，必须和注解类同包名，否则会有访问权限问题
                    TypeSpec.classBuilder(finalClassName)
                    .addSuperinterface(ClassName.get(parameterType))//实现了什么接口
                    .addModifiers(Modifier.PUBLIC)
                    .addMethod(factory.build())
                    .build())
            .build()
            .writeTo(filer);
        }
    }

    private void valueOfParameterMap(Set<? extends Element> elements) {
        for(Element element:elements){
            //属性注解的父节点是类节点
            TypeElement typeElement = (TypeElement) element.getEnclosingElement();
            //如果map集合中有这个类节点,直接添加，否则new一个集合再put 到map
            if(tempParameterMap.containsKey(typeElement)){
                tempParameterMap.get(typeElement).add(element);
            }else{
                List<Element> fields = new ArrayList<>();
                fields.add(element);
                tempParameterMap.put(typeElement,fields);
            }
        }
    }















}
