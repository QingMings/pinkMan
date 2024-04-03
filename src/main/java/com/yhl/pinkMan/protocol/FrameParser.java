package com.yhl.pinkMan.protocol;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;

public class FrameParser implements Handler<Buffer> {


  private Buffer _buffer;
  private int _offset;

  private final Handler<AsyncResult<Object>> client;

  public FrameParser(Handler<AsyncResult<Object>> client) {
    this.client = client;
  }

  @Override
  public void handle(Buffer buffer) {
    append(buffer);

    int offset;

    while (true) {
      offset = _offset;


      int remainingBytes = bytesRemaining();


      if (remainingBytes < 4) {
        break;
      }


      int length = _buffer.getInt(_offset);
      _offset += 4;

      if (remainingBytes - 4 >= length) {

        try {
          client.handle(Future.succeededFuture(_buffer.getBytes(_offset, _offset + length)));
        } catch (DecodeException e) {

          client.handle(Future.failedFuture(e));
        }
        _offset += length;
      } else {

        _offset = offset;
        break;
      }
    }
  }

  private void append(Buffer newBuffer) {
    if (newBuffer == null) {
      return;
    }


    if (_buffer == null) {
      _buffer = newBuffer;

      return;
    }


    if (_offset >= _buffer.length()) {
      _buffer = newBuffer;
      _offset = 0;

      return;
    }


    if (_offset > 0) {
      _buffer = _buffer.getBuffer(_offset, _buffer.length());
    }
    _buffer.appendBuffer(newBuffer);

    _offset = 0;
  }

  private int bytesRemaining() {
    return (_buffer.length() - _offset) < 0 ? 0 : (_buffer.length() - _offset);
  }
}
