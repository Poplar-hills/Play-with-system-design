package main.java.CallCenter;

public class Respondent extends Employee {
    public Respondent(int id, String name, CallMediator mediator) {
        super(id, name, mediator);
        rank = Rank.Respondent;
    }

    @Override
    public void processCall(Call call) {
        System.out.println("Respondent " + id + " is processing the call " + call.getId());

    }
}
