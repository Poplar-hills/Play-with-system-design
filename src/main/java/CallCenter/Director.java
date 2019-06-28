package main.java.CallCenter;

public class Director extends Employee {
    public Director(int id, String name, CallMediator handler) {
        super(id, name, handler);
        rank = Rank.Director;
    }

    @Override
    public void processCall(Call call) {
        System.out.println("Director " + name + " is processing the call " + call.getId());

        try {
            Thread.sleep(1000);  // simulate the processing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        completeCall(call);  // Assume a director can always solve the problem, no need to escalate further
    }
}
