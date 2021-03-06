# ModularizationDemo

效果图

!![image](https://github.com/panyiyiyi/ModularizationDemo/blob/master/test.gif)

引用方式

dependencies {

    //自定义View
    implementation 'com.even:chart-view:1.0.0'
    //通用工具类
    implementation 'com.even:utils:1.0.0'  
}

自定义View使用方式：

 <!--比例进度条风格-->
    <declare-styleable name="RateProgressBarView">
        <!--进度条高度-->
        <attr name="progressHeight" format="dimension" />
        <!--第一个点值-->
        <attr name="firstDotValue" format="float" />
        <!--第二个点值-->
        <attr name="secondDotValue" format="float" />
        <!--当前的值-->
        <attr name="currentValue" format="float" />
        <!--当前值文本大小-->
        <attr name="currentValueTextSize" format="dimension" />
        <!--少了的颜色-->
        <attr name="lowColor" format="color" />
        <!--正常的颜色-->
        <attr name="normalColor" format="color" />
        <!--超了的颜色-->
        <attr name="overColor" format="color" />
        <!--分界点文本大小-->
        <attr name="dotTextSize" format="dimension" />
        <!--分界点文本颜色-->
        <attr name="dotTextColor" format="color" />
        <!--进度条默认颜色-->
        <attr name="progressDefaultColor" format="color" />

    </declare-styleable>


    <!--比例圆环样式风格-->
    <declare-styleable name="CircularRateView">
        <!--默认圆环颜色-->
        <attr name="defaultRingColor" format="color" />
        <!--圆环半径-->
        <attr name="ringRadius" format="dimension" />
        <!--圆环宽度-->
        <attr name="ringWidth" format="dimension" />
        <!--主文本的字体大小-->
        <attr name="firstTextSize" format="dimension" />
        <!--主文本文字颜色-->
        <attr name="firstTextColor" format="color" />
        <!--副文本的字体大小-->
        <attr name="secondTextSize" format="dimension" />
        <!--副文本的文字颜色-->
        <attr name="secondTextColor" format="color" />
        <!--副文本提示文字-->
        <attr name="secondText" format="string" />
        <!--提示文本大小-->
        <attr name="remindTextSize" format="dimension" />
        <!--提示文本颜色-->
        <attr name="remindTextColor" format="color" />
        <!--提示文本与圆环的距离-->
        <attr name="remindTextDistance" format="dimension" />
        <!--圆点颜色-->
        <attr name="circleColor" format="color" />
        <!--圆点半径-->
        <attr name="circleRadius" format="dimension" />
        <!--折线颜色-->
        <attr name="brokenLineColor" format="color" />
        <!--是否显示提示文字-->
        <attr name="isShowRemindText" format="boolean" />
    </declare-styleable>



