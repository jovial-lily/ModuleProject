package com.jovial.compiler;

import com.google.auto.service.AutoService;
import com.jovial.annotation.ARouter;
import com.jovial.annotation.model.RouterBean;
import com.jovial.compiler.utils.Constants;
import com.jovial.compiler.utils.EmptyUtils;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

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
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/19 0019]
 * About Class:[ 注解处理器 ]
 */
/*
    @AutoService(Processor.class)
    通过AutoService来自动生成AutoService注解处理器生成对应的文件来做注册
    相当于在manifest里面注册Activity
 */
@AutoService(Processor.class)
//代替下面的getSupportedAnnotationTypes方法：该注解处理器要处理那种类型的注解
@SupportedAnnotationTypes({Constants.AROUTER_ANNOTATION_TYPES})
//代替下面的getSupportedSourceVersion方法：需要是用版本的jdk来编译
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//代替下面的getSupportedOptions方法：注解处理器能够接收的参数比如：content
@SupportedOptions({Constants.MODULE_NAME, Constants.APT_PACKAGE})
public class ARouterProcessor extends AbstractProcessor {

    //用来处理Element的工具类
    private Elements elementUtils;
    //用来处理类信息的工具类
    private Types typeUtils;
    //用来输出错误、警告等日志信息
    private Messager messager;
    //文件生成器
    private Filer filer;

    //每个子模块的模块名
    private String moduleName;
    //用于存放APT生成的类文件
    private String packageNameForAPT;

    //临时map存储Group对应的详细path类对象：key：组名（app）  value：app组的路由路径类（ARouter$$Path$$app.class）
    private Map<String, List<RouterBean>> tempPathMap = new HashMap<>();
    //临时map存储组Group信息，生成路由组类文件时调遍历：key：组名（app） value：类名（ARouter$$Path$$app.class）
    private Map<String, String> tempGroupMap = new HashMap<>();

    /**
     * 做初始化的工作：比如一切有用的工具类，文件处理类都在这里初始化
     * 相当于Activity的onCreate方法
     *
     * @param processingEnv
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();

        //通过ProcessingEnvironment去获取对应的参数
        Map<String, String> options = processingEnv.getOptions();
        if (!EmptyUtils.isEmpty(options)) {
            moduleName = options.get(Constants.MODULE_NAME);
            packageNameForAPT = options.get(Constants.APT_PACKAGE);
            //这里不能像android中Log.e（Diagnostic.Kind.ERROR）会报错
            messager.printMessage(Diagnostic.Kind.NOTE, "moduleName:" + moduleName + "--packageNameForAPT" + packageNameForAPT);
        }
        //对必要参数判空，如果控制台出现中文乱码，请在gradle配置乱码处理代码
        if (EmptyUtils.isEmpty(moduleName) || EmptyUtils.isEmpty(packageNameForAPT)) {
            throw new RuntimeException("注解处理器需要的参数module或者packageName为空，请在对应大的build.gradle配置参数");
        }

    }

    /**
     * 这个方法至关重要，表示处理上面支持的注解类型
     * 相当于main函数，是注解处理器的核心方法，开始处理具体的注解，生成java文件
     *
     * @param annotations 使用了支持处理注解的节点集合（上面写了注解的类的集合）
     * @param roundEnv    当前或是之前的运行环境，可以通过该对象查找找到的注解
     * @return true 表示后续处理器不会再处理（已经处理完成） false表示处理完或者处理失败了
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        /** 最终的APT+JavaPoet */
        if (!EmptyUtils.isEmpty(annotations)) {
            Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
            if (!EmptyUtils.isEmpty(elements)) {
                //解析元素
                try {
                    parseElements(elements);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return true;
        }
        return false;
        /**         传统的方式      ******/
//        if(annotations.isEmpty()) return false;
//        //查找项目中所有使用了ARouter注解的类节点
//        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
        //遍历所有的类节点
//        for(Element e:elements) {
            /*EventBus里面使用的是拼接的方式，其他的框架里面有使用到javapote技术
            这里我们先用传统的也是大部分人使用的拼接方式
            模板就是在app模块中的test目录下的模板*/
        //类节点之上就是包节点
//            String packgeName = elementUtils.getPackageOf(e).getQualifiedName().toString();
//            //获取简单类名
//            String className = e.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE,"被注解的类有："+className);
//            //最终我们想要生成的类文件
//            String finalClassName = className + "$$ARouter";
//            try{
//                JavaFileObject sourceFile = filer.createSourceFile(packgeName + "." +finalClassName);
//                Writer writer = sourceFile.openWriter();
//                //设置包名
//                writer.write("package " + packgeName + ";\n");
//                writer.write("public class " + finalClassName +"{\n");
//                writer.write("public static Class<?> findTargetClass (String path){");
//                //获取类之上@ARouter注解的path值
//                ARouter aRouter = e.getAnnotation(ARouter.class);
//                writer.write("if(path.equalsIgnoreCase(\""+aRouter.path()+"\")){\n");
//                writer.write("return " +className + ".class;\n}\n");
//                writer.write("return null;\n");
//                writer.write("}\n}");
//                //切记 非常重要，不关闭就会一直在写
//                writer.close();
//            }catch (Exception e2){
//                e2.printStackTrace();
//            }
//        }
//        return true;
        /***         Javapoet的方式      ******/
//        if(annotations.isEmpty()) return false;
//        //查找项目中所有使用了ARouter注解的类节点
//        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(ARouter.class);
//        for(Element e:elements){
//            //类节点之上就是包节点
//            String packgeName = elementUtils.getPackageOf(e).getQualifiedName().toString();
//            //获取简单类名
//            String className = e.getSimpleName().toString();
//            messager.printMessage(Diagnostic.Kind.NOTE,"被注解的类有："+className);
//            //最终我们想要生成的类文件
//            String finalClassName = className + "$$ARouter";
//            ARouter aRouter = e.getAnnotation(ARouter.class);
//            MethodSpec findTargetClass = MethodSpec.methodBuilder("findTargetClass")
//                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//                    .returns(Class.class)
//                    .addParameter(String.class, "path")
//                    .addStatement("return path.equalsIgnoreCase($S)?$T.class:null",
//                            aRouter.path(),
//                            ClassName.get((TypeElement)e))
//                    .build();
//
//            TypeSpec x$$ARouter = TypeSpec.classBuilder(finalClassName)
//                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//                    .addMethod(findTargetClass)
//                    .build();
//
//            JavaFile javaFile = JavaFile.builder(packgeName, x$$ARouter)
//                    .build();
//            try {
//                javaFile.writeTo(filer);
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return true;
    }

    /**
     * 解析所有被@ARouter注解的元素集合，这里需要判断被注解的是Activity
     */
    private void parseElements(Set<? extends Element> elements) throws IOException{
        //通过Element工具类获取Activity类型、Callbcak类型
        TypeElement activityElement = elementUtils.getTypeElement(Constants.ACTIVITY);
        TypeElement callElement = elementUtils.getTypeElement(Constants.CALL);

        //显示类信息
        TypeMirror activityMirror = activityElement.asType();
        TypeMirror callMirror = callElement.asType();

        for (Element element : elements) {
            //获取每个元素的类信息
            TypeMirror elementMirror = element.asType();
            messager.printMessage(Diagnostic.Kind.NOTE, "遍历的元素信息为" + elementMirror.toString());
            //获取每个类上的@ARouter注解对应的path值
            ARouter aRouter = element.getAnnotation(ARouter.class);
            //路由详细信息封装到实体
            RouterBean bean = new RouterBean.Builder()
                    .setGroup(aRouter.group())
                    .setPath(aRouter.path())
                    .setElement(element)
                    .build();
            //高级判断，说明ARouter注解仅仅只能用在类之上，并且规定是Activity
            if (typeUtils.isSubtype(elementMirror, activityMirror)) {
                bean.setType(RouterBean.Type.ACTIVITY);
            }else if(typeUtils.isSubtype(elementMirror,callMirror)){
                bean.setType(RouterBean.Type.CALL);
            } else {
                throw new RuntimeException("@ARouter注解目前仅限在Activity、Call接口实现类之上");
            }
            //赋值临时map存储以上信息，用来遍历时生成类文件
            valueOfPathMap(bean);
        }
        //ARouterGroupLoad & ArouterPathLoad 接口类型，用来生成类文件时实现接口
        TypeElement groupElement = elementUtils.getTypeElement(Constants.AROUTER_GROUP);
        TypeElement pathElement = elementUtils.getTypeElement(Constants.AROUTER_PATH);

        //1.生成路由的详细Path类文件，如：ARouter&&Path$$app
        createPathFile(pathElement);
        //2.生成卤藕组Group类文件（因为没有Path类文件，所以这个类要在path之后生成）
        createGroupFile(groupElement,pathElement);


    }

    /**
     * 生成对应的path文件：ARouter$$Path$$order
     * @param pathElement ARouterPathLoad 接口信息
     */
    private void createPathFile(TypeElement pathElement) throws IOException{
        if(EmptyUtils.isEmpty(tempPathMap)) return;
        /**下面都是javapoet的API语法了*/
        //方法的返回值Map<String,RouterBean>
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ClassName.get(RouterBean.class)
        );
        //遍历分组，每一个分组创建一个路径类文件，如：ARouter$$Path$$app
        for(Map.Entry<String,List<RouterBean>> entry : tempPathMap.entrySet()){
            //方法体构造public Map<String,RouterBean> loadPath()
            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder(Constants.PATH_METHOD_NAME)//方法名
            .addAnnotation(Override.class)
            .addModifiers(Modifier.PUBLIC)
            .returns(methodReturns);

            //方法体中不需要循环的部分，也就是不管有多少个Activity需要添加到map，这些代码都是共用的
            //模板参考app模块test包下的两个类:Map<String, RouterBean> pathMap = new HashMap<>();
            methodBuilder.addStatement("$T<$T,$T> $N = new $T<>()",
                    ClassName.get(Map.class),
                    ClassName.get(String.class),
                    ClassName.get(RouterBean.class),
                    Constants.PATH_PARAMETER_NAME,
                    HashMap.class);

            //  /app/MainActivity   /app/OtherAtctivity  ....
            List<RouterBean> pathList = entry.getValue();
            for(RouterBean bean : pathList) {
                //需要循环添加模块中的Activity
                /*
                pathMap.put("/order/Order_MainActivity",
                    RouterBean.create(RouterBean.Type.ACTIVITY,
                            Order_MainActivity.class,
                            "/order/Order_MainActivity",
                            "order"));
                */
                methodBuilder.addStatement(
                        "$N.put($S,$T.create($T.$L,$T.class,$S,$S))",
                        Constants.PATH_PARAMETER_NAME,
                        bean.getPath(),
                        ClassName.get(RouterBean.class),
                        ClassName.get(RouterBean.Type.class),
                        bean.getType(),
                        ClassName.get((TypeElement) bean.getElement()),
                        bean.getPath(),
                        bean.getGroup()
                );
            }
            //循环遍历添加完毕后，方法返回
            methodBuilder.addStatement("return $N",Constants.PATH_PARAMETER_NAME);

            //生成的类文件:ARouter$$Path$$app
            String finalClassName = Constants.PATH_FILE_NAME +  entry.getKey();
            messager.printMessage(Diagnostic.Kind.NOTE,"APT生成路由Path类文件为："
                    +packageNameForAPT+"."+finalClassName);
            //生成类
            JavaFile.builder(packageNameForAPT,
                    TypeSpec.classBuilder(finalClassName)
                            .addSuperinterface(ClassName.get(pathElement))
                            .addModifiers(Modifier.PUBLIC)
                            .addMethod(methodBuilder.build())
                            .build())
                    .build()
                    .writeTo(filer);
            //最后别忘记把生成的path类添加到组
            tempGroupMap.put(entry.getKey(),finalClassName);
        }

    }

    /**
     * 生成对应的group文件 :ARouter$$Group$$order
     * @param groupElement ARouterGroupLoad 接口信息
     * @param pathElement ARouterPathLoad 接口信息
     */
    private void createGroupFile(TypeElement groupElement, TypeElement pathElement) throws IOException{
        //判断是否有需要生成的类文件
        if(EmptyUtils.isEmpty(tempGroupMap) || EmptyUtils.isEmpty(tempPathMap)) return;
        TypeName methodReturns = ParameterizedTypeName.get(
                ClassName.get(Map.class),//Map
                ClassName.get(String.class),//Map<String,
                //第二个参数：Class<? extends ARouterLoadPath>
                //某某Class是否属于ARouterLoadPath接口的实现类
                ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(pathElement)))
        );
        //方法配置：public Map<String,Class<? extends ARouterLoadPath>> LoadGroup(){
        MethodSpec.Builder methodBuidler = MethodSpec.methodBuilder(Constants.GROUP_METHOD_NAME)
                .addAnnotation(Override.class)
                .addModifiers(Modifier.PUBLIC)
                .returns(methodReturns);
        //遍历之前：Map<String,Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
        methodBuidler.addStatement("$T<$T,$T> $N = new $T<>()",
                ClassName.get(Map.class),
                ClassName.get(String.class),
                ParameterizedTypeName.get(ClassName.get(Class.class),
                WildcardTypeName.subtypeOf(ClassName.get(pathElement))),
                Constants.GROUP_PARAMETER_NAME,
                HashMap.class);

        //方法内容配置
        for(Map.Entry<String,String> entry : tempGroupMap.entrySet()){
            //类String.format("hello %s net163 %d" , "net",163)
            //groupMap.put("main",ARouter$$Path$$app.class);
            methodBuidler.addStatement("$N.put($S,$T.class)",
            Constants.GROUP_PARAMETER_NAME,
            entry.getKey(),
            //类文件在指定包名下
            ClassName.get(packageNameForAPT,entry.getValue()));
        }
        //遍历之后：return groupMap
        methodBuidler.addStatement("return $N",Constants.GROUP_PARAMETER_NAME);
        //最终生成的类文件名
        String finalClassName = Constants.GROUP_FILE_NAME + moduleName;
        messager.printMessage(Diagnostic.Kind.NOTE,"APT生成路由组Gropu类文件："+packageNameForAPT+"."+finalClassName);
        //生成类文件：ARouter$$Group$$app
        JavaFile.builder(packageNameForAPT, TypeSpec.classBuilder(finalClassName)
                .addSuperinterface(ClassName.get(groupElement))
                .addModifiers(Modifier.PUBLIC)
                .addMethod(methodBuidler.build())
                .build())
                .build()
                .writeTo(filer);


    }



    /**
     * 赋值临时map存储,用来存放路由组Group对应的详细Path类对象路由路径类文件时遍历
     *
     * @param bean 路由详细信息，最终实体封装类
     */
    private void valueOfPathMap(RouterBean bean) {
        //做一层判断，如果路径没有按照规定的格式传输过来，就抛出异常
        if (checkRouterPath(bean)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "RouterBean>>>" + bean.toString());
            //开始赋值
            List<RouterBean> routerBeans = tempPathMap.get(bean.getGroup());
            if (EmptyUtils.isEmpty(routerBeans)) {
                //如果找不到这个组，说明还没有添加过这个组相关的类，那就初始化并且put到map中去
                routerBeans = new ArrayList<>();
                routerBeans.add(bean);
                tempPathMap.put(bean.getGroup(), routerBeans);
            } else {
                //如果找到了组，那就判断该对象是否已经存在集合中，不存在则添加，存在就不用重复添加
                routerBeans.add(bean);
            }
        } else {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解路径非法，请按照格式传输：/app/MainActvity");
        }
    }

    /**
     * 判断路径是否合法
     */
    private boolean checkRouterPath(RouterBean bean) {
        String group = bean.getGroup();
        String path = bean.getPath();
        //@ARouter注解的path值，必须要/开头（这里其实可以随意，但是模拟阿里的ARouter路由架构）
        if (EmptyUtils.isEmpty(path) || !path.startsWith("/")) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解路径非法，请按照格式传输：/app/MainActvity");
            return false;
        }
        //如果path = "/MainActivity"
        if (path.lastIndexOf("/") == 0) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解路径非法，请按照格式传输：/app/MainActvity");
            return false;
        }
        //从第一个 / 到第二个 / 中间截取路由组名
        String finalGroup = path.substring(1, path.indexOf("/", 1));
        //比如开发者代码为：path = "/MainActivity/MainActiivty/MainActivity"
        if (finalGroup.contains("/")) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解路径非法，请按照格式传输：/app/MainActvity");
            return false;
        }
        //以上都通过，但是组名和模块名不一致
        if (!EmptyUtils.isEmpty(group) && !group.equals(moduleName)) {
            messager.printMessage(Diagnostic.Kind.NOTE, "@ARouter注解的group必须和当前子模块名相同");
            return false;
        } else {
            bean.setGroup(finalGroup);
        }
        return true;
    }

    /**
     *以下三个方法可以直接用注解的方式取代
     */
//    /**
//     * 获取支持的注解类型：比如我们自定义的ARouter注解
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedAnnotationTypes() {
//        return super.getSupportedAnnotationTypes();
//    }
//    /**
//     * 你需要通过什么JDK版本来编译生成class，（注意，这个是必填）
//     * @return
//     */
//    @Override
//    public SourceVersion getSupportedSourceVersion() {
//        return super.getSupportedSourceVersion();
//    }
//    /**
//     * 用来接收外面传来的参数
//     * @return
//     */
//    @Override
//    public Set<String> getSupportedOptions() {
//        return super.getSupportedOptions();
//    }
}
