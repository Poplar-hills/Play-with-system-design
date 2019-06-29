package main.java.CallCenter;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class CallMediator {

    private static final int LEVELS = 3;
    private List<BlockingQueue<Call>> callQueues;
    private List<BlockingQueue<Employee>> handlerQueues;

    public CallMediator() {
        callQueues = new ArrayList<>(LEVELS);
        handlerQueues = new ArrayList<>(LEVELS);

        for (int i = 0; i < LEVELS; i++) {
            callQueues.add(new ArrayBlockingQueue<>(100));  // 每个 call queue 最多存储 100 个 call
            handlerQueues.add(new ArrayBlockingQueue<>(100));  // 每个 handler queue 最多存储 100 个 handler
        }
    }

    public void receiveCall(int callNumber) {  // 有电话进入 Call Center 时调用的方法
        String id = UUID.randomUUID().toString();
        BlockingQueue<Call> queue = callQueues.get(Rank.Respondent.getValue());
        try {
            queue.put(new Call(id, callNumber));  // 进入 rank 为 Respondent 的队列中等待被 respondent 消费
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void dispatchCall() {
        for (int i = 0; i < LEVELS; i++) {  // 为每一组队列（如 callQueues.get(1) 和 handlerQueues.get(1) 是对应的一组队列）启动一个线程
            final int index = i;            // lambda 中不能有变量
            new Thread(() -> dispatchCallToHandler(index)).start();
        }
    }

    private void dispatchCallToHandler(int queueIndex) {
        BlockingQueue<Call> callQueue = callQueues.get(queueIndex);
        BlockingQueue<Employee> handlerQueue = handlerQueues.get(queueIndex);
        Call call = null;
        Employee handler = null;

        while (true) {  // 该线程不断轮询 callQueue 和 handlerQueue 尝试取出元素
            try {
                call = callQueue.take();
                handler = handlerQueue.take();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if (call != null && handler != null)  // 如果 callQueue 中有 call 且 handlerQueue 中有 handler，则让这个 handler 处理这个 call
                handler.answerCall(call);
        }
    }

    public void escalate(Call call) {
        call.incrementRank();   // 先将该 call 所需的 handler rank 升一级
        Rank rankNeeded = call.getRank();
        try {
            callQueues.get(rankNeeded.getValue()).put(call);  // 将该 call 放入相应的队列中等待消费（当够级别的 handler 出现闲置时即被处理）
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void putBackToHandlerQueue(Employee employee) {  // 将该 employee 放入相应的队列中等待消费（当够级别的 call 出现时即开始处理）
        Rank employeeRank = employee.getRank();
        try {
            handlerQueues.get(employeeRank.getValue()).put(employee);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
