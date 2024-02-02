package com.seweryn.DependenciesProject.Services.ChallengeService;

import com.seweryn.DependenciesProject.Services.DataSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("prod")
@Service("dataSource")
public class ProductionService implements DataSource {
    @Override
    public String getData() {
        return "Production Data Source";
    }
}
