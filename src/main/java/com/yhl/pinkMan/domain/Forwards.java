package com.yhl.pinkMan.domain;

import lombok.Data;

/**
 * 消息转发
 */
@Data
public class Forwards {

  /**
   * 转发给指定车
   */
   private Integer MachineId;
  /**
   * 转发给指定的席位
   */
  private Integer SeatId;


}
