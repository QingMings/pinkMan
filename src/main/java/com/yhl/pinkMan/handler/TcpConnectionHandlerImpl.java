package com.yhl.pinkMan.handler;

import com.google.common.collect.HashBiMap;
import com.google.protobuf.InvalidProtocolBufferException;

import com.yhl.pinkMan.protocol.FrameParser;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpConnectionHandlerImpl implements TcpConnectionHandler {
  private static Logger logger = LoggerFactory.getLogger(TcpConnectionHandlerImpl.class);
  private Vertx vertx;

  private HashBiMap<NetSocket, String> socketMap = HashBiMap.create();

  public TcpConnectionHandlerImpl(Vertx vertx) {
    this.vertx = vertx;
  }

  @Override
  public void handle(NetSocket netSocket) {
    socketMap.put(netSocket, netSocket.writeHandlerID());
    logger.info("Incoming connection!");
    FrameParser frameParser = new FrameParser(res -> {
      if (res.failed()) {
        // could not parse the message properly
        System.out.println(res.cause());
        return;
      }
      Object object = res.result();
      byte[] bytes = (byte[])object;
//      try {
//
////        System.out.println(person);
//      } catch (InvalidProtocolBufferException e) {
//        e.printStackTrace();
//      }
      System.out.println(res.result());
    });
    netSocket.exceptionHandler((exception) -> {
      logger.error("The TCP server caught a TCP socket error - closing connection", exception);
      netSocket.close();
    });
    netSocket.endHandler(v -> {
      logger.info("endHandler");
      socketMap.remove(netSocket);
    });
    netSocket.handler(frameParser);
  }
}
