package com.evooq.archunit;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private final JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.evooq");

    @Test
    void noFieldInjectionAllowed() {
        ArchRuleDefinition.noFields()
            .should().beAnnotatedWith(org.springframework.beans.factory.annotation.Autowired.class)
            .check(importedClasses);
    }

    @Test
    void servicesShouldResideInServicePackage() {
        ArchRuleDefinition.classes()
            .that().haveSimpleNameEndingWith("Service")
            .should().resideInAPackage("..service..")
            .check(importedClasses);
    }

    @Test
    void repositoryClassesShouldOnlyBeAccessedByService() {
        ArchRuleDefinition.classes()
                .that().resideInAPackage("..repository..")
                .should().onlyBeAccessed().byAnyPackage("..service..", "..repository..")
                .check(importedClasses);
    }
}