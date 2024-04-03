package com.yhl.pinkMan;

import io.vertx.core.Vertx;

public class TestClient {
  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new VertxTcpClientVerticle(),ar->{
      if (ar.succeeded()){
        System.out.println("client 启动成功");
      }else {
        System.out.println(ar.cause().getMessage());
      }
    });
  }
}
