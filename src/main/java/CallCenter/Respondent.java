package main.java.CallCenter;

import java.time.Instant;

public class Respondent extends Employee {
    private int managerId;  // todo: 层级关系存在 Employee 中，而非 CallHandler 中？？？

    public Respondent(int id, String name, int managerId) {
        this.id = id;
        this.name = name;
        this.managerId = managerId;
        isAvailable = true;
    }

    @Override
    public void answerCall(Call call) {
        if (/* tougher than a respondent can handle */) {
            if (/* is an urgent call */) call.incrementToughness(CallToughness.Medium);
            escalate(call);
        }

        // process the call

        // finish up
        call.setEndTime(Instant.now().getEpochSecond());
        isAvailable = false;


        return;  // solved
    }

    public void escalate(Call call) {

        // todo: 如何获得 CallHandler？？？
    }

}
