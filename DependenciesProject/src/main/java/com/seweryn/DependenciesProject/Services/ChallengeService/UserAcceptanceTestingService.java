package com.seweryn.DependenciesProject.Services.ChallengeService;

import com.seweryn.DependenciesProject.Services.DataSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("uat")
@Service("dataSource")
public class UserAcceptanceTestingService implements DataSource {
    @Override
    public String getData() {
        return "User Acceptance Test Data Source";
    }
}
