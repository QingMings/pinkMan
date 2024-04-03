package com.yhl.pinkMan.handler;

import com.google.common.collect.HashBiMap;

import com.yhl.pinkMan.domain.BaseFrame;
import com.yhl.pinkMan.protocol.FrameParser;
import com.yhl.pinkMan.util.EnumUtils;
import com.yhl.pinkMan.util.ProtocolCodeEnum;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class





TcpConnectionHandlerImpl implements TcpConnectionHandler {
  private final static Logger logger = LoggerFactory.getLogger(TcpConnectionHandlerImpl.class);
  private final Vertx vertx;

  private final HashBiMap<NetSocket, String> socketMap = HashBiMap.create();

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

      JsonObject  frameData = Buffer.buffer((byte[]) object).toJsonObject();
      BaseFrame baseFrame = frameData.mapTo(BaseFrame.class);
      logger.info("接收到消息："+ baseFrame.toString());
      ProtocolCodeEnum protocolCodeEnum = EnumUtils.valueOf(ProtocolCodeEnum.class, baseFrame.getProtocolCode());

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
