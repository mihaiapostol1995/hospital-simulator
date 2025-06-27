Set the JDK to java 21 before running the project.

The premise is that each patient, identified by the patient state in the first command-line argument, receives the drug listed at the same position in the second argument.
Since auditing is necessary in medicine, the application also uses Envers to audit possible changes to the patient's status and drugs in an AUD table.

Entry class for the application is HospitalSimulationRunner.

To build & run the application with arguments, run this command from the root folder:
mvn spring-boot:run -Dspring-boot.run.arguments="F,F P"
For example, in this case the application will create two users suffering from fever (F), and only the first one receives Paracetamol (P).
Logs will be printed to console.

Example output:

2025-06-12T10:19:59.328+03:00  INFO 82070 --- [           main] c.evooq.HospitalSimulationApplication    : Started HospitalSimulationApplication in 1.356 seconds (process running for 1.528)
2025-06-12T10:19:59.329+03:00  INFO 82070 --- [           main] com.evooq.HospitalSimulationRunner       : Argument 0: F,F
2025-06-12T10:19:59.329+03:00  INFO 82070 --- [           main] com.evooq.HospitalSimulationRunner       : Argument 1: P
2025-06-12T10:19:59.330+03:00 DEBUG 82070 --- [           main] com.evooq.service.PatientService         : Saving patients into database: [Patient(id=null, name=Witty Bernstein, age=49, gender=FEMALE, drugList=[], state=FEVER), Patient(id=null, name=Brave Newton, age=49, gender=FEMALE, drugList=[], state=FEVER)]
2025-06-12T10:19:59.418+03:00 DEBUG 82070 --- [           main] com.evooq.service.DrugService            : Administering drug PARACETAMOL to patient Witty Bernstein suffering from FEVER
2025-06-12T10:19:59.424+03:00  INFO 82070 --- [           main] c.e.service.PatientAssessmentService     : Fever patient Witty Bernstein has been cured
2025-06-12T10:19:59.428+03:00  INFO 82070 --- [           main] c.e.service.PatientAssessmentService     : Patient Brave Newton has not been cured

To run unit and integration tests:
mvn clean install

To see test coverage, after running clean install go to target/site/index.html, and open it in your preferred browser.
Code coverage is 100% for instructions and 97% for branches. 
The reason for 97% coverage is that Jacoco doesn't support switch expressions, used in checkOnPatient method.

All dependencies are up to date.
To run owasp dependency check to check for vulnerabilities, run:
mvn org.owasp:dependency-check-maven:check -Powasp
Be aware that it will run for a while.
Also the code is scanned with spotbugs Maven plugin on each build, to ensure code reliability.
For dependency updates I added versions-maven-plugin. It can be used to update outdated dependencies, except for release-candidates, snapshots etc.

Checkstyle enforces a uniform coding standard for all contributors to the project.

There are also architectural tests, to make sure the project follows a certain structure, in ArchitectureTest test class.
