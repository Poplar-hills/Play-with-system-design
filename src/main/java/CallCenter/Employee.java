package main.java.CallCenter;

import java.time.Instant;

public abstract class Employee {
    private int id;
    private String name;
    protected Rank rank;
    private CallMediator callMediator;       // Employee 中保持一个 CallMediator 的引用 todo: 组合？？聚合？？

    public Employee(int id, String name, CallMediator handler) {
        this.id = id;
        this.name = name;
        callMediator = handler;
    }

    public Rank getRank() { return rank; }

    public abstract void answerCall(Call call);

    public void completeCall(Call call) {
        call.setEndTime(Instant.now().getEpochSecond());  // put an end to the call
        goIdling();
    }

    public void escalateCall(Call call) {
        callMediator.escalate(call, this);
        goIdling();
    }

    private void goIdling() {
        callMediator.putBackToHandlerPool(this);
    }
}
