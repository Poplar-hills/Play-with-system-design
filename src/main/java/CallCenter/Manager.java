package main.java.CallCenter;

public class Manager extends Employee {
    public Manager(int id, String name, CallMediator handler) {
        super(id, name, handler);
        rank = Rank.Manager;
    }

    @Override
    public void processCall(Call call) {
        System.out.println("Manager " + id + " is processing the call " + call.getId());
            
        completeCall(call);
        escalateCall(call);
    }
}
