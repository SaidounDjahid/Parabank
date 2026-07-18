package com.parabank.runner;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.parabank.steps",
    plugin = {
        "pretty",
        "summary",

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
