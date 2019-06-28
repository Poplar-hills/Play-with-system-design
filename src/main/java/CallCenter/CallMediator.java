package main.java.CallCenter;

import java.util.*;

public class CallMediator {

    private static final int LEVELS = 3;
    private List<Queue<Call>> callQueues;
    private List<Queue<Employee>> handlerQueues;

    public CallMediator() {
        callQueues = new ArrayList<>(LEVELS);
        handlerQueues = new ArrayList<>(LEVELS);

        for (int i = 0; i < LEVELS; i++) {
            callQueues.add(new LinkedList<>());
            handlerQueues.add(new LinkedList<>());
        }
    }

    public void receiveCall(int callNumber) {  // 电话进入 Call Center 时调用的方法
        String id = UUID.randomUUID().toString();
        Queue<Call> respondentCallQueue = callQueues.get(Rank.Respondent.getValue());
        respondentCallQueue.offer(new Call(id, callNumber));  // 进入 rank 为 Respondent 的队列中等待被 respondent 消费
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
        call.incrementRank();   // 将该 call 所需的 handler rank 升一级
        reassignHandler(call);
    }

    private void reassignHandler(Call call) {  // 将该 call 放入相应的队列中等待消费（当有符合其所需 rank 的 employee 进入 handlerQueues 的相应队列中时）
        Rank rankNeeded = call.getRank();
        callQueues.get(rankNeeded.getValue()).offer(call);
    }

    public void putBackToHandlerQueue(Employee employee) {  // 将该 employee 放入对应的队列中等待消费（当有符合其 rank 的 call 进入 callQueues 相应队列时）
        Rank employeeRank = employee.getRank();
        handlerQueues.get(employeeRank.getValue()).offer(employee);
    }
}
