## True Accord Take Home

### Run Instructions
- Please run this with java version 11 or greater. 
- Clone the github repository.
- Go to the command prompt where the repository was downloaded. 
- To run junits execute the *runDebtTests.bat* (Windows) or *runDebtTests.sh* (Unix).

Example:
![](.README_images/CmdTestRun.png)
- To run application execute the *runDebtApp.bat* (Windows) or *runDebtApp.sh* (Unix).  

Example:
![](.README_images/CmdAppRun.png)

### Structure
- *src/main* - Contains the main source class files
- *src/test* - Contains the test source class files.
- *mock/* - Contains json files used as mock endpoints for additional mock testing.
- *lib* - Contains required dependency jar files and the trueaccord.jar for the application.
- *runDebtApp.bat*, *runDebtApp.sh * - batch and shell script to execute the debt application
- *runDebtTests.bat*, *runDebtTests.sh * - batch and shell script to execute the junit tests

### Main Classes
- *App* - Main application which processes all debts, associated payments plans if any and their payments, and then outputs the debts with the new fields.
- *Debt* - Represents a given customers debt account and how much is owed.  May or may not be associated with a payment plan.
- *PaymentPlan* - Represents a payment plan for a given debt account. 
- *HttpGetClient* - Interface to fetch various data (i.e. debts, payment plans, payments) from the data store.  
