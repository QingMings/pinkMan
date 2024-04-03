package com.yhl.pinkMan;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;

public class Test {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    DeploymentOptions options = new DeploymentOptions();
    options.setConfig(Buffer.buffer("{\"tcpServer\": {\"host\": \"127.0.0.1\", \"port\": 1111}}").toJsonObject());
    vertx.deployVerticle(new MainVerticle(),options,ar->{
      if (ar.succeeded()){
        System.out.println("启动成功");
      }else {
        System.out.println(ar.cause().getMessage());
      }
    });
  }
}
//{"tcpServer": {"host": "127.0.0.1", "port": 1111}}
