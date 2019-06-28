package main.java.CallCenter;

public class Manager extends Employee {
    public Manager(int id, String name, CallMediator handler) {
        super(id, name, handler);
        rank = Rank.Manager;
    }

    @Override
    public void processCall(Call call) {
        System.out.println("Manager " + name + " is processing the call " + call.getId());

        try {
            Thread.sleep(2000);  // simulate the processing time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean canHandle = call.getRank().compareTo(Rank.Manager) == 0;  // Enum 实现了 Comparable 接口，所以可以直接比较
        if (canHandle) completeCall(call);
        else escalateCall(call);
    }
}
