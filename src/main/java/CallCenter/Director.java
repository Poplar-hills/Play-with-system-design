package main.java.CallCenter;

public class Director extends Employee {
    public Director(int id, String name, CallMediator handler) {
        super(id, name, handler);
        rank = Rank.Director;
    }

    @Override
    public void processCall(Call call) {
        System.out.println("Director " + id + " is processing the call " + call.getId());
        completeCall(call);  // Assume a director can always solve the problem, no need to escalate further
    }
}
