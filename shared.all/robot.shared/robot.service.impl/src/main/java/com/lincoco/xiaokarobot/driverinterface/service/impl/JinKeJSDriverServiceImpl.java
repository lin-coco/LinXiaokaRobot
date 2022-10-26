package com.lincoco.xiaokarobot.driverinterface.service.impl;

import com.lincoco.xiaokarobot.driverinterface.service.exception.RobotException;
import com.lincoco.xiaokarobot.model.UserIdentity;
import com.lincoco.xiaokarobot.service.DriverService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @author ：xys
 * @description：使用js替换实现
 * @date ：2022/10/16
 */
@Slf4j
@Service
public class JinKeJSDriverServiceImpl implements DriverService {

    //金陵科技学院-统一身份认证
    private static final String URL = "http://authserver.jit.edu.cn/authserver/login?service=http%3A%2F%2Fehall.jit.edu.cn%2Flogin%3Fservice%3Dhttp%3A%2F%2Fehall.jit.edu.cn%2Fnew%2Findex.html";

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");


    @Override
    public void driverDeclare(UserIdentity identity) throws InterruptedException {
        LocalDateTime startTime = LocalDateTime.now();
        String formatStartTime = DTF.format(startTime);
        log.info(formatStartTime + " 用户 " + identity.getId() + " 开始执行自动打卡程序");

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(Boolean.TRUE);
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-gpu");
        options.addArguments("--disable-dev-shm-usage");

        ChromeDriver driver = new ChromeDriver(options);
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        try {
            driver.get(URL);
            String title = driver.getTitle();
            Thread.sleep(1000);
            log.info(title);

            //设置延迟等待时间
            //显示等待，规定时间内，看元素出现没，如果元素没有出现，就一直等，除非规定时间还没有出现，则抛出异常
            WebDriverWait wait = new WebDriverWait(driver,15);

            //身份认证
            log.info("开始对" + identity.getId() + " , " + identity.getPassword() + " 进行认证");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='username']")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@name='password']")));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='submit']")));
            WebElement inputUsername = driver.findElement(By.xpath("//input[@name='username']"));
            WebElement inputPassword = driver.findElement(By.xpath("//input[@name='password']"));
            WebElement submit = driver.findElement(By.xpath("//input[@type='submit']"));
//            Thread.sleep(2000);
            log.info("开始输入用户名密码");
//            inputUsername.sendKeys(identity.getId());
//            inputPassword.sendKeys(identity.getPassword());
//            String body = (String) jsExecutor.executeScript("document.body");
//            log.info(body);
//            String str = (String) jsExecutor.executeScript("document.querySelector(\"input[name=username]\")");
//            log.info(str);
            jsExecutor.executeScript("document.querySelector(\"input[name=username]\").setAttribute('value',"+identity.getId()+")");
            jsExecutor.executeScript("document.querySelector(\"input[name=password]\").setAttribute('value',"+identity.getPassword()+")");

            log.info("用户名密码成功");
//            Thread.sleep(2000);
//            submit.click();
            jsExecutor.executeScript("document.querySelector(\"input[type=submit]\").click()");
            log.info("尝试验证");

            Thread.sleep(1000);
            //跳转页面进行隐式等待
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            //点击搜索栏
            log.info("点击搜索栏");
            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='amp-header-search-input ampHeaderSearchFlag']")));
            }catch (TimeoutException e){
                log.info("进入异常，可能身份认证失败，建议检查学号密码是否正确: " + identity.getId() +" , "+ identity.getPassword());
                throw new RobotException("进入异常，可能身份认证失败，建议检查学号密码是否正确: " + identity.getId() +" , "+ identity.getPassword(),identity.getId());
            }
            WebElement enableSearch = driver.findElement(By.xpath("//div[@class='amp-header-search-input ampHeaderSearchFlag']"));
//            enableSearch.click();
            jsExecutor.executeScript("document.querySelector(\"div.amp-header-search-input.ampHeaderSearchFlag\").click()");

            Thread.sleep(1000);
            //写入搜索内容
            log.info("写入搜索内容: 健康信息每日打卡");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@id='ampServiceSearchInput']")));
            WebElement searchContent = driver.findElement(By.xpath("//input[@id='ampServiceSearchInput']"));
//            searchContent.sendKeys("健康信息每日打卡");
            jsExecutor.executeScript("document.querySelector(\"input#ampServiceSearchInput\").setAttribute('value','健康信息每日打卡')");

            Thread.sleep(1000);
            //执行搜索
            log.info("执行搜索");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@id='ampServiceSousuo']")));
            WebElement searchClick = driver.findElement(By.xpath("//span[@id='ampServiceSousuo']"));
//            searchClick.click();
            jsExecutor.executeScript("document.querySelector(\"span#ampServiceSousuo\").click()");


            Thread.sleep(1000);
//            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='ampServiceCenterSearchApps']//header")));
//            WebElement searchNumber = driver.findElement(By.xpath("//div[@id='ampServiceCenterSearchApps']//header"));
//            String number = (String) jsExecutor.executeScript("document.querySelector(\"div#ampServiceCenterSearchApps header\").getElementsByTagName('span')[1].textContent");
//            log.info("搜索到的数量为：" + (number == null ? "0" : number));
            //点击搜索到的第一个
            log.info("点击搜索到的第一个");
            //wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='amp-app-card-single amp-clearfix-child']")));
            //WebElement healthPunch = driver.findElement(By.xpath("//div[@class='amp-app-card-single amp-clearfix-child']"));
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='amp-hover-app-card-group amp-pull-left amp-service-center-app-group']")));
            WebElement healthPunch = driver.findElement(By.xpath("//div[@class='amp-hover-app-card-group amp-pull-left amp-service-center-app-group']"));

            Actions actions = new Actions(driver);
            actions.moveToElement(healthPunch).perform();
//            Thread.sleep(1000);
            healthPunch.click();
//            jsExecutor.executeScript("document.querySelector(\"div.amp-hover-app-card-group.amp-pull-left.amp-service-center-app-group.amp-active\").click()");
//            jsExecutor.executeScript("document.querySelector(\"div.amp-app-card-single.amp-clearfix-child\").click()");


            Thread.sleep(1000);
            //跳转页面进行隐式等待
            driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
            //重新获取句柄，始终获得当前最后的窗口
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }
            //判断是否有下次不再显示
            try {
                WebElement unViewInput = driver.findElement(By.xpath("//input[@id='ampDetailUnViewInput']"));
                log.info("具有下次不再显示");
                log.info("第一次进入打卡系统，要点击下次不再显示，并进入服务");
                WebElement next = driver.findElement(By.xpath("//div[@class='amp-checkbox ']/label"));
//                next.click();
                jsExecutor.executeScript("document.querySelector(\"div.amp-checkbox\").click()");
                Thread.sleep(200);
                WebElement enter = driver.findElement(By.xpath("//div[@id='ampDetailEnter']"));
//                enter.click();
                jsExecutor.executeScript("document.querySelector(\"div#ampDetailEnter\").click()");
            }catch (NoSuchElementException e){
                log.info("没有下次不再显示直接进入打卡页面");
            }

            log.info(driver.getTitle());
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }
            //点击新增按钮
            log.info("点击新增按钮");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-action='add']")));
            WebElement addButton = driver.findElement(By.xpath("//div[@data-action='add']"));
//            addButton.click();
            jsExecutor.executeScript("document.querySelector(\"div[data-action=add]\").click()");

            Thread.sleep(1000);
            //重新获取句柄，始终获得当前最后的窗口
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }
            //先判断是否打完卡
            Thread.sleep(1000);
            try {
                WebElement resultContent = driver.findElement(By.xpath("//div[@class='content']"));
//                String text = (String) jsExecutor.executeScript("document.querySelector(\"div.content\").innerText");
                if ("今日已打卡！".equals(resultContent.getText())){
                    log.info(" 用户 " + identity.getId() + " 已经成功打完卡，取消爬虫");
                    return;
                }
//                driver.close();
            }catch (NoSuchElementException e){
                log.info("还未打完卡，继续执行");
            }

            //点击已知晓并承诺（延迟时间长一点，固定12秒，因为10秒后才可点击）
            log.info("点击已知晓并承诺");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@class='bh-btn bh-btn-primary bh-pull-right']")));
            Thread.sleep(10 * 1000);
            WebElement promiseButton = driver.findElement(By.xpath("//button[@class='bh-btn bh-btn-primary bh-pull-right']"));
//            promiseButton.click();
            jsExecutor.executeScript("document.querySelector(\"button.bh-btn.bh-btn-primary.bh-pull-right\").click()");


            Thread.sleep(1000);
            //检查联系方式
            log.info("检查联系方式");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@data-caption='联系方式']")));
            WebElement phoneNumber = driver.findElement(By.xpath("//input[@data-caption='联系方式']"));
            if (StringUtils.isBlank(phoneNumber.getAttribute("value"))){
                log.info("联系方式 无默认，开始设置: " + identity.getPhoneNumber());
                String js = "document.querySelector(\"input[data-caption='联系方式']\").setAttribute('value',"+identity.getPhoneNumber()+")";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //phoneNumber.sendKeys(identity.getPhoneNumber());
            }

            Thread.sleep(1000);
            //检查明日所在省份
            log.info("检查明日所在省份");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='明日所在省份']//input")));
            WebElement tomorrowProvince = driver.findElement(By.xpath("//div[@data-caption='明日所在省份']//input"));
            if (StringUtils.isBlank(tomorrowProvince.getAttribute("value"))){
                log.info("明日所在省份 无默认，开始设置: " + "320000");
                //隐藏的元素是不可以直接修改的
                String js = "document.querySelector(\"div[data-caption='明日所在省份']\").getElementsByTagName('input')[0].setAttribute('value','320000')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //tomorrowProvince.sendKeys("320000");
            }

            Thread.sleep(1000);
            //检查明日所在区域
            log.info("检查明日所在区域");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='明日所在区域']//input")));
            WebElement tomorrowRegion = driver.findElement(By.xpath("//div[@data-caption='明日所在区域']//input"));
            if (StringUtils.isBlank(tomorrowRegion.getAttribute("value"))){
                log.info("明日所在区域 无默认，开始设置: " + "320115");
                //隐藏的元素是不可以直接修改的
                String js = "document.querySelector(\"div[data-caption='明日所在区域']\").getElementsByTagName('input')[0].setAttribute('value','320115')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //tomorrowRegion.sendKeys("320115");
            }

            Thread.sleep(1000);
            //检查今日状态
            log.info("检查今日状态");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='今日状态']//input")));
            WebElement todayStatus = driver.findElement(By.xpath("//div[@data-caption='今日状态']//input"));
            if (StringUtils.isBlank(todayStatus.getAttribute("value"))){
                log.info("今日状态 无默认，开始设置: " + "012");
                String js = "document.querySelector(\"div[data-caption='今日状态']\").getElementsByTagName('input')[0].setAttribute('value','012')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //todayStatus.sendKeys("012");
            }

            Thread.sleep(1000);
            //检查今日核酸检测情况
            log.info("检查今日核酸检测情况");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='今日核酸检测情况']//input")));
            WebElement todayAcidStatus = driver.findElement(By.xpath("//div[@data-caption='今日核酸检测情况']//input"));
            if (StringUtils.isBlank(todayAcidStatus.getAttribute("value"))){
                log.info("今日核酸检测情况 无默认，开始设置: " + "001");
                String js = "document.querySelector(\"div[data-caption='今日核酸检测情况']\").getElementsByTagName('input')[0].setAttribute('value','001')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //todayAcidStatus.sendKeys("001");
            }

            Thread.sleep(1000);
            //检查今日健康码
            log.info("检查今日健康码");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='今日健康码']//input")));
            WebElement todayHealthCode = driver.findElement(By.xpath("//div[@data-caption='今日健康码']//input"));
            if (StringUtils.isBlank(todayHealthCode.getAttribute("value"))){
                log.info("今日健康码 无默认，开始设置: " + "001");
                String js = "document.querySelector(\"div[data-caption='今日健康码']\").getElementsByTagName('input')[0].setAttribute('value','001')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //todayHealthCode.sendKeys("001");
            }

            Thread.sleep(1000);
            //检查14天漫游地
            log.info("检查14天漫游地");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='14天漫游地']//input")));
            WebElement day14Ramble = driver.findElement(By.xpath("//div[@data-caption='14天漫游地']//input"));
            if (StringUtils.isBlank(day14Ramble.getAttribute("value"))){
                log.info("14天漫游地 无默认，开始设置: " + "320115");
                String js = "document.querySelector(\"div[data-caption='14天漫游地']\").getElementsByTagName('input')[0].setAttribute('value','320115')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //day14Ramble.sendKeys("320115");
            }

            Thread.sleep(1000);
            //检查明日所在城市
            log.info("检查明日所在城市");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='明日所在城市']//input")));
            WebElement tomorrowCity = driver.findElement(By.xpath("//div[@data-caption='明日所在城市']//input"));
            if (StringUtils.isBlank(tomorrowCity.getAttribute("value"))){
                log.info("明日所在城市 无默认，开始设置: " + "320100");
                String js = "document.querySelector(\"div[data-caption='明日所在城市']\").getElementsByTagName('input')[0].setAttribute('value','320100')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //tomorrowCity.sendKeys("320100");
            }

            Thread.sleep(1000);
            //检查明日所在位置
            log.info("检查明日所在位置");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='明日所在位置']//input")));
            WebElement tomorrowPosition = driver.findElement(By.xpath("//div[@data-caption='明日所在位置']//input"));
            if (StringUtils.isBlank(tomorrowPosition.getAttribute("value"))){
                log.info("明日所在位置 无默认，开始设置: " + "001");
                String js = "document.querySelector(\"div[data-caption='明日所在位置']\").getElementsByTagName('input')[0].setAttribute('value','001')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //tomorrowPosition.sendKeys("001");
            }

            Thread.sleep(1000);
            //检查异常情况
            log.info("检查异常情况");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='异常情况']//input")));
            WebElement abnormalConditions = driver.findElement(By.xpath("//div[@data-caption='异常情况']//input"));
            if (StringUtils.isBlank(abnormalConditions.getAttribute("value"))){
                log.info("异常情况 无默认，开始设置: " + "001");
                String js = "document.querySelector(\"div[data-caption='异常情况']\").getElementsByTagName('input')[0].setAttribute('value','001')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //abnormalConditions.sendKeys("001");
            }

            Thread.sleep(1000);
            //检查今日疫苗接种情况
            log.info("检查今日疫苗接种情况");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='今日疫苗接种情况']//input")));
            WebElement todayVaccination = driver.findElement(By.xpath("//div[@data-caption='今日疫苗接种情况']//input"));
            if (StringUtils.isBlank(todayVaccination.getAttribute("value"))){
                log.info("今日疫苗接种情况 无默认，开始设置: " + "004");
                String js = "document.querySelector(\"div[data-caption='今日疫苗接种情况']\").getElementsByTagName('input')[0].setAttribute('value','004')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //todayVaccination.sendKeys("004");
            }

            Thread.sleep(1000);
            //检查家庭住址
            log.info("检查今日疫苗接种情况");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-caption='家庭住址（多选）']//input")));
            WebElement homeAddress = driver.findElement(By.xpath("//div[@data-caption='家庭住址（多选）']//input"));
            if (StringUtils.isBlank(homeAddress.getAttribute("value"))){
                log.info("家庭住址（多选） 无默认，开始设置: " + "006");
                String js = "document.querySelector(\"div[data-caption='家庭住址（多选）']\").getElementsByTagName('input')[0].setAttribute('value','006')";
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript(js);
                //homeAddress.sendKeys("006");
            }

            Thread.sleep(1000);
            //点击保存按钮
            log.info("点击保存按钮");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@id='save']")));
            WebElement save = driver.findElement(By.xpath("//div[@id='save']"));
//            save.click();
            jsExecutor.executeScript("document.querySelector(\"div[id='save']\").click()");


            Thread.sleep(3000);
            //重新获取句柄，始终获得当前最后的窗口
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }

            //点击确认
            log.info("点击确认");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@class='bh-dialog-btn bh-bg-primary bh-color-primary-5']")));
            WebElement confirm = driver.findElement(By.xpath("//a[@class='bh-dialog-btn bh-bg-primary bh-color-primary-5']"));
//            confirm.click();
            jsExecutor.executeScript("document.querySelector(\"a[class='bh-dialog-btn bh-bg-primary bh-color-primary-5']\").click()");

            //强制睡眠10秒等待页面数据更新完毕
            Thread.sleep(10 * 1000);
            //重新获取句柄，始终获得当前最后的窗口
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }
            //点击新增按钮
            log.info("验证是否成功打卡完成");
            log.info("点击新增按钮");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@data-action='add']")));
            addButton = driver.findElement(By.xpath("//div[@data-action='add']"));
//            addButton.click();
            jsExecutor.executeScript("document.querySelector(\"div[data-action='add']\").click()");

            Thread.sleep(1000);
            //重新获取句柄，始终获得当前最后的窗口
            for (String handle : driver.getWindowHandles()) {
                driver.switchTo().window(handle);
            }
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='content']")));
            WebElement resultContent = driver.findElement(By.xpath("//div[@class='content']"));
            LocalDateTime endTime = LocalDateTime.now();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            String formatEndTime = dtf.format(endTime);
            if ("今日已打卡！".equals(resultContent.getText())){
                log.info(formatEndTime + " 用户 " + identity.getId() + " 成功打完卡");
            }else {
                throw new RobotException("已经执行完打卡任务，但最终校验发生错误，请检查是否打卡成功",identity.getId());
            }
        }finally {
//            driver.close();
            driver.quit();
            log.info("关闭driver");
            log.info("用户 " + identity.getId() + " 执行自动打卡结束");
        }
    }
}
