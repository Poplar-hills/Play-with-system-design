package main.java.CallCenter;

public class Respondent extends Employee {
    public Respondent(int id, String name, CallMediator mediator) {
        super(id, name, mediator);
        rank = Rank.Respondent;
    }

    @Override
    public void processCall(Call call) {
        System.out.println("Respondent " + name + " is processing the call " + call.getId());

        try {
            Thread.sleep(3000);  // simulate the processing
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean canHandle = call.getRank().compareTo(Rank.Respondent) == 0;  // Enum 实现了 Comparable 接口，所以可以直接比较
        if (canHandle) completeCall(call);
        else escalateCall(call);
    }
}
