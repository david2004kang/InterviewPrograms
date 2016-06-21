/**
 * The MIT License
 * Copyright (c) 2014 Ilkka Seppälä
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sunilsahoo.designpatterns.poison.pill;

import com.sunilsahoo.designpatterns.poison.pill.Message.Headers;

/**
 * Class responsible for receiving and handling submitted to the queue messages
 */
public class Consumer {

  private final MqSubscribePoint queue;
  private final String name;

  public Consumer(String name, MqSubscribePoint queue) {
    this.name = name;
    this.queue = queue;
  }

  /**
   * Consume message
   */
  public void consume() {
    while (true) {
      Message msg;
      try {
        msg = queue.take();
        if (Message.POISON_PILL.equals(msg)) {
          System.out.println(String.format("Consumer %s receive request to terminate.", name));
          break;
        }
      } catch (InterruptedException e) {
        // allow thread to exit
        System.err.println(e);
        return;
      }

      String sender = msg.getHeader(Headers.SENDER);
      String body = msg.getBody();
      System.out.println(String.format("Message [%s] from [%s] received by [%s]", body, sender,
          name));
    }
  }
}
