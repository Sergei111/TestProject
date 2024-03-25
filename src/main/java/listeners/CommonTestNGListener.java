package listeners;

import org.testng.*;

public class CommonTestNGListener implements ITestListener, ISuiteListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {
        //do smt
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        //do smt
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        //do smt
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {

    }
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        //do smt
    }
    @Override
    public void onStart(ITestContext context) {
        //do smt
    }
    @Override
    public void onFinish(ITestContext context) {
        //do smt
    }

    @Override
    public void onStart(ISuite iSuite) {

    }

    @Override
    public void onFinish(ISuite iSuite) {
        //do smt
    }
}
