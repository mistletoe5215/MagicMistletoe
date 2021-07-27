
### Magic Mistletoe Android多主题（换肤）切换框架 [![](https://jitpack.io/v/mistletoe5215/MagicMistletoe.svg)](https://jitpack.io/#mistletoe5215/MagicMistletoe)

 - 背景
 
  > 时隔四年，在网易换肤之前的思路下，现在完全通过反射创建View，并且提供一个`configCustomAttrs`支持自定义View的属性插队替换
  
  > 摈弃了之前的`AsyncTask`,使用kotlin 协程进行主题包的资源转换
  
  > 使用kotlin重构所有Java代码实现

 - 使用方式
 
  > STEP 1 制作多主题包，并拷贝至assets目录下备用

 ```shell script
   cd theme-pkg
   ./gradlew assembleDebug
 
``` 
  拷贝生成的apk至`宿主app`的assets目录下,重命名为`你喜欢的名字.zip`（如果有强迫症）
 
 > STEP 2 依赖多主题框架AAR

  ```groovy
  //in root build.gradle
  allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
  // in app/module build.gradle
  dependencies {
  	        implementation 'com.github.mistletoe5215:MagicMistletoe:1.0.0'
  	}

 ```
 > > STEP 3 代码执行切换

 ```kotlin
   /**
    * 初始化多主题框架
    **/ 
   SkinLoadManager.getInstance().init(application)
   /**
    * Activity onCreate 前设置`multiThemeFactory`
    **/
  layoutInflater.factory = SkinLoadManager.getInstance().multiThemeFactory
   /**
    * 将换肤包从assets目录拷入应用内
    **/
  ArchTaskExecutor.getIOThreadExecutor().execute {
                copyAssetAndWrite(fileName)
                ArchTaskExecutor.getMainThreadExecutor().execute {
                    Toast.makeText(this,"成功",Toast.LENGTH_SHORT).show()
                }
            }
   /**
     * 传入拷贝后的多主题路径，执行多主题切换
     **/
    val dataFile = File(cacheDir, fileName)
                   SkinLoadManager.getInstance().loadSkin(dataFile.absolutePath, object : ILoadListener {
                       override fun onStart() {
                           Log.i("Mistletoe", "onStart")
                       }
   
                       override fun onSuccess() {
                           Log.i("Mistletoe", "onSuccess")
                       }
   
                       override fun onFailed(e: SkinLoadException) {
                           Log.e("Mistletoe", "onFailed:${e.message}")
                       }
   
                   })
    /**
      * 切换回App默认主题
      **/
     SkinLoadManager.getInstance().restoreDefaultTheme()
```