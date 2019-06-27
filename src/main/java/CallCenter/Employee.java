package main.java.CallCenter;

import java.time.Instant;

public abstract class Employee {
    protected int id;
    protected String name;
    protected boolean isAvailable = true;  // default to true
    private CallHandler callHandler;       // Employee 中保持一个 CallHandler 的引用

    public Employee(int id, String name, CallHandler handler) {
        this.id = id;
        this.name = name;
        callHandler = handler;
    }

    public abstract void answerCall(Call call);

    public void completeCall(Call call) {
        call.setEndTime(Instant.now().getEpochSecond());
        isAvailable = true;
    }

    public void escalate(Call call) {
        call.incrementToughness();
        callHandler.escalate(call, this);
        isAvailable = true;
    }
}
