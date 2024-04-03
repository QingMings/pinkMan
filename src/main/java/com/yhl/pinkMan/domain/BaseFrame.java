package com.yhl.pinkMan.domain;

import java.util.List;

/**
 * 通用协议
 */
public class BaseFrame {

  /**
   * 协议号
   */
  private Integer   ProtocolCode;

  /**
   * 机号
   */
  private Integer MachineId;
  /**
   * 席位号
   */
  private Integer SeatId;
  /**
   * 车型
   */
  private Integer CarType;
  /**
   * 消息转发
   */
  private List<Forwards> Forwards;

  /**
   * 具体的消息内容
   */
  private String Msg;
}
