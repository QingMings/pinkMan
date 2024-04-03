package com.yhl.pinkMan;

import com.yhl.pinkMan.verticle.TcpServerVerticle;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class MainVerticle extends AbstractVerticle {
  private static final Logger logger = LoggerFactory.getLogger(MainVerticle.class);
  public static final String PID_FILE = "process.pid";
  public static final String PID_LOCK = "process.lck";

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    if (!lockInstance(PID_LOCK)) {
      startPromise.fail("只能启动一个程序实例");
      vertx.close();
      return;
    }

    Promise<JsonObject> getConfPromise = Promise.promise();
    ConfigStoreOptions file = new ConfigStoreOptions().setType("file").setFormat("json").setConfig(new JsonObject().put("path", "config.json"));
    ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(file);
    ConfigRetriever retriever = ConfigRetriever.create(vertx, options);
    retriever.getConfig(getConfPromise);

    Future<JsonObject> future = getConfPromise.future();
    future.compose(jsonConfig ->
      Future.<String>future(f -> {
        logger.debug("配置文件加载");
        if (config().size() == 0) {
          logger.info("加载jar包内置配置文件！");
          jsonConfig.fieldNames().forEach(key -> config().put(key, jsonConfig.getValue(key)));
        } else {
          if (processArgs() != null && processArgs().size() == 2 && processArgs().get(0).equals("-conf")) {
            logger.info("加载外部配置文件 :" + processArgs().get(1));
          } else {
            logger.info("从 Options 加载配置");
          }
        }
        logger.info("配置文件加载完成！");
        logger.debug("\n" + config().encodePrettily());
        config().fieldNames().forEach(key -> {
          jsonConfig.put(key, config().getValue(key));
        });
        vertx.deployVerticle(TcpServerVerticle.class.getName(), new DeploymentOptions().setConfig(config()), f);
      })
    ).onSuccess(ar -> {
      writePid();
      startPromise.complete();
    }).onFailure(cause -> {
      startPromise.fail(cause);
    });
  }
  private void writePid() {
    Long pid = getPID();
    vertx.fileSystem().writeFile(PID_FILE, Buffer.buffer(pid.toString()), voidAsyncResult -> {
      if (voidAsyncResult.succeeded()) {
        logger.debug("write pid success");
      } else {
        logger.debug("write pid fail");
      }
    });
  }

  public static long getPID() {
    String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
    if (processName != null && processName.length() > 0) {
      try {
        return Long.parseLong(processName.split("@")[0]);
      } catch (Exception e) {
        return 0;
      }
    }

    return 0;
  }

  private boolean lockInstance(final String lockFile) {
    try {
      final File file = new File(lockFile);
      final RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
      final FileLock fileLock = randomAccessFile.getChannel().tryLock();
      if (fileLock != null) {
        Runtime.getRuntime().addShutdownHook(new Thread() {
          public void run() {
            try {
              fileLock.release();
              randomAccessFile.close();
              file.delete();
            } catch (Exception e) {
              logger.error("Unable to remove lock file: " + lockFile, e);
            }
          }
        });
        return true;
      }
    } catch (Exception e) {
      logger.error("Unable to create and/or lock file: " + lockFile, e);
    }
    return false;
  }
}
