# ProtoDataStore
A simple demo of Proto DataStore that reference office document.
> 这个demo是参考了官方开发文档和官方的codelab教程后写的一个小demo。  
其中的业务逻辑只是在activity中实现，没有使用viewmodel和hilt注入，仅仅只是演示了Proto DataStore的使用。  
其实现主要有以下几个步骤：
* 在App module模块中引入相关依赖（需要配置如何创建生成类）  
* 定义一个proto file（这个类用于描述你要生成的类）
* 创建一个proto dataStore  
* 读proto dataStore的数据
* 更新（写入）proto dataStore中的数据
