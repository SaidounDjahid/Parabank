
# ParaBank Test Automation

This project is a test automation framework created for the ParaBank demo application.

The main goal was to build a simple framework that combines API and UI testing while keeping the code easy to understand and maintain.

The framework uses Java, Maven, Cucumber, TestNG, Selenium WebDriver and Rest Assured.

![ParaBank home page](docs/images/parabank-homepage.png)

Application under test:

```text
https://parabank.parasoft.com/parabank/
```

## Swagger Page
The swagger page of the web application is stored in:
https://parabank.parasoft.com/parabank/api-docs/index.html



## Known limitation

The UI tests rely on the public ParaBank demo environment.

During the development, the application sometimes returned an internal server error during customer login. When this happens, the customer dashboard cannot be loaded and the UI scenarios cannot access authenticated features such as **Open New Account** or **Bill Pay**.

This issue comes from the public test environment and not necessarily from the automation framework. The same login error can also be reproduced manually in the browser.

The complete test suite was successfully executed locally, in headless mode and through GitHub Actions when the ParaBank environment if fully available.

![ParaBank UI instability](docs/images/ui-instability.png)

---

## Project overview

The project currently contains 9 automated test scenarios.

### API scenarios

- Login with valid credentials
- Login with invalid credentials
- Create a new bank account with valid data
- Reject account creation with an invalid funding account

### UI scenarios

- Open a new savings account
- Pay a bill with valid data
- Reject a bill payment with missing mandatory information
- Reject a bill payment with an invalid amount (amount=abc)

### Framework startup scenario

- Check that Cucumber starts correctly
- Check that the configuration file can be loaded

![Feature files in the project](docs/images/feature-files.png)

---

## Test approach

The UI tests use a hybrid approach.

The API prepares the test data (to reach certain back-end stade) before Selenium starting the browser.

Example flow:

```text
API login
→ API gets the customer ID
→ API gets or creates a source account
→ API deposits money when required
→ Selenium logs in
→ Selenium performs the UI action
→ Cucumber verifies the result
```

This approach avoids preparing all test data through long & fragile UI forms.

It also makes the scenarios faster to execute and more stable.



---

## Technologies

- Java 17
- Maven
- Cucumber 7
- TestNG
- Selenium WebDriver
- Rest Assured
- PicoContainer
- GitHub Actions

---

# Setup

The following setup was used on Windows 11.

## 1. Install Java 17

Download and install a Java 17 JDK.

After the installation, open PowerShell and run:

```powershell
java -version
```

Expected result:

```text
openjdk version "17..."
```

![Java version](docs/images/java-version.png)

### Configure `JAVA_HOME`

Open:

```text
Windows Search
→ Edit the system environment variables
→ Environment Variables
```

Create a new system variable:

```text
Variable name: JAVA_HOME
Variable value: C:\Program Files\Java\<your-jdk-folder>
```

Example:

```text
C:\Program Files\Java\ZuluJDK17_x64
```

Then edit the Windows `Path` variable and add:

```text
%JAVA_HOME%\bin
```

Close and reopen PowerShell, then verify:

```powershell
echo $env:JAVA_HOME
java -version
```

![JAVA_HOME environment variable](docs/images/java-home.png)
![Windows path environment variable](docs/images/windows-path.png)

---

## 2. Install Maven

Download Apache Maven and extract it to a local folder.

Example:

```text
C:\apache-maven-3.9.16
```

Create a system variable:

```text
Variable name: MAVEN_HOME
Variable value: C:\apache-maven-3.9.16
```

Add this value to the Windows `Path`:

```text
%MAVEN_HOME%\bin
```

Restart PowerShell and verify the installation:

```powershell
mvn -version
```

The output should display both Maven and Java 17.

![Maven version](docs/images/maven-version.png)

---

## 3. Install Git

Install Git for Windows.

Verify the installation:

```powershell
git --version
```

![Git version](docs/images/git-version.png)

---

## 4. Install Google Chrome

The UI tests use Google Chrome.

Make sure Chrome is installed and updated.

Selenium Manager automatically finds and manages the required ChromeDriver =>  so no manual ChromeDriver setup is required for this project.

![Google Chrome version](docs/images/chrome-version.png)

---

## 5. Clone the repository

Open PowerShell and run git clone command to clone project + ChangeDirectory command to navigate inside project:

```powershell
git clone https://github.com/SaidounDjahid/Parabank.git
cd Parabank
```

Check the repository:

```powershell
git status
```

![Repository cloned locally](docs/images/git-clone.png)
![Git Status](docs/images/git-status.png)

---

## 6. Open the project

The project can be opened with Visual Studio Code or IntelliJ IDEA.

For Visual Studio Code:

![Project opened in VS Code](docs/images/vscode-project.png)

(Optional) Recommended VS Code extensions:

- Extension Pack for Java
- Cucumber
- GitHub Actions

![Used Extensions](docs/images/vscode-extensions.png)

---

## 7. Check the configuration

The main configuration file is:

```text
src/test/resources/config.properties
```

Example:

```properties
ui.base.url=https://parabank.parasoft.com/parabank/
api.base.url=https://parabank.parasoft.com/parabank/services/bank

username=john
password=demo

wait.seconds=20
headless=false
demo.delay.ms=0

billpay.deposit=500.00
```

The same file also contains the test payee information used by the Bill Pay scenarios.

![Configuration file](docs/images/config-properties.png)

### Main properties

| Property | Purpose |
|---|---|
| `ui.base.url` | ParaBank website URL |
| `api.base.url` | ParaBank REST API URL |
| `username` | Demo customer username |
| `password` | Demo customer password |
| `wait.seconds` | Maximum Selenium wait time |
| `headless` | Runs Chrome without showing the browser |
| `demo.delay.ms` | Optional delay after browser actions |
| `billpay.deposit` | Amount deposited before the Bill Pay scenario |

System properties passed in the Maven command have priority over this file.

Example:

```powershell
mvn clean test '-Dheadless=true'
```

---

# Running the tests

## Compile the project


```powershell
mvn clean test-compile
```
![Build Compilation](docs/images/build-result.png)

## Run all tests

```powershell
mvn clean test
```

Expected result:

```text
9 Scenarios (9 passed)
BUILD SUCCESS
```

![Successful execution result](docs/images/execution-result.png)

---

## Run only API tests

```powershell
mvn clean test '-Dcucumber.filter.tags=@api'
```

![API tests execution](docs/images/api-tests.png)

---

## Run only UI tests

```powershell
mvn clean test '-Dcucumber.filter.tags=@ui'
```

![UI tests execution](docs/images/ui-tests.png)


---

## Run in headless mode = False (Show the broswser during test execution)


```powershell
mvn clean test '-Dheadless=false' 
```
![headless = false](docs/images/headless.png)

Chrome is controlled by Selenium:

![headless true](docs/images/browser.png)

Headless mode is useful for CI execution because Chrome runs without opening a visible browser window, and also because the host execution machine does not have/need a graphical interface.

---

## Run tests and open the Cucumber report

```powershell
mvn clean test; Start-Process .\target\cucumber-report.html
```

The report is generated here:

```text
target/cucumber-report.html
```

![Cucumber HTML report](docs/images/cucumber-report.png)

---

# Main classes

## `ApiClient`

Contains the HTTP requests sent to the ParaBank REST API.

Examples:

- login
- get customer accounts
- create an account
- deposit money

## `ApiSteps`

Connects the API Gherkin steps to `ApiClient`.

It also saves API results inside `TestContext`.

## `TestContext`

Stores data shared during one scenario.

Examples:

- API response
- customer ID
- source account ID
- new account ID
- WebDriver

PicoContainer provides the same context object to the step and hook classes during a scenario.

## `Hooks`

Starts Chrome before scenarios tagged with `@ui` and closes it after the scenario.

## `BasePage`

Contains reusable Selenium actions such as:

- click
- type
- read text
- select an option
- wait for an element

Will be used on all page objects page

## Page Objects

Each Page Object represents one part of the ParaBank website.

- `LoginPage` handles login
- `OpenNewAccountPage` handles account creation
- `AccountOverviewPage` verifies that an account is displayed
- `BillPayPage` handles bill payment and validation messages

## `UiSteps`

Connects the UI Gherkin steps to the Page Objects.

It coordinates the browser actions but does not contain page locators.

## `TestRunner`

Starts the Cucumber scenarios with TestNG and generates the HTML report.

---

# Example scenarios

## Open a new account

The API prepares the customer and selects an existing account.

Selenium then logs in and creates a new savings account.

![Open new account page](docs/images/open-new-account-page.png)

The test  verifies that the new account creation is sucessfll

![New account displayed](docs/images/new-account-result.png).

& check the new account is displayed in account overview

![New account displayed](docs/images/new-account-overview.png).


---

## Bill Pay

The API prepares a funded source account.

Selenium opens the Bill Pay page and submits a payment.

![Bill Pay form](docs/images/billpay-form.png)

The framework also checks:

- successful payment
- missing mandatory fields
- invalid amount

![Bill Pay validation](docs/images/billpay-validation.png)

---

# GitHub Actions

The project contains a simple GitHub Actions workflow:

```text
.github/workflows/tests.yml
```

It runs automatically after each push or pul request.

The workflow:

1. downloads the repository;
2. installs Java 17;
3. runs all tests in headless mode.

A successful execution is displayed with a green status in the GitHub Actions page:

![GitHub Actions successful run](docs/images/github-actions-success.png)



When one or more tests fail, the workflow is marked as failed and the related job is displayed in red:

![GitHub Actions unsuccessful run](docs/images/github-actions-no-success.png)

When GitHub Actions email notifications are enabled, the user who triggered the workflow receive an email containing the failed workflow status and a direct link to the execution details. 

![Build Failure Email](docs/images/build-failure.png)

This confirms that the framework also works on a clean Ubuntu machine and not only on the local Windows environment.

---

# Notes

ParaBank is a public demonstration environment.

The data can often change between executions and the service may sometimes respond slowly, or throw differents erros.

The objective was to demonstrate:

- API testing
- UI testing
- shared scenario data model
- Page Object Model
- Cucumber reporting
- continuous integration

---

# Possible improvements

Possible future improvements include:

- Attach a screenshot to the Cucumber report when a UI scenario fails 
- Upload the Cucumber report as a GitHub Actions artifact 
- Add more negative scenarios
- Move credentials to environment variables 
- Add retries for temporary public environment failures (Because we saw the issues are intermittent)
- Run API and UI tests in separate CI jobs (Main change)
- add scheduler for test execution in github action... 

