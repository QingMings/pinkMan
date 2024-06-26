package com.yhl.pinkMan;


import com.yhl.pinkMan.domain.BaseFrame;
import com.yhl.pinkMan.domain.Forwards;
import com.yhl.pinkMan.protocol.FrameHelper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

import java.util.ArrayList;
import java.util.List;

public class VertxTcpClientVerticle extends AbstractVerticle {

  @Override
  public void start() {
    NetClient tcpClient = vertx.createNetClient();

    tcpClient.connect(1111, "127.0.0.1", this::handle);
  }

  public void handle(AsyncResult<NetSocket> result) {
    NetSocket socket = result.result();

    //read data
    socket.handler(new Handler<Buffer>() {
      @Override
      public void handle(Buffer buffer) {
        System.out.println("Received data length: " + buffer.length());

        String data = buffer.getString(0, buffer.length());
        System.out.println("Received data: " + data);

      }
    });


//        JsonObject jsonObject = new JsonObject();
//        for (int i = 0; i < 500; i++){
//            jsonObject.put("" + i, i);
//        }
//        FrameHelper.writeFrame(jsonObject, socket);


//        vertx.setPeriodic(1000,t -> {
//          FrameHelper.writeFrame(person.toByteArray(), socket);
//        });
    BaseFrame clientInitReportFrame = createBaseFrame(1, 2, 3, 4, new ArrayList<Forwards>(), JsonObject.of("EquipType", 1));
    JsonObject jsonObject = JsonObject.mapFrom(clientInitReportFrame);
    FrameHelper.writeFrame(jsonObject,socket);
    socket.exceptionHandler(t -> {
      System.out.println(t.getMessage());
      socket.close();
    });

    socket.endHandler(v -> {
    });
  }


  private BaseFrame createBaseFrame(Integer protocolCode, Integer machineld, Integer seatId, Integer carType, List<Forwards> forwards, JsonObject msg) {
    return new BaseFrame(protocolCode, machineld, seatId, carType, forwards, msg.encode());

  }

  public static void main(String[] args) {
    VertxTcpClientVerticle client = new VertxTcpClientVerticle();
    Vertx.vertx().deployVerticle(client);
  }
}
