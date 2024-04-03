package com.yhl.pinkMan;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.resolver.ResolverProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class PLauncher extends Launcher {
  private static final Logger logger = LoggerFactory.getLogger(PLauncher.class);

  public static void main(String[] args) {
    new PLauncher().dispatch(args);
  }


  @Override
  public void beforeDeployingVerticle(DeploymentOptions deploymentOptions) {
    super.beforeDeployingVerticle(deploymentOptions);
    System.setProperty(ResolverProvider.DISABLE_DNS_RESOLVER_PROP_NAME, "true");
  }

  @Override
  public void beforeStartingVertx(VertxOptions options) {
    super.beforeStartingVertx(options);
    options.setWarningExceptionTime(10).setWarningExceptionTimeUnit(TimeUnit.SECONDS)
      .setMaxEventLoopExecuteTime(20).setMaxEventLoopExecuteTimeUnit((TimeUnit.SECONDS));
  }

  @Override
  public void afterStartingVertx(Vertx vertx) {
    super.afterStartingVertx(vertx);
    printVersion(vertx);
  }

  private void printVersion(Vertx vertx) {
    ConfigRetriever.create(vertx,
      new ConfigRetrieverOptions().addStore(
        new ConfigStoreOptions()
          .setType("file")
          .setFormat("properties")
          .setConfig(new JsonObject().put("path", "p.properties")))
    ).getConfig().onSuccess(config -> logger.info("Application version: " + config.getString("p.version")));

  }
}
