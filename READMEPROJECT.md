# cloudpivot-extensions

## 插件模板工程项目结构

```
├── cloudpivot-extensions
│    ├── cloudpivot-extension-template  #插件开发模板工程
│    ├── cloudpivot-extension-debug     #插件开发调试工程
│    └── cloudpivot-extension-my        #自定义插件工程
```

## 自定义插件开发流程
- 在cloudpivot-plugins下新建一个子module, 如上述模板工程项目结构中的cloudpivot-extension-my
- 按需拷贝cloudpivot-extension-template中的内容至cloudpivot-extension-my,如需要扩展单据号等内容，则将model下的CustomSequenceServiceImpl拷贝到新项目，并重写其中的方法
- 可同时开发多个模块的插件(在同一个项目中重写多个模块的方法)

## 插件工程支持的扩展
- 支持@Service等类型的component注解，其类扫描路径为app.modules, 请将这些component放于该路径下
- 支持@Transcational注解
- 若有需要支持三方包的引入，直接在cloudpivot-extension-my的pom文件中加入相关的依赖，后续的打包过程中会将这些三方包放入指定的位置

## 插件工程的调试
- 调试项目启动
cloudpivot-extension-my插件工程开发完毕之后，进入cloudpivot-extension-debug工程，在pom文件的dependencies中加入以下依赖项：
```
    <dependency>
        <groupId>app.modules</groupId>
        <artifactId>cloudpivot-extension-my</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
```
cloudpivot-extension主工程，pom文件的properties中，将cloudpivot.version修改云枢当前使用的版本号，启动项目，并进行后续的调试

## 插件工程的发布
在cloudpivot-extension或者cloudpivot-extension-my工程下使用
```shell
    mvn -U clean package
``` 
来进行构建，构建产物包含插件jar及引入的lib，在cloudpivot-extension工程的target目录下，其结构如下
```
├── target
│    ├── common
│    └── cloudpivot-extension-my.jar
```
其中自定义插件项目cloudpivot-extension-my引入的三方包在保存在common中

## 插件工程的使用
将common文件夹及cloudpivot-extension-my.jar拷贝到与webapi并行的extensions目录，重启服务即可，详见部署文档
