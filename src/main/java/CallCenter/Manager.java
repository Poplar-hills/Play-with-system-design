package main.java.CallCenter;

public class Manager extends Employee {
    private int directorId;  // todo: 1.层级关系存在 Manager 中，而非 CallHandler 中？？？ 2.存上级的 id 还是存引用？？？

    public Manager(int id, String name, CallHandler handler) {
        super(id, name, handler);
    }

    @Override
    public void answerCall(Call call) {
        isAvailable = false;
        if (call.getToughness().equals(CallToughness.High))
            escalate(call);
        return; // solved
    }
}
