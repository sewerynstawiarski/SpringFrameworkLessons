package com.seweryn.DependenciesProject.Services.ChallengeService;

import com.seweryn.DependenciesProject.Services.DataSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("qa")
@Service("dataSource")
public class QualityAssuranceService implements DataSource {
    @Override
    public String getData() {
        return "Quality Assurance Data Source";
    }
}
