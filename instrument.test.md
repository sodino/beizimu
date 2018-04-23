# androidTest : Class not found : "***.XXTest" Empty test suite.
 

## Empty test suite
在Android Studio新建Project，创建个类，在该类中创建测试用例，点击`@Test`左边的`Run`三角形并选择`Run BeanTest`，但出来如下错误：

```
Process finished with exit code 1
Class not found: "com.test.unit.BeanTest"Empty test suite.
```
 
如下图：
![Empty.test.suite](https://wx2.sinaimg.cn/large/e3dc9ceagy1fqj9g3q375j21ee0rcjxs.jpg)

## 解决办法

* 点击`Run`->`Edit Configurations...`
* 在左边的选择中如果没有`Android Instrumented Tests`，则点击`+`创建，如下图`1` 、 `2`步骤。
* 在`General`的`Modules`中选择`app`或其它`library`
* 选择`Class` 及 `Method`，逐一填写，如图`3` 、 `4`

最后点击`Apply`。

![steps](https://wx2.sinaimg.cn/large/e3dc9ceagy1fqj9hflogdj21ec0ty445.jpg)

在Studio的菜单栏中`Run`下拉框选择刚才保存的配置名，最后点击`Run`或`Debug`，即可出现`Select Deployment Target`选择调度设备，正常运行或调度。
![run](https://wx1.sinaimg.cn/mw1024/e3dc9ceagy1fqj9uhdmrbj20j0086q44.jpg)

