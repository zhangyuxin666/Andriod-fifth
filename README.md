# Android 实验四 翻牌游戏应用

张雨鑫 · 2024110548 · 2024级5班

包名：`cn.edu.sicnu.stu.zhangyuxin.fourth`

应用标题：张雨鑫2024110548

## 功能

- 玩家中心展示姓名、性别、学号、电话、邮箱、爱好及当前会话最高分。
- 信息修改支持保存、取消、必填与联系方式校验，资料保存在进程内。
- 4×4 随机翻牌，同点数奖励16分、同花色奖励4分，不匹配扣2分，每次翻开扣1分。
- 匹配后锁定牌面，可重置游戏、退出，支持 Activity 重建时恢复游戏状态。
- 使用附件 Kotlin 模型，XML 显示界面，Java Activity 处理交互；界面文字使用字符串资源。

## 目录

- `app/src/main/`：三个 Activity、Kotlin 模型、布局与资源。
- `app/src/test/`：9项 JUnit4 模型单元测试。
- `app/src/androidTest/`：4项 AndroidJUnitRunner UI 测试。
- `screenshots/`：9张模拟器运行截图及3张实际源码截图。
- `deliverables/`：完成的 Word 报告和可安装的 Debug APK。
- `verification/`：单元测试 XML、UI 测试输出、构建和静态检查记录。

## 构建

使用 Android Studio 打开本目录，配置 SDK 路径与 Gradle JDK。当前验证环境为 Android Studio 自带 JBR、Android SDK 37、AGP 9.3.0、Gradle 9.5.0；运行设备为 API35 模拟器。`local.properties` 不提交，请由 Android Studio 自动生成。

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest lintDebug
.\gradlew.bat connectedDebugAndroidTest
```

若 Gradle 下载 UTP 测试组件出现 TLS 错误，可安装相同测试 APK 并直接运行 AndroidJUnitRunner：

```powershell
.\gradlew.bat assembleDebug assembleDebugAndroidTest
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb install -r app\build\outputs\apk\androidTest\debug\app-debug-androidTest.apk
adb shell am instrument -w -r cn.edu.sicnu.stu.zhangyuxin.fourth.test/androidx.test.runner.AndroidJUnitRunner
```

## 验证结果

2026年10月23日：Debug APK 构建成功；9项模型单元测试全部通过；API35 模拟器上4项 UI 测试全部通过；`lintDebug` 为0错误。保留的 lint 警告主要涉及依赖版本提示、旧目标 API、未使用资源等。

截图中的 `13800138000` 与 `zhangyuxin@example.com` 是功能测试示例资料。运行截图采用随机牌面；自动化计分测试注入固定牌面，以重复验证 +14、+2、−4 等预期结果。个人资料及最高分在进程结束后恢复默认值。

![翻牌匹配成功](screenshots/07_同点数匹配成功.png)
