package com.wanglin;

import com.wanglin.db.UserCoinDAO;
import com.wanglin.resources.CoinsResource;
import io.dropwizard.Application;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.skife.jdbi.v2.DBI;

public class CoinsApplication extends Application<CoinsConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CoinsApplication().run(args);
    }

    @Override
    public String getName() {
        return "Coins";
    }

    @Override
    public void initialize(final Bootstrap<CoinsConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final CoinsConfiguration configuration,
                    final Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI myDbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        //final UserCoinDAO dao = myDbi.onDemand(UserCoinDAO.class);

        final CoinsResource resource = new CoinsResource(myDbi);
        environment.jersey().register(resource);
    }

}
