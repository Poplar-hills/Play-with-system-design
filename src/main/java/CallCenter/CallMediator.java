package main.java.CallCenter;

import java.util.*;

public class CallMediator {

    private static final int LEVELS = 3;
    private Queue<Call> callQueue;
    private Queue[] handlerPool;

    public CallMediator() {
        callQueue = new LinkedList<>();
        handlerPool = new Queue[LEVELS];
        for (int i = 0; i < handlerPool.length; i++)
            handlerPool[i] = new LinkedList<>();
    }

    public void receiveCall(int callNumber) {
        String id = UUID.randomUUID().toString();
        callQueue.offer(new Call(id, callNumber));
    }

    public void dispatchCall() {  // todo: 新启一个消费者线程来 dispatch call，使用 BlockingQueue
//        while (true) {            // 该线程不断循环检查是否有没被 handle 的 call 和空闲的 respondent
//            Respondent respondent = idleRespondents.poll();
//            Call call = respondentCallQueue.poll();
//            if (respondent != null && call != null)
//                respondent.answerCall(call);
//        }
    }

    public void escalate(Call call, Employee employee) {
        call.incrementRank();
        reassignHandler(call);
    }

    private void reassignHandler(Call call) {
        int rankValNeeded = call.getRank().getValue();
        handlerPool[rankValNeeded].offer(call);
    }

    public void putBackToHandlerPool(Employee employee) {
        int employeeRankVal = employee.getRank().getValue();
        handlerPool[employeeRankVal].offer(employee);
    }
}
