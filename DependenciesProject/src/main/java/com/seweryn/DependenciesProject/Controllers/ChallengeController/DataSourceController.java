package com.seweryn.DependenciesProject.Controllers.ChallengeController;

import com.seweryn.DependenciesProject.Services.DataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;

@Controller
public class DataSourceController {

    private final DataSource dataSource;

    public DataSourceController(@Qualifier("dataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }
    public String getData() {
        return dataSource.getData();
    }
}
