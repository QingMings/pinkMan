package com.yhl.pinkMan.util;

import com.yhl.pinkMan.util.annotation.EnumKey;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProtocolCodeEnum {

  //发送周期：建立连接后
  CLIENT_INIT_REPORT(1, "初始上报客户端数据", "设备管理软件、模拟训练软件", "导控"),
  CLIENT_USER_LOGIN(2, "用户登录", "模拟训练软件", "导控"),

  TASK_DISTRIBUTION(4, "任务下发", "导控", "模拟训练软件、设备管理软件"),
  TRAINING_BEGIN(4, "训练开始", "导控", "模拟训练软件、设备管理软件"),
  //  发送周期：实时发送
  DOSE_RATE_INFORMATION(101, "剂量率信息", "模拟训练软件", "导控->设备管理软件"),
  //发送周期：实时发送
  POISON_INFORMATION(102, "剂量率信息", "模拟训练软件", "导控->设备管理软件"),
  //发送周期：实时发送
  BEIDOU_INFORMATION(103, "北斗信息", "模拟训练软件", "导控->设备管理软件"),
  //发送周期：5s发送1次
  METEOROLOGICAL_INFORMATION(104, "气象信息", "模拟训练软件", "导控->设备管理软件"),
  //  发送周期：实时发送
  BIOAEROSOL_CONCENTRATION_INFORMATION(106, "气象信息", "模拟训练软件", "导控->设备管理软件"),
  EXAM_IS_OVER(9, "考试结束", "模拟训练软件", "导控->设备管理软件"),
  INSERT_A_FLAG(17, "插旗子", "模拟训练软件", "导控->模拟训练软件"),
  REPORT_RECONNAISSANCE_RESULTS(18, "上报侦察结果", "模拟训练软件", "导控"),
  //  发送周期：车长按下车
  GET_OFF_THE_CAR(19, "下车", "模拟训练软件", "导控->模拟训练软件"),
  //发送周期：下车结果
  GET_OFF_THE_CAR_RESULT(20, "下车结果", "模拟训练软件", "导控->模拟训练软件"),
  //发送周期：车长按上车
  GET_INTO_THE_CAR(21, "上车", "模拟训练软件", "导控->模拟训练软件"),
  //发送周期：车长按上车
  GET_INTO_THE_CAR_RESULT(22, "上车结果", "模拟训练软件", "导控->模拟训练软件"),
  //发送周期：按防护按钮，进行防护
  PROTECTION(23, "防护", "模拟训练软件", "导控->模拟训练软件"),
  INSERT_A_FLAG_2(24, "插旗子", "模拟训练软件", "导控->模拟训练软件"),
  SCORE_DISTRIBUTION(26, "成绩下发", "导控", "模拟训练软件"),
  REQUESTING_ANSWERS(28, "请求答题（三维内部转发）", "模拟训练软件", "导控->模拟训练软件"),
  REQUESTING_ANSWERS_RESULT(29, "请求答题结果（三维内部转发）", "模拟训练软件", "导控->模拟训练软件"),
  ANSWERING_REPORT(30, "答题上报", "模拟训练软件", "导控"),
  VEHICLE_SPEED_WHEN_APPROACHING_THE_MEASUREMENT_POINT(31, "接近测量点时的车速", "模拟训练软件", "导控"),
  FORWARD_RECONNAISSANCE_RESULTS(32, "把侦察结果给指定人（2号上报结果时不知道插旗子的信息）", "模拟训练软件", "导控->模拟训练软件"),
  //发送周期：按钮点击时触发
  GUIDANCE_TRAINING_PROCESS_CONTROL(33, "导控训练进程控制", "导控", "设备管理软件、模拟训练软件"),
  //发送周期：间隔固定时间
  VEHICLE_DATA_SYNC(34, "导控训练进程控制", "模拟训练软件", "导控->模拟训练软件"),
  ////发送周期：间隔固定时间
  SITUATION_INFORMATION_SYNC(35, "态势信息同步", "模拟训练软件", "导控->模拟训练软件"),
  //发送周期：按钮点击时触发
  RANDOM_TUNING(36, "随机导调", "导控", "模拟训练软件"),


  ;
  @EnumKey
  private final Integer code;
  private final String desc;

  private final String sender;
  private final String receiver;
}
