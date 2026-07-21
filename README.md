# ParaBank Test Automation

This project is a test automation framework created for the ParaBank demo application.

The main goal was to build a simple framework that combines API and UI testing while keeping the code easy to understand and maintain.

The framework uses Java, Maven, Cucumber, TestNG, Selenium WebDriver, Rest Assured and PicoContainer.

![ParaBank home page](docs/images/parabank-homepage.png)

Application under test:

```text
https://parabank.parasoft.com/parabank/
```

## Swagger page

The ParaBank REST API documentation is available here:

```text
https://parabank.parasoft.com/parabank/api-docs/index.html
```

---

## Known limitation and login recovery

The UI tests depend on the public ParaBank demo environment.

During development, the normal customer login sometimes returned this server-side message:

```text
An internal error has occurred and has been logged.
```
![ParaBank UI instability](docs/images/ui-instability.png)

Or: 
```text
The username & 
```


![ParaBank UI instability](docs/images/ui-instability2.png)

When this happens, the customer dashboard is not loaded and authenticated functions such as **Open New Account** and **Bill Pay** are unavailable.


To make the UI scenarios more resilient, the framework keeps the normal username/password login as the primary path and uses ParaBank's official **Customer Lookup** page only when an internal error is detected.

The recovery flow is:

```text
Normal UI login
→ Login succeeds: continue the scenario
→ Internal ParaBank error: open "Forgot login info?"
→ Fill Customer Lookup with the demo customer information
→ ParaBank validates the customer and opens an authenticated session
→ Continue the original scenario
```

![Customer Lookup page](docs/images/customer-lookup.png)

The fallback is not used for invalid credentials or any other unexpected login failure. In those situations, the scenario fails normally.

This recovery mechanism is useful because it:

- keeps the main login flow as the default;
- handles a known intermittent problem in the public environment;
- does not hard-code or reuse cookies such as the `JSESSIONID`;
- uses a real ParaBank user journey;
- keeps the recovery details inside Page Objects instead of the feature files.

The Customer Lookup page displays the recovered password because ParaBank is a demonstration application.

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
- Reject a bill payment with an invalid amount such as `abc`

### Framework startup scenario

- Check that Cucumber starts correctly
- Check that the configuration file can be loaded

![Feature files in the project](docs/images/feature-files.png)

---

## Test approach

The UI tests use a hybrid approach.

The API prepares the required back-end state before Selenium starts the browser.

Example flow:

```text
API login
→ API gets the customer ID
→ API gets or creates a source account
→ API deposits money when required
→ Selenium authenticates the customer
→ Selenium performs the UI action
→ Cucumber verifies the result
```

This approach avoids preparing all test data through long and fragile UI forms. It also makes the scenarios faster and more stable.

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

After installation, open PowerShell and run:

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
![Windows Path environment variable](docs/images/windows-path.png)

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

Selenium Manager automatically finds and manages the required ChromeDriver, so no manual ChromeDriver setup is required.

![Google Chrome version](docs/images/chrome-version.png)

---

## 5. Clone the repository

Open PowerShell and run:

```powershell
git clone https://github.com/SaidounDjahid/Parabank.git
cd Parabank
```

Check the repository:

```powershell
git status
```

![Repository cloned locally](docs/images/git-clone.png)
![Git status](docs/images/git-status.png)

---

## 6. Open the project

The project can be opened with Visual Studio Code or IntelliJ IDEA.

![Project opened in VS Code](docs/images/vscode-project.png)

Optional VS Code extensions:

- Extension Pack for Java
- Cucumber
- GitHub Actions

![Used extensions](docs/images/vscode-extensions.png)

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

customer.first.name=John
customer.last.name=Smith
customer.street=1431 Main St
customer.city=Beverly Hills
customer.state=CA
customer.zip.code=90210
customer.ssn=622-11-9999

wait.seconds=30
headless=false
demo.delay.ms=0

billpay.deposit=500.00
```

The customer fields are used only by the Customer Lookup recovery flow when the normal UI login returns the known internal server error.

The same file also contains the test payee information used by the Bill Pay scenarios.

![Configuration file](docs/images/config-properties.png)

### Main properties

| Property | Purpose |
|---|---|
| `ui.base.url` | ParaBank website URL |
| `api.base.url` | ParaBank REST API URL |
| `username` | Demo customer username |
| `password` | Demo customer password |
| `customer.*` | Demo customer identity used by Customer Lookup |
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

![Build compilation](docs/images/build-result.png)

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

## Run with a visible browser

```powershell
mvn clean test '-Dheadless=false'
```

![Headless set to false](docs/images/headless.png)

Chrome is controlled by Selenium:

![Browser controlled by Selenium](docs/images/browser.png)

For CI execution, headless mode is used because the execution machine does not require a graphical interface.

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

It also saves dynamic API results inside `TestContext`.

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
- check whether an element exists
- wait for one of several possible page outcomes

These helper methods are reused by all Page Objects.

## Page Objects

Each Page Object represents one part of the ParaBank website.

- `LoginPage` handles the normal username/password login and detects login outcomes
- `CustomerLookupPage` handles the official recovery form used after the known internal login error
- `OpenNewAccountPage` handles account creation
- `AccountOverviewPage` verifies that an account is displayed
- `BillPayPage` handles bill payment and validation messages

### Why `CustomerLookupPage` is separate

Customer Lookup is a different page with its own URL, fields, button and result.

Keeping it in a separate Page Object:

- follows the Page Object Model;
- avoids mixing two different screens inside `LoginPage`;
- keeps locators close to the page that owns them;
- makes the recovery flow easier to maintain and explain.

## `UiSteps`

Connects the UI Gherkin steps to the Page Objects.

It coordinates the normal login and the conditional Customer Lookup recovery, but it does not contain page locators.

The existing Gherkin step remains:

```gherkin
And the customer is logged in to the web portal
```

No extra feature step is required because Customer Lookup is an implementation detail used to achieve the same business result: an authenticated customer session.

A separate Gherkin scenario would only be useful if the Customer Lookup feature itself needed to be tested as an independent business requirement.

## `TestRunner`

Starts the Cucumber scenarios with TestNG and generates the HTML report.

---

# Example scenarios

## Open a new account

The API prepares the customer and selects an existing account.

Selenium then authenticates the customer and creates a new savings account.

![Open new account page](docs/images/open-new-account-page.png)

The test verifies that the account creation is successful:

![New account displayed](docs/images/new-account-result.png)

It also checks that the new account is displayed in Accounts Overview:

![New account displayed in overview](docs/images/new-account-overview.png)

---

## Bill Pay

The API prepares a funded source account.

Selenium authenticates the customer, opens the Bill Pay page and submits a payment.

![Bill Pay form](docs/images/billpay-form.png)

The framework also checks:

- successful payment;
- missing mandatory fields;
- invalid amount.

![Bill Pay validation](docs/images/billpay-validation.png)

---

# GitHub Actions

The project contains a GitHub Actions workflow:

```text
.github/workflows/tests.yml
```

It runs automatically after each push or pull request.

The workflow:

1. downloads the repository;
2. installs Java 17;
3. runs the tests in headless mode.

A successful execution is displayed with a green status:

![GitHub Actions successful run](docs/images/github-actions-success.png)

When one or more tests fail, the workflow and the related job are displayed in red:

![GitHub Actions unsuccessful run](docs/images/github-actions-no-success.png)

When GitHub Actions email notifications are enabled, the user who triggered the workflow may receive an email containing the failed workflow status and a direct link to the execution details.

![Build failure email](docs/images/build-failure.png)

This confirms that the framework can run on a clean Ubuntu machine and not only on the local Windows environment.

---

# Notes

ParaBank is a public demonstration environment.

The test data can change between executions, and the service may sometimes respond slowly or return temporary server-side errors.

The project demonstrates:

- API testing;
- UI testing;
- hybrid API/UI scenarios;
- shared scenario data;
- Page Object Model;
- conditional recovery from a known public-environment issue;
- Cucumber reporting;
- continuous integration.

---

# Possible improvements

Possible future improvements include:

- Attach a screenshot to the Cucumber report when a UI scenario fails
- Add more negative scenarios with more assertions 
- Better catch ui exceptions & handle them in specific way : not generic error css block
- Move credentials and personal demo data to environment variables 
- Run API and UI tests in separate CI jobs
- Add a scheduled GitHub Actions execution for direct execution
...
