package com.yhl.pinkMan.verticle;

import com.yhl.pinkMan.handler.TcpConnectionHandler;
import io.netty.handler.logging.ByteBufFormat;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerVerticle extends AbstractVerticle {
  private static Logger logger = LoggerFactory.getLogger(TcpServerVerticle.class);
  private NetServer netServer;

  @Override
  public void start() throws Exception {
    JsonObject server = config().getJsonObject("tcpServer");
    NetServerOptions options = new NetServerOptions();
    options
      .setTcpKeepAlive(true)
      .setLogActivity(true)
      .setActivityLogDataFormat(ByteBufFormat.SIMPLE);
    netServer = vertx.createNetServer(options);
    netServer.connectHandler(TcpConnectionHandler.create(vertx));
    netServer.listen(server.getInteger("port"), server.getString("host"), asyncResult -> {
      if (asyncResult.succeeded()) {
        logger.info("TCP SERVER 监听端口：{} 成功！", server.getInteger("port"));

      } else {
        logger.info("TCP SERVER 监听端口：" + server.getInteger("port") + " 失败! " + asyncResult.cause());
      }
    });
  }
}
