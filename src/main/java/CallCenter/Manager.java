package main.java.CallCenter;

public class Manager extends Employee {
    public Manager(int id, String name, CallMediator handler) {
        super(id, name, handler);
        rank = Rank.Manager;
    }

    @Override
    public void answerCall(Call call) {

    }
}
