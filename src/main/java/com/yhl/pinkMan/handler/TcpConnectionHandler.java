package com.yhl.pinkMan.handler;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;



public interface TcpConnectionHandler extends Handler<NetSocket> {

  static TcpConnectionHandler create(Vertx vertx){
    return new TcpConnectionHandlerImpl(vertx);
  }
}
