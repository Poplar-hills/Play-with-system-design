package main.java.CallCenter;

public class Director extends Employee {
    public Director(int id, String name, CallHandler handler) {
        super(id, name, handler);
    }

    @Override
    public void answerCall(Call call) {
        isAvailable = false;
        completeCall(call);  // Assume a director can always solve the problem, no need to escalate further
    }
}
