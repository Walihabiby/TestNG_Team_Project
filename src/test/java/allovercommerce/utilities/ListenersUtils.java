package com.utilities;

import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ListenersUtils implements ITestListener, IRetryAnalyzer, IAnnotationTransformer {
    @Override
    public void onStart(ITestContext context) {
//        ITestListener.super.onStart(context);
        System.out.println("onStart: executes only ONCE BEFORE ALL test cases. @Test: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
//        ITestListener.super.onFinish(context);
        System.out.println("onFinish: executes only ONCE AFTER ALL test cases. @Test: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
//        ITestListener.super.onTestStart(result);
        System.out.println("onTestStart: executes BEFORE Each test case. @Test: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
//        ITestListener.super.onTestSuccess(result);
        System.out.println("onTestSuccess: executes for Each successful test case. @Test: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
//        ITestListener.super.onTestSkipped(result);
        System.out.println("onTestSkipped: executes for Each SKIPPED test case. @Test: " + result.getName());
    }


    @Override
    public void onTestFailure(ITestResult result) {
//        ITestListener.super.onTestFailure(result);
        System.out.println("onTestFailure: executes ONLY for FAILED test case. @Test: " + result.getName());

        try {
            MediaUtils.takeScreenshotOfTheEntirePage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

//    @Override
//    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
//        ITestListener.super.onTestFailedButWithinSuccessPercentage(result);
//    }
//
//    @Override
//    public void onTestFailedWithTimeout(ITestResult result) {
//        ITestListener.super.onTestFailedWithTimeout(result);
//    }



//    Rerun Failed Test Cases automatically one more time
//    IRetryAnalyzer Method Is Implemented
//    This method will be called automatically to RETRY THE FAILED TEST CASES
    private int retryCount = 0;
    private static final int maxRetryCount = 1;//RERUN COUNT
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < maxRetryCount) {
            retryCount++;
            return true;
        }
        return false;
    }
    //    IIAnnotationTransformer method is added
//    This makes the failed test cases automatically rerun using testng xml files
    @Override
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(ListenersUtils.class);
    }






}