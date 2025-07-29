package com.utils;

import com.BaseReq.Email;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class TestResultListener implements ITestListener {

    public static int passed = 0;
    public static int failed = 0;

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult result) {
        passed++;
        System.out.println("[Listener]  Passed: " + result.getName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        failed++;
        System.out.println("[Listener]  Failed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext context) {
        int total = passed + failed;
        double passPercentage = total > 0 ? (passed * 100.0) / total : 0;

        TestSummary.passed = passed;
        TestSummary.failed = failed;
        TestSummary.total = total;
        TestSummary.passPercentage = passPercentage;

        System.out.println("[Listener] Execution Summary — Passed: " + passed + ", Failed: " + failed);

        //  Send email after test summary is finalized
        try {
            File reportFile = new File("target/opencart.html");
            if (reportFile.exists()) {
                System.out.println("Report found. Sending email from listener...");
                Email email = new Email();
                email.mail();  // This now includes the summary in the message
                System.out.println("Email sent successfully.");
            } else {
                System.out.println("Report not found at: " + reportFile.getAbsolutePath());
            }
        } catch (Exception e) {
            System.err.println("Failed to send email from listener:");
            e.printStackTrace();
        }
    }

}
