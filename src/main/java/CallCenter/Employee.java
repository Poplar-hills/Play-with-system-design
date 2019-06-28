package main.java.CallCenter;

import java.time.Instant;

public abstract class Employee {
    protected int id;
    protected String name;
    protected Rank rank;
    private CallMediator callMediator;  // Employee 中保持一个 CallMediator 的引用 todo: 这属于组合？？聚合？？

    public Employee(int id, String name, CallMediator handler) {
        this.id = id;
        this.name = name;
        callMediator = handler;
    }

    public Rank getRank() { return rank; }

    public void answerCall(Call call) {
        call.setStartTime(Instant.now().getEpochSecond());  // start processing the call
        new Thread(() -> processCall(call)).start();        // spawn a new thread to process the call
    }

    public abstract void processCall(Call call);

    public void completeCall(Call call) {
        call.setEndTime(Instant.now().getEpochSecond());  // put an end to the call
        System.out.println("Call " + call.getId() + " has been completed");
        goIdling();
    }

    public void escalateCall(Call call) {
        callMediator.escalate(call, this);
        System.out.println("Escalating the call " + call.getId());
        goIdling();
    }

    private void goIdling() {
        callMediator.putBackToHandlerQueue(this);
    }
}
