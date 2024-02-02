package com.seweryn.DependenciesProject.Controllers.ChallengeController;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles({"qa", "EN"})
@SpringBootTest
class QualityAssuranceServiceTest {

    @Autowired
    DataSourceController dataSourceController;

    @Test
    void getData() {
        System.out.println(dataSourceController.getData());
    }
}