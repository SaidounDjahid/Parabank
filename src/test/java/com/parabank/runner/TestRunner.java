package com.parabank.runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features", 
    glue = "com.parabank", //so tgat cucumber inspects the package com.parabank & its subpackages (not only .steps) com.parabank.steps + com.parabank.hooks
    plugin = {
        "pretty",
        "summary",
        "html:target/cucumber-report.html" // to generate html report inside target directory on every test exeuction

    },
    monochrome = true,
    publish = false
)

public class TestRunner extends AbstractTestNGCucumberTests {
    
    @Override
    @DataProvider(parallel = false)

    public Object [][] scenarios(){
        return super.scenarios();
    }
    
}
