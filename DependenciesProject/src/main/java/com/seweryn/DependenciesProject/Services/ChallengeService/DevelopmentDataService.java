package com.seweryn.DependenciesProject.Services.ChallengeService;

import com.seweryn.DependenciesProject.Services.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
@Profile({"dev", "default"})
@Service("dataSource")
public class DevelopmentDataService implements DataSource {
    @Override
    public String getData() {
        return "Development Data Source";
    }
}
